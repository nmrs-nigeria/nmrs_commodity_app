package org.openmrs.module.openhmis.inventory.api.model;

import org.openmrs.module.openhmis.commons.api.entity.model.BaseSerializableOpenmrsMetadata;

/**
 * @author Toyeeb Abdulfatai
 */
public class CrffOperationsSummary extends BaseSerializableOpenmrsMetadata {

	private Integer id;
	private Integer totalPositiveAdjustment;
	private Integer totalNegativeAdjustment;
	private Integer totalLossDamagesExpires;
	private Item item;
	
	public Integer getTotalPositiveAdjustment() {
		return totalPositiveAdjustment;
	}
	
	public void setTotalPositiveAdjustment(Integer totalPositiveAdjustment) {
		this.totalPositiveAdjustment = totalPositiveAdjustment;
	}
	
	public Integer getTotalNegativeAdjustment() {
		return totalNegativeAdjustment;
	}
	
	public void setTotalNegativeAdjustment(Integer totalNegativeAdjustment) {
		this.totalNegativeAdjustment = totalNegativeAdjustment;
	}
	
	public Integer getTotalLossDamagesExpires() {
		return totalLossDamagesExpires;
	}
	
	public void setTotalLossDamagesExpires(Integer totalLossDamagesExpires) {
		this.totalLossDamagesExpires = totalLossDamagesExpires;
	}
	
	public Item getItem() {
		return item;
	}
	
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
