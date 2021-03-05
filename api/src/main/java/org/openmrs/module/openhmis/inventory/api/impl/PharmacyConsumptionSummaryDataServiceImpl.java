/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.List;
import org.hibernate.criterion.Order;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionSummaryDataService;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MORRISON.I
 */
public class PharmacyConsumptionSummaryDataServiceImpl extends BaseMetadataDataServiceImpl<NewPharmacyConsumptionSummary>
        implements IPharmacyConsumptionSummaryDataService, IMetadataAuthorizationPrivileges {

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	@Override
	public List<PharmacyConsumptionSummary> getConsumptionSummarys(PharmacyConsumptionSummary consumptionSummary,
	        PagingInfo pagingInfo) {
		//temp impl
		return null;
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	protected Order[] getDefaultSort() {
		return new Order[] { Order.asc("startDate") };
		// return super.getDefaultSort(); 
	}

	@Override
	public List<NewPharmacyConsumptionSummary> getByNameFragment(String nameFragment, boolean includeRetired,
	        PagingInfo pagingInfo) {
		return super.getAll(includeRetired, pagingInfo);
	}

	@Override
	public List<NewPharmacyConsumptionSummary> getByNameFragment(String nameFragment, boolean includeRetired) {
		return getByNameFragment(nameFragment, includeRetired, null);
	}

	@Override
	public String getRetirePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getSavePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getPurgePrivilege() {
		return PrivilegeConstants.PURGE_CONSUMPTION;
	}

	@Override
	public String getGetPrivilege() {
		return PrivilegeConstants.VIEW_CONSUMPTIONS;
	}

	@Override
	protected void validate(NewPharmacyConsumptionSummary object) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
