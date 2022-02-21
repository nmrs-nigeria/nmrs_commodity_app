/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.*;
import org.openmrs.module.openhmis.inventory.api.model.*;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	private IItemStockDetailDataService itemStockDetailDataService;

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
	        @RequestParam(value = "treatmentCategory", required = false) String treatmentCategory,
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
		this.itemStockDetailDataService = Context.getService(IItemStockDetailDataService.class);

		SimpleObject result = new SimpleObject();
		this.request = request;
		System.out.println("retreive data, about to route with category: " + treatmentCategory);
		String path = routeReport(reportId, treatmentCategory);

		result.put("results", path);

		return result;

	}

	private String routeReport(String reportId,String treatmentCategory) {

        this.dispensarys = this.departmentService.getAll().stream()
                .filter(a -> a.getDepartmentType().equalsIgnoreCase(ConstantUtils.PHARMACY_COMMODITY_TYPE))
                .collect(Collectors.toList());

        distinctItems = itemDataService.getAll();
        distinctItems = distinctItems.stream().filter(a -> a.getItemType()
                .equals(ConstantUtils.PHARMACY_COMMODITY_TYPE))
                .collect(Collectors.toList());



        if (reportId.equalsIgnoreCase(ConstantUtils.ADULT_DISPENSARY_MODALITIES_REPORT)) {
            adultConsumptionSummaryAtDispensaryByModalities(reportId,treatmentCategory);
            return returnURL(reportId);

        }


        return null;

    }

	private String adultConsumptionSummaryAtDispensaryByModalities(String reportId, String treatmentCategory) {

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		List<CrrfArvCotrim> finalConsumptionSummarys = new ArrayList<>();

		finalConsumptionSummarys = itemStockDetailDataService
		        .getItemStockSummaryByPharmacy(startDate, endDate, null);

		String reportFolder = RestUtils.ensureReportDownloadFolderExist(request);

		return iPharmacyReports
		        .getItemStockSummaryByPharmacyByDate(reportId, finalConsumptionSummarys, reportFolder);
	}

	private String returnURL(String reportId) {
		String filename = reportId + ".csv";
		return Paths.get(request.getContextPath(), "CMReports", filename).toString();
	}
}
