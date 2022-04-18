package org.openmrs.module.openhmis.inventory.api.model;

import org.openmrs.Concept;
import org.openmrs.OpenmrsObject;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

import java.util.Set;

public class ClosingbalanceUpdate implements OpenmrsObject {

	public static final long serialVersionUID = 1L;

	private Integer itemId;
	private String itemName;
	private String itemUUID;
	private String unitOfMeasure;
	private String itemType;
	private Integer closingBalanceQty;
	private String drugStrenght;
	private Integer packSize;

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public void setId(Integer id) {

	}

	@Override
	public String getUuid() {
		return null;
	}

	@Override
	public void setUuid(String uuid) {

	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemUUID() {
		return itemUUID;
	}

	public void setItemUUID(String itemUUID) {
		this.itemUUID = itemUUID;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getClosingBalanceQty() {
		return closingBalanceQty;
	}

	public void setClosingBalanceQty(Integer closingBalanceQty) {
		this.closingBalanceQty = closingBalanceQty;
	}

	public String getDrugStrenght() {
		return drugStrenght;
	}

	public void setDrugStrenght(String drugStrenght) {
		this.drugStrenght = drugStrenght;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
}
