/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import org.openmrs.module.ModuleConstants;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.openhmis.inventory.api.IConsumptionDataService;

/**
 * @author MORRISON.I
 */

@Resource(name = ModuleRestConstants.CONSUMPTION_RESOURCE, supportedClass = Consumption.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ConsumptionResource extends BaseRestMetadataResource<Consumption> {

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("item", Representation.REF);
		description.addProperty("department", Representation.REF);
		description.addProperty("consumptionDate");
		description.addProperty("quantity");
		description.addProperty("wastage");

		return description;
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
