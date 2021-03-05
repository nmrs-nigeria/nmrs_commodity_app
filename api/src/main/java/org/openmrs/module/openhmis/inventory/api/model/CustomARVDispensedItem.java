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
public class CustomARVDispensedItem extends BaseOpenmrsObject {

	private Integer id;
	private CustomARVPharmacyDispense dispensary;
	private Item item;
	private Integer quantityPrescribed;
	private Integer quantityDispensed;
	private Integer duration;
	private Date expiration;
	private CustomARVPharmacyDispense batchOperation;
	private Boolean calculatedExpiration;
	private Boolean calculatedBatch;
	private String itemBatch;

	public CustomARVPharmacyDispense getDispensary() {
		return dispensary;
	}

	public void setDispensary(CustomARVPharmacyDispense dispensary) {
		this.dispensary = dispensary;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public CustomARVPharmacyDispense getBatchOperation() {
		return batchOperation;
	}

	public void setBatchOperation(CustomARVPharmacyDispense batchOperation) {
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

	public Integer getQuantityPrescribed() {
		return quantityPrescribed;
	}

	public void setQuantityPrescribed(Integer quantityPrescribed) {
		this.quantityPrescribed = quantityPrescribed;
	}

	public Integer getQuantityDispensed() {
		return quantityDispensed;
	}

	public void setQuantityDispensed(Integer quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}
