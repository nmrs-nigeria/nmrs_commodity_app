/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.search;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.search.PharmacyConsumptionSearch;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.resource.AlreadyPagedWithLength;
import org.openmrs.module.webservices.rest.resource.PagingUtil;
import org.openmrs.module.webservices.rest.resource.search.BaseSearchHandler;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.EmptySearchResult;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author MORRISON.I
 */
public class PharmacyConsumptionSearchHandler
        extends BaseSearchHandler
        implements SearchHandler {

	private final SearchConfig searchConfig = new SearchConfig("default",
	        ModuleRestConstants.PHARMACY_CONSUMPTION_RESOURCE,
	        Arrays.asList("*"),
	        Arrays.asList(
	                new SearchQuery.Builder(
	                        "Find a consumption by  optionally filtering by department, item and dates")
	                        // .withRequiredParameters("q")
	                        .withOptionalParameters("department_uuid", "consumption_start_date",
	                            "consumption_end_date", "item_uuid")
	                        .build()
	                )
	        );

	private IItemDataService itemService;
	private IDepartmentDataService departmentService;
	public IPharmacyConsumptionDataService service;

	public PharmacyConsumptionSearchHandler(IDepartmentDataService departmentService,
	    IPharmacyConsumptionDataService service, IItemDataService itemService) {
		this.itemService = itemService;
		this.departmentService = departmentService;
		this.service = service;

	}

	@Override
	public SearchConfig getSearchConfig() {
		return searchConfig;
	}

	@Override
	public PageableResult search(RequestContext context) throws ResponseException {

		String query = context.getParameter("q");
		query = query.isEmpty() ? null : query;

		String consumptionStartDateText = context.getParameter("consumption_start_date");
		String consumptionEndDateText = context.getParameter("consumption_end_date");

		Department department = getOptionalEntityByUuid(departmentService, context.getParameter("department_uuid"));
		Item item = getOptionalEntityByUuid(itemService, context.getParameter("item_uuid"));

		Date consumptionStartDate = null;
		if (!StringUtils.isEmpty(consumptionStartDateText)) {
			consumptionStartDate = Utility.parseOpenhmisDateString(consumptionStartDateText);
			if (consumptionStartDate == null) {
				return new EmptySearchResult();
			}
		}

		Date consumptionEndDate = null;
		if (!StringUtils.isEmpty(consumptionEndDateText)) {
			consumptionEndDate = Utility.parseOpenhmisDateString(consumptionEndDateText);
			if (consumptionEndDate == null) {
				return new EmptySearchResult();
			}
		}

		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
		List<PharmacyConsumption> consumptions = null;

		if (consumptionStartDate != null && consumptionEndDate != null) {
			consumptions = service.getConsumptionByConsumptionDate(consumptionStartDate, consumptionEndDate, pagingInfo);

		} else {

			if (department == null && item == null) {
				//return all removing retired.
				consumptions = service.getAll(false, pagingInfo);
			} else {

				PharmacyConsumptionSearch consumptionSearch = createConsumptionSearch(context, item,
				    department, consumptionStartDate, consumptionEndDate);

				consumptions = service.getConsumptionsByConsumptionSearch(consumptionSearch, pagingInfo);

			}

		}

		if (consumptions == null || consumptions.isEmpty()) {
			return new EmptySearchResult();
		} else {
			return new AlreadyPagedWithLength<PharmacyConsumption>(context, consumptions, pagingInfo.hasMoreResults(),
			        pagingInfo.getTotalRecordCount());
		}

	}

	private PharmacyConsumptionSearch createConsumptionSearch(RequestContext context, Item item, Department department,
	        Date consumptionStartDate, Date consumptionEndDate) {

		PharmacyConsumptionSearch template = new PharmacyConsumptionSearch();

		template.getTemplate().setDepartment(department);
		template.getTemplate().setItem(item);

		if (!context.getIncludeAll()) {
			template.getTemplate().setRetired(false);
		}

		return template;

	}

}
