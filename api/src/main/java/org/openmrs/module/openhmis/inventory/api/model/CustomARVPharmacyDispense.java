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
import org.openmrs.module.openhmis.commons.api.entity.model.IInstanceAttribute;

/**
 * @author MORRISON.I
 */
public class CustomARVPharmacyDispense extends BaseInstanceCustomizableMetadata
        implements Comparable<CustomARVPharmacyDispense> {
	public static final long serialVersionUID = 2L;

	private Integer id;
	private Date dispenseDate;
	private String patientCategory;
	private String treatmentType;
	private String visitType;
	private String pickupReason;
	private Set<CustomARVDispensedItem> items;
	private Department department;
	protected Patient patient;

	public Date getDispenseDate() {
		return dispenseDate;
	}

	public void setDispenseDate(Date dispenseDate) {
		this.dispenseDate = dispenseDate;
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

	public CustomARVDispensedItem addItem(Item item, int quantityPrescribed, int quantityDispensed) {
		return addItem(item, quantityPrescribed, quantityDispensed, null, null, null);
	}

	public CustomARVDispensedItem addItem(Item item, int quantityPrescribed,
	        int quantityDispensed, Date expiration, String itemBatch) {
		return addItem(item, quantityPrescribed, quantityDispensed, expiration, itemBatch, null);
	}

	public CustomARVDispensedItem addItem(Item item, int quantityPrescribed,
	        int quantityDispensed, Date expiration, String itemBatch,
	        CustomARVPharmacyDispense batchOperation) {
		if (item == null) {
			throw new IllegalArgumentException("The item must be defined");
		}

		CustomARVDispensedItem operationItem = new CustomARVDispensedItem();
		operationItem.setItem(item);
		operationItem.setQuantityDispensed(quantityDispensed);
		operationItem.setQuantityPrescribed(quantityPrescribed);
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

	public CustomARVDispensedItem addItem(CustomARVDispensedItem item) {
		if (item == null) {
			throw new IllegalArgumentException("This item to add must be defined.");
		}

		if (items == null) {
			items = new HashSet<CustomARVDispensedItem>();
		}

		item.setDispensary(this);

		items.add(item);

		return item;
	}

	public void removeItem(CustomARVDispensedItem item) {
		if (item != null) {
			if (items == null) {
				return;
			}

			items.remove(item);
		}
	}

	public Set<CustomARVDispensedItem> getItems() {
		return items;
	}

	public void setItems(Set<CustomARVDispensedItem> items) {
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
	public int compareTo(CustomARVPharmacyDispense o) {
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

	@Override
	public void addAttribute(IInstanceAttribute arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeAttribute(IInstanceAttribute arg0) {
		// TODO Auto-generated method stub
	}

}
