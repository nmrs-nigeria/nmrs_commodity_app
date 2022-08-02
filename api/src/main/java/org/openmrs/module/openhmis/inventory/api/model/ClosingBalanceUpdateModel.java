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
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;

import org.hibernate.annotations.Entity;
import org.openmrs.BaseOpenmrsObject;

/**
 * Base model class used by models that have item stock detail information.
 */
@Entity
public class ClosingBalanceUpdateModel extends BaseOpenmrsObject {

	public static final long serialVersionUID = 0L;

	private Integer id;
	private String reportingPeriod;
	private String reportingYear;
	private String stockroomType;
	private Item item;
	private String packSize;
	private String strength;
	private Integer calculatedClosingBalance;
	private Integer updatedClosingBalance;
	private Date dateCreated;
	//pharmacy
	private Integer atmQuantity;
	private Integer comPharmacyQuantity;
	private Integer communityARTGroupsQuantity;
	private Integer courierDeliveryQuantity;
	private Integer dispensaryQuantity;
	private Integer patentMedicineStoreQuantity;
	private Integer privateClinicsQuantity;
	private Integer othersQuantity;
	private Integer bulkStockQuantity;
	//lab
	private Integer ancQuantity;
	private Integer comQuantity;
	private Integer eidQuantity;
	private Integer emergQuantity;
	private Integer fpQuantity;
	private Integer inpatQuantity;
	private Integer landdQuantity;
	private Integer labQuantity;
	private Integer malQuantity;
	private Integer mobQuantity;
	private Integer opdQuantity;
	private Integer ossQuantity;
	private Integer othQuantity;
	private Integer paedQuantity;
	private Integer ppQuantity;
	private Integer stiQuantity;
	private Integer tbQuantity;
	private Integer vctQuantity;
	private Integer labStockQuantity;

	public String getReportingPeriod() {
		return reportingPeriod;
	}

	public void setReportingPeriod(String reportingPeriod) {
		this.reportingPeriod = reportingPeriod;
	}

	public String getReportingYear() {
		return reportingYear;
	}

	public void setReportingYear(String reportingYear) {
		this.reportingYear = reportingYear;
	}

	public String getStockroomType() {
		return stockroomType;
	}

	public void setStockroomType(String stockroomType) {
		this.stockroomType = stockroomType;
	}

	public String getPackSize() {
		return packSize;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public Integer getCalculatedClosingBalance() {
		return calculatedClosingBalance;
	}

	public void setCalculatedClosingBalance(Integer calculatedClosingBalance) {
		this.calculatedClosingBalance = calculatedClosingBalance;
	}

	public Integer getUpdatedClosingBalance() {
		return updatedClosingBalance;
	}

	public void setUpdatedClosingBalance(Integer updatedClosingBalance) {
		this.updatedClosingBalance = updatedClosingBalance;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getAtmQuantity() {
		return atmQuantity;
	}

	public void setAtmQuantity(Integer atmQuantity) {
		this.atmQuantity = atmQuantity;
	}

	public Integer getComPharmacyQuantity() {
		return comPharmacyQuantity;
	}

	public void setComPharmacyQuantity(Integer comPharmacyQuantity) {
		this.comPharmacyQuantity = comPharmacyQuantity;
	}

	public Integer getCommunityARTGroupsQuantity() {
		return communityARTGroupsQuantity;
	}

	public void setCommunityARTGroupsQuantity(Integer communityARTGroupsQuantity) {
		this.communityARTGroupsQuantity = communityARTGroupsQuantity;
	}

	public Integer getCourierDeliveryQuantity() {
		return courierDeliveryQuantity;
	}

	public void setCourierDeliveryQuantity(Integer courierDeliveryQuantity) {
		this.courierDeliveryQuantity = courierDeliveryQuantity;
	}

	public Integer getDispensaryQuantity() {
		return dispensaryQuantity;
	}

	public void setDispensaryQuantity(Integer dispensaryQuantity) {
		this.dispensaryQuantity = dispensaryQuantity;
	}

	public Integer getPatentMedicineStoreQuantity() {
		return patentMedicineStoreQuantity;
	}

	public void setPatentMedicineStoreQuantity(Integer patentMedicineStoreQuantity) {
		this.patentMedicineStoreQuantity = patentMedicineStoreQuantity;
	}

	public Integer getPrivateClinicsQuantity() {
		return privateClinicsQuantity;
	}

	public void setPrivateClinicsQuantity(Integer privateClinicsQuantity) {
		this.privateClinicsQuantity = privateClinicsQuantity;
	}

	public Integer getOthersQuantity() {
		return othersQuantity;
	}

	public void setOthersQuantity(Integer othersQuantity) {
		this.othersQuantity = othersQuantity;
	}

	public Integer getAncQuantity() {
		return ancQuantity;
	}

	public void setAncQuantity(Integer ancQuantity) {
		this.ancQuantity = ancQuantity;
	}

	public Integer getComQuantity() {
		return comQuantity;
	}

	public void setComQuantity(Integer comQuantity) {
		this.comQuantity = comQuantity;
	}

	public Integer getEidQuantity() {
		return eidQuantity;
	}

	public void setEidQuantity(Integer eidQuantity) {
		this.eidQuantity = eidQuantity;
	}

	public Integer getEmergQuantity() {
		return emergQuantity;
	}

	public void setEmergQuantity(Integer emergQuantity) {
		this.emergQuantity = emergQuantity;
	}

	public Integer getFpQuantity() {
		return fpQuantity;
	}

	public void setFpQuantity(Integer fpQuantity) {
		this.fpQuantity = fpQuantity;
	}

	public Integer getInpatQuantity() {
		return inpatQuantity;
	}

	public void setInpatQuantity(Integer inpatQuantity) {
		this.inpatQuantity = inpatQuantity;
	}

	public Integer getLanddQuantity() {
		return landdQuantity;
	}

	public void setLanddQuantity(Integer landdQuantity) {
		this.landdQuantity = landdQuantity;
	}

	public Integer getLabQuantity() {
		return labQuantity;
	}

	public void setLabQuantity(Integer labQuantity) {
		this.labQuantity = labQuantity;
	}

	public Integer getMalQuantity() {
		return malQuantity;
	}

	public void setMalQuantity(Integer malQuantity) {
		this.malQuantity = malQuantity;
	}

	public Integer getMobQuantity() {
		return mobQuantity;
	}

	public void setMobQuantity(Integer mobQuantity) {
		this.mobQuantity = mobQuantity;
	}

	public Integer getOpdQuantity() {
		return opdQuantity;
	}

	public void setOpdQuantity(Integer opdQuantity) {
		this.opdQuantity = opdQuantity;
	}

	public Integer getOssQuantity() {
		return ossQuantity;
	}

	public void setOssQuantity(Integer ossQuantity) {
		this.ossQuantity = ossQuantity;
	}

	public Integer getOthQuantity() {
		return othQuantity;
	}

	public void setOthQuantity(Integer othQuantity) {
		this.othQuantity = othQuantity;
	}

	public Integer getPaedQuantity() {
		return paedQuantity;
	}

	public void setPaedQuantity(Integer paedQuantity) {
		this.paedQuantity = paedQuantity;
	}

	public Integer getPpQuantity() {
		return ppQuantity;
	}

	public void setPpQuantity(Integer ppQuantity) {
		this.ppQuantity = ppQuantity;
	}

	public Integer getStiQuantity() {
		return stiQuantity;
	}

	public void setStiQuantity(Integer stiQuantity) {
		this.stiQuantity = stiQuantity;
	}

	public Integer getTbQuantity() {
		return tbQuantity;
	}

	public void setTbQuantity(Integer tbQuantity) {
		this.tbQuantity = tbQuantity;
	}

	public Integer getVctQuantity() {
		return vctQuantity;
	}

	public void setVctQuantity(Integer vctQuantity) {
		this.vctQuantity = vctQuantity;
	}

	public Integer getBulkStockQuantity() {
		return bulkStockQuantity;
	}

	public void setBulkStockQuantity(Integer bulkStockQuantity) {
		this.bulkStockQuantity = bulkStockQuantity;
	}

	public Integer getLabStockQuantity() {
		return labStockQuantity;
	}

	public void setLabStockQuantity(Integer labStockQuantity) {
		this.labStockQuantity = labStockQuantity;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * @param item The item.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

}
