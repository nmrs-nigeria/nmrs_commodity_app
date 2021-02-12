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

// System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
// System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
// System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
// 
//        AtomicInteger rowCount = new AtomicInteger(0);
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet(Utils.DISPENSARY_CONSUMPTION_REPORT_SHEET_NAME);
//        Row header = sheet.createRow(rowCount.get());
//        Cell headerCell = header.createCell(Utils.ZERO_INTEGER);
//        headerCell.setCellValue(Utils.DCR_ITEM_HEADER);
//        headerCell = header.createCell(Utils.ONE_INTEGER);
//        headerCell.setCellValue(Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER);
//        headerCell = header.createCell(Utils.TWO_INTEGER);
//        headerCell.setCellValue(Utils.DCR_TOTAL_QUANTITY_CONSUMED_HEADER);
//        headerCell = header.createCell(Utils.THREE_INTEGER);
//        headerCell.setCellValue(Utils.DCR_TOTAL_WASTAGE_HEADER);
//        headerCell = header.createCell(Utils.FOUR_INTEGER);
//        headerCell.setCellValue(Utils.DCR_STOCK_BALANCE_HEADER);
//
//        reportData.stream()
//                .forEach(con -> {
//
//                    Row row = sheet.createRow(rowCount.incrementAndGet());
//                    Cell cell = row.createCell(Utils.ZERO_INTEGER);
//                    cell.setCellValue(con.getItem().getName());
//
//                    cell = row.createCell(Utils.ONE_INTEGER);
//                    cell.setCellValue(con.getTotalQuantityReceived());
//
//                    cell = row.createCell(Utils.TWO_INTEGER);
//                    cell.setCellValue(con.getTotalQuantityConsumed());
//
//                    cell = row.createCell(Utils.THREE_INTEGER);
//                    cell.setCellValue(con.getTotalQuantityWasted());
//
//                    cell = row.createCell(Utils.FOUR_INTEGER);
//                    cell.setCellValue(con.getStockBalance());
//
//                });

        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();
//        try {
//
//            FileOutputStream outputStream = new FileOutputStream(fileName);
//            workbook.write(outputStream);
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(PharmacyReportsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(PharmacyReportsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }

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

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

            String[] HEADERS = {Utils.SSR_ITEM_HEADER,Utils.SSR_DEPARTMENT_HEADER, Utils.SSR_BATCH_HEADER,
                Utils.SSR_EXPIRATION_HEADER, Utils.SSR_QUANTITY_HEADER};

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {
                            csvPrinter.printRecord(con.getItem().getName(),con.getDepartment().getName(), 
                                    con.getItemBatch(),
                                    con.getExp(), con.getQuantity());
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
