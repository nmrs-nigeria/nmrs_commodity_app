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
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.commons.api.util.PrivilegeUtil;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.security.BasicMetadataAuthorizationPrivileges;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data service implementation class for {@link Department}s.
 */
@Transactional
public class DepartmentDataServiceImpl extends BaseMetadataDataServiceImpl<Department> implements IDepartmentDataService {

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return new BasicMetadataAuthorizationPrivileges();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Department> getByNameFragment(final String nameFragment, final String departmentType,
	        PagingInfo pagingInfo) {
		IMetadataAuthorizationPrivileges privileges = getPrivileges();
		if (privileges != null && !StringUtils.isEmpty(privileges.getGetPrivilege())) {
			PrivilegeUtil.requirePrivileges(Context.getAuthenticatedUser(), privileges.getGetPrivilege());
		}

		return executeCriteria(getEntityClass(), pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				if (nameFragment != null) {
					criteria.add(Restrictions.ilike("name", nameFragment, MatchMode.START));
				}

				if (departmentType != null) {
					criteria.add(Restrictions.eq("departmentType", departmentType));
				}

				criteria.add(Restrictions.eq("retired", false));

			}
		}, getDefaultSort());
	}

	@Override
	protected void validate(Department entity) {
		return;
	}
}
