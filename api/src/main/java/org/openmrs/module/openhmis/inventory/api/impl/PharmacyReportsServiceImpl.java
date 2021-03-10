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
import org.hibernate.validator.util.LoggerFactory;
import org.openmrs.module.openhmis.inventory.api.model.ItemExpirationSummaryReport;
import org.openmrs.module.openhmis.inventory.api.model.PharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.openmrs.module.openhmis.inventory.api.IPharmacyReportsService;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;

/**
 * @author MORRISON.I
 */
public class PharmacyReportsServiceImpl implements IPharmacyReportsService {

    private static final Log LOG = LogFactory.getLog(PharmacyReportsServiceImpl.class);

    @Override
    public String getPharmacyConsumptionByDate(String reportId, List<NewPharmacyConsumptionSummary> reportData,
            String reportFolder) {

        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

            String[] HEADERS = {Utils.DCR_ITEM_HEADER, Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER,
                Utils.DCR_DRUG_CATEGORY_HEADER};

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {
                            csvPrinter.printRecord(con.getItem(), con.getTotalQuantityReceived(), con.getDrugCategory());
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

    @Override
    public String getPharmacyStockroomConsumptionByDate(String reportId,
            List<PharmacyConsumptionSummary> reportData, String reportFolder) {

        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

            String[] HEADERS = {Utils.DCR_ITEM_HEADER, Utils.DCR_TOTAL_QUANTITY_RECEIVED_HEADER,
                Utils.DCR_TOTAL_QUANTITY_ISSUED_HEADER, Utils.DCR_STOCK_BALANCE_HEADER};

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {
                            csvPrinter.printRecord(con.getItem().getName(),
                                    con.getTotalQuantityReceived(),
                                    con.getTotalQuantityConsumed(), con.getStockBalance());
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

        String fileName = Paths.get(reportFolder, reportId + ".csv").toString();

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(reportFolder, reportId + ".csv"));

            String[] HEADERS = {Utils.SSR_ITEM_HEADER, Utils.SSR_DEPARTMENT_HEADER, Utils.SSR_BATCH_HEADER,
                Utils.SSR_EXPIRATION_HEADER, Utils.SSR_QUANTITY_HEADER};

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(HEADERS));

            reportData.stream()
                    .forEach(con -> {

                        try {

                            csvPrinter.printRecord(con.getItem().getName(), con.getDepartment().getName(),
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
