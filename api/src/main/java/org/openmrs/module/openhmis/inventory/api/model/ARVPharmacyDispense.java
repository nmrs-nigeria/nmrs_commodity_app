/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.openmrs.Patient;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseInstanceCustomizableMetadata;

/**
 * @author MORRISON.I
 */
public class ARVPharmacyDispense extends BaseInstanceCustomizableMetadata implements Comparable<ARVPharmacyDispense> {

	public static final long serialVersionUID = 2L;

	private Integer id;
	private Date dispenseDate;
	private String batchNumber;
	private String patientCategory;
	private String treatmentType;
	private String visitType;
	private String pickupReason;
	private Integer quantityPrescribed;
	private int quantityDispensed;
	private Integer duration;
	private Set<ARVDispensedItem> items;
	private Department department;
	protected Patient patient;

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

	public ARVDispensedItem addItem(Item item, int quantity) {
		return addItem(item, quantity, null, null, null);
	}

	public ARVDispensedItem addItem(Item item, int quantity, Date expiration, String itemBatch) {
		return addItem(item, quantity, expiration, itemBatch, null);
	}

	public ARVDispensedItem addItem(Item item, int quantity, Date expiration, String itemBatch,
	        ARVPharmacyDispense batchOperation) {
		if (item == null) {
			throw new IllegalArgumentException("The item must be defined");
		}

		ARVDispensedItem operationItem = new ARVDispensedItem();
		operationItem.setItem(item);
		operationItem.setQuantity(quantity);
		operationItem.setItemBatch(itemBatch);

		if (expiration == null) {
			if (Boolean.TRUE.equals(item.getHasExpiration())) {
				operationItem.setCalculatedExpiration(true);
			} else {
				operationItem.setCalculatedExpiration(false);
			}
		} else {
			operationItem.setExpiration(expiration);
			operationItem.setCalculatedExpiration(false);
		}

		if (batchOperation == null) {
			operationItem.setCalculatedBatch(true);
		} else {
			operationItem.setBatchOperation(batchOperation);
			operationItem.setCalculatedBatch(false);
		}

		return addItem(operationItem);
	}

	public ARVDispensedItem addItem(ARVDispensedItem item) {
		if (item == null) {
			throw new IllegalArgumentException("This item to add must be defined.");
		}

		if (items == null) {
			items = new HashSet<ARVDispensedItem>();
		}

		item.setDispensary(this);

		items.add(item);

		return item;
	}

	public void removeItem(StockOperationItem item) {
		if (item != null) {
			if (items == null) {
				return;
			}

			items.remove(item);
		}
	}

	public Set<ARVDispensedItem> getItems() {
		return items;
	}

	public void setItems(Set<ARVDispensedItem> items) {
		this.items = items;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int compareTo(ARVPharmacyDispense o) {
		if (o == null) {
			return 1;
		}

		int result = 0;

		if (getId() != null && o.getId() != null) {
			result = getId().compareTo(o.getId());
		}

		if (result == 0) {
			result = getUuid().compareTo(o.getUuid());
		}

		return result;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

}
