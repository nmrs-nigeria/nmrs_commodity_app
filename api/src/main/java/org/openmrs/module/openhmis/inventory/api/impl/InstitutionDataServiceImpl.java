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
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IInstitutionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Institution;
import org.openmrs.module.openhmis.inventory.api.security.BasicMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.util.HibernateCriteriaConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data service implementation class for {@link Institution}s.
 */
@Transactional
public class InstitutionDataServiceImpl extends BaseMetadataDataServiceImpl<Institution> implements IInstitutionDataService {

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return new BasicMetadataAuthorizationPrivileges();
	}

	@Override
	protected void validate(Institution entity) {
		return;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Institution> getInstitutionByStateAndLga(String state, String lga) {
		if (state == null || lga == null) {
			throw new IllegalArgumentException("The state and lga must be defined.");
		}

		return executeCriteria(Institution.class, new Action1<Criteria>() {
			@Override
			public void apply(Criteria parameter) {
				parameter.add(Restrictions.eq(HibernateCriteriaConstants.FACILITY_STATE, state).ignoreCase())
				        .add(Restrictions.eq(HibernateCriteriaConstants.FACILITY_LGA, lga).ignoreCase());
			}
		});

	}

}
