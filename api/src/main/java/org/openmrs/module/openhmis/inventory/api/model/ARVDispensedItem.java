/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import org.openmrs.BaseOpenmrsObject;

/**
 * @author MORRISON.I
 */
public class ARVDispensedItem extends BaseOpenmrsObject {

	private Integer id;
	private ARVPharmacyDispense dispensary;
	private Item item;
	private Integer quantity;
	private Date expiration;
	private ARVPharmacyDispense batchOperation;

	public ARVPharmacyDispense getDispensary() {
		return dispensary;
	}

	public void setDispensary(ARVPharmacyDispense dispensary) {
		this.dispensary = dispensary;
	}

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

	public ARVPharmacyDispense getBatchOperation() {
		return batchOperation;
	}

	public void setBatchOperation(ARVPharmacyDispense batchOperation) {
		this.batchOperation = batchOperation;
	}

	public Boolean getCalculatedExpiration() {
		return calculatedExpiration;
	}

	public void setCalculatedExpiration(Boolean calculatedExpiration) {
		this.calculatedExpiration = calculatedExpiration;
	}

	public Boolean getCalculatedBatch() {
		return calculatedBatch;
	}

	public void setCalculatedBatch(Boolean calculatedBatch) {
		this.calculatedBatch = calculatedBatch;
	}

	public String getItemBatch() {
		return itemBatch;
	}

	public void setItemBatch(String itemBatch) {
		this.itemBatch = itemBatch;
	}
	private Boolean calculatedExpiration;
	private Boolean calculatedBatch;
	private String itemBatch;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
