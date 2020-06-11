/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionReportType;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionSummaryType;
import org.openmrs.module.openhmis.ndrmodel.Container;
import org.openmrs.module.openhmis.ndrmodel.InventoryReportType;
import org.openmrs.module.openhmis.ndrmodel.MessageHeaderType;
import org.openmrs.module.openhmis.ndrmodel.NewConsumptionType;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MORRISON.I
 */
@Controller(value = "invNDRExtractionController")
@RequestMapping(value = ModuleWebConstants.NDR_EXTRACTION_ROOT)
public class NDRExtractionController {

    private static final Log LOG = LogFactory.getLog(NDRExtractionController.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public SimpleObject get(@RequestParam(value = "startDate", required = true) String startDateString,
            @RequestParam(value = "endDate", required = true) String endDateString,
            HttpServletRequest request, HttpServletResponse response) {

        SimpleObject result = new SimpleObject();
        String datimCode = RestUtils.getFacilityLocalId();
        String facilityName = RestUtils.getFacilityName();
        String IPShortName = RestUtils.getIPShortName();
        String reportType = "Commodity";

        // Provider com.sun.xml.internal.bind.v2.ContextFactory could not be instantiated: javax.xml.bind.JAXBException: 
        // "org.openmrs.module.openhmis.ndrmodel" doesnt contain ObjectFactory.class or jaxb.index
        JAXBContext jaxbContext;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            System.out.println("about to create jaxb context");
            // jaxbContext = JAXBContext.newInstance("org.openmrs.module.openhmis.ndrmodel");
            jaxbContext = JAXBContext.newInstance(Container.class);
            System.out.println("done creating jaxb context");
            System.out.println("about to create marshaller");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            System.out.println("done creating marshaller");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String formattedDate = new SimpleDateFormat("ddMMyy").format(new Date());

            Container reportObject = extractData(startDate, endDate);
            if (reportObject != null) {

                System.out.println("starting xml creating process");
                LOG.info("Testing log4j");
                String reportFolder = RestUtils.ensureReportFolderExist(request, reportType);

                String fileName = IPShortName + "_" + "Commodity" + "_" + datimCode + "_" + formattedDate;

                String xmlFile = Paths.get(reportFolder, fileName + ".xml").toString();

                File aXMLFile = new File(xmlFile);
                Boolean b;

                b = aXMLFile.createNewFile();
                System.out.println("creating xml file : " + xmlFile + "was successful : " + b);
                writeFile(reportObject, aXMLFile, jaxbMarshaller);

                String zipFileName = facilityName + "_ " + IPShortName + "_" + datimCode + "_" + formattedDate + ".zip";

                String zipresponse = RestUtils.zipFolder(request, reportFolder, zipFileName, reportType);
                System.out.println("Final response \n" + zipresponse);

                result.put("results", zipresponse);

            }

        } catch (Exception ex) {
            result.put("error", ex.getMessage());
            System.err.println(ex.getStackTrace());

        }

        return result;

    }

    private Container extractData(Date startDate, Date endDate) throws DatatypeConfigurationException {

        //	XMLGregorianCalendar convertStartDate = RestUtils.getXmlDate(startDate);
        //	XMLGregorianCalendar convertEndDate = RestUtils.getXmlDate(endDate);
        Container ndrReportTemplate = new Container();
        MessageHeaderType messageHeaderType = new MessageHeaderType();
        //	messageHeaderType.setExportEndDate(convertEndDate);
        //	messageHeaderType.setExportStartDate(convertStartDate);
        //	messageHeaderType.setMessageCreationDateTime(RestUtils.getXmlDateTime(new Date()));
        messageHeaderType.setMessageStatusCode("UPDATED");
        messageHeaderType.setMessageUniqueID(UUID.randomUUID().toString());
        messageHeaderType.setMessageVersion(1.0f);

        ndrReportTemplate.setMessageHeader(messageHeaderType);

        Byte b = 1;
        Byte c = 2;

        InventoryReportType inventoryReportType = new InventoryReportType();
        ConsumptionReportType consumptionReportType = new ConsumptionReportType();

        ConsumptionSummaryType consumptionSummaryType = new ConsumptionSummaryType();
        consumptionSummaryType.setDepartmentCode(b);
        consumptionSummaryType.setItemCode(c);
        consumptionSummaryType.setStockBalance(getRandomValue());
        consumptionSummaryType.setTotalQuantityConsumed(getRandomValue());
        consumptionSummaryType.setTotalQuantityReceived(getRandomValue());

        consumptionReportType.setConsumptionSummary(consumptionSummaryType);

        NewConsumptionType newConsumptionType = new NewConsumptionType();
        newConsumptionType.setConsumptionDate(new Date().toString());
        newConsumptionType.setItemBatch("HY78383");
        newConsumptionType.setItemCode(getRandomValue());
        newConsumptionType.setTestPurposeCode("testing");
        newConsumptionType.setTestingPointCode("3");
        newConsumptionType.setTotalUsed("55");
        newConsumptionType.setTotalWastageLoses("23");

        consumptionReportType.getNewConsumption().add(newConsumptionType);

        inventoryReportType.setConsumptionReport(consumptionReportType);
        ndrReportTemplate.setInventoryReport(inventoryReportType);

        return ndrReportTemplate;
    }

    private void writeFile(Container ndrReportTemplate, File file, Marshaller jaxbMarshaller) {

        try {
            //	javax.xml.validation.Validator validator = jaxbMarshaller.getSchema().newValidator();
            jaxbMarshaller.marshal(ndrReportTemplate, file);

        } catch (Exception ex) {
            System.out.println("File " + file.getName() + " throw an exception \n" + ex.getMessage());
            //	throw ex;
        }

    }

    private byte getRandomValue() {
        final int a = 10;
        return Byte.parseByte(String.valueOf(Math.round(Math.random() * a)));
    }

}
