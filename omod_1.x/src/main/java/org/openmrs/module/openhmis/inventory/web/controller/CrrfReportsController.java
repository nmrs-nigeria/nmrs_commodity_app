/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IInstitutionDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemExpirationSummaryService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyReportsService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTypeDataService;
import org.openmrs.module.openhmis.inventory.api.model.CrffOperationsSummary;
import org.openmrs.module.openhmis.inventory.api.model.Crrf;
import org.openmrs.module.openhmis.inventory.api.model.CrrfDetails;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.IStockOperationType;
import org.openmrs.module.openhmis.inventory.api.model.Institution;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummaryReport;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchStockOnHandSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.RestUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author MORRISON.I
 */
@Controller(value = "invCrrfReportsController")
@RequestMapping(ModuleWebConstants.CRRF_REPORT_ROOT)
public class CrrfReportsController {

	private IStockOperationTypeDataService stockOperationTypeDataService;
	private IDepartmentDataService departmentService;
	private IPharmacyConsumptionDataService consumptionDataService;
	private IStockOperationDataService stockOperationDataService;
	private IItemDataService itemDataService;
	private IPharmacyReportsService iPharmacyReports;
	private IItemExpirationSummaryService itemExpirationSummaryService;
	private IARVPharmacyDispenseService iARVPharmacyDispenseService;

	private IInstitutionDataService institutionDataService;

	private Date startDate;
	private Date endDate;
	private String startDateStringVal;
	private String endDateStringVal;
	List<Department> dispensarys = null;
	HttpServletRequest request = null;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SimpleObject get(@RequestParam(value = "reportId", required = true) String reportId,
	        @RequestParam(value = "startDate", required = true) String startDateString,
	        @RequestParam(value = "endDate", required = true) String endDateString,
	        @RequestParam(value = "crrfCategory", required = false) String crrfCategory,
	        HttpServletRequest request, HttpServletResponse response) {

		this.stockOperationTypeDataService = Context.getService(IStockOperationTypeDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.consumptionDataService = Context.getService(IPharmacyConsumptionDataService.class);
		this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
		this.itemDataService = Context.getService(IItemDataService.class);
		this.itemExpirationSummaryService = Context.getService(IItemExpirationSummaryService.class);

		this.institutionDataService = Context.getService(IInstitutionDataService.class);

		this.startDate = RestUtils.parseCustomOpenhmisDateString(startDateString);
		this.endDate = RestUtils.parseCustomOpenhmisDateString(endDateString);
		this.startDateStringVal = startDateString;
		this.endDateStringVal = endDateString;
		this.iPharmacyReports = Context.getService(IPharmacyReportsService.class);
		this.iARVPharmacyDispenseService = Context.getService(IARVPharmacyDispenseService.class);

		SimpleObject result = new SimpleObject();
		this.request = request;
		System.out.println("retreive data, about to route for crrf: " + crrfCategory);

		System.out.println("reportId: " + reportId);
		System.out.println("Start Date get: " + startDate);
		System.out.println("End Date get: " + endDate);
		System.out.println("CrrfType get: " + crrfCategory);

		String path = routeReport(reportId, startDate, endDate, crrfCategory);

		result.put("results", path);

		return result;

	}

	private String routeReport(String reportId, Date startDate, Date endDate, String crrfCategory) {

		if (crrfCategory.equalsIgnoreCase("ARV Cotrim")) {
			reportId = ConstantUtils.ARV_COTRIM;
			System.out.println("ARV Cotrim: " + crrfCategory);

			aRVCotrimCrffReport(reportId, startDate, endDate);
			return returnURL(reportId);

		}

		if (crrfCategory.equalsIgnoreCase("HIV RTKS and DBS")) {
			reportId = ConstantUtils.HIV_RTKS_AND_DBS;
			System.out.println("HIV RTKS and DBS: " + crrfCategory);
			//  HIVRTKSDBSCrffReport(reportId, startDate, endDate);
			return returnURL(reportId);

		}

		if (crrfCategory.equalsIgnoreCase("Other OIs")) {
			reportId = ConstantUtils.OTHER_OIS;
			System.out.println("Other OIs: " + crrfCategory);
			//   OtherOIsCrffReport(reportId, startDate, endDate);
			return returnURL(reportId);

		}

		return null;

	}

	private String aRVCotrimCrffReport(String reportId, Date startDate, Date endDate) {

		Crrf crrf = new Crrf();

		//facilityName
		String facilityName = RestUtils.getFacilityName();
		System.out.println("facilityName: " + facilityName);
		crrf.setFacilityName(facilityName);

		//facilityCode
		String facilityCode = RestUtils.getFacilityDATIMId();
		System.out.println("facilityCode: " + facilityCode);
		crrf.setFacilityCode(facilityCode);

		//state and lga
		List<Institution> instList = institutionDataService.getInstitutionByUuid(facilityCode);
		String state = "";
		String lga = "";
		for (Institution institution : instList) {
			state = institution.getState();
			lga = institution.getLga();
		}

		System.out.println("state: " + state);
		System.out.println("lga: " + lga);
		crrf.setState(state);
		crrf.setLga(lga);

		//reporting period start
		System.out.println("startDate: " + startDate);
		crrf.setReportingPeriodStart(startDate);

		//reporting period end
		System.out.println("endDate: " + endDate);
		crrf.setReportingPeriodEnd(endDate);

		//date prepared
		Date datePrepared = new Date();
		System.out.println("datePrepared: " + datePrepared);
		crrf.setDatePrepared(datePrepared);

		//crrfAdultRegimenCategory
		List<CrrfDetails> crrfAdultRegimenCategory = new ArrayList<CrrfDetails>();
		crrfAdultRegimenCategory = getCrrfAdultRegimenCategory(startDate, endDate);

		//SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		//List<NewPharmacyConsumptionSummary> finalConsumptionSummarys = new ArrayList<>();

		//	finalConsumptionSummarys = aRVPharmacyDispenseService
		//	        .getAdultDrugDispenseSummaryByModalities(startDate, endDate);

		// String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		//	return iPharmacyReports
		//	        .getAdultModalitiesPharmacyConsumptionByDate(reportId, finalConsumptionSummarys, reportFolder);

		return null;
	}

	private List<CrrfDetails> getCrrfAdultRegimenCategory(Date startDate, Date endDate) {
		List<CrrfDetails> crrfAdultRegimen = new ArrayList<CrrfDetails>();
		
		//get distinct items for adult regimen line as base.
	  	List<Item> distinctAdultItemBase = new ArrayList<Item>();
	  	distinctAdultItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.ADULT))
                 .collect(Collectors.toList());
       
        List<Item> distinctElements = new ArrayList<Item>();
        for(Item it : distinctAdultItemBase) {
        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
        		distinctElements.add(it);
        	}            		
        }
                                           
	  	System.out.println("Adult Item Size: " + distinctAdultItemBase.size());	
	  	System.out.println("Distinct Adult Item Size: " + distinctElements.size());
	  	
	  	//total consumed
	  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
	  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

	  	System.out.println("forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
	  	System.out.println("forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
	  	
	  	//total received
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
	  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
	  	
	  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
	  	//List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
	  	//		consumptionSummaryAtStockroom(null, startDate, distinctElements);
	  	
	  	
	  	//positive adjustment, negative adjustment, loses/damages/expires
	  	List<CrffOperationsSummary> crffOperationsSummary = 
	  			positiveAndNegativeAdjustment(startDate, endDate, distinctElements);
	  
  		int i = 0;
	  	for(Item item: distinctElements) {
	  		CrrfDetails crrfDetails = new CrrfDetails();
	  		crrfDetails.setDrugs(item);
	  		crrfDetails.setBasicUnit(item.getPackSize() +  " " + item.getUnitOfMeasure());	  		
	  		
	  		//get total quantity received for the item
	  		Optional<PharmacyConsumptionSummary> matchingObject = pharmacyConsumptionSummary.stream().
	  			    filter(p -> p.getItem().equals(item)).findFirst();
	  
	  		PharmacyConsumptionSummary pcs = matchingObject.orElse(null);
	  		if(pcs == null) {
	  			crrfDetails.setQuantityReceived(0);
	  		}else {
	  			crrfDetails.setQuantityReceived(pcs.getTotalQuantityReceived());
	  		}
	  		
	  		//get total quantity consumed for the item
	  		Optional<NewPharmacyConsumptionSummary> matchingObjectConsumed = forPharmacyConsumptionSummary.stream().
	  			    filter(a -> a.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
	  
	  		NewPharmacyConsumptionSummary pcsConsumed = matchingObjectConsumed.orElse(null);
	  		if(pcsConsumed == null) {
	  			crrfDetails.setQuantityDispensed(0);	  			
	  		}else {
	  			crrfDetails.setQuantityDispensed(pcsConsumed.getTotalQuantityReceived());
	  		}
	  		
	  		//get positive adjustment, negative adjustment and losses
	  		Optional<CrffOperationsSummary> matchingObjectAdjustment = crffOperationsSummary.stream().
	  			    filter(b -> b.getItem().equals(item)).findFirst();
	  
	  		CrffOperationsSummary pcsAdjustment = matchingObjectAdjustment.orElse(null);
	  		if(pcsAdjustment == null) {
	  			crrfDetails.setPositiveAdjustments(0);	
	  			crrfDetails.setNegativeAdjustments(0);	
	  			crrfDetails.setLossesdDamagesExpiries(0);	
	  		}else {
	  			crrfDetails.setPositiveAdjustments(pcsAdjustment.getTotalPositiveAdjustment());	
	  			crrfDetails.setNegativeAdjustments(pcsAdjustment.getTotalNegativeAdjustment());	
	  			crrfDetails.setLossesdDamagesExpiries(pcsAdjustment.getTotalLossDamagesExpires());	
	  		}
	  		
	  		//begining balance
	  		crrfDetails.setBeginningBalance(0);
	  		
	  		//physical count or stock on hand
	  		Integer physicalCount = (crrfDetails.getBeginningBalance() + crrfDetails.getQuantityReceived()
	  		+ crrfDetails.getPositiveAdjustments()) - (crrfDetails.getNegativeAdjustments()
	  				+ crrfDetails.getQuantityDispensed());
	  		crrfDetails.setPhysicalCount(physicalCount);
	  		
	  		//maximum stock
	  		crrfDetails.setMaximumStockQuantity(crrfDetails.getQuantityDispensed() * 2);
	  		
	  		//quantity to order
	  		crrfDetails.setQuantityToOrder(crrfDetails.getMaximumStockQuantity() - crrfDetails.getPhysicalCount());
	  		
	  		
	  		i++;
	  		System.out.println("Count: " + i);		  		
	  		System.out.println("Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("Basic Unit: " + crrfDetails.getBasicUnit());
	  		System.out.println("BeginningBalance: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
		  	System.out.println("QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
			System.out.println("PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("PhysicalCount: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
		  	
		  	
	  		crrfAdultRegimen.add(crrfDetails);
	  	}

		return crrfAdultRegimen;
	}

	private List<PharmacyConsumptionSummary> consumptionSummaryAtStockroom(
	        Date startDate, Date endDate, List<Item> distinctItems) {

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String distributeOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
		String receiptOperationTypeUuid = ConstantUtils.RECEIPT_TYPE_UUID;

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		List<PharmacyConsumptionSummary> finalConsumptionSummarys = new ArrayList<>();

		//searchConsumptionSummary.setDepartment(d);
		//  searchConsumptionSummary.setItem(searchItem);
		searchConsumptionSummary.setStartDate(startDate);
		searchConsumptionSummary.setEndDate(endDate);
		searchConsumptionSummary.setOperationStatus(status);
		searchConsumptionSummary.setCommodityType(ConstantUtils.PHARMACY_COMMODITY_TYPE);

		IStockOperationType stockOperationType = stockOperationTypeDataService.getByUuid(receiptOperationTypeUuid);
		searchConsumptionSummary.setOperationType(stockOperationType);

		List<StockOperation> receiptStockOps = null;
		List<StockOperation> distributeStockOps = null;

		receiptStockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		IStockOperationType distributeStockOperationType =
		        stockOperationTypeDataService.getByUuid(distributeOperationTypeUuid);

		searchConsumptionSummary.setOperationType(distributeStockOperationType);

		distributeStockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		finalConsumptionSummarys.addAll(consumptionDataService.retrieveConsumptionSummaryForStockroom(receiptStockOps,
		    distributeStockOps, null, distinctItems));

		//String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return finalConsumptionSummarys;
	}


	private List<CrffOperationsSummary> positiveAndNegativeAdjustment(
	        Date startDate, Date endDate, List<Item> distinctItems) {

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String adjustmentOperationTypeUuid = ConstantUtils.ADJUSTMENT_TYPE_UUID;
		String transferOperationTypeUuid = ConstantUtils.TRANSFER_TYPE_UUID;
		String disposedOperationTypeUuid = ConstantUtils.DISPOSED_TYPE_UUID;

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		List<CrffOperationsSummary> finalCrffOperationsSummary = new ArrayList<>();
		List<StockOperation> adjustmentStockOps = null;
		List<StockOperation> transferStockOps = null;
		List<StockOperation> disposedStockOps = null;

		searchConsumptionSummary.setStartDate(startDate);
		searchConsumptionSummary.setEndDate(endDate);
		searchConsumptionSummary.setOperationStatus(status);
		searchConsumptionSummary.setCommodityType(ConstantUtils.PHARMACY_COMMODITY_TYPE);

		//for adjustment
		IStockOperationType adjustmentStockOperationType =
				stockOperationTypeDataService.getByUuid(adjustmentOperationTypeUuid);

		searchConsumptionSummary.setOperationType(adjustmentStockOperationType);
		adjustmentStockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);
		System.out.println("adjustmentStockOps: " + adjustmentStockOps.size());

		//for transfer
		IStockOperationType transferStockOperationType =
		        stockOperationTypeDataService.getByUuid(transferOperationTypeUuid);
		searchConsumptionSummary.setOperationType(transferStockOperationType);
		transferStockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);


		System.out.println("transferStockOps: " + transferStockOps.size());

		//for transfer
		IStockOperationType disposedStockOperationType =
		        stockOperationTypeDataService.getByUuid(disposedOperationTypeUuid);
		searchConsumptionSummary.setOperationType(disposedStockOperationType);
		disposedStockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);


		System.out.println("disposedStockOps: " + disposedStockOps.size());

		finalCrffOperationsSummary.addAll(consumptionDataService.retrieveConsumptionSummaryForStockroom(adjustmentStockOps,
		    transferStockOps, disposedStockOps, null, distinctItems));

		//String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return finalCrffOperationsSummary;
	}

	private String returnURL(String reportId) {
		String filename = reportId + ".csv";
		return Paths.get(request.getContextPath(), "CMReports", filename).toString();
	}
}
