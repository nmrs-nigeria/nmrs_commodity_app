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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
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

/**
 * REST resource representing an {@link ItemStockSummary}.
 */
@Resource(name = ModuleRestConstants.INVENTORY_STOCK_TAKE_SUMMARY_RESOURCE, supportedClass = ItemStockSummary.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ItemStockSummaryResource extends DelegatingCrudResource<ItemStockSummary> {

	private IStockroomDataService stockroomDataService;
	private IItemStockDetailDataService itemStockDetailDataService;
	private IDepartmentDataService departmentService;
	private IARVPharmacyDispenseService iARVPharmacyDispenseService;

	public ItemStockSummaryResource() {
		this.stockroomDataService = Context.getService(IStockroomDataService.class);
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.iARVPharmacyDispenseService = Context.getService(IARVPharmacyDispenseService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("item", Representation.DEFAULT);
		description.addProperty("expiration", Representation.DEFAULT);
		description.addProperty("quantity", Representation.DEFAULT);
		description.addProperty("actualQuantity", Representation.DEFAULT);
		description.addProperty("itemBatch", Representation.DEFAULT);
		description.addProperty("reasonForChange", Representation.DEFAULT);
		description.addProperty("pharmStockOnHandId", Representation.DEFAULT);
		description.addProperty("conceptId", Representation.DEFAULT);
		description.addProperty("strengthConceptId", Representation.DEFAULT);
		return description;
	}

	@PropertySetter("expiration")
	public void setExpiration(ItemStockSummary instance, String dateText) {
		instance.setExpiration(Utility.parseOpenhmisDateString(dateText));
	}

	@Override
	public ItemStockSummary newDelegate() {
		return new ItemStockSummary();
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		PageableResult result = null;

		String stockroomUuid = context.getParameter("stockroom_uuid");
		String departmentUuid = context.getParameter("department_uuid");

		if (StringUtils.isNotBlank(stockroomUuid) || StringUtils.isNotBlank(departmentUuid)) {
			PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

			if (stockroomUuid != null && !stockroomUuid.isEmpty()) {
				Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
				List<ItemStockSummary> itemStockSummaries =
				        itemStockDetailDataService.getItemStockSummaryByStockroom(stockroom, pagingInfo);
				result =
				        new AlreadyPagedWithLength<ItemStockSummary>(context, itemStockSummaries,
				                pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());

			} else if (departmentUuid != null && !departmentUuid.isEmpty()) {

				Department department = departmentService.getByUuid(departmentUuid);
				List<ItemStockSummary> itemStockSummaries =
				        itemStockDetailDataService.getItemStockSummaryByDepartmentPharmacy(department, pagingInfo);
				result =
				        new AlreadyPagedWithLength<ItemStockSummary>(context, itemStockSummaries,
				                pagingInfo.hasMoreResults(), pagingInfo.getTotalRecordCount());

			}
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
	public ItemStockSummary save(ItemStockSummary delegate) {
		return null;
	}

	@Override
	public ItemStockSummary getByUniqueId(String uniqueId) {
		return null;
	}

	@Override
	protected void delete(ItemStockSummary delegate, String reason, RequestContext context) throws ResponseException {
		// Deletes not supported
	}

	@Override
	public void purge(ItemStockSummary delegate, RequestContext context) throws ResponseException {
		// Purges not supported
	}
}
