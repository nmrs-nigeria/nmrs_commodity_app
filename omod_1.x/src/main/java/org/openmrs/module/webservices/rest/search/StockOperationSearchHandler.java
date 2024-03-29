/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.webservices.rest.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.search.StockOperationSearch;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.resource.AlreadyPagedWithLength;
import org.openmrs.module.webservices.rest.resource.PagingUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Search handler for {@link StockOperation}s.
 */
@Component
public class StockOperationSearchHandler implements SearchHandler {

	private static final Log LOG = LogFactory.getLog(StockOperationSearchHandler.class);

	private final SearchConfig searchConfig = new SearchConfig("default", ModuleRestConstants.OPERATION_RESOURCE,
	        Arrays.asList("*"),
	        Arrays.asList(
	                new SearchQuery.Builder("Finds stock operations with an optional status and/or stockroom.")
	                        .withOptionalParameters("status", "stockroom_uuid", "operation_date", "operation_date_filter",
	                            "operation_date_filter_end", "commodityType")
	                        .build()
	                )
	        );

	private IStockroomDataService stockroomDataService;
	private IStockOperationDataService operationDataService;

	@Autowired
	public StockOperationSearchHandler(IStockroomDataService stockroomDataService,
	    IStockOperationDataService operationDataService) {
		this.stockroomDataService = stockroomDataService;
		this.operationDataService = operationDataService;
	}

	@Override
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}

	@Override
	public PageableResult search(RequestContext context) {
		String operationDateText = context.getParameter("operation_date");
		String statusText = context.getParameter("status");
		String stockroomText = context.getParameter("stockroom_uuid");
		String dateCreatedText = context.getParameter("operation_date_filter");
		String dateCreatedTextEnd = context.getParameter("operation_date_filter_end");

		Date operationDate = null;
		if (!StringUtils.isEmpty(operationDateText)) {
			operationDate = Utility.parseOpenhmisDateString(operationDateText);
			if (operationDate == null) {
				return new EmptySearchResult();
			}
		}

		Date dateCreated = null;
		if (!StringUtils.isEmpty(dateCreatedText)) {
			try {
				dateCreated = new SimpleDateFormat("yyyy-MM-dd").parse(dateCreatedText);
			} catch (ParseException e) {
				LOG.warn("Could not parse date created '" + dateCreatedText + "'");
			}
		}

		Date dateCreatedEnd = null;
		if (!StringUtils.isEmpty(dateCreatedTextEnd)) {
			try {
				dateCreatedEnd = new SimpleDateFormat("yyyy-MM-dd").parse(dateCreatedTextEnd);
			} catch (ParseException e) {
				LOG.warn("Could not parse date created end '" + dateCreatedTextEnd + "'");
			}
		}

		StockOperationStatus status = null;
		if (!StringUtils.isEmpty(statusText)) {
			status = StockOperationStatus.valueOf(statusText);
			if (status == null) {
				LOG.warn("Could not parse Stock Operation Status '" + statusText + "'");
				return new EmptySearchResult();
			}
		}

		Stockroom stockroom = null;
		if (!StringUtils.isEmpty(stockroomText)) {
			stockroom = stockroomDataService.getByUuid(stockroomText);
			if (stockroom == null) {
				LOG.warn("Could not find stockroom '" + stockroomText + "'");
				return new EmptySearchResult();
			}
		}

		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
		List<StockOperation> operations;
		if (operationDate != null) {
			operations = operationDataService.getOperationsByDate(operationDate, pagingInfo);
		} else {
			StockOperationSearch search = null;

			if (status != null) {
				search = new StockOperationSearch();
				search.getTemplate().setStatus(status);
			}

			if (stockroom == null) {
				if (search == null) {
					// No search was defined so just return everything (excluding retired)
					operations = operationDataService.getAll(false, pagingInfo);
				} else {
					// Return the operations with the specified status
					operations = operationDataService.getOperations(search, pagingInfo);
				}
			} else {
				// Return the operations for the specified stockroom and status
				operations = stockroomDataService.getOperations(stockroom, search, pagingInfo);
			}

			if (dateCreated != null) {
				operations = operationDataService.getOperationsByDate(dateCreated, pagingInfo);
			}

			if (dateCreated != null) {
				if (dateCreatedEnd != null) {
					SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
					searchConsumptionSummary.setStartDate(dateCreated);
					searchConsumptionSummary.setEndDate(dateCreatedEnd);
					operations = operationDataService.getOperationsByDateDiff(searchConsumptionSummary, pagingInfo);
				}
			}

		}

		if (operations == null || operations.size() == 0) {
			return new EmptySearchResult();
		} else {
			return new AlreadyPagedWithLength<StockOperation>(context, operations, pagingInfo.hasMoreResults(),
			        pagingInfo.getTotalRecordCount());
		}
	}
}
