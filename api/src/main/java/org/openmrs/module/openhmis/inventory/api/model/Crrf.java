package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import java.util.List;

import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author Toyeeb Abdulfatai
 */
public class Crrf extends BaseSerializableOpenmrsMetadata {

	private Integer crrfReportId;

	private String facilityName;

	private String facilityCode;

	private String lga;

	private String state;

	private Date reportingPeriodStart;

	private Date reportingPeriodEnd;

	private Date datePrepared;

	private List<CrrfDetails> crrfAdultRegimenCategory;

	private List<CrrfDetails> crrfPediatricRegimenCategory;

	private List<CrrfDetails> crrfOIRegimenCategory;

	private List<CrrfDetails> crrfAdvanceHIVRegimenCategory;

	private List<CrrfDetails> crrfTBRegimenCategory;

	private List<CrrfDetails> crrfSTIRegimenCategory;

	public Crrf() {}

	public Crrf(Integer crrfReportId, String facilityName, String facilityCode,
	    String lga, String state, Date reportingPeriodStart,
	    Date reportingPeriodEnd, Date datePrepared, List<CrrfDetails> crrfAdultRegimenCategory,
	    List<CrrfDetails> crrfPediatricRegimenCategory, List<CrrfDetails> crrfOIRegimenCategory,
	    List<CrrfDetails> crrfAdvanceHIVRegimenCategory, List<CrrfDetails> crrfTBRegimenCategory,
	    List<CrrfDetails> crrfSTIRegimenCategory) {
		super();
		this.crrfReportId = crrfReportId;
		this.facilityName = facilityName;
		this.facilityCode = facilityCode;
		this.lga = lga;
		this.state = state;
		this.reportingPeriodStart = reportingPeriodStart;
		this.reportingPeriodEnd = reportingPeriodEnd;
		this.datePrepared = datePrepared;
		this.crrfAdultRegimenCategory = crrfAdultRegimenCategory;
		this.crrfPediatricRegimenCategory = crrfPediatricRegimenCategory;
		this.crrfOIRegimenCategory = crrfOIRegimenCategory;
		this.crrfAdvanceHIVRegimenCategory = crrfAdvanceHIVRegimenCategory;
		this.crrfTBRegimenCategory = crrfTBRegimenCategory;
		this.crrfSTIRegimenCategory = crrfSTIRegimenCategory;
	}

	public Integer getCrrfReportId() {
		return crrfReportId;
	}

	public void setCrrfReportId(Integer crrfReportId) {
		this.crrfReportId = crrfReportId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}

	public String getLga() {
		return lga;
	}

	public void setLga(String lga) {
		this.lga = lga;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getReportingPeriodStart() {
		return reportingPeriodStart;
	}

	public void setReportingPeriodStart(Date reportingPeriodStart) {
		this.reportingPeriodStart = reportingPeriodStart;
	}

	public Date getReportingPeriodEnd() {
		return reportingPeriodEnd;
	}

	public void setReportingPeriodEnd(Date reportingPeriodEnd) {
		this.reportingPeriodEnd = reportingPeriodEnd;
	}

	public Date getDatePrepared() {
		return datePrepared;
	}

	public void setDatePrepared(Date datePrepared) {
		this.datePrepared = datePrepared;
	}

	public List<CrrfDetails> getCrrfAdultRegimenCategory() {
		return crrfAdultRegimenCategory;
	}

	public void setCrrfAdultRegimenCategory(List<CrrfDetails> crrfAdultRegimenCategory) {
		this.crrfAdultRegimenCategory = crrfAdultRegimenCategory;
	}

	public List<CrrfDetails> getCrrfPediatricRegimenCategory() {
		return crrfPediatricRegimenCategory;
	}

	public void setCrrfPediatricRegimenCategory(List<CrrfDetails> crrfPediatricRegimenCategory) {
		this.crrfPediatricRegimenCategory = crrfPediatricRegimenCategory;
	}

	public List<CrrfDetails> getCrrfOIRegimenCategory() {
		return crrfOIRegimenCategory;
	}

	public void setCrrfOIRegimenCategory(List<CrrfDetails> crrfOIRegimenCategory) {
		this.crrfOIRegimenCategory = crrfOIRegimenCategory;
	}

	public List<CrrfDetails> getCrrfAdvanceHIVRegimenCategory() {
		return crrfAdvanceHIVRegimenCategory;
	}

	public void setCrrfAdvanceHIVRegimenCategory(List<CrrfDetails> crrfAdvanceHIVRegimenCategory) {
		this.crrfAdvanceHIVRegimenCategory = crrfAdvanceHIVRegimenCategory;
	}

	public List<CrrfDetails> getCrrfTBRegimenCategory() {
		return crrfTBRegimenCategory;
	}

	public void setCrrfTBRegimenCategory(List<CrrfDetails> crrfTBRegimenCategory) {
		this.crrfTBRegimenCategory = crrfTBRegimenCategory;
	}

	public List<CrrfDetails> getCrrfSTIRegimenCategory() {
		return crrfSTIRegimenCategory;
	}

	public void setCrrfSTIRegimenCategory(List<CrrfDetails> crrfSTIRegimenCategory) {
		this.crrfSTIRegimenCategory = crrfSTIRegimenCategory;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.crrfReportId;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.crrfReportId = id;
	}

}
