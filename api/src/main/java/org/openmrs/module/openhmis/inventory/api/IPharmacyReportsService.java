/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api;

import java.util.Date;
import java.util.List;

import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummaryReport;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;

/**
 * @author MORRISON.I
 */
public interface IPharmacyReportsService {

	String getPharmacyConsumptionByDate(String reportId,
	        List<PharmacyConsumptionSummary> reportData, String reportFolder);

	String getPharmacyStockroomConsumptionByDate(String reportId,
	        List<PharmacyConsumptionSummary> reportData, String reportFolder);

	String getDispensaryStockOnHandByDate(String reportId,
	        List<ItemExpirationSummaryReport> reportData, String reportFolder);

}
