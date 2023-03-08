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
package org.openmrs.module.openhmis.inventory.api;

import java.util.Date;
import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IObjectDataService;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetail;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockSummary;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.ViewInvStockonhandPharmacyDispensary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.ClosingBalanceUpdateModel;
import org.openmrs.module.openhmis.inventory.api.model.ClosingbalanceUpdate;
import org.openmrs.module.openhmis.inventory.api.model.CrrfDetails;

/**
 * Interface that represents classes which perform data operations for {@link ItemStockDetail}s.
 */
@Transactional
public interface IItemStockDetailDataService extends IObjectDataService<ItemStockDetail> {
	/**
	 * Returns the {@link ItemStockDetail} records for the specified {@link Stockroom}.
	 * @param stockroom The stockroom.
	 * @param pagingInfo The paging information.
	 * @return The item stock detail records or an empty list when none are found.
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<ItemStockDetail> getItemStockDetailsByStockroom(Stockroom stockroom, PagingInfo pagingInfo);

	/**
	 * Returns the aggregate {@link ItemStockSummary} records for the specified {@link Stockroom}.
	 * @param stockroom The stockroom.
	 * @param pagingInfo The paging information.
	 * @return The item stock summary records or an empty list when none are found.
	 * @should throw IllegalArgumentException if the stockroom is null
	 * @should return an empty list if no records are found
	 * @should return the item stock summary records
	 * @should return paged results if paging is specified
	 * @should return correctly paged results when aggregate qty is zero
	 */
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<ItemStockSummary> getItemStockSummaryByStockroom(Stockroom stockroom, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<ClosingbalanceUpdate> getItemStockSummaryByItemType(Stockroom stockroom, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<ItemStockSummary> getItemStockSummaryByDepartment(Department department, PagingInfo pagingInfo);

	@Transactional(readOnly = false)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	List<ItemStockSummary> getItemStockSummaryByDepartmentPharmacy(Department department, PagingInfo pagingInfo);

	@Transactional
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	void updatePharmacyAtDispensary(List<ViewInvStockonhandPharmacyDispensary> viewInvStockonhandPharmacyDispensary);

	@Transactional
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	void updateStockOnHandAtLabStockroom(List<ItemStockDetail> itemStockDetailList);

	@Transactional
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	void addNewDistributionDataPharmacyAtDispensary(StockOperation operation);

	@Transactional(readOnly = false)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	void updateStockBalanceAtDispensary(int stockBalance, int id);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	List<CrrfDetails> getItemStockSummaryByPharmacy(Date startDate, Date endDate, PagingInfo pagingInfo);

	@Transactional
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	void insertInvClosingBalanceUpdate(List<ClosingBalanceUpdateModel> closingBalanceUpdateModels);

}
