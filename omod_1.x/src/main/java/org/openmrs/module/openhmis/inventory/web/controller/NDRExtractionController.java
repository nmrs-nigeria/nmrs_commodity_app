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
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IItemStockDataService;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemStock;
import org.openmrs.module.openhmis.inventory.api.model.ItemStockDetailBase;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.openhmis.ndrmodel.NdrReportTemplate;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.web.RestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author MORRISON.I
 */
@Controller(value = "invNDRExtractionController")
@RequestMapping(value = ModuleWebConstants.NDR_EXTRACTION_PAGE)
public class NDRExtractionController {

    private static final Log LOG = LogFactory.getLog(NDRExtractionController.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public SimpleObject get(@RequestParam(value = "startDate", required = true) Date startDate,
            @RequestParam(value = "endDate", required = true) Date endDate,
            HttpServletRequest request, HttpServletResponse response) {

        SimpleObject result = new SimpleObject();
        String datimCode = RestUtils.getFacilityLocalId();
        String facilityName = RestUtils.getFacilityName();
        String IPShortName = RestUtils.getIPShortName();

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance("org.openmrs.module.openhmis.ndrmodel");
            Marshaller jaxbMarshaller = RestUtils.createMarshaller(jaxbContext);

            String formattedDate = new SimpleDateFormat("ddMMyy").format(new Date());

            NdrReportTemplate reportObject = extractData();
            if (reportObject != null) {

                String reportFolder = RestUtils.ensureReportFolderExist(request, "Commodity");

                String fileName = IPShortName + "_" + facilityName + "_" + datimCode + "_" + formattedDate;

                String xmlFile = Paths.get(reportFolder, fileName + ".xml").toString();

                File aXMLFile = new File(xmlFile);
                Boolean b;

                b = aXMLFile.createNewFile();
                System.out.println("creating xml file : " + xmlFile + "was successful : " + b);
                writeFile(reportObject, aXMLFile, jaxbMarshaller);
            
                result.put("results", xmlFile);

            }

        } catch (Exception ex) {
            result.put("error", ex.getMessage());

        }

        return result;

    }

    private NdrReportTemplate extractData() {
        NdrReportTemplate ndrReportTemplate = new NdrReportTemplate();
        return ndrReportTemplate;
    }

    private void writeFile(NdrReportTemplate ndrReportTemplate, File file, Marshaller jaxbMarshaller) {

        try {
            javax.xml.validation.Validator validator = jaxbMarshaller.getSchema().newValidator();
            jaxbMarshaller.marshal(ndrReportTemplate, file);

        } catch (Exception ex) {
            System.out.println("File " + file.getName() + " throw an exception \n" + ex.getMessage());
            //	throw ex;
        }

    }

}
