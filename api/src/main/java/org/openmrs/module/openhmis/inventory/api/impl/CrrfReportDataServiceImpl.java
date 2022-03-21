/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.ICrrfReportDataService;
import org.openmrs.module.openhmis.inventory.api.model.Crrf;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;

/**
 * @author Toyeeb
 */
public class CrrfReportDataServiceImpl extends BaseMetadataDataServiceImpl<Crrf>
        implements ICrrfReportDataService, IMetadataAuthorizationPrivileges {

	@Override
	public String getGetPrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.VIEW_CONSUMPTIONS;
	}

	@Override
	public String getPurgePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.PURGE_CONSUMPTION;
	}

	@Override
	public String getSavePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getRetirePrivilege() {
		// TODO Auto-generated method stub
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected void validate(Crrf arg0) {
		throw new UnsupportedOperationException("Not supported yet.");

	}

}
