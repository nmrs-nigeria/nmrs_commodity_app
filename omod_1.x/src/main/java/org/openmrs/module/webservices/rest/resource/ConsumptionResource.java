/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.Date;
import java.util.UUID;
import org.openmrs.module.ModuleConstants;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.openhmis.inventory.api.IConsumptionDataService;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;

/**
 * @author MORRISON.I
 */

@Resource(name = ModuleRestConstants.CONSUMPTION_RESOURCE, supportedClass = Consumption.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ConsumptionResource extends BaseRestMetadataResource<Consumption> {

	//	private IConsumptionDataService iConsumptionDataService;

	//	public ConsumptionResource(IConsumptionDataService iConsumptionDataService) {
	//		this.iConsumptionDataService = iConsumptionDataService;
	//	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("item", Representation.REF);
		description.addProperty("department", Representation.REF);
		description.addProperty("consumptionDate", Representation.DEFAULT);
		description.addProperty("dateCreated", Representation.DEFAULT);
		description.addProperty("quantity");
		description.addProperty("wastage");

		if (!(rep instanceof RefRepresentation)) {
			description.addProperty("creator", Representation.DEFAULT);
		}

		System.out.println("PRINTING RESOURCE REF");
		System.out.println(description.toString());

		return description;
	}

	@PropertySetter("consumptionDate")
	public void setConsumptionDate(Consumption instance, String dateText) {
		Date date = Utility.parseOpenhmisDateString(dateText);
		if (date == null) {
			throw new IllegalArgumentException("Could not parse '" + dateText + "' as a date.");
		}

		instance.setConsumptionDate(date);
	}

	@Override
	public Consumption save(Consumption entity) {

		System.out.println("PRINTING ENTITIES BEFORE A SAVE");
		//	System.out.println(entity.toString());

		//	entity.setUuid(UUID.randomUUID().toString());

		return super.save(entity); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Consumption newDelegate() {
		return new Consumption();
	}

	@Override
	public Class<? extends IMetadataDataService<Consumption>> getServiceClass() {
		return IConsumptionDataService.class;
	}

}
