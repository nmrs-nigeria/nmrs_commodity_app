/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.OpenmrsObject;
import org.openmrs.api.ConceptNameType;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IObjectAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemConceptSuggestion;

/**
 * @author MORRISON.I
 */
public class ARVPharmacyDispenseImpl extends BaseMetadataDataServiceImpl implements IARVPharmacyDispense {

	@Override
	public List<ARVPharmacyDispense> getARVs(Date startDate, Date endDate) {
		String queryString = "select  a from " + Encounter.class.getName() + " "
		        + "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
		        + "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) and a.encounterType = 13 ";

		Query query = getRepository().createQuery(queryString);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		List<Encounter> encounters = (List<Encounter>)query.list();

		return null;

	}

	@Override
	protected void validate(OpenmrsObject object) {
		return;
	}

	@Override
	protected IObjectAuthorizationPrivileges getPrivileges() {
		return null;
	}

}
