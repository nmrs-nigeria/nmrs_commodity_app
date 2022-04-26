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
package org.openmrs.module.openhmis.inventory.model;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.ClosingBalanceUpdateModel;
import org.openmrs.module.openhmis.inventory.api.model.Stockroom;

import java.util.List;

/**
 * View model class that represents the information needed for the stock take page.
 */
public class InventoryClosingBalanceUpdateStockTake extends BaseOpenmrsObject {
	public static final long serialVersionUID = 0L;

	private List<ClosingBalanceUpdateModel> closingBalanceUpdateModel;
	private String operationNumber;
	private String stockroom;

	//private Department department;

	public String getOperationNumber() {
		return operationNumber;
	}

	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}

	public String getStockroom() {
		return stockroom;
	}

	public void setStockroom(String stockroom) {
		this.stockroom = stockroom;
	}

	public List<ClosingBalanceUpdateModel> getClosingBalanceUpdateModel() {
		return closingBalanceUpdateModel;
	}

	public void setClosingBalanceUpdateModel(List<ClosingBalanceUpdateModel> closingBalanceUpdateModel) {
		this.closingBalanceUpdateModel = closingBalanceUpdateModel;
	}

	//	public Department getDepartment() {
	//		return department;
	//	}
	//
	//	public void setDepartment(Department department) {
	//		this.department = department;
	//	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {

	}
}
