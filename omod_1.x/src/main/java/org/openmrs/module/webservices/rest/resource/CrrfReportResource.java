/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.IMetadataDataService;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.ICrrfReportDataService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionSummaryDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTransactionDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTypeDataService;
import org.openmrs.module.openhmis.inventory.api.model.Crrf;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.IStockOperationType;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.web.ModuleRestConstants;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;

/**
 * @author MORRISON.I
 */
@Resource(name = ModuleRestConstants.CRRF_REPORT_RESOURCE,
        supportedClass = CrrfReportResource.class,
        supportedOpenmrsVersions = { "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class CrrfReportResource extends BaseRestMetadataResource<Crrf> {

	private static final Log LOG = LogFactory.getLog(CrrfReportResource.class);

	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;
	private IStockOperationDataService stockOperationDataService;
	private IStockOperationTransactionDataService stockOperationTransactionDataService;
	private IPharmacyConsumptionDataService consumptionDataService;
	private IStockOperationTypeDataService stockOperationTypeDataService;
	private List<Item> distinctItems = null;
	private IARVPharmacyDispenseService iARVPharmacyDispenseService;

	public CrrfReportResource() {
		this.itemDataService = Context.getService(IItemDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
		this.stockOperationTransactionDataService = Context.getService(IStockOperationTransactionDataService.class);
		this.consumptionDataService = Context.getService(IPharmacyConsumptionDataService.class);
		this.stockOperationTypeDataService = Context.getService(IStockOperationTypeDataService.class);
		this.iARVPharmacyDispenseService = Context.getService(IARVPharmacyDispenseService.class);
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addProperty("crrfReportId");
		description.addProperty("facilityName");
		description.addProperty("facilityCode");
		description.addProperty("lga");
		description.addProperty("state");
		description.addProperty("reportingPeriodStart");
		description.addProperty("reportingPeriodEnd");
		description.addProperty("datePrepared");
		description.addProperty("crrfAdultRegimenCategory");
		description.addProperty("crrfPediatricRegimenCategory");
		description.addProperty("crrfOIRegimenCategory");
		description.addProperty("crrfAdvanceHIVRegimenCategory");
		description.addProperty("crrfTBRegimenCategory");
		description.addProperty("crrfSTIRegimenCategory");

		return description;
	}

	@Override
	public Crrf newDelegate() {
		return new Crrf();
	}

	@Override
	public Class<? extends IMetadataDataService<Crrf>> getServiceClass() {
		return ICrrfReportDataService.class;
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {

		//Department searchDepartment = getDepartment(context);
		Date startDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("startDate"));
		Date endDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("endDate"));
		String crrfType = context.getParameter("crffRportType");

		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

		System.out.println("Start Date doSearch: " + startDate);
		System.out.println("End Date doSearch: " + endDate);
		System.out.println("CrrfType doSearch: " + crrfType);

		List<NewPharmacyConsumptionSummary> finalConsumptionSummarys =
		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, pagingInfo);

		return new AlreadyPagedWithLength<NewPharmacyConsumptionSummary>(context, finalConsumptionSummarys,
		        pagingInfo.hasMoreResults(),
		        pagingInfo.getTotalRecordCount());

	}

	private PageableResult searchConsumptionSummary(RequestContext context) {

		List<PharmacyConsumptionSummary> fromConsumption = null;
		List<PharmacyConsumptionSummary> fromReceived = null;
		List<PharmacyConsumptionSummary> finalConsumptionSummarys = null;

		PagingInfo pagingInfo = PagingUtil.getPagingInfoFromContext(context);

		Item searchItem = getItem(context);
		Department searchDepartment = getDepartment(context);
		StockOperationStatus status = StockOperationStatus.COMPLETED;
		IStockOperationType stockOperationType = getStockOperationType(context);
		Date startDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("startDate"));
		Date endDate = RestUtils.parseCustomOpenhmisDateString(context.getParameter("endDate"));
		String crrfType = context.getParameter("crrfCategory");

		System.out.println("Start Date searchConsumptionSummary: " + startDate);
		System.out.println("End Date searchConsumptionSummary: " + endDate);
		System.out.println("CrrfType searchConsumptionSummary: " + crrfType);

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();

		searchConsumptionSummary.setDepartment(searchDepartment);
		searchConsumptionSummary.setItem(searchItem);
		searchConsumptionSummary.setStartDate(startDate);
		searchConsumptionSummary.setEndDate(endDate);
		searchConsumptionSummary.setOperationStatus(status);
		searchConsumptionSummary.setOperationType(stockOperationType);

		//	List<StockOperation> stockOps = null;
		//	List<PharmacyConsumption> consumptions = new ArrayList<>();

		//	stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, pagingInfo);

		//	System.out.println("stock operations result: " + stockOps.size());

		//	finalConsumptionSummarys = consumptionDataService.retrieveConsumptionSummary(stockOps,
		//	    searchConsumptionSummary, pagingInfo, distinctItems);

		//	System.out.println("final consumption summary is " + finalConsumptionSummarys.size());

		return new AlreadyPagedWithLength<PharmacyConsumptionSummary>(context, finalConsumptionSummarys,
		        pagingInfo.hasMoreResults(),
		        pagingInfo.getTotalRecordCount());

	}

	protected StockOperationStatus getStatus(RequestContext context) {
		StockOperationStatus status = null;
		String statusText = context.getParameter("operation_status");
		if (StringUtils.isNotEmpty(statusText)) {
			status = StockOperationStatus.valueOf(statusText.toUpperCase());

			if (status == null) {
				LOG.warn("Could not parse Stock Operation Status '" + statusText + "'");
				throw new IllegalArgumentException("The status '" + statusText + "' is not a valid operation status.");
			}
		}

		return status;
	}

	private IStockOperationType getStockOperationType(RequestContext context) {
		IStockOperationType stockOperationType = null;
		String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
		if (StringUtils.isNotEmpty(stockOperationTypeUuid)) {
			stockOperationType = stockOperationTypeDataService.getByUuid(stockOperationTypeUuid);
			if (stockOperationType == null) {
				LOG.warn("Could not parse Stock Operation Type '" + stockOperationTypeUuid + "'");
				throw new IllegalArgumentException("The type '" + stockOperationTypeUuid
				        + "' is not a valid operation type.");
			}
		}

		return stockOperationType;
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
