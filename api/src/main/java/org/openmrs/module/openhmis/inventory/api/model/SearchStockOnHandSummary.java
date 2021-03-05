/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;

/**
 * @author MORRISON.I
 */
public class SearchStockOnHandSummary {

	private Item item;
	private Department department;
	private IStockOperationType operationType;
	private StockOperationStatus operationStatus;
	private String startDate;
	private String endDate;
	private String commodityType;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public IStockOperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(IStockOperationType operationType) {
		this.operationType = operationType;
	}

	public StockOperationStatus getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(StockOperationStatus operationStatus) {
		this.operationStatus = operationStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCommodityType() {
		return commodityType;
	}

	public void setCommodityType(String commodityType) {
		this.commodityType = commodityType;
	}

}
