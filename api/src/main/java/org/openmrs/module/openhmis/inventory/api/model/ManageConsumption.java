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
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;

import org.openmrs.BaseOpenmrsObject;

/**
 * Base model class used by models that have item stock detail information.
 */
public class ManageConsumption extends BaseOpenmrsObject {

	public static final long serialVersionUID = 0L;

	private Integer id;
	private Item item;
	private Integer quantity;
	private Department department;

	private Date consumptionDate;
	private Integer wastage;
	private String batchNumber;
	private String testPurpose;

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public Integer getWastage() {
		return wastage;
	}

	public void setWastage(Integer wastage) {
		this.wastage = wastage;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getTestPurpose() {
		return testPurpose;
	}

	public void setTestPurpose(String testPurpose) {
		this.testPurpose = testPurpose;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * @param item The item.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Gets the quantity.
	 * @return The quantity.
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 * @param quantity The quantity.
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the department.
	 * @return The department.
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * Sets the department.
	 * @param department The department.
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

}
