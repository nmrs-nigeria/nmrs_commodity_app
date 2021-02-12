/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemExpirationSummaryService;
import org.openmrs.module.openhmis.inventory.api.IPharmacyConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTypeDataService;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.IStockOperationType;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummaryReport;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.SearchStockOnHandSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.openmrs.module.openhmis.inventory.api.IPharmacyReportsService;

/**
 * @author MORRISON.I
 */
@Controller(value = "invPharmacyReportsController")
@RequestMapping(ModuleWebConstants.PHARMACY_REPORT_ROOT)
public class PharmacyReportsController {

	private IStockOperationTypeDataService stockOperationTypeDataService;
	private IDepartmentDataService departmentService;
	private IPharmacyConsumptionDataService consumptionDataService;
	private IStockOperationDataService stockOperationDataService;
	private IItemDataService itemDataService;
	private IPharmacyReportsService iPharmacyReports;
	private IItemExpirationSummaryService itemExpirationSummaryService;

	private Date startDate;
	private Date endDate;
	private String startDateStringVal;
	private String endDateStringVal;
	private List<Item> distinctItems = null;
	List<Department> dispensarys = null;
	HttpServletRequest request = null;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public SimpleObject get(@RequestParam(value = "reportId", required = true) String reportId,
	        @RequestParam(value = "startDate", required = true) String startDateString,
	        @RequestParam(value = "endDate", required = true) String endDateString,
	        HttpServletRequest request, HttpServletResponse response) {

		this.stockOperationTypeDataService = Context.getService(IStockOperationTypeDataService.class);
		this.departmentService = Context.getService(IDepartmentDataService.class);
		this.consumptionDataService = Context.getService(IPharmacyConsumptionDataService.class);
		this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
		this.itemDataService = Context.getService(IItemDataService.class);
		this.itemExpirationSummaryService = Context.getService(IItemExpirationSummaryService.class);

		this.startDate = RestUtils.parseCustomOpenhmisDateString(startDateString);
		this.endDate = RestUtils.parseCustomOpenhmisDateString(endDateString);
		this.startDateStringVal = startDateString;
		this.endDateStringVal = endDateString;
		this.iPharmacyReports = Context.getService(IPharmacyReportsService.class);

		SimpleObject result = new SimpleObject();
		this.request = request;

		String path = routeReport(reportId);

		result.put("results", path);

		return result;

	}

	private String routeReport(String reportId) {

        this.dispensarys = this.departmentService.getAll().stream()
                .filter(a -> a.getDepartmentType().equalsIgnoreCase(ConstantUtils.PHARMACY_COMMODITY_TYPE))
                .collect(Collectors.toList());

        distinctItems = itemDataService.getAll();
        distinctItems = distinctItems.stream().filter(a -> a.getItemType()
                .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE))
                .collect(Collectors.toList());

        if (reportId.equalsIgnoreCase(ConstantUtils.DISPENSARY_CONSUMPTION_REPORT)) {
            consumptionSummaryAtDispensary(reportId);
            return returnURL(reportId);

        }

        if (reportId.equalsIgnoreCase(ConstantUtils.STOCK_ROOM_CONSUMPTION_REPORT)) {
            consumptionSummaryAtStockroom(reportId);
            return returnURL(reportId);

        }
        
        if (reportId.equalsIgnoreCase(ConstantUtils.DISPENSARY_STOCKONHAND_REPORT)) {
            stockonhandSummaryAtDispensary(reportId);
            String filename = reportId + ".csv";
            return Paths.get(request.getContextPath(), "CMReports", filename).toString();

        }
        
        if (reportId.equalsIgnoreCase(ConstantUtils.STORE_STOCKONHAND_REPORT)) {
            stockonhandSummaryAtStockroom(reportId);
            String filename = reportId + ".csv";
            return Paths.get(request.getContextPath(), "CMReports", filename).toString();

        }

        return null;

    }

	private String consumptionSummaryAtDispensary(String reportId) {

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
		IStockOperationType stockOperationType = stockOperationTypeDataService.getByUuid(stockOperationTypeUuid);

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		List<PharmacyConsumptionSummary> finalConsumptionSummarys = new ArrayList<>();

		for (Department d : dispensarys) {

			searchConsumptionSummary.setDepartment(d);
			//  searchConsumptionSummary.setItem(searchItem);
			searchConsumptionSummary.setStartDate(startDate);
			searchConsumptionSummary.setEndDate(endDate);
			searchConsumptionSummary.setOperationStatus(status);
			searchConsumptionSummary.setOperationType(stockOperationType);

			List<StockOperation> stockOps = null;

			stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

			System.out.println("stock operations result: " + stockOps.size());

			finalConsumptionSummarys.addAll(consumptionDataService.retrieveConsumptionSummary(stockOps,
			    searchConsumptionSummary, null, distinctItems));

		}

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return iPharmacyReports.getPharmacyConsumptionByDate(reportId, finalConsumptionSummarys, reportFolder);
	}

	private String stockonhandSummaryAtDispensary(String reportId) {

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
		IStockOperationType stockOperationType = stockOperationTypeDataService.getByUuid(stockOperationTypeUuid);

		SearchStockOnHandSummary searchStockOnHandSummary = new SearchStockOnHandSummary();
		List<ItemExpirationSummaryReport> finalStockOnHandSummarys = new ArrayList<>();

		searchStockOnHandSummary.setStartDate(startDateStringVal);
		searchStockOnHandSummary.setEndDate(endDateStringVal);
		searchStockOnHandSummary.setCommodityType(ConstantUtils.PHARMACY_COMMODITY_TYPE);

		List<ItemExpirationSummaryReport> itemExpirationSummaryReport = null;
		itemExpirationSummaryReport = itemExpirationSummaryService.getItemStockSummaryByDate(
		    searchStockOnHandSummary, null);

		finalStockOnHandSummarys.addAll(itemExpirationSummaryReport);

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return iPharmacyReports.getDispensaryStockOnHandByDate(reportId, finalStockOnHandSummarys, reportFolder);

	}

	private String stockonhandSummaryAtStockroom(String reportId) {

		System.out.println("Report ID: " + reportId);

		StockOperationStatus status = StockOperationStatus.COMPLETED;
		String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
		IStockOperationType stockOperationType = stockOperationTypeDataService.getByUuid(stockOperationTypeUuid);

		SearchStockOnHandSummary searchStockOnHandSummary = new SearchStockOnHandSummary();
		List<ItemExpirationSummaryReport> finalStockOnHandSummarys = new ArrayList<>();

		searchStockOnHandSummary.setStartDate(startDateStringVal);
		searchStockOnHandSummary.setEndDate(endDateStringVal);
		searchStockOnHandSummary.setCommodityType(ConstantUtils.PHARMACY_COMMODITY_TYPE);

		List<ItemExpirationSummaryReport> itemExpirationSummaryReport = null;
		itemExpirationSummaryReport = itemExpirationSummaryService.getItemStockRoomStockOnHandByDate(
		    searchStockOnHandSummary, null);

		finalStockOnHandSummarys.addAll(itemExpirationSummaryReport);

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return iPharmacyReports.getDispensaryStockOnHandByDate(reportId, finalStockOnHandSummarys, reportFolder);

	}

	private String consumptionSummaryAtStockroom(String reportId) {

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

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return iPharmacyReports.getPharmacyStockroomConsumptionByDate(reportId, finalConsumptionSummarys, reportFolder);
	}

	private String returnURL(String reportId) {
		String filename = reportId + ".csv";
		return Paths.get(request.getContextPath(), "CMReports", filename).toString();
	}
}
