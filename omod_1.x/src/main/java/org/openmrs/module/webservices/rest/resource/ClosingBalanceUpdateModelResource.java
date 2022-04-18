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

import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationService;
import org.openmrs.module.openhmis.inventory.api.WellKnownOperationTypes;
import org.openmrs.module.openhmis.inventory.api.model.*;
import org.openmrs.module.openhmis.inventory.model.InventoryClosingBalanceUpdateStockTake;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.IdgenHelper;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.springframework.web.client.RestClientException;

import java.util.*;

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
	public InventoryClosingBalanceUpdateStockTake newDelegate() {
		return new InventoryClosingBalanceUpdateStockTake();
	}

	public Boolean userCanProcessAdjustment() {
		return StockOperationTypeResource.userCanProcess(WellKnownOperationTypes.getAdjustment());
	}

	@Override
	public InventoryClosingBalanceUpdateStockTake save(InventoryClosingBalanceUpdateStockTake delegate) {
		// Ensure that the current user can process the operation
		if (!userCanProcessAdjustment()) {
			throw new RestClientException("The current user not authorized to process this operation.");
		}

		itemStockDetailDataService.insertInvClosingBalanceUpdate(delegate.getItemStockSummaryList());

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
