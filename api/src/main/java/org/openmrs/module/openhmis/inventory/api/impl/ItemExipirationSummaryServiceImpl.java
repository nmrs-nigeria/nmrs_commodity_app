/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummary;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.openhmis.inventory.api.IItemExipirationSummaryService;

/**
 * @author MORRISON.I
 */
public class ItemExipirationSummaryServiceImpl extends BaseMetadataDataServiceImpl<ItemExpirationSummary>
        implements IItemExipirationSummaryService, IMetadataAuthorizationPrivileges {

	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_ITEMS })
	@Override
	public List<ItemExpirationSummary> getItemExipirationSummary(PagingInfo pagingInfo) {
		return null;
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	protected void validate(ItemExpirationSummary object) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getRetirePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getSavePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getPurgePrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

	@Override
	public String getGetPrivilege() {
		return PrivilegeConstants.VIEW_ITEMS;
	}

}
