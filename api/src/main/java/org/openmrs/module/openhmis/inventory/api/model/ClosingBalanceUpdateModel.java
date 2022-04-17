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

import org.hibernate.annotations.Entity;
import org.openmrs.BaseOpenmrsObject;

/**
 * Base model class used by models that have item stock detail information.
 */
@Entity
public class ClosingBalanceUpdateModel extends BaseOpenmrsObject {

	public static final long serialVersionUID = 0L;

	private Integer id;
	private String reportingPeriod;
	private String reportingYear;
	private String stockroomType;
	private Item item;
	private String packSize;
	private String strength;
	private Integer calculatedClosingBalance;
	private Integer updatedClosingBalance;
	private Date dateCreated;

	public String getReportingPeriod() {
		return reportingPeriod;
	}

	public void setReportingPeriod(String reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	public String getReportingYear() {
		return reportingYear;
	}

	public void setReportingYear(String reportingYear) {
		this.reportingYear = reportingYear;
	}

	public String getStockroomType() {
		return stockroomType;
	}

	public void setStockroomType(String stockroomType) {
		this.stockroomType = stockroomType;
	}

	public String getPackSize() {
		return packSize;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public Integer getCalculatedClosingBalance() {
		return calculatedClosingBalance;
	}

	public void setCalculatedClosingBalance(Integer calculatedClosingBalance) {
		this.calculatedClosingBalance = calculatedClosingBalance;
	}

	public Integer getUpdatedClosingBalance() {
		return updatedClosingBalance;
	}

	public void setUpdatedClosingBalance(Integer updatedClosingBalance) {
		this.updatedClosingBalance = updatedClosingBalance;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
