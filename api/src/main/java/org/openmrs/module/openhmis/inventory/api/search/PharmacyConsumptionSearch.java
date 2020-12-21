/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.search;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;

/**
 * @author MORRISON.I
 */
public class PharmacyConsumptionSearch extends CustomBaseMetadataTemplateSearch<PharmacyConsumption> {

	public static final long serialVersionUID = 0L;

	public PharmacyConsumptionSearch() {
		this(new PharmacyConsumption(), StringComparisonType.EQUAL, false);
	}

	public PharmacyConsumptionSearch(PharmacyConsumption consumptionTemplate, StringComparisonType nameComparisonType,
	    Boolean includeRetired) {
		//	super(consumptionTemplate, nameComparisonType, includeRetired);
		super(consumptionTemplate, includeRetired);
	}

	public PharmacyConsumptionSearch(PharmacyConsumption consumptionTemplate) {
		this(consumptionTemplate, StringComparisonType.EQUAL, false);
	}

	public PharmacyConsumptionSearch(PharmacyConsumption consumptionTemplate, Boolean includeRetired) {
		this(consumptionTemplate, StringComparisonType.EQUAL, includeRetired);
	}

	private ComparisonType conceptComparisonType;

	public ComparisonType getConceptComparisonType() {
		return conceptComparisonType;
	}

	public void setConceptComparisonType(ComparisonType conceptComparisonType) {
		this.conceptComparisonType = conceptComparisonType;
	}

	@Override
	public void updateCriteria(Criteria criteria) {
		super.updateCriteria(criteria);

		PharmacyConsumption consumption = getTemplate();
		if (consumption.getConsumptionDate() != null) {
			criteria.add(Restrictions.eq("consumptionDate", consumption.getConsumptionDate()));
		}

		//		if (consumption.getDepartment() != null) {
		//			criteria.add(Restrictions.eq("department", consumption.getDepartment()));
		//		}

		if (consumption.getItem() != null) {
			criteria.add(Restrictions.eq("item", consumption.getItem()));
		}

		if (consumption.getQuantity() != null) {
			criteria.add(Restrictions.eq("quantity", consumption.getQuantity()));
		}
		if (consumption.getWastage() != null) {
			criteria.add(Restrictions.eq("wastage", consumption.getWastage()));
		}

	}

}
