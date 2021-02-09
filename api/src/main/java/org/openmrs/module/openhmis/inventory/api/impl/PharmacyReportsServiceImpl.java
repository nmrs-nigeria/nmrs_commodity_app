/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.openmrs.module.openhmis.inventory.api.IPharmacyReportsService;

/**
 * @author MORRISON.I
 */
public class PharmacyReportsServiceImpl implements IPharmacyReportsService {

    @Override
    public String getPharmacyConsumptionByDate(String reportId, List<PharmacyConsumptionSummary> reportData,
            String reportFolder) {
        AtomicInteger rowCount = new AtomicInteger(0);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(Utils.DISPENSARY_CONSUMPTION_REPORT_SHEET_NAME);
        Row header = sheet.createRow(rowCount.get());
        Cell headerCell = header.createCell(Utils.ZERO_INTEGER);
        headerCell.setCellValue(Utils.DCR_ITEM_HEADER);
        headerCell = header.createCell(Utils.ONE_INTEGER);
        headerCell.setCellValue(Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER);
        headerCell = header.createCell(Utils.TWO_INTEGER);
        headerCell.setCellValue(Utils.DCR_TOTAL_QUANTITY_CONSUMED_HEADER);
        headerCell = header.createCell(Utils.THREE_INTEGER);
        headerCell.setCellValue(Utils.DCR_TOTAL_WASTAGE_HEADER);
        headerCell = header.createCell(Utils.FOUR_INTEGER);
        headerCell.setCellValue(Utils.DCR_STOCK_BALANCE_HEADER);

        reportData.stream()
                .forEach(con -> {

                    Row row = sheet.createRow(rowCount.incrementAndGet());
                    Cell cell = row.createCell(Utils.ZERO_INTEGER);
                    cell.setCellValue(con.getItem().getName());

                    cell = row.createCell(Utils.ONE_INTEGER);
                    cell.setCellValue(con.getTotalQuantityReceived());

                    cell = row.createCell(Utils.TWO_INTEGER);
                    cell.setCellValue(con.getTotalQuantityConsumed());

                    cell = row.createCell(Utils.THREE_INTEGER);
                    cell.setCellValue(con.getTotalQuantityWasted());

                    cell = row.createCell(Utils.FOUR_INTEGER);
                    cell.setCellValue(con.getStockBalance());

                });

        String fileName = Paths.get(reportFolder, reportId + ".xlsx").toString();
        try {

            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PharmacyReportsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PharmacyReportsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return fileName;
    }
}