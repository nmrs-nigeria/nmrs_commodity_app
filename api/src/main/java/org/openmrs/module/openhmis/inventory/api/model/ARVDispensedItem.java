/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsObject;

/**
 * @author MORRISON.I
 */
public class ARVDispensedItem implements Serializable {

	private Integer quantityPrescribed;
	private int quantityDispensed;
	private Integer duration;
	private String itemName;
	private String drugStrength;
	private String arvPharmacyDispenseUuid;
	private String drugCategory;
	private String serviceDeliveryModel;
	private String deliveryType;
	private Integer singleDose;
	private String frequency;

	public Integer getQuantityPrescribed() {
		return quantityPrescribed;
	}

	public void setQuantityPrescribed(Integer quantityPrescribed) {
		this.quantityPrescribed = quantityPrescribed;
	}

	public int getQuantityDispensed() {
		return quantityDispensed;
	}

	public void setQuantityDispensed(int quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getArvPharmacyDispenseUuid() {
		return arvPharmacyDispenseUuid;
	}

	public void setArvPharmacyDispenseUuid(String arvPharmacyDispenseUuid) {
		this.arvPharmacyDispenseUuid = arvPharmacyDispenseUuid;
	}

	public String getDrugStrength() {
		return drugStrength;
	}

	public void setDrugStrength(String drugStrength) {
		this.drugStrength = drugStrength;
	}

	public String getDrugCategory() {
		return drugCategory;
	}

	public void setDrugCategory(String drugCategory) {
		this.drugCategory = drugCategory;
	}

	public String getServiceDeliveryModel() {
		return serviceDeliveryModel;
	}

	public void setServiceDeliveryModel(String serviceDeliveryModel) {
		this.serviceDeliveryModel = serviceDeliveryModel;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public Integer getSingleDose() {
		return singleDose;
	}

	public void setSingleDose(Integer singleDose) {
		this.singleDose = singleDose;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

}
