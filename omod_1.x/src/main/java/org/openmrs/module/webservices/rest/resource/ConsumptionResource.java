/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleConstants;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.Utility;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.openhmis.inventory.api.IConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.search.ConsumptionSearch;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.CONSUMPTION_RESOURCE, supportedClass = Consumption.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class ConsumptionResource extends BaseRestMetadataResource<Consumption> {

	private static final Log LOG = LogFactory.getLog(ConsumptionResource.class);

	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;
	private IConsumptionDataService consumptionService;

	//	private IConsumptionDataService iConsumptionDataService;
	public ConsumptionResource() {
		this.itemDataService = Context.getService(IItemDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.consumptionService = Context.getService(IConsumptionDataService.class);
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
		description.addProperty("testPurpose");
		description.addProperty("dataSystem");

		if (!(rep instanceof RefRepresentation)) {
			description.addProperty("creator", Representation.DEFAULT);
		}

		System.out.println("PRINTING RESOURCE REF");
		System.out.println(description.toString());

		return description;
	}

	@PropertySetter("consumptionDate")
	public void setConsumptionDate(Consumption instance, String dateText) {
		Date date = RestUtils.parseCustomOpenhmisDateString(dateText);
		if (date == null) {
			throw new IllegalArgumentException("Could not parse '" + dateText + "' as a date.");
		}

		instance.setConsumptionDate(date);
	}

	@Override
	public Consumption save(Consumption entity) {

		System.out.println("PRINTING ENTITIES BEFORE A SAVE");
		System.out.println(entity.toString());

		System.out.println("Entity UUID: " + entity.getUuid());

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

	@Override
	protected PageableResult doSearch(RequestContext context) {
		PageableResult result;

		String item_uuid = context.getParameter("item_uuid");
		String department_uuid = context.getParameter("department_uuid");
		String consumption_uuid = context.getParameter("consumption_uuid");

		if (consumption_uuid != null) {
			System.out.println("Consumption UUID IsNot Null: " + consumption_uuid);
			int rest = deleteConsumptionByUUID(context);
			result = super.doSearch(context);
		} else if (item_uuid != null || department_uuid != null) {

			System.out.println("STARTED CONSUMPTION SEARCH");

			System.out.println("STARTED A SEARCH WITH A PARAM");
			result = getOperationsByContextParams(context);

		} else {
			System.out.println("STARTED A SEARCH WITHOUT A PARAM");
			result = super.doSearch(context);
		}

		return result;
	}

	protected int deleteConsumptionByUUID(RequestContext context) {

		Consumption consumption = null;
		String consumptionUUID = context.getParameter("consumption_uuid");
		if (StringUtils.isNotEmpty(consumptionUUID)) {
			consumption = consumptionService.getByUuid(consumptionUUID);
			if (consumption == null) {
				LOG.warn("Could not parse Consumption '" + consumptionUUID + "'");
				throw new IllegalArgumentException("The Consumption '" + consumptionUUID
				        + "' is not a valid operation type.");
			}
		}

		int res = ((IConsumptionDataService)getService()).deleteConsumption(consumption);
		return res;
	}

	protected PageableResult getOperationsByContextParams(RequestContext context) {
		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);
		Department department = getDepartment(context);
		Item item = getItem(context);

		List<Consumption> results;
		if (department == null && item == null) {
			results = getService().getAll(context.getIncludeAll(), pagingInfo);
		} else {
			ConsumptionSearch search = new ConsumptionSearch();
			//  ConsumptionTem template = search.getTemplate();
			if (item != null) {
				search.getTemplate().setItem(item);
			}
			if (department != null) {
				search.getTemplate().setDepartment(department);
			}

			results = ((IConsumptionDataService)getService()).getConsumptionsByConsumptionSearch(search, pagingInfo);
		}

		return new AlreadyPagedWithLength<Consumption>(context, results, pagingInfo.hasMoreResults(),
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
