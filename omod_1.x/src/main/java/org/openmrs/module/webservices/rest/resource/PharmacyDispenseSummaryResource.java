/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.IConsumptionSummaryDataService;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.model.ConsumptionSummary;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.PHARMACY_DISPENSE_SUMMARY_RESOURCE, supportedClass = ARVPharmacyDispense.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class PharmacyDispenseSummaryResource extends BaseRestMetadataResource<ARVPharmacyDispense> {

	private static final Log LOG = LogFactory.getLog(PharmacyDispenseSummaryResource.class);
	private IARVPharmacyDispenseService iARVPharmacyDispenseService;

	public PharmacyDispenseSummaryResource() {
		this.iARVPharmacyDispenseService = Context.getService(IARVPharmacyDispenseService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("items", Representation.REF);
		description.addProperty("patientCategory");
		description.addProperty("treatmentType");
		description.addProperty("visitType");
		description.addProperty("pickupReason");
		description.addProperty("dateOfDispensed");
		description.addProperty("uuid");
		description.addProperty("patientID");
		description.addProperty("patientDBId");
		description.addProperty("encounterId");
		description.addProperty("treatmentAge");
		description.addProperty("currentLine");
		description.addProperty("currentRegimen");

		return description;
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		Date startDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("startDate"));
		Date endDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("endDate"));
		String encounterId = "";

		System.out.println(context.getParameter("uuid"));

		if (context.getParameter("uuid") != null) {
			encounterId = context.getParameter("uuid");
		}

		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

		System.out.println("started calling ARVPharmacy");
		List<ARVPharmacyDispense> aRVPharmacyDispenses = null;
		ARVPharmacyDispense aRVPharmacyDispense = null;

		if (encounterId.isEmpty() || encounterId.equals("")) {

			aRVPharmacyDispenses =
			        iARVPharmacyDispenseService.getARVs(startDate, endDate, pagingInfo);

			return new AlreadyPagedWithLength<ARVPharmacyDispense>(context, aRVPharmacyDispenses,
			        pagingInfo.hasMoreResults(),
			        pagingInfo.getTotalRecordCount());
		}

		aRVPharmacyDispense =
		        iARVPharmacyDispenseService.getARVsByUuid(Integer.parseInt(encounterId));

		return (PageableResult)aRVPharmacyDispense;

		//  return super.doSearch(context); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public ARVPharmacyDispense newDelegate() {
		return new ARVPharmacyDispense();
	}

	@Override
	public Class<? extends IMetadataDataService<ARVPharmacyDispense>> getServiceClass() {
		return IARVPharmacyDispenseService.class;
	}

}
