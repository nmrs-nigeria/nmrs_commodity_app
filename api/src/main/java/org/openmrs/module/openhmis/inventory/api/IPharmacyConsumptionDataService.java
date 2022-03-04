/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api;

import java.util.Date;
import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.model.CrffOperationsSummary;
import org.openmrs.module.openhmis.inventory.api.model.CrrfDetails;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.search.PharmacyConsumptionSearch;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public interface IPharmacyConsumptionDataService extends IMetadataDataService<PharmacyConsumption> {

	//temp using manage operations priv for all consumption data service

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptionByDepartment(Department department,
	        boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptionByItem(Item item, boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptions(Department department, Item item,
	        boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	// @Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptions(Department department, Item item, boolean includeRetired);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	// @Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptionsByConsumptionSearch(PharmacyConsumptionSearch consumptionSearch);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptionsByConsumptionSearch(PharmacyConsumptionSearch consumptionSearch,
	        PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	//	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTIONS })
	List<PharmacyConsumption> getConsumptionByConsumptionDate(Date startDate,
	        Date endDate, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	List<PharmacyConsumptionSummary> retrieveConsumptionSummary(List<StockOperation> stockOperations,
	        SearchConsumptionSummary searchConsumptionSummary,
	        PagingInfo pagingInfo, List<Item> distinctItems);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	List<PharmacyConsumptionSummary> retrieveConsumptionSummaryForStockroom(List<StockOperation> receiptStockOperations,
	        List<StockOperation> distributeStockOperations, PagingInfo pagingInfo, List<Item> distinctItems);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	List<CrffOperationsSummary> retrieveConsumptionSummaryForStockroom(List<StockOperation> adjustmentStockOperations,
	        List<StockOperation> transferStockOperations, List<StockOperation> disposedStockOperations,
	        PagingInfo pagingInfo, List<Item> distinctItems);

}
