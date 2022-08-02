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

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
//import org.openmrs.module.openhmis.inventory.api.model.*;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IStockroomDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.search.ItemSearch;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
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
@Resource(name = ModuleRestConstants.CLOSINGBALANCEUPDATE_RESOURCE, supportedClass = ItemStockSummary.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ClosingBalanceUpdateResource extends DelegatingCrudResource<ItemStockSummary> {

	private IStockroomDataService stockroomDataService;
	private IItemDataService iitemDataService;

	public ClosingBalanceUpdateResource() {
		this.stockroomDataService = Context.getService(IStockroomDataService.class);
		this.iitemDataService = Context.getService(IItemDataService.class);
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
		description.addProperty("atmQuantity", Representation.DEFAULT);
		description.addProperty("comPharmacyQuantity", Representation.DEFAULT);
		description.addProperty("communityARTGroupsQuantity", Representation.DEFAULT);
		description.addProperty("courierDeliveryQuantity", Representation.DEFAULT);
		description.addProperty("dispensaryQuantity", Representation.DEFAULT);
		description.addProperty("patentMedicineStoreQuantity", Representation.DEFAULT);
		description.addProperty("privateClinicsQuantity", Representation.DEFAULT);
		description.addProperty("othersQuantity", Representation.DEFAULT);
		description.addProperty("ancQuantity", Representation.DEFAULT);
		description.addProperty("comQuantity", Representation.DEFAULT);
		description.addProperty("eidQuantity", Representation.DEFAULT);
		description.addProperty("emergQuantity", Representation.DEFAULT);
		description.addProperty("fpQuantity", Representation.DEFAULT);
		description.addProperty("inpatQuantity", Representation.DEFAULT);
		description.addProperty("landdQuantity", Representation.DEFAULT);
		description.addProperty("labQuantity", Representation.DEFAULT);
		description.addProperty("malQuantity", Representation.DEFAULT);
		description.addProperty("mobQuantity", Representation.DEFAULT);
		description.addProperty("opdQuantity", Representation.DEFAULT);
		description.addProperty("ossQuantity", Representation.DEFAULT);
		description.addProperty("othQuantity", Representation.DEFAULT);
		description.addProperty("paedQuantity", Representation.DEFAULT);
		description.addProperty("ppQuantity", Representation.DEFAULT);
		description.addProperty("stiQuantity", Representation.DEFAULT);
		description.addProperty("tbQuantity", Representation.DEFAULT);
		description.addProperty("vctQuantity", Representation.DEFAULT);

		return description;
	}

	@Override
	public ItemStockSummary newDelegate() {
		return new ItemStockSummary();
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		PageableResult result = null;

		String stockroomUuid = context.getParameter("stockroom_uuid");

		if (StringUtils.isNotBlank(stockroomUuid)) {
			PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

			if (stockroomUuid != null && !stockroomUuid.isEmpty()) {

				Stockroom stockroom = stockroomDataService.getByUuid(stockroomUuid);
				ItemSearch itemSearch = new ItemSearch();
				if (stockroom.getStockroomType() != null) {
					itemSearch.getTemplate().setItemType(stockroom.getStockroomType());
				}
				List<Item> items = iitemDataService.getItemsByItemSearch(itemSearch, pagingInfo);

				List<ItemStockSummary> results = new ArrayList<ItemStockSummary>();

				for (Item item : items) {

					ItemStockSummary itemStockSummary = new ItemStockSummary();

					itemStockSummary.setItem(item);
					//					itemStockSummary.setAtmQuantity(0);
					//					itemStockSummary.setCommunityARTGroupsQuantity(0);
					//					itemStockSummary.setComPharmacyQuantity(0);
					//					itemStockSummary.setCourierDeliveryQuantity(0);
					//					itemStockSummary.setDispensaryQuantity(0);
					//					itemStockSummary.setOthersQuantity(0);
					//					itemStockSummary.setPatentMedicineStoreQuantity(0);
					//					itemStockSummary.setPrivateClinicsQuantity(0);

					results.add(itemStockSummary);
				}

				result =
				        new AlreadyPagedWithLength<ItemStockSummary>(context, results,
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
