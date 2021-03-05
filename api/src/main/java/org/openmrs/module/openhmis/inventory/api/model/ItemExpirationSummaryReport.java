/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author MORRISON.I
 */
public class ItemExpirationSummaryReport implements OpenmrsObject {

	private Item item;
	private Department department;
	private Integer quantity;
	private Date expiration;
	private String itemBatch;
	private String uuid;
	private Integer consumptionId;
	private String exp;

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public Integer getId() {
		return this.consumptionId;
	}

	@Override
	public void setId(Integer intgr) {
		this.consumptionId = intgr;
	}

}
