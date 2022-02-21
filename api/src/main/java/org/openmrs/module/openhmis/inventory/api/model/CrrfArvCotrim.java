package org.openmrs.module.openhmis.inventory.api.model;

import org.openmrs.BaseOpenmrsObject;

public class CrrfArvCotrim extends BaseOpenmrsObject {

	private Integer id;
	private String drugs;
	private String basicunits;
	private String beginningbalance;
	private String quantityreceived;
	private String quantitydispensed;
	private String positiveadjustments;
	private String negativeadjustments;
	private String losses;
	private String physicalcount;
	private String maximumstockquantity;
	private String quantitytoorder;

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {

	}

	public String getDrugs() {
		return drugs;
	}

	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}

	public String getBasicunits() {
		return basicunits;
	}

	public void setBasicunits(String basicunits) {
		this.basicunits = basicunits;
	}

	public String getBeginningbalance() {
		return beginningbalance;
	}

	public void setBeginningbalance(String beginningbalance) {
		this.beginningbalance = beginningbalance;
	}

	public String getQuantityreceived() {
		return quantityreceived;
	}

	public void setQuantityreceived(String quantityreceived) {
		this.quantityreceived = quantityreceived;
	}

	public String getQuantitydispensed() {
		return quantitydispensed;
	}

	public void setQuantitydispensed(String quantitydispensed) {
		this.quantitydispensed = quantitydispensed;
	}

	public String getPositiveadjustments() {
		return positiveadjustments;
	}

	public void setPositiveadjustments(String positiveadjustments) {
		this.positiveadjustments = positiveadjustments;
	}

	public String getNegativeadjustments() {
		return negativeadjustments;
	}

	public void setNegativeadjustments(String negativeadjustments) {
		this.negativeadjustments = negativeadjustments;
	}

	public String getLosses() {
		return losses;
	}

	public void setLosses(String losses) {
		this.losses = losses;
	}

	public String getPhysicalcount() {
		return physicalcount;
	}

	public void setPhysicalcount(String physicalcount) {
		this.physicalcount = physicalcount;
	}

	public String getMaximumstockquantity() {
		return maximumstockquantity;
	}

	public void setMaximumstockquantity(String maximumstockquantity) {
		this.maximumstockquantity = maximumstockquantity;
	}

	public String getQuantitytoorder() {
		return quantitytoorder;
	}

	public void setQuantitytoorder(String quantitytoorder) {
		this.quantitytoorder = quantitytoorder;
	}
}
