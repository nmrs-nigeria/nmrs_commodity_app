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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationService;
import org.openmrs.module.openhmis.inventory.api.WellKnownOperationTypes;
import org.openmrs.module.openhmis.inventory.api.model.ClosingBalanceUpdateModel;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;
import org.openmrs.module.openhmis.inventory.api.model.ViewInvStockonhandPharmacyDispensary;
import org.openmrs.module.openhmis.inventory.model.InventoryClosingBalanceUpdateStockTake;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.IdgenHelper;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.springframework.web.client.RestClientException;

/**
 * REST resource representing an {@link InventoryClosingBalanceUpdateStockTake}.
 */
@Resource(name = ModuleRestConstants.INVENTORY_CLOSINGBALANCEUPDATE_STOCKTAKE_RESOURCE,
        supportedClass = InventoryClosingBalanceUpdateStockTake.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ClosingBalanceUpdateModelResource extends BaseRestObjectResource<InventoryClosingBalanceUpdateStockTake> {

	private IStockOperationService operationService;
	private IItemStockDetailDataService itemStockDetailDataService;

	public ClosingBalanceUpdateModelResource() {
		this.operationService = Context.getService(IStockOperationService.class);
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.removeProperty("name");
		description.removeProperty("description");
		description.addProperty("operationNumber");
		description.addProperty("stockroom");
		description.addProperty("department");
		description.addProperty("itemStockSummaryList");
		return description;
	}

	@Override
	public InventoryClosingBalanceUpdateStockTake newDelegate() {
		return new InventoryClosingBalanceUpdateStockTake();
	}

	public Boolean userCanProcessAdjustment() {
		return StockOperationTypeResource.userCanProcess(WellKnownOperationTypes.getAdjustment());
	}

	@Override
	public InventoryClosingBalanceUpdateStockTake save(InventoryClosingBalanceUpdateStockTake delegate) {
		// Ensure that the current user can process the operation

		List<ClosingBalanceUpdateModel> closingBalanceUpdateModels =
		        new ArrayList<ClosingBalanceUpdateModel>();

		String repPeriodRepYearStockroomTypeChoose = delegate.getOperationNumber();
		String splited[] = repPeriodRepYearStockroomTypeChoose.split("_");
		String reportingPeriod = splited[0];
		String reportingYear = splited[1];
		String stockroomTypeChoose = splited[2];

		List<ItemStockSummary> inventoryStockTakeList = delegate.getItemStockSummaryList();
		System.out.println("ItemStockSummary : " + inventoryStockTakeList.size());
		int i = 1;
		System.out.println("Count : " + i);
		for (ItemStockSummary itemStockSummary : inventoryStockTakeList) {

			ClosingBalanceUpdateModel closingBalanceUpdateModel = new ClosingBalanceUpdateModel();
			closingBalanceUpdateModel.setCalculatedClosingBalance(0);
			closingBalanceUpdateModel.setDateCreated(new Date());
			closingBalanceUpdateModel.setItem(itemStockSummary.getItem());
			closingBalanceUpdateModel.setPackSize(itemStockSummary.getItem().getPackSize().toString());
			closingBalanceUpdateModel.setReportingPeriod(reportingPeriod);
			closingBalanceUpdateModel.setReportingYear(reportingYear);
			closingBalanceUpdateModel.setStockroomType(stockroomTypeChoose);
			closingBalanceUpdateModel.setStrength(itemStockSummary.getItem().getStrength());
			closingBalanceUpdateModel.setUpdatedClosingBalance(itemStockSummary.getActualQuantity());

			System.out.println("setCalculatedClosingBalance : " + closingBalanceUpdateModel.getCalculatedClosingBalance());
			System.out.println("setDateCreated : " + closingBalanceUpdateModel.getDateCreated());
			System.out.println("setItem : " + closingBalanceUpdateModel.getItem());
			System.out.println("setPackSize : " + closingBalanceUpdateModel.getPackSize());
			System.out.println("setReportingPeriod : " + closingBalanceUpdateModel.getReportingPeriod());
			System.out.println("setReportingYear : " + closingBalanceUpdateModel.getReportingYear());
			System.out.println("setStockroomType : " + closingBalanceUpdateModel.getStockroomType());
			System.out.println("setStrength : " + closingBalanceUpdateModel.getStrength());
			System.out.println("setUpdatedClosingBalance : " + closingBalanceUpdateModel.getUpdatedClosingBalance());

			if (closingBalanceUpdateModel.getUpdatedClosingBalance() != null) {
				closingBalanceUpdateModels.add(closingBalanceUpdateModel);
			}

			i++;
			System.out.println("Count : " + i);

		}

		System.out.println("ClosingBalanceUpdateModels : " + closingBalanceUpdateModels.size());

		itemStockDetailDataService.insertInvClosingBalanceUpdate(closingBalanceUpdateModels);

		return newDelegate();
	}

	@Override
	public Class<? extends IObjectDataService<InventoryClosingBalanceUpdateStockTake>> getServiceClass() {
		return null;
	}

	//update stock on hand at dispensary by deducting from quantity dispensed on pharmacy order form
	public void updateStockOnHandAtDispensaryViaPharmacyOrderForm() {

	}

}
