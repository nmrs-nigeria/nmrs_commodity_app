/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.openmrs.module.openhmis.commons.api.entity.model.BaseSimpleCustomizableMetadata;
import org.openmrs.module.openhmis.commons.api.entity.model.ISimpleCustomizable;

/**
 * @author MORRISON.I
 */
public class Consumption extends BaseSimpleCustomizableMetadata<ConsumptionAttribute>
        implements ISimpleCustomizable<ConsumptionAttribute> {
	public static final long serialVersionUID = 1L;

	private Integer consumption_id;
	private Department department;
	private Item item;
	private Date consumptionDate;
	private Integer quantity;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public Integer getId() {
		return this.consumption_id;
	}

	@Override
	public void setId(Integer intgr) {
		this.consumption_id = intgr;
	}

	@Override
	@JsonIgnore
	public Boolean getRetired() {
		return super.getRetired();
	}

}
