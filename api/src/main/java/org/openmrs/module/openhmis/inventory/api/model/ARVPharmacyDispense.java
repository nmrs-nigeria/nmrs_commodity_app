/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author MORRISON.I
 */
public class ARVPharmacyDispense implements Serializable {

	public static final long serialVersionUID = 2L;

	private Date dispenseDate;
	private String batchNumber;
	private String patientCategory;
	private String treatmentType;
	private String visitType;
	private String pickupReason;
	private Date dateOfDispensed;
	private String uuid;

	private Set<ARVDispensedItem> items;
	private String patientID;

	public Date getDispenseDate() {
		return dispenseDate;
	}

	public void setDispenseDate(Date dispenseDate) {
		this.dispenseDate = dispenseDate;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getPickupReason() {
		return pickupReason;
	}

	public void setPickupReason(String pickupReason) {
		this.pickupReason = pickupReason;
	}

	public Date getDateOfDispensed() {
		return dateOfDispensed;
	}

	public void setDateOfDispensed(Date dateOfDispensed) {
		this.dateOfDispensed = dateOfDispensed;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Set<ARVDispensedItem> getItems() {
		return items;
	}

	public void setItems(Set<ARVDispensedItem> items) {
		this.items = items;
	}

	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

}
