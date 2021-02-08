/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.model;

import java.util.Date;

/**
 *
 * @author MORRISON.I
 */
public class PharmacyReports {
    
    private Date startDate;
    private Date endDate;
    private String stockroom;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStockroom() {
        return stockroom;
    }

    public void setStockroom(String stockroom) {
        this.stockroom = stockroom;
    }
    
    
    
    
}
