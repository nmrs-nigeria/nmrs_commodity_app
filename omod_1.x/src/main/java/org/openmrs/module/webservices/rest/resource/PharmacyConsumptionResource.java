/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.search.PharmacyConsumptionSearch;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.PHARMACY_CONSUMPTION_RESOURCE, supportedClass = PharmacyConsumption.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class PharmacyConsumptionResource extends BaseRestMetadataResource<PharmacyConsumption> {

	private static final Log LOG = LogFactory.getLog(PharmacyConsumptionResource.class);

	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;

	//	private IConsumptionDataService iConsumptionDataService;
	public PharmacyConsumptionResource() {
		this.itemDataService = Context.getService(IItemDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("item", Representation.REF);
		description.addProperty("department", Representation.REF);
		description.addProperty("consumptionDate", Representation.DEFAULT);
		description.addProperty("dateCreated", Representation.DEFAULT);
		description.addProperty("quantity");
		description.addProperty("wastage");
		description.addProperty("batchNumber");

		if (!(rep instanceof RefRepresentation)) {
			description.addProperty("creator", Representation.DEFAULT);
		}

		System.out.println("PRINTING RESOURCE REF");
		System.out.println(description.toString());

		return description;
	}

	@PropertySetter("consumptionDate")
	public void setConsumptionDate(PharmacyConsumption instance, String dateText) {
		Date date = RestUtils.parseCustomOpenhmisDateString(dateText);
		if (date == null) {
			throw new IllegalArgumentException("Could not parse '" + dateText + "' as a date.");
		}

		instance.setConsumptionDate(date);
	}

	@Override
	public PharmacyConsumption save(PharmacyConsumption entity) {

		System.out.println("PRINTING ENTITIES BEFORE A SAVE");
		System.out.println(entity.toString());
		//	System.out.println(entity.toString());

		//	entity.setUuid(UUID.randomUUID().toString());
		return super.save(entity); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public PharmacyConsumption newDelegate() {
		return new PharmacyConsumption();
	}

	@Override
	public Class<? extends IMetadataDataService<PharmacyConsumption>> getServiceClass() {
		return IPharmacyConsumptionDataService.class;
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		PageableResult result;

		String item_uuid = context.getParameter("item_uuid");
		String department_uuid = context.getParameter("department_uuid");

		System.out.println("STARTED CONSUMPTION SEARCH");

		if (item_uuid != null || department_uuid != null) {
			System.out.println("STARTED A SEARCH WITH A PARAM");
			result = getOperationsByContextParams(context);

		} else {
			System.out.println("STARTED A SEARCH WITHOUT A PARAM");
			result = super.doSearch(context);
		}

		return result;
	}

	protected PageableResult getOperationsByContextParams(RequestContext context) {
		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
		Department department = getDepartment(context);
		Item item = getItem(context);

		List<PharmacyConsumption> results;
		if (department == null && item == null) {
			results = getService().getAll(context.getIncludeAll(), pagingInfo);
		} else {
			PharmacyConsumptionSearch search = new PharmacyConsumptionSearch();
			//  ConsumptionTem template = search.getTemplate();
			if (item != null) {
				search.getTemplate().setItem(item);
			}
			//			if (department != null) {
			//				search.getTemplate().setDepartment(department);
			//			}

			results = ((IPharmacyConsumptionDataService)getService())
			        .getConsumptionsByConsumptionSearch(search, pagingInfo);
		}

		return new AlreadyPagedWithLength<PharmacyConsumption>(context, results, pagingInfo.hasMoreResults(),
		        pagingInfo.getTotalRecordCount());
	}

	private Item getItem(RequestContext context) {
		Item item = null;
		String ItemUuid = context.getParameter("item_uuid");
		if (StringUtils.isNotEmpty(ItemUuid)) {
			item = itemDataService.getByUuid(ItemUuid);
			if (item == null) {
				LOG.warn("Could not parse Item '" + ItemUuid + "'");
				throw new IllegalArgumentException("The item '" + ItemUuid
				        + "' is not a valid operation type.");
			}
		}
		return item;
	}

	private Department getDepartment(RequestContext context) {
		Department department = null;
		String departmentUUID = context.getParameter("department_uuid");
		if (StringUtils.isNotEmpty(departmentUUID)) {
			department = departmentService.getByUuid(departmentUUID);
			if (department == null) {
				LOG.warn("Could not parse Department '" + departmentUUID + "'");
				throw new IllegalArgumentException("The Department '" + departmentUUID
				        + "' is not a valid operation type.");
			}
		}

		return department;
	}

}
