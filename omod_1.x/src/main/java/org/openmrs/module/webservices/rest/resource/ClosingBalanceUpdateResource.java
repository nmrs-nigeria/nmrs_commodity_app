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
package org.openmrs.module.webservices.rest.resource;

import com.google.common.primitives.Ints;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.ClosingbalanceUpdate;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.search.ItemSearch;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * REST resource representing an {@link ClosingbalanceUpdate}.
 */
@Resource(name = ModuleRestConstants.CLOSINGBALANCEUPDATE_RESOURCE, supportedClass = ClosingbalanceUpdate.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ClosingBalanceUpdateResource extends DelegatingCrudResource<ClosingbalanceUpdate> {

	private IStockroomDataService stockroomDataService;
	private IItemStockDetailDataService itemStockDetailDataService;
	private IDepartmentDataService departmentService;
	private IARVPharmacyDispenseService iARVPharmacyDispenseService;
	private IItemDataService iitemDataService;

	public ClosingBalanceUpdateResource() {
		this.stockroomDataService = Context.getService(IStockroomDataService.class);
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.iARVPharmacyDispenseService = Context.getService(IARVPharmacyDispenseService.class);
		this.iitemDataService = Context.getService(IItemDataService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("itemId", Representation.DEFAULT);
		description.addProperty("itemName", Representation.DEFAULT);
		description.addProperty("itemUUID", Representation.DEFAULT);
		description.addProperty("unitOfMeasure", Representation.DEFAULT);
		description.addProperty("itemType", Representation.DEFAULT);
		description.addProperty("drugStrenght", Representation.DEFAULT);
		description.addProperty("packSize", Representation.DEFAULT);
		return description;
	}

	@Override
	public ClosingbalanceUpdate newDelegate() {
		return new ClosingbalanceUpdate();
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		PageableResult result = null;

		String stockroomUuid = context.getParameter("stockroom_uuid");
		//	String departmentUuid = context.getParameter("department_uuid");

		if (StringUtils.isNotBlank(stockroomUuid)) {
			PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

			if (stockroomUuid != null && !stockroomUuid.isEmpty()) {

				Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
				ItemSearch itemSearch = new ItemSearch();
				if (stockroom.getStockroomType() != null) {
					itemSearch.getTemplate().setItemType(stockroom.getStockroomType());
				}
				List<Item> items = iitemDataService.getItemsByItemSearch(itemSearch, pagingInfo);
				//				List<ClosingbalanceUpdate> itemStockSummaries =
				//				        itemStockDetailDataService.getItemStockSummaryByItemType(stockroom, pagingInfo);

				List<ClosingbalanceUpdate> results = new ArrayList<ClosingbalanceUpdate>();
				for (Item i : items) {
					ClosingbalanceUpdate summary = new ClosingbalanceUpdate();
					Integer itemid = Ints.checkedCast(i.getId());
					summary.setItemId(itemid);
					String itemname = (String)i.getName();
					summary.setItemName(itemname);
					String itemuuid = (String)i.getUuid();
					summary.setItemUUID(itemuuid);

					String itemstrength = (String)i.getStrength();
					summary.setDrugStrenght(itemstrength);

					Integer itempacksize = Ints.checkedCast(i.getPackSize());
					summary.setPackSize(itempacksize);

					results.add(summary);
				}
				result =
				        new AlreadyPagedWithLength<ClosingbalanceUpdate>(context, results,
				                pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());

			}

			//		if (StringUtils.isNotBlank(stockroomUuid) || StringUtils.isNotBlank(departmentUuid)) {
			//			PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
			//
			//			if (stockroomUuid != null && !stockroomUuid.isEmpty()) {
			//				Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
			//				List<ClosingbalanceUpdate> itemStockSummaries =
			//				        itemStockDetailDataService.getItemStockSummaryByItemType(stockroom, pagingInfo);
			//				result =
			//				        new AlreadyPagedWithLength<ClosingbalanceUpdate>(context, itemStockSummaries,
			//				                pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
			//
			//			}
			//			else if (departmentUuid != null && !departmentUuid.isEmpty()) {
			//
			//				Department department = departmentService.getByUuid(departmentUuid);
			//				List<ClosingbalanceUpdate> itemStockSummaries =
			//				        itemStockDetailDataService
			//	.getItemStockSummaryByDepartmentPharmacy(department, pagingInfo);
			//				result =
			//				        new AlreadyPagedWithLength<ClosingbalanceUpdate>(context, itemStockSummaries,
			//				                pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());
			//
			//			}
		} else {
			result = super.doSearch(context);
		}

		return result;
	}

	@Override
	public SimpleObject getAll(RequestContext context) throws ResponseException {
		return new SimpleObject();
	}

	@Override
	public ClosingbalanceUpdate save(ClosingbalanceUpdate delegate) {
		return null;
	}

	@Override
	public ClosingbalanceUpdate getByUniqueId(String uniqueId) {
		return null;
	}

	@Override
	protected void delete(ClosingbalanceUpdate delegate, String reason, RequestContext context) throws ResponseException {
		// Deletes not supported
	}

	@Override
	public void purge(ClosingbalanceUpdate delegate, RequestContext context) throws ResponseException {
		// Purges not supported
	}
}
