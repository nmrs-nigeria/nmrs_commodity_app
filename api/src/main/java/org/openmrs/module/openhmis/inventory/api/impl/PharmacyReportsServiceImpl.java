/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.util.LoggerFactory;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummaryReport;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.openmrs.module.openhmis.inventory.api.IPharmacyReportsService;

/**
 * @author MORRISON.I
 */
public class PharmacyReportsServiceImpl implements IPharmacyReportsService {

	private static final Log LOG = LogFactory.getLog(PharmacyReportsServiceImpl.class);

	@Override
    public String getPharmacyConsumptionByDate(String reportId, List<PharmacyConsumptionSummary> reportData,
            String reportFolder) {


        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();


        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

            String[] HEADERS = {Utils.DCR_ITEM_HEADER,Utils.DCR_DISPENSARY_HEADER, Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER,
                Utils.DCR_TOTAL_QUANTITY_CONSUMED_HEADER, Utils.DCR_TOTAL_WASTAGE_HEADER, Utils.DCR_STOCK_BALANCE_HEADER};

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {
                            csvPrinter.printRecord(con.getItem().getName(),con.getDepartment().getName(), 
                                    con.getTotalQuantityReceived(),
                                    con.getTotalQuantityConsumed(), con.getTotalQuantityWasted(), con.getStockBalance());
                        } catch (IOException ex) {
                            LOG.error(ex.getMessage());
                        }

                    });
            csvPrinter.flush();

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

        return fileName;
    }

<<<<<<< HEAD
	public String getDispensaryStockOnHandByDate(String reportId, List<ItemExpirationSummaryReport> reportData,
            String reportFolder) {
 
		System.out.println("Report Data ");
		for (ItemExpirationSummaryReport irs : reportData){	
			System.out.println(irs.getItemBatch());
			System.out.println(irs.getDepartment().getName());
			System.out.println(irs.getExpiration());
			System.out.println(irs.getExp());
			System.out.println(irs.getQuantity());
			System.out.println(irs.getItem().getName());
		}
		
        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();
        System.out.println("File name: "+ fileName);
=======
	@Override
    public String getPharmacyStockroomConsumptionByDate(String reportId, 
            List<PharmacyConsumptionSummary> reportData, String reportFolder) {
      

        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();

>>>>>>> a9031794e93461fed669b7133471ebddfa02dddd

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

<<<<<<< HEAD
            String[] HEADERS = {Utils.SSR_ITEM_HEADER,Utils.SSR_DEPARTMENT_HEADER, Utils.SSR_BATCH_HEADER,
                Utils.SSR_EXPIRATION_HEADER, Utils.SSR_QUANTITY_HEADER};
=======
            String[] HEADERS = {Utils.DCR_ITEM_HEADER, Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER,
                Utils.DCR_TOTAL_QUANTITY_ISSUED_HEADER, Utils.DCR_STOCK_BALANCE_HEADER};
>>>>>>> a9031794e93461fed669b7133471ebddfa02dddd

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {
<<<<<<< HEAD
                            csvPrinter.printRecord(con.getItem().getName(),con.getDepartment().getName(), 
                                    con.getItemBatch(),
                                    con.getExp(), con.getQuantity());
=======
                            csvPrinter.printRecord(con.getItem().getName(), 
                                    con.getTotalQuantityReceived(),
                                    con.getTotalQuantityConsumed(), con.getStockBalance());
>>>>>>> a9031794e93461fed669b7133471ebddfa02dddd
                        } catch (IOException ex) {
                            LOG.error(ex.getMessage());
                        }

                    });
            csvPrinter.flush();

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

        return fileName;
    }
}
