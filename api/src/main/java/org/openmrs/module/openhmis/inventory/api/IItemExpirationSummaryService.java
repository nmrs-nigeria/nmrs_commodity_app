/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataService;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummary;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public interface IItemExpirationSummaryService extends IObjectDataService<ItemExpirationSummary> {

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_ITEMS })
	List<ItemExpirationSummary> getItemExipirationSummary(PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<ItemExpirationSummary> getItemStockSummaryByStockroom(Stockroom stockroom, PagingInfo pagingInfo);

}
