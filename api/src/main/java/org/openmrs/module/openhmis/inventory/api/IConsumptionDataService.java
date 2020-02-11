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
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.search.ConsumptionSearch;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public interface IConsumptionDataService extends IMetadataDataService<Consumption> {

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptionByDepartment(Department department, boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptionByItem(Item item, boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptions(Department department, Item item, boolean includeRetired, PagingInfo pagingInfo);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptions(Department department, Item item, boolean includeRetired);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptionsByConsumptionSearch(ConsumptionSearch consumptionSearch);

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_CONSUMPTION })
	List<Consumption> getConsumptionsByConsumptionSearch(ConsumptionSearch consumptionSearch, PagingInfo pagingInfo);

}
