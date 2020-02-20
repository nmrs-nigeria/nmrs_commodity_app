/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.search;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.OpenmrsMetadata;
import org.openmrs.module.openhmis.commons.api.entity.search.BaseAuditableTemplateSearch;

/**
 * @author MORRISON.I
 */
public class CustomBaseMetadataTemplateSearch<T extends OpenmrsMetadata> extends BaseAuditableTemplateSearch<T> {

	public static final long serialVersionUID = 0L;
	//	private StringComparisonType nameComparisonType;
	private StringComparisonType descriptionComparisonType;
	private StringComparisonType retireReasonComparisonType;
	private DateComparisonType dateRetiredComparisonType;
	private Boolean includeRetired;

	public CustomBaseMetadataTemplateSearch(T template) {
		this(template, null);
		//	this(template, StringComparisonType.EQUAL, null);
	}

	//	public CustomBaseMetadataTemplateSearch(T template, Boolean includeRetired) {
	//		this(template,  includeRetired);
	//	}

	public CustomBaseMetadataTemplateSearch(T template, Boolean includeRetired) {
		super(template);

		//	this.nameComparisonType = nameComparisonType;
		this.includeRetired = includeRetired;
		this.descriptionComparisonType = StringComparisonType.EQUAL;
		this.retireReasonComparisonType = StringComparisonType.EQUAL;
		this.dateRetiredComparisonType = DateComparisonType.EQUAL;
	}

	//	public StringComparisonType getNameComparisonType() {
	//		return nameComparisonType;
	//	}
	//
	//	public void setNameComparisonType(StringComparisonType nameComparisonType) {
	//		this.nameComparisonType = nameComparisonType;
	//	}

	public StringComparisonType getDescriptionComparisonType() {
		return descriptionComparisonType;
	}

	public void setDescriptionComparisonType(StringComparisonType descriptionComparisonType) {
		this.descriptionComparisonType = descriptionComparisonType;
	}

	public StringComparisonType getRetireReasonComparisonType() {
		return retireReasonComparisonType;
	}

	public void setRetireReasonComparisonType(StringComparisonType retireReasonComparisonType) {
		this.retireReasonComparisonType = retireReasonComparisonType;
	}

	public DateComparisonType getDateRetiredComparisonType() {
		return dateRetiredComparisonType;
	}

	public void setDateRetiredComparisonType(DateComparisonType dateRetiredComparisonType) {
		this.dateRetiredComparisonType = dateRetiredComparisonType;
	}

	public Boolean getIncludeRetired() {
		return includeRetired;
	}

	public void setIncludeRetired(Boolean includeRetired) {
		this.includeRetired = includeRetired;
	}

	@Override
	public void updateCriteria(Criteria criteria) {
		super.updateCriteria(criteria);

		T t = getTemplate();

		if (includeRetired != null) {
			if (!includeRetired) {
				criteria.add(Restrictions.eq("retired", false));
			}
		} else if (t.isRetired() != null) {
			criteria.add(Restrictions.eq("retired", t.isRetired()));
		}

		if (t.getRetiredBy() != null) {
			criteria.add(Restrictions.eq("retiredBy", t.getRetiredBy()));
		}
		if (t.getDateRetired() != null) {
			criteria.add(createCriterion("dateRetired", t.getDateRetired(), dateRetiredComparisonType));
		}
		if (t.getRetireReason() != null) {
			criteria.add(createCriterion("retireReason", t.getRetireReason(), retireReasonComparisonType));
		}
	}

}
