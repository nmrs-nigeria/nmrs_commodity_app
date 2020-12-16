/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author MORRISON.I
 */
public class PharmacyConsumption extends BaseSerializableOpenmrsMetadata {

	public static final long serialVersionUID = 1L;

	private Integer consumptionId;
	private Department department;
	private Item item;
	private Date consumptionDate;
	private Integer quantity;
	private Integer wastage;
	private String batchNumber;

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public PharmacyConsumption(Integer consumptionId) {
		this.consumptionId = consumptionId;
	}

	public PharmacyConsumption() {}

	//private String testType;
	public Integer getWastage() {
		return wastage;
	}

	public void setWastage(Integer wastage) {
		this.wastage = wastage;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public Integer getId() {
		return this.consumptionId;
	}

	@Override
	public void setId(Integer intgr) {
		this.consumptionId = intgr;
	}

	@Override
	@JsonIgnore
	public Boolean getRetired() {
		return super.getRetired();
	}

	@Override
	public String toString() {
		return "PharmacyConsumption{" + "consumptionId=" + consumptionId + ", "
		        + "department=" + department + ", item=" + item + ", "
		        + "consumptionDate=" + consumptionDate + ", quantity=" + quantity + ", "
		        + "wastage=" + wastage + ", batchNumber=" + batchNumber + '}';
	}

}
