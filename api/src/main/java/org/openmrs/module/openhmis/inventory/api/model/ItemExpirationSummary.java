/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author MORRISON.I
 */
public class ItemExpirationSummary implements OpenmrsObject {

	private Item item;
	//private Department department;
	private Integer quantity;
	private Date expiration;
	//private Integer itemExpirationId;
	private String itemBatch;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer intgr) {

	}

	@Override
	public String getUuid() {
		return null;
	}

	@Override
	public void setUuid(String string) {

	}

}
