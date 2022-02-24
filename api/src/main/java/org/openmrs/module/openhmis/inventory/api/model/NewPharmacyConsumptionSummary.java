/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author MORRISON.I
 */
public class NewPharmacyConsumptionSummary extends BaseSerializableOpenmrsMetadata {

	private Integer totalQuantityReceived;
	private String item;
	private String uuid;
	private String groupUuid;
	private Integer consumptionSummaryId;
	private String drugCategory;
	private String serviceDeliveryModel;
	private String deliveryType;
	private Integer itemConceptId;

	public Integer getTotalQuantityReceived() {
		return totalQuantityReceived;
	}

	public void setTotalQuantityReceived(Integer totalQuantityReceived) {
		this.totalQuantityReceived = totalQuantityReceived;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getConsumptionSummaryId() {
		return consumptionSummaryId;
	}

	public void setConsumptionSummaryId(Integer consumptionSummaryId) {
		this.consumptionSummaryId = consumptionSummaryId;
	}

	public String getDrugCategory() {
		return drugCategory;
	}

	public void setDrugCategory(String drugCategory) {
		this.drugCategory = drugCategory;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupUuid) {
		this.groupUuid = groupUuid;
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

	public Integer getItemConceptId() {
		return itemConceptId;
	}

	public void setItemConceptId(Integer itemConceptId) {
		this.itemConceptId = itemConceptId;
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
