/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTransactionDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTypeDataService;
import org.openmrs.module.openhmis.inventory.api.model.DistributionOperationType;
import org.openmrs.module.openhmis.inventory.api.model.IStockOperationType;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionReportType;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionSummaryType;
import org.openmrs.module.openhmis.ndrmodel.Container;
import org.openmrs.module.openhmis.ndrmodel.DistributionType;
import org.openmrs.module.openhmis.ndrmodel.InventoryReportType;
import org.openmrs.module.openhmis.ndrmodel.ItemType;
import org.openmrs.module.openhmis.ndrmodel.MessageHeaderType;
import org.openmrs.module.openhmis.ndrmodel.NewConsumptionType;
import org.openmrs.module.openhmis.ndrmodel.OperationItemType;
import org.openmrs.module.openhmis.ndrmodel.ReceiptType;
import org.openmrs.module.openhmis.ndrmodel.TaskOperationType;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.DictionaryMaps;
import org.openmrs.module.webservices.rest.helper.RestUtils;
import org.openmrs.module.webservices.rest.resource.PagingUtil;
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
    private IItemDataService itemDataService;
    private IDepartmentDataService departmentService;
    private IStockOperationDataService stockOperationDataService;
    private IStockOperationTransactionDataService stockOperationTransactionDataService;
    private IStockOperationTypeDataService stockOperationTypeDataService;
    private Date startDate;
    private Date endDate;
    private DictionaryMaps dictionaryMaps = new DictionaryMaps();

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public SimpleObject get(@RequestParam(value = "startDate", required = true) String startDateString,
            @RequestParam(value = "endDate", required = true) String endDateString,
            HttpServletRequest request, HttpServletResponse response) {

        this.itemDataService = Context.getService(IItemDataService.class);
        this.departmentService = Context.getService(IDepartmentDataService.class);
        this.stockOperationDataService = Context.getService(IStockOperationDataService.class);
        this.stockOperationTransactionDataService = Context.getService(IStockOperationTransactionDataService.class);
        this.stockOperationTypeDataService = Context.getService(IStockOperationTypeDataService.class);

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
            this.startDate = dateFormat.parse(startDateString);
            this.endDate = dateFormat.parse(endDateString);

            System.out.println("about to create jaxb context");
            // jaxbContext = JAXBContext.newInstance("org.openmrs.module.openhmis.ndrmodel");
            jaxbContext = JAXBContext.newInstance(Container.class);
            System.out.println("done creating jaxb context");
            System.out.println("about to create marshaller");
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            System.out.println("done creating marshaller");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            String formattedDate = new SimpleDateFormat("ddMMyy").format(new Date());

            Container reportObject = extractData(this.startDate, this.endDate);
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

        consumptionReportType.getConsumptionSummary().add(consumptionSummaryType);

        NewConsumptionType newConsumptionType = new NewConsumptionType();
        newConsumptionType.setConsumptionDate(RestUtils.getXmlDate(new Date()));
        newConsumptionType.setItemBatch("HY78383");
        newConsumptionType.setItemCode(getRandomValue().intValue());
        newConsumptionType.setTestPurposeCode("testing");
        newConsumptionType.setTestingPointCode(getRandomValue().intValue());
        newConsumptionType.setTotalUsed(getRandomValue());
        newConsumptionType.setTotalWastageLoses(getRandomValue());

        consumptionReportType.getNewConsumption().add(newConsumptionType);

        inventoryReportType.setConsumptionReport(consumptionReportType);
        ndrReportTemplate.setInventoryReport(inventoryReportType);

        return ndrReportTemplate;
    }

    private TaskOperationType extractTaskOperation() {

        TaskOperationType taskOperationType = new TaskOperationType();
        
        SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
        List<StockOperation> stockOps = null;

        //  searchConsumptionSummary.setDepartment();
        // searchConsumptionSummary.setItem(searchItem);
        searchConsumptionSummary.setStartDate(this.startDate);
        searchConsumptionSummary.setEndDate(this.endDate);
        searchConsumptionSummary.setOperationStatus(StockOperationStatus.COMPLETED);

        
        
        // for distribution
        IStockOperationType stockOperationType = getStockOperationType(ConstantUtils.DISTRIBUTION_TYPE_UUID);
        searchConsumptionSummary.setOperationType(stockOperationType);

        stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);
        
        
        List<DistributionType> allDistributionTypes = mapAndExtractDistribution(stockOps);
        if(!allDistributionTypes.isEmpty()){
            org.openmrs.module.openhmis.ndrmodel.DistributionOperationType distributionOperationType = new org.openmrs.module.openhmis.ndrmodel.DistributionOperationType();
            distributionOperationType.getDistribution().addAll(allDistributionTypes);
        }
        
        

        return null;

    }

    private List<DistributionType> mapAndExtractDistribution(List<StockOperation> stockOperations) {

        List<DistributionType> distributions = new ArrayList<>();

        for (StockOperation st : stockOperations) {

            DistributionType distributionType = null;

            try {
                distributionType = new DistributionType();
                distributionType.setOperationID(st.getOperationNumber());
                if (st.getDepartment() != null) {
                    distributionType.setDepartmentCode(dictionaryMaps.getDepartmentMappings()
                            .get(st.getDepartment().getUuid()));
                    distributionType.setDistributeTypeCode(dictionaryMaps.getDistributeToMappings()
                            .get(ConstantUtils.DEPARTMENT_STRING));
                }
                distributionType.setOperationDate(RestUtils.getXmlDate(st.getDateCreated()));
                distributionType.setSourceStockroomCode(dictionaryMaps.getSourceStockRoomMappings().get(st.getSource().getUuid()));
                
                List<ItemType> itemTypes = new ArrayList<>();
                st.getItems().forEach(a -> {
                  
                    ItemType operationItemType = new ItemType();
                    operationItemType.setBatch(a.getItemBatch());
                      try{
                    operationItemType.setExpirationDate(RestUtils.getXmlDate(a.getExpiration()));
                    }catch(Exception ex){
                          System.out.println("Error occured for distribution ITEM "+ex.getMessage());
                    }
                    operationItemType.setItemCode(dictionaryMaps.getItemMappings().get(a.getItem().getUuid()));
                    operationItemType.setQuantity(a.getQuantity().shortValue());
                    
                    //TODO: add checks for required feilds later
                    itemTypes.add(operationItemType);
                });
                
                
                OperationItemType op = new OperationItemType();
                if(!itemTypes.isEmpty()){
                    op.getItem().addAll(itemTypes);
                    distributionType.getOperationItem().add(op);
                }

            } catch (Exception ex) {
                System.out.println("Error pulling a distribution " + ex.getMessage());
            }

            if (distributionType != null) {
                distributions.add(distributionType);
            }

        }
        
        return distributions;

    }
    
    
     private List<ReceiptType> mapAndExtractReceipt(List<StockOperation> stockOperations) {

        List<ReceiptType> receipts = new ArrayList<>();

        for (StockOperation st : stockOperations) {

            ReceiptType receiptType = null;

            try {
                receiptType = new ReceiptType();
                receiptType.setOperationID(st.getOperationNumber());              
                receiptType.setOperationDate(RestUtils.getXmlDate(st.getDateCreated()));
                receiptType.setDestinationStockroomCode(dictionaryMaps.getSourceStockRoomMappings().get(st.getSource().getUuid()));
                
                List<ItemType> itemTypes = new ArrayList<>();
                st.getItems().forEach(a -> {
                  
                    ItemType operationItemType = new ItemType();
                    operationItemType.setBatch(a.getItemBatch());
                      try{
                    operationItemType.setExpirationDate(RestUtils.getXmlDate(a.getExpiration()));
                    }catch(Exception ex){
                          System.out.println("Error occured for distribution ITEM "+ex.getMessage());
                    }
                    operationItemType.setItemCode(dictionaryMaps.getItemMappings().get(a.getItem().getUuid()));
                    operationItemType.setQuantity(a.getQuantity().shortValue());
                    
                    //TODO: add checks for required feilds later
                    itemTypes.add(operationItemType);
                });
                
                
                OperationItemType op = new OperationItemType();
                if(!itemTypes.isEmpty()){
                    op.getItem().addAll(itemTypes);
                    receiptType.getOperationItem().add(op);
                }

            } catch (Exception ex) {
                System.out.println("Error pulling a distribution " + ex.getMessage());
            }

            if (receiptType != null) {
                receipts.add(receiptType);
            }

        }
        
        return receipts;

    }
    
    

    private IStockOperationType getStockOperationType(String stockOperationTypeUuid) {
        IStockOperationType stockOperationType = null;
        //   String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
        if (StringUtils.isNotEmpty(stockOperationTypeUuid)) {
            stockOperationType = stockOperationTypeDataService.getByUuid(stockOperationTypeUuid);
            if (stockOperationType == null) {
                LOG.warn("Could not parse Stock Operation Type '" + stockOperationTypeUuid + "'");
                throw new IllegalArgumentException("The type '" + stockOperationTypeUuid
                        + "' is not a valid operation type.");
            }
        }

        return stockOperationType;
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

    private BigInteger getRandomValue() {
        final int a = 10;
        return BigInteger.valueOf(Math.round(Math.random() * a));
    }

}
