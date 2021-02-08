/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api;

import java.util.Date;
import java.util.List;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumption;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;

/**
 *
 * @author MORRISON.I
 */
public interface IPharmacyReports {
    
    List<PharmacyConsumptionSummary> getPharmacyConsumptionByDate(Date startDate, Date endDate);
    
}
