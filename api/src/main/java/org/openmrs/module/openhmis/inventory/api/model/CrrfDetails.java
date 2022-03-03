package org.openmrs.module.openhmis.inventory.api.model;

import org.openmrs.BaseOpenmrsObject;

/**
 * @author Toyeeb
 */
public class CrrfDetails extends BaseOpenmrsObject {

	private Integer id;
	private Item drugs;
	private String basicUnit;
	private Integer beginningBalance;
	private Integer quantityReceived;
	private Integer quantityDispensed;
	private Integer positiveAdjustments;
	private Integer negativeAdjustments;
	private Integer lossesdDamagesExpiries;
	private Integer physicalCount;
	private Integer maximumStockQuantity;
	private Integer quantityToOrder;

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {

	}

	public Item getDrugs() {
		return drugs;
	}

	public void setDrugs(Item drugs) {
		this.drugs = drugs;
	}

	public String getBasicUnit() {
		return basicUnit;
	}

	public void setBasicUnit(String basicUnit) {
		this.basicUnit = basicUnit;
	}

	public Integer getBeginningBalance() {
		return beginningBalance;
	}

	public void setBeginningBalance(Integer beginningBalance) {
		this.beginningBalance = beginningBalance;
	}

	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public Integer getQuantityDispensed() {
		return quantityDispensed;
	}

	public void setQuantityDispensed(Integer quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}

	public Integer getPositiveAdjustments() {
		return positiveAdjustments;
	}

	public void setPositiveAdjustments(Integer positiveAdjustments) {
		this.positiveAdjustments = positiveAdjustments;
	}

	public Integer getNegativeAdjustments() {
		return negativeAdjustments;
	}

	public void setNegativeAdjustments(Integer negativeAdjustments) {
		this.negativeAdjustments = negativeAdjustments;
	}

	public Integer getLossesdDamagesExpiries() {
		return lossesdDamagesExpiries;
	}

	public void setLossesdDamagesExpiries(Integer lossesdDamagesExpiries) {
		this.lossesdDamagesExpiries = lossesdDamagesExpiries;
	}

	public Integer getPhysicalCount() {
		return physicalCount;
	}

	public void setPhysicalCount(Integer physicalCount) {
		this.physicalCount = physicalCount;
	}

	public Integer getMaximumStockQuantity() {
		return maximumStockQuantity;
	}

	public void setMaximumStockQuantity(Integer maximumStockQuantity) {
		this.maximumStockQuantity = maximumStockQuantity;
	}

	public Integer getQuantityToOrder() {
		return quantityToOrder;
	}

	public void setQuantityToOrder(Integer quantityToOrder) {
		this.quantityToOrder = quantityToOrder;
	}

}