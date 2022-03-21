/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;

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

	//Gson gson = RestUtils.getGsonObject();

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public String get(@RequestParam(value = "reportId", required = true) String reportId,
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

		//		String path = routeReport(reportId, startDate, endDate, crrfCategory);
		//		result.put("results", path);

		Crrf crrf = routeReport(reportId, startDate, endDate, crrfCategory);
		//result.put("results", crrf);
		//		Gson gson = new Gson();
		//		System.out.println("Crrf: " + crrf);
		//		gson.toJson(crrf);
		//		System.out.println("Crrf: " + gson.toJson(crrf));
		//		String results = gson.toJson(crrf);
		return "results";

	}

	private Crrf routeReport(String reportId, Date startDate, Date endDate, String crrfCategory) {

		if (crrfCategory.equalsIgnoreCase("ARV Cotrim")) {
			reportId = ConstantUtils.ARV_COTRIM;
			System.out.println("ARV Cotrim: " + crrfCategory);

			Crrf crrf = aRVCotrimCrffReport(reportId, startDate, endDate);

			//String response = null;
			//response = gson.toJson(crrf);

			return crrf;

			//return returnURL(reportId);
		}

		if (crrfCategory.equalsIgnoreCase("HIV RTKS and DBS")) {
			reportId = ConstantUtils.HIV_RTKS_AND_DBS;
			System.out.println("HIV RTKS and DBS: " + crrfCategory);
			//  HIVRTKSDBSCrffReport(reportId, startDate, endDate);
			//return returnURL(reportId);

		}

		if (crrfCategory.equalsIgnoreCase("Other OIs")) {
			reportId = ConstantUtils.OTHER_OIS;
			System.out.println("Other OIs: " + crrfCategory);
			//   OtherOIsCrffReport(reportId, startDate, endDate);
			//return returnURL(reportId);

		}

		return null;

	}

	private Crrf aRVCotrimCrffReport(String reportId, Date startDate, Date endDate) {

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
		crrf.setCrrfAdultRegimenCategory(crrfAdultRegimenCategory);

		//crrfPediatricRegimenCategory
		List<CrrfDetails> crrfPediatricRegimenCategory = new ArrayList<CrrfDetails>();
		crrfPediatricRegimenCategory = getCrrfPediatricRegimenCategory(startDate, endDate);
		crrf.setCrrfPediatricRegimenCategory(crrfPediatricRegimenCategory);

		//crrfOIRegimenCategory
		List<CrrfDetails> crrfOIRegimenCategory = new ArrayList<CrrfDetails>();
		crrfOIRegimenCategory = getCrrfOIRegimenCategory(startDate, endDate);
		crrf.setCrrfOIRegimenCategory(crrfOIRegimenCategory);

		//crrfAdvanceHIVRegimenCategory
		List<CrrfDetails> crrfAdvanceHIVRegimenCategory = new ArrayList<CrrfDetails>();
		crrfAdvanceHIVRegimenCategory = getCrrfAdvanceHIVRegimenCategory(startDate, endDate);
		crrf.setCrrfAdvanceHIVRegimenCategory(crrfAdvanceHIVRegimenCategory);

		//crrfTBRegimenCategory
		List<CrrfDetails> crrfTBRegimenCategory = new ArrayList<CrrfDetails>();
		crrfTBRegimenCategory = getCrrfTBRegimenCategory(startDate, endDate);
		crrf.setCrrfTBRegimenCategory(crrfTBRegimenCategory);

		//crrfSTIRegimenCategory
		List<CrrfDetails> crrfSTIRegimenCategory = new ArrayList<CrrfDetails>();
		crrfSTIRegimenCategory = getCrrfSTIRegimenCategory(startDate, endDate);
		crrf.setCrrfSTIRegimenCategory(crrfSTIRegimenCategory);

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		iPharmacyReports
		        .getARVCRRIFAdultModalitiesPharmacyConsumptionByDate(reportId, crrf, reportFolder);

		return crrf;
	}

	//adult
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
	  	
	  	//get minimum receipt operation data recorded
	  	Date minimumReceiptDate = getMinimumReceiptDate();
	  	
	  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
	  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
	  	
		List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(minimumReceiptDate, startDate, null);
	  	
	  	
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
	  		
	  		//beginning balance
	  		int totalBeginingBalanceReceived = 0; 
	  		int totalBeginingBalanceConsumed = 0;
	  		int beginingBalance = 0;
	  		
	  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
	  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
	  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
	  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
	  		if(pcsBeginingBalance == null) {
	  			totalBeginingBalanceReceived = 0;
	  		}else {
	  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
	  		}
	  		
	  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
	  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
	  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
	  
	  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
	  				matchingObjectBeginingBalanceConsumed.orElse(null);
	  		if(pcsBeginingBalanceConsumed == null) {
	  			totalBeginingBalanceConsumed = 0; 			
	  		}else {
	  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
	  		}
	  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
	  		crrfDetails.setBeginningBalance(beginingBalance);
	  		
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
		  	
		  	
		  	//set negative values to 0
		  	if(crrfDetails.getBeginningBalance() < 0) {
		  		crrfDetails.setBeginningBalance(0);
		  	}
			if(crrfDetails.getQuantityReceived() < 0) {
		  		crrfDetails.setQuantityReceived(0);
		  	}
			if(crrfDetails.getQuantityDispensed() < 0) {
		  		crrfDetails.setQuantityDispensed(0);
		  	}
			if(crrfDetails.getPositiveAdjustments() < 0) {
		  		crrfDetails.setPositiveAdjustments(0);
		  	}
			if(crrfDetails.getNegativeAdjustments() < 0) {
		  		crrfDetails.setNegativeAdjustments(0);
		  	}
			if(crrfDetails.getLossesdDamagesExpiries() < 0) {
		  		crrfDetails.setLossesdDamagesExpiries(0);
		  	}
			if(crrfDetails.getPhysicalCount() < 0) {
		  		crrfDetails.setPhysicalCount(0);
		  	}
			if(crrfDetails.getMaximumStockQuantity() < 0) {
		  		crrfDetails.setMaximumStockQuantity(0);
		  	}
			if(crrfDetails.getQuantityToOrder() < 0) {
		  		crrfDetails.setQuantityToOrder(0);
		  	}
			
			System.out.println("Count: " + i);		  		
	  		System.out.println("Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("Basic Unit 2: " + crrfDetails.getBasicUnit());
	  		System.out.println("BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
		  	System.out.println("QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
			System.out.println("PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
			
	  		crrfAdultRegimen.add(crrfDetails);
	  	}

		return crrfAdultRegimen;
	}

	//pediatric
	private List<CrrfDetails> getCrrfPediatricRegimenCategory(Date startDate, Date endDate) {
		List<CrrfDetails> crrfPedeatricRegimen = new ArrayList<CrrfDetails>();
		
		//get distinct items for Pedeatric regimen line as base.
	  	List<Item> distinctPedeatricItemBase = new ArrayList<Item>();
	  	distinctPedeatricItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.PEDITRIC))
                 .collect(Collectors.toList());
       
        List<Item> distinctElements = new ArrayList<Item>();
        for(Item it : distinctPedeatricItemBase) {
        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
        		distinctElements.add(it);
        	}            		
        }
                                           
	  	System.out.println("Pedeatric Item Size: " + distinctPedeatricItemBase.size());	
	  	System.out.println("Distinct Pedeatric Item Size: " + distinctElements.size());
	  	
	  	//total consumed
	  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
	  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

	  	System.out.println("Pedeatric forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
	  	System.out.println("Pedeatric forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
	  	
	  	//total received
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
	  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
	  	
	  	//get minimum receipt operation data recorded
	  	Date minimumReceiptDate = getMinimumReceiptDate();
	  	
	  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
	  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
	  	
		List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(minimumReceiptDate, startDate, null);
	  	
	  	
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
	  		
	  		//beginning balance
	  		int totalBeginingBalanceReceived = 0; 
	  		int totalBeginingBalanceConsumed = 0;
	  		int beginingBalance = 0;
	  		
	  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
	  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
	  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
	  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
	  		if(pcsBeginingBalance == null) {
	  			totalBeginingBalanceReceived = 0;
	  		}else {
	  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
	  		}
	  		
	  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
	  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
	  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
	  
	  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
	  				matchingObjectBeginingBalanceConsumed.orElse(null);
	  		if(pcsBeginingBalanceConsumed == null) {
	  			totalBeginingBalanceConsumed = 0; 			
	  		}else {
	  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
	  		}
	  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
	  		crrfDetails.setBeginningBalance(beginingBalance);
	  		
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
	  		System.out.println("Pedeatric Count: " + i);		  		
	  		System.out.println("Pedeatric Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("Pedeatric Basic Unit: " + crrfDetails.getBasicUnit());
	  		System.out.println("Pedeatric BeginningBalance: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("Pedeatric getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
		  	System.out.println("Pedeatric QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
			System.out.println("Pedeatric PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("Pedeatric NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("Pedeatric LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("Pedeatric PhysicalCount: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("Pedeatric MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("Pedeatric QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
		  	
		  	
		  	//set negative values to 0
		  	if(crrfDetails.getBeginningBalance() < 0) {
		  		crrfDetails.setBeginningBalance(0);
		  	}
			if(crrfDetails.getQuantityReceived() < 0) {
		  		crrfDetails.setQuantityReceived(0);
		  	}
			if(crrfDetails.getQuantityDispensed() < 0) {
		  		crrfDetails.setQuantityDispensed(0);
		  	}
			if(crrfDetails.getPositiveAdjustments() < 0) {
		  		crrfDetails.setPositiveAdjustments(0);
		  	}
			if(crrfDetails.getNegativeAdjustments() < 0) {
		  		crrfDetails.setNegativeAdjustments(0);
		  	}
			if(crrfDetails.getLossesdDamagesExpiries() < 0) {
		  		crrfDetails.setLossesdDamagesExpiries(0);
		  	}
			if(crrfDetails.getPhysicalCount() < 0) {
		  		crrfDetails.setPhysicalCount(0);
		  	}
			if(crrfDetails.getMaximumStockQuantity() < 0) {
		  		crrfDetails.setMaximumStockQuantity(0);
		  	}
			if(crrfDetails.getQuantityToOrder() < 0) {
		  		crrfDetails.setQuantityToOrder(0);
		  	}
			
			System.out.println("Pedeatric Count: " + i);		  		
	  		System.out.println("Pedeatric Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("Pedeatric Basic Unit 2: " + crrfDetails.getBasicUnit());
	  		System.out.println("Pedeatric BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("Pedeatric getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
		  	System.out.println("Pedeatric QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
			System.out.println("Pedeatric PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("Pedeatric NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("Pedeatric LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("Pedeatric PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("Pedeatric MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("Pedeatric QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
			
	  		crrfPedeatricRegimen.add(crrfDetails);
	  	}

		return crrfPedeatricRegimen;
	}

	//OI
	private List<CrrfDetails> getCrrfOIRegimenCategory(Date startDate, Date endDate) {
		List<CrrfDetails> crrfOIRegimen = new ArrayList<CrrfDetails>();
		
		//get distinct items for OI regimen line as base.
	  	List<Item> distinctOIItemBase = new ArrayList<Item>();
	  	distinctOIItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.OI))
                 .collect(Collectors.toList());
       
        List<Item> distinctElements = new ArrayList<Item>();
        for(Item it : distinctOIItemBase) {
        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
        		distinctElements.add(it);
        	}            		
        }
                                           
	  	System.out.println("OI Item Size: " + distinctOIItemBase.size());	
	  	System.out.println("Distinct OI Item Size: " + distinctElements.size());
	  	
	  	//total consumed
	  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
	  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

	  	System.out.println("OI forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
	  	System.out.println("OI forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
	  	
	  	//total received
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
	  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
	  	
	  	//get minimum receipt operation data recorded
	  	Date minimumReceiptDate = getMinimumReceiptDate();
	  	
	  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
	  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
	  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
	  	
		List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(minimumReceiptDate, startDate, null);
	  	
	  	
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
	  		
	  		//beginning balance
	  		int totalBeginingBalanceReceived = 0; 
	  		int totalBeginingBalanceConsumed = 0;
	  		int beginingBalance = 0;
	  		
	  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
	  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
	  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
	  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
	  		if(pcsBeginingBalance == null) {
	  			totalBeginingBalanceReceived = 0;
	  		}else {
	  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
	  		}
	  		
	  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
	  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
	  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
	  
	  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
	  				matchingObjectBeginingBalanceConsumed.orElse(null);
	  		if(pcsBeginingBalanceConsumed == null) {
	  			totalBeginingBalanceConsumed = 0; 			
	  		}else {
	  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
	  		}
	  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
	  		crrfDetails.setBeginningBalance(beginingBalance);
	  		
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
	  		System.out.println("OI Count: " + i);		  		
	  		System.out.println("OI Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("OI Basic Unit: " + crrfDetails.getBasicUnit());
	  		System.out.println("OI BeginningBalance: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("OI getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
		  	System.out.println("OI QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
			System.out.println("OI PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("OI NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("OI LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("OI PhysicalCount: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("OI MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("OI QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
		  	
		  	
		  	//set negative values to 0
		  	if(crrfDetails.getBeginningBalance() < 0) {
		  		crrfDetails.setBeginningBalance(0);
		  	}
			if(crrfDetails.getQuantityReceived() < 0) {
		  		crrfDetails.setQuantityReceived(0);
		  	}
			if(crrfDetails.getQuantityDispensed() < 0) {
		  		crrfDetails.setQuantityDispensed(0);
		  	}
			if(crrfDetails.getPositiveAdjustments() < 0) {
		  		crrfDetails.setPositiveAdjustments(0);
		  	}
			if(crrfDetails.getNegativeAdjustments() < 0) {
		  		crrfDetails.setNegativeAdjustments(0);
		  	}
			if(crrfDetails.getLossesdDamagesExpiries() < 0) {
		  		crrfDetails.setLossesdDamagesExpiries(0);
		  	}
			if(crrfDetails.getPhysicalCount() < 0) {
		  		crrfDetails.setPhysicalCount(0);
		  	}
			if(crrfDetails.getMaximumStockQuantity() < 0) {
		  		crrfDetails.setMaximumStockQuantity(0);
		  	}
			if(crrfDetails.getQuantityToOrder() < 0) {
		  		crrfDetails.setQuantityToOrder(0);
		  	}
			
			System.out.println("OI Count: " + i);		  		
	  		System.out.println("OI Item/Drugs: " + crrfDetails.getDrugs());
	  		System.out.println("OI Basic Unit 2: " + crrfDetails.getBasicUnit());
	  		System.out.println("OI BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
	  		System.out.println("OI getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
		  	System.out.println("OI QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
			System.out.println("OI PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
	  		System.out.println("OI NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
		  	System.out.println("OI LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
		  	System.out.println("OI PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
		  	System.out.println("OI MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
		  	System.out.println("OI QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
			
	  		crrfOIRegimen.add(crrfDetails);
	  	}

		return crrfOIRegimen;
	}

	//OI
	private List<CrrfDetails> getCrrfAdvanceHIVRegimenCategory(Date startDate, Date endDate) {
			List<CrrfDetails> crrfAdvanceHIVRegimen = new ArrayList<CrrfDetails>();
			
			//get distinct items for OI regimen line as base.
		  	List<Item> distinctAdvanceHIVItemBase = new ArrayList<Item>();
		  	distinctAdvanceHIVItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
	                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.ADVANCE_HIV))
	                 .collect(Collectors.toList());
	       
	        List<Item> distinctElements = new ArrayList<Item>();
	        for(Item it : distinctAdvanceHIVItemBase) {
	        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
	        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
	        		distinctElements.add(it);
	        	}            		
	        }
	                                           
		  	System.out.println("AdvanceHIV Item Size: " + distinctAdvanceHIVItemBase.size());	
		  	System.out.println("Distinct AdvanceHIV Item Size: " + distinctElements.size());
		  	
		  	//total consumed
		  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
		  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

		  	System.out.println("AdvanceHIV forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
		  	System.out.println("AdvanceHIV forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
		  	
		  	//total received
		  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
		  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
		  	
		  	//get minimum receipt operation data recorded
		  	Date minimumReceiptDate = getMinimumReceiptDate();
		  	
		  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
		  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
		  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
		  	
			List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
	  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(minimumReceiptDate, startDate, null);
		  	
		  	
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
		  		
		  		//beginning balance
		  		int totalBeginingBalanceReceived = 0; 
		  		int totalBeginingBalanceConsumed = 0;
		  		int beginingBalance = 0;
		  		
		  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
		  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
		  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
		  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
		  		if(pcsBeginingBalance == null) {
		  			totalBeginingBalanceReceived = 0;
		  		}else {
		  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
		  		}
		  		
		  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
		  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
		  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
		  
		  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
		  				matchingObjectBeginingBalanceConsumed.orElse(null);
		  		if(pcsBeginingBalanceConsumed == null) {
		  			totalBeginingBalanceConsumed = 0; 			
		  		}else {
		  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
		  		}
		  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
		  		crrfDetails.setBeginningBalance(beginingBalance);
		  		
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
		  		System.out.println("AdvanceHIV Count: " + i);		  		
		  		System.out.println("AdvanceHIV Item/Drugs: " + crrfDetails.getDrugs());
		  		System.out.println("AdvanceHIV Basic Unit: " + crrfDetails.getBasicUnit());
		  		System.out.println("AdvanceHIV BeginningBalance: " + crrfDetails.getBeginningBalance());	
		  		System.out.println("AdvanceHIV getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
			  	System.out.println("AdvanceHIV QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
				System.out.println("AdvanceHIV PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
		  		System.out.println("AdvanceHIV NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
			  	System.out.println("AdvanceHIV LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
			  	System.out.println("AdvanceHIV PhysicalCount: " + crrfDetails.getPhysicalCount());	
			  	System.out.println("AdvanceHIV MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
			  	System.out.println("AdvanceHIV QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
			  	
			  	
			  	//set negative values to 0
			  	if(crrfDetails.getBeginningBalance() < 0) {
			  		crrfDetails.setBeginningBalance(0);
			  	}
				if(crrfDetails.getQuantityReceived() < 0) {
			  		crrfDetails.setQuantityReceived(0);
			  	}
				if(crrfDetails.getQuantityDispensed() < 0) {
			  		crrfDetails.setQuantityDispensed(0);
			  	}
				if(crrfDetails.getPositiveAdjustments() < 0) {
			  		crrfDetails.setPositiveAdjustments(0);
			  	}
				if(crrfDetails.getNegativeAdjustments() < 0) {
			  		crrfDetails.setNegativeAdjustments(0);
			  	}
				if(crrfDetails.getLossesdDamagesExpiries() < 0) {
			  		crrfDetails.setLossesdDamagesExpiries(0);
			  	}
				if(crrfDetails.getPhysicalCount() < 0) {
			  		crrfDetails.setPhysicalCount(0);
			  	}
				if(crrfDetails.getMaximumStockQuantity() < 0) {
			  		crrfDetails.setMaximumStockQuantity(0);
			  	}
				if(crrfDetails.getQuantityToOrder() < 0) {
			  		crrfDetails.setQuantityToOrder(0);
			  	}
				
				System.out.println("AdvanceHIV Count: " + i);		  		
		  		System.out.println("AdvanceHIV Item/Drugs: " + crrfDetails.getDrugs());
		  		System.out.println("AdvanceHIV Basic Unit 2: " + crrfDetails.getBasicUnit());
		  		System.out.println("AdvanceHIV BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
		  		System.out.println("AdvanceHIV getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
			  	System.out.println("AdvanceHIV QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
				System.out.println("AdvanceHIV PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
		  		System.out.println("AdvanceHIV NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
			  	System.out.println("AdvanceHIV LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
			  	System.out.println("AdvanceHIV PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
			  	System.out.println("AdvanceHIV MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
			  	System.out.println("AdvanceHIV QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
				
		  		crrfAdvanceHIVRegimen.add(crrfDetails);
		  	}

			return crrfAdvanceHIVRegimen;
		}

	//TB
	private List<CrrfDetails> getCrrfTBRegimenCategory(Date startDate, Date endDate) {
				List<CrrfDetails> crrfTBRegimen = new ArrayList<CrrfDetails>();
				
				//get distinct items for TB regimen line as base.
			  	List<Item> distinctTBItemBase = new ArrayList<Item>();
			  	distinctTBItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
		                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.TB))
		                 .collect(Collectors.toList());
		       
		        List<Item> distinctElements = new ArrayList<Item>();
		        for(Item it : distinctTBItemBase) {
		        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
		        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
		        		distinctElements.add(it);
		        	}            		
		        }
		                                           
			  	System.out.println("TB Item Size: " + distinctTBItemBase.size());	
			  	System.out.println("Distinct TB Item Size: " + distinctElements.size());
			  	
			  	//total consumed
			  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
			  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

			  	System.out.println("TB forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
			  	System.out.println("TB forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
			  	
			  	//total received
			  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
			  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
			  	
			  	//get minimum receipt operation data recorded
			  	Date minimumReceiptDate = getMinimumReceiptDate();
			  	
			  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
			  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
			  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
			  	
				List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
		  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(
		  		        		minimumReceiptDate, startDate, null);
			  	
			  	
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
			  		
			  		//beginning balance
			  		int totalBeginingBalanceReceived = 0; 
			  		int totalBeginingBalanceConsumed = 0;
			  		int beginingBalance = 0;
			  		
			  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
			  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
			  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
			  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
			  		if(pcsBeginingBalance == null) {
			  			totalBeginingBalanceReceived = 0;
			  		}else {
			  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
			  		}
			  		
			  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
			  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
			  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
			  
			  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
			  				matchingObjectBeginingBalanceConsumed.orElse(null);
			  		if(pcsBeginingBalanceConsumed == null) {
			  			totalBeginingBalanceConsumed = 0; 			
			  		}else {
			  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
			  		}
			  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
			  		crrfDetails.setBeginningBalance(beginingBalance);
			  		
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
			  		System.out.println("TB Count: " + i);		  		
			  		System.out.println("TB Item/Drugs: " + crrfDetails.getDrugs());
			  		System.out.println("TB Basic Unit: " + crrfDetails.getBasicUnit());
			  		System.out.println("TB BeginningBalance: " + crrfDetails.getBeginningBalance());	
			  		System.out.println("TB getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
				  	System.out.println("TB QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
					System.out.println("TB PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
			  		System.out.println("TB NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
				  	System.out.println("TB LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
				  	System.out.println("TB PhysicalCount: " + crrfDetails.getPhysicalCount());	
				  	System.out.println("TB MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
				  	System.out.println("TB QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
				  	
				  	
				  	//set negative values to 0
				  	if(crrfDetails.getBeginningBalance() < 0) {
				  		crrfDetails.setBeginningBalance(0);
				  	}
					if(crrfDetails.getQuantityReceived() < 0) {
				  		crrfDetails.setQuantityReceived(0);
				  	}
					if(crrfDetails.getQuantityDispensed() < 0) {
				  		crrfDetails.setQuantityDispensed(0);
				  	}
					if(crrfDetails.getPositiveAdjustments() < 0) {
				  		crrfDetails.setPositiveAdjustments(0);
				  	}
					if(crrfDetails.getNegativeAdjustments() < 0) {
				  		crrfDetails.setNegativeAdjustments(0);
				  	}
					if(crrfDetails.getLossesdDamagesExpiries() < 0) {
				  		crrfDetails.setLossesdDamagesExpiries(0);
				  	}
					if(crrfDetails.getPhysicalCount() < 0) {
				  		crrfDetails.setPhysicalCount(0);
				  	}
					if(crrfDetails.getMaximumStockQuantity() < 0) {
				  		crrfDetails.setMaximumStockQuantity(0);
				  	}
					if(crrfDetails.getQuantityToOrder() < 0) {
				  		crrfDetails.setQuantityToOrder(0);
				  	}
					
					System.out.println("TB Count: " + i);		  		
			  		System.out.println("TB Item/Drugs: " + crrfDetails.getDrugs());
			  		System.out.println("TB Basic Unit 2: " + crrfDetails.getBasicUnit());
			  		System.out.println("TB BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
			  		System.out.println("TB getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
				  	System.out.println("TB QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
					System.out.println("TB PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
			  		System.out.println("TB NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
				  	System.out.println("TB LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
				  	System.out.println("TB PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
				  	System.out.println("TB MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
				  	System.out.println("TB QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
					
			  		crrfTBRegimen.add(crrfDetails);
			  	}

				return crrfTBRegimen;
	}

	//STI
	private List<CrrfDetails> getCrrfSTIRegimenCategory(Date startDate, Date endDate) {
					List<CrrfDetails> crrfSTIRegimen = new ArrayList<CrrfDetails>();
					
					//get distinct items for TB regimen line as base.
				  	List<Item> distinctSTIItemBase = new ArrayList<Item>();
				  	distinctSTIItemBase = itemDataService.getAll().stream().filter(a -> a.getItemType()
			                 .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE) && a.getRegimenLine().equals(ConstantUtils.STI))
			                 .collect(Collectors.toList());
			       
			        List<Item> distinctElements = new ArrayList<Item>();
			        for(Item it : distinctSTIItemBase) {
			        	if(!(distinctElements.stream().filter(b -> b.getConcept().getConceptId()
			        			.equals(it.getConcept().getConceptId())).findFirst().isPresent())) {
			        		distinctElements.add(it);
			        	}            		
			        }
			                                           
				  	System.out.println("STI Item Size: " + distinctSTIItemBase.size());	
				  	System.out.println("Distinct STI Item Size: " + distinctElements.size());
				  	
				  	//total consumed
				  	List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummary =
				  		        iARVPharmacyDispenseService.getDrugDispenseSummary(startDate, endDate, null);

				  	System.out.println("STI forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary);
				  	System.out.println("STI forPharmacyConsumptionSummary: " + forPharmacyConsumptionSummary.size());
				  	
				  	//total received
				  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummary = 
				  			consumptionSummaryAtStockroom(startDate, endDate, distinctElements);
				  	
				  	//get minimum receipt operation data recorded
				  	Date minimumReceiptDate = getMinimumReceiptDate();
				  	
				  	//beginning balance - get stock on hand of data below the start date (total received and total consumed)
				  	List<PharmacyConsumptionSummary> pharmacyConsumptionSummaryBeginningBalanceReceived = 
				  			consumptionSummaryAtStockroomBeggingBalance(startDate, endDate, distinctElements);
				  	
					List<NewPharmacyConsumptionSummary> forPharmacyConsumptionSummaryBeginningBalanceConsumed =
			  		        iARVPharmacyDispenseService.getDrugDispenseSummaryBeginingBalance(
			  		        		minimumReceiptDate, startDate, null);
				  	
				  	
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
				  		Optional<NewPharmacyConsumptionSummary> matchingObjectConsumed = 
				  				forPharmacyConsumptionSummary.stream().
				  			    filter(a -> a.getItemConceptId().equals(
				  			    		item.getConcept().getConceptId())).findFirst();
				  
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
				  		
				  		//beginning balance
				  		int totalBeginingBalanceReceived = 0; 
				  		int totalBeginingBalanceConsumed = 0;
				  		int beginingBalance = 0;
				  		
				  		Optional<PharmacyConsumptionSummary> matchingObjectBeginingBalance = 
				  				pharmacyConsumptionSummaryBeginningBalanceReceived.stream().
				  			    filter(pr -> pr.getItem().equals(item)).findFirst();	  
				  		PharmacyConsumptionSummary pcsBeginingBalance = matchingObjectBeginingBalance.orElse(null);
				  		if(pcsBeginingBalance == null) {
				  			totalBeginingBalanceReceived = 0;
				  		}else {
				  			totalBeginingBalanceReceived = pcsBeginingBalance.getTotalQuantityReceived();
				  		}
				  		
				  		Optional<NewPharmacyConsumptionSummary> matchingObjectBeginingBalanceConsumed = 
				  				forPharmacyConsumptionSummaryBeginningBalanceConsumed.stream().
				  			    filter(pc -> pc.getItemConceptId().equals(item.getConcept().getConceptId())).findFirst();
				  
				  		NewPharmacyConsumptionSummary pcsBeginingBalanceConsumed = 
				  				matchingObjectBeginingBalanceConsumed.orElse(null);
				  		if(pcsBeginingBalanceConsumed == null) {
				  			totalBeginingBalanceConsumed = 0; 			
				  		}else {
				  			totalBeginingBalanceConsumed = pcsBeginingBalanceConsumed.getTotalQuantityReceived();
				  		}
				  		beginingBalance = totalBeginingBalanceReceived - totalBeginingBalanceConsumed;
				  		crrfDetails.setBeginningBalance(beginingBalance);
				  		
				  		//physical count or stock on hand
				  		Integer physicalCount = (crrfDetails.getBeginningBalance() + crrfDetails.getQuantityReceived()
				  		+ crrfDetails.getPositiveAdjustments()) - (crrfDetails.getNegativeAdjustments()
				  				+ crrfDetails.getQuantityDispensed());
				  		crrfDetails.setPhysicalCount(physicalCount);
				  		
				  		//maximum stock
				  		crrfDetails.setMaximumStockQuantity(crrfDetails.getQuantityDispensed() * 2);
				  		
				  		//quantity to order
				  		crrfDetails.setQuantityToOrder(
				  				crrfDetails.getMaximumStockQuantity() - crrfDetails.getPhysicalCount());
				  		
				  		
				  		i++;
				  		System.out.println("STI Count: " + i);		  		
				  		System.out.println("STI Item/Drugs: " + crrfDetails.getDrugs());
				  		System.out.println("STI Basic Unit: " + crrfDetails.getBasicUnit());
				  		System.out.println("STI BeginningBalance: " + crrfDetails.getBeginningBalance());	
				  		System.out.println("STI getTotalQuantityReceived: " + crrfDetails.getQuantityReceived());
					  	System.out.println("STI QuantityDispensed: " + crrfDetails.getQuantityDispensed());	
						System.out.println("STI PositiveAdjustments: " + crrfDetails.getPositiveAdjustments());
				  		System.out.println("STI NegativeAdjustments: " + crrfDetails.getNegativeAdjustments());
					  	System.out.println("STI LossesdDamagesExpiries: " + crrfDetails.getLossesdDamagesExpiries());	
					  	System.out.println("STI PhysicalCount: " + crrfDetails.getPhysicalCount());	
					  	System.out.println("STI MaximumStockQuantity: " + crrfDetails.getMaximumStockQuantity());	
					  	System.out.println("STI QuantityToOrder: " + crrfDetails.getQuantityToOrder());	
					  	
					  	
					  	//set negative values to 0
					  	if(crrfDetails.getBeginningBalance() < 0) {
					  		crrfDetails.setBeginningBalance(0);
					  	}
						if(crrfDetails.getQuantityReceived() < 0) {
					  		crrfDetails.setQuantityReceived(0);
					  	}
						if(crrfDetails.getQuantityDispensed() < 0) {
					  		crrfDetails.setQuantityDispensed(0);
					  	}
						if(crrfDetails.getPositiveAdjustments() < 0) {
					  		crrfDetails.setPositiveAdjustments(0);
					  	}
						if(crrfDetails.getNegativeAdjustments() < 0) {
					  		crrfDetails.setNegativeAdjustments(0);
					  	}
						if(crrfDetails.getLossesdDamagesExpiries() < 0) {
					  		crrfDetails.setLossesdDamagesExpiries(0);
					  	}
						if(crrfDetails.getPhysicalCount() < 0) {
					  		crrfDetails.setPhysicalCount(0);
					  	}
						if(crrfDetails.getMaximumStockQuantity() < 0) {
					  		crrfDetails.setMaximumStockQuantity(0);
					  	}
						if(crrfDetails.getQuantityToOrder() < 0) {
					  		crrfDetails.setQuantityToOrder(0);
					  	}
						
						System.out.println("STI Count: " + i);		  		
				  		System.out.println("STI Item/Drugs: " + crrfDetails.getDrugs());
				  		System.out.println("STI Basic Unit 2: " + crrfDetails.getBasicUnit());
				  		System.out.println("STI BeginningBalance 2: " + crrfDetails.getBeginningBalance());	
				  		System.out.println("STI getTotalQuantityReceived 2: " + crrfDetails.getQuantityReceived());
					  	System.out.println("STI QuantityDispensed 2: " + crrfDetails.getQuantityDispensed());	
						System.out.println("STI PositiveAdjustments 2: " + crrfDetails.getPositiveAdjustments());
				  		System.out.println("STI NegativeAdjustments 2: " + crrfDetails.getNegativeAdjustments());
					  	System.out.println("STI LossesdDamagesExpiries 2: " + crrfDetails.getLossesdDamagesExpiries());	
					  	System.out.println("STI PhysicalCount 2: " + crrfDetails.getPhysicalCount());	
					  	System.out.println("STI MaximumStockQuantity 2: " + crrfDetails.getMaximumStockQuantity());	
					  	System.out.println("STI QuantityToOrder: 2 " + crrfDetails.getQuantityToOrder());	
						
				  		crrfSTIRegimen.add(crrfDetails);
				  	}

					return crrfSTIRegimen;
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

	private Date getMinimumReceiptDate() {

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String receiptOperationTypeUuid = ConstantUtils.RECEIPT_TYPE_UUID;

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		List<PharmacyConsumptionSummary> finalConsumptionSummarys = new ArrayList<>();

		searchConsumptionSummary.setOperationStatus(status);
		searchConsumptionSummary.setCommodityType(ConstantUtils.PHARMACY_COMMODITY_TYPE);

		IStockOperationType stockOperationType = stockOperationTypeDataService.getByUuid(receiptOperationTypeUuid);
		searchConsumptionSummary.setOperationType(stockOperationType);

		StockOperation receiptStockOps = stockOperationDataService.getOperationsByMinDateCreated(
		    searchConsumptionSummary, null);

		return receiptStockOps.getDateCreated();

	}

	private List<PharmacyConsumptionSummary> consumptionSummaryAtStockroomBeggingBalance(
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

		receiptStockOps = stockOperationDataService.getOperationsByDateDiffBeginningBalance(searchConsumptionSummary, null);

		IStockOperationType distributeStockOperationType =
		        stockOperationTypeDataService.getByUuid(distributeOperationTypeUuid);

		searchConsumptionSummary.setOperationType(distributeStockOperationType);

		distributeStockOps = stockOperationDataService.getOperationsByDateDiffBeginningBalance(
		    searchConsumptionSummary, null);

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
