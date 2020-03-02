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
public class ConsumptionSummary extends BaseSerializableOpenmrsMetadata {

	private Integer totalQuantityReceived;
	private Integer totalQuantityConsumed;
	private Item item;
	private Department department;
	private String uuid;
	private Integer consumptionSummaryId;

	public Integer getTotalQuantityReceived() {
		return totalQuantityReceived;
	}

	public void setTotalQuantityReceived(Integer totalQuantityReceived) {
		this.totalQuantityReceived = totalQuantityReceived;
	}

	public Integer getTotalQuantityConsumed() {
		return totalQuantityConsumed;
	}

	public void setTotalQuantityConsumed(Integer totalQuantityConsumed) {
		this.totalQuantityConsumed = totalQuantityConsumed;
	}

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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public Integer getId() {
		return this.consumptionSummaryId;
	}

	@Override
	public void setId(Integer intgr) {
		this.consumptionSummaryId = intgr;
	}

	@Override
	@JsonIgnore
	public Boolean getRetired() {
		return super.getRetired();
	}

}
