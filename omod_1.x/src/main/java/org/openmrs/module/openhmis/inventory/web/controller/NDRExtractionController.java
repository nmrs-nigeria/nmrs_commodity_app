/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.web.controller;

import com.umb.ndr.signer.Signer;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.openhmis.inventory.api.IConsumptionDataService;
import org.openmrs.module.openhmis.inventory.api.IDepartmentDataService;
import org.openmrs.module.openhmis.inventory.api.IItemDataService;
import org.openmrs.module.openhmis.inventory.api.INDRValidationService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTransactionDataService;
import org.openmrs.module.openhmis.inventory.api.IStockOperationTypeDataService;
import org.openmrs.module.openhmis.inventory.api.model.Consumption;
import org.openmrs.module.openhmis.inventory.api.model.ConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.Department;
import org.openmrs.module.openhmis.inventory.api.model.IStockOperationType;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.SearchConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.model.StockOperation;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationItem;
import org.openmrs.module.openhmis.inventory.api.model.StockOperationStatus;
import org.openmrs.module.openhmis.inventory.web.ModuleWebConstants;
import org.openmrs.module.openhmis.ndrmodel.AdjustmentOperationType;
import org.openmrs.module.openhmis.ndrmodel.AdjustmentType;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionReportType;
import org.openmrs.module.openhmis.ndrmodel.ConsumptionSummaryType;
import org.openmrs.module.openhmis.ndrmodel.Container;
import org.openmrs.module.openhmis.ndrmodel.DistributionType;
import org.openmrs.module.openhmis.ndrmodel.InventoryReportType;
import org.openmrs.module.openhmis.ndrmodel.ItemType;
import org.openmrs.module.openhmis.ndrmodel.MessageHeaderType;
import org.openmrs.module.openhmis.ndrmodel.MessageSendingOrganisationType;
import org.openmrs.module.openhmis.ndrmodel.NewConsumptionType;
import org.openmrs.module.openhmis.ndrmodel.OperationItemType;
import org.openmrs.module.openhmis.ndrmodel.ReceiptOperationType;
import org.openmrs.module.openhmis.ndrmodel.ReceiptType;
import org.openmrs.module.openhmis.ndrmodel.ReturnOperationType;
import org.openmrs.module.openhmis.ndrmodel.ReturnType;
import org.openmrs.module.openhmis.ndrmodel.TaskOperationType;
import org.openmrs.module.openhmis.ndrmodel.TransferOperationType;
import org.openmrs.module.openhmis.ndrmodel.TransferType;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.helper.ConstantUtils;
import org.openmrs.module.webservices.rest.helper.DictionaryMaps;
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
	private IItemDataService itemDataService;
	private IDepartmentDataService departmentService;
	private IStockOperationDataService stockOperationDataService;
	private IStockOperationTransactionDataService stockOperationTransactionDataService;
	private IStockOperationTypeDataService stockOperationTypeDataService;
	private IConsumptionDataService consumptionDataService;
	private INDRValidationService nDRValidationService;

	private Date startDate;
	private Date endDate;
	private DictionaryMaps dictionaryMaps = new DictionaryMaps();
	private List<Department> allDepartments = null;
	private List<Item> allItems = null;

	//general date formats
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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
		this.consumptionDataService = Context.getService(IConsumptionDataService.class);
		this.nDRValidationService = Context.getService(INDRValidationService.class);

		SimpleObject result = new SimpleObject();
		String datimCode = RestUtils.getFacilityLocalId();
		String facilityName = RestUtils.getFacilityName();
		String IPShortName = RestUtils.getIPShortName();
		String reportType = "Commodity";

		// Provider com.sun.xml.internal.bind.v2.ContextFactory could not be instantiated: javax.xml.bind.JAXBException: 
		// "org.openmrs.module.openhmis.ndrmodel" doesnt contain ObjectFactory.class or jaxb.index
		JAXBContext jaxbContext;
		try {

			//	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			this.startDate = RestUtils.parseCustomOpenhmisDateString(startDateString);
			this.endDate = RestUtils.parseCustomOpenhmisDateString(endDateString);

			System.out.println("about to create jaxb context");
			// jaxbContext = JAXBContext.newInstance("org.openmrs.module.openhmis.ndrmodel");
			jaxbContext = JAXBContext.newInstance(Container.class);
			System.out.println("done creating jaxb context");
			System.out.println("about to create marshaller");
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			System.out.println("done creating marshaller");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			String formattedDate = new SimpleDateFormat("ddMMyy").format(new Date());

			Container reportObject = extractData();
			String validationStr = extractValidation();
			if (validationStr != null) {
				reportObject.getMessageHeader().setValidation(validationStr);
			}

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
			System.err.println(ex.getMessage());

		}

		return result;

	}

	private String extractValidation() {
		String validationString = nDRValidationService.getValidation(RestUtils.getFacilityLocalId());
		return validationString;

	}

	private Container extractData() throws Exception {

		//	XMLGregorianCalendar convertStartDate = RestUtils.getXmlDate(startDate);
		//	XMLGregorianCalendar convertEndDate = RestUtils.getXmlDate(endDate);
		Container ndrReportTemplate = new Container();
		MessageHeaderType messageHeaderType = new MessageHeaderType();
		messageHeaderType.setExportEndDate(dateTimeFormat.format(endDate));
		messageHeaderType.setExportStartDate(dateTimeFormat.format(startDate));
		messageHeaderType.setMessageCreationDateTime(dateTimeFormat.format(new Date()));
		messageHeaderType.setMessageStatusCode("INITIAL");
		messageHeaderType.setMessageUniqueID(UUID.randomUUID().toString());
		messageHeaderType.setMessageVersion(1.0f);
		messageHeaderType.setXmlType("commodity");

		MessageSendingOrganisationType messageSendingOrganisationType = new MessageSendingOrganisationType();
		messageSendingOrganisationType.setFacilityID(RestUtils.getFacilityLocalId());
		messageSendingOrganisationType.setFacilityName(RestUtils.getFacilityName());
		messageSendingOrganisationType.setFacilityTypeCode(RestUtils.getFacilityType());

		messageHeaderType.setMessageSendingOrganisation(messageSendingOrganisationType);

		ndrReportTemplate.setMessageHeader(messageHeaderType);

		InventoryReportType inventoryReportType = new InventoryReportType();
		ConsumptionReportType consumptionReportType = new ConsumptionReportType();

		List<ConsumptionSummaryType> consumptionSummaryTypes = extractConsumptionSummaryReport();

		if (!consumptionSummaryTypes.isEmpty()) {
			consumptionReportType.getConsumptionSummary().addAll(consumptionSummaryTypes);
		}

		List<NewConsumptionType> newConsumptionTypeList = extractConsumptionReport();

		if (!newConsumptionTypeList.isEmpty()) {
			consumptionReportType.getNewConsumption().addAll(newConsumptionTypeList);
		}

		inventoryReportType.setConsumptionReport(consumptionReportType);

		TaskOperationType taskOperationType = extractTaskOperation();
		if (taskOperationType != null) {
			inventoryReportType.setTaskOperation(taskOperationType);
		}

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
		if (!allDistributionTypes.isEmpty()) {
			org.openmrs.module.openhmis.ndrmodel.DistributionOperationType distributionOperationType =
			        new org.openmrs.module.openhmis.ndrmodel.DistributionOperationType();
			distributionOperationType.getDistribution().addAll(allDistributionTypes);
			taskOperationType.setDistributionOperation(distributionOperationType);
		}

		// for Receipt
		IStockOperationType receiptStockOperationType = getStockOperationType(ConstantUtils.RECEIPT_TYPE_UUID);
		searchConsumptionSummary.setOperationType(receiptStockOperationType);

		stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		List<ReceiptType> allRceiptTypes = mapAndExtractReceipt(stockOps);
		if (!allRceiptTypes.isEmpty()) {
			ReceiptOperationType receiptOperationType = new ReceiptOperationType();
			receiptOperationType.getReceipt().addAll(allRceiptTypes);
			taskOperationType.setReceiptOperation(receiptOperationType);
		}

		// for adjustment
		IStockOperationType adjustmentStockOperationType = getStockOperationType(ConstantUtils.ADJUSTMENT_TYPE_UUID);
		searchConsumptionSummary.setOperationType(adjustmentStockOperationType);

		stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		List<AdjustmentType> allAdjustmentTypes = mapAndExtractAdjustment(stockOps);
		if (!allAdjustmentTypes.isEmpty()) {
			AdjustmentOperationType adjustmentOperationType = new AdjustmentOperationType();
			adjustmentOperationType.getAdjustment().addAll(allAdjustmentTypes);
			taskOperationType.setAdjustmentOperation(adjustmentOperationType);
		}

		// for return
		IStockOperationType returnStockOperationType = getStockOperationType(ConstantUtils.RETURN_TYPE_UUID);
		searchConsumptionSummary.setOperationType(returnStockOperationType);

		stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		List<ReturnType> allReturnTypes = mapAndExtractReturn(stockOps);
		if (!allReturnTypes.isEmpty()) {
			ReturnOperationType returnOperationType = new ReturnOperationType();
			returnOperationType.getReturn().addAll(allReturnTypes);
			taskOperationType.setReturnOperation(returnOperationType);
		}

		//for transfers
		IStockOperationType transferStockOperationType = getStockOperationType(ConstantUtils.TRANSFER_TYPE_UUID);
		searchConsumptionSummary.setOperationType(transferStockOperationType);

		stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

		List<TransferType> allTRansferTypes = mapAndExtractTransfer(stockOps);
		if (!allTRansferTypes.isEmpty()) {
			TransferOperationType transferOperationType = new TransferOperationType();
			transferOperationType.getTransfer().addAll(allTRansferTypes);
			taskOperationType.setTransferOperation(transferOperationType);
		}

		return taskOperationType;

	}

	private List<ConsumptionSummaryType> extractConsumptionSummaryReport() {

		List<StockOperation> stockOps;
		List<ConsumptionSummaryType> finalConsumptionSummaryTypes = new ArrayList<>();

		allDepartments = departmentService.getAll();
		allItems = itemDataService.getAll();

		SearchConsumptionSummary searchConsumptionSummary = new SearchConsumptionSummary();
		StockOperationStatus status = StockOperationStatus.COMPLETED;
		IStockOperationType stockOperationType = getStockOperationType();

		for (Department department : allDepartments) {
			try {
				searchConsumptionSummary.setDepartment(department);
				//	searchConsumptionSummary.setItem(searchItem);
				searchConsumptionSummary.setStartDate(startDate);
				searchConsumptionSummary.setEndDate(endDate);
				searchConsumptionSummary.setOperationStatus(status);
				searchConsumptionSummary.setOperationType(stockOperationType);

				stockOps = stockOperationDataService.getOperationsByDateDiff(searchConsumptionSummary, null);

				System.out.println("About to get summary for " + department.getName());

				List<ConsumptionSummary> consumptionSummarys = consumptionDataService.retrieveConsumptionSummary(stockOps,
				    searchConsumptionSummary, null, allItems);

				System.err.println("Records for testing point is " + consumptionSummarys.size());

				if (!consumptionSummarys.isEmpty()) {
					List<ConsumptionSummaryType> consumptionSummaryTypes =
					        convertToConsumptionSummaryType(consumptionSummarys);
					finalConsumptionSummaryTypes.addAll(consumptionSummaryTypes);
				}

				System.out.println("Finished getting summary for " + department.getName());

			} catch (Exception ex) {
				LOG.error("error occured during consumption for" + department, ex);
			}

		}

		return finalConsumptionSummaryTypes;

	}

	private List<NewConsumptionType> extractConsumptionReport() {
        List<Consumption> consumptions
                = consumptionDataService.getConsumptionByConsumptionDate(startDate, endDate, null);
        
        List<NewConsumptionType> finalConsumptionReport = new ArrayList<>();
        
        consumptions.stream().forEach(con -> {
            try {
                NewConsumptionType newConsumptionType = new NewConsumptionType();
                newConsumptionType.setConsumptionDate(dateFormat.format(con.getConsumptionDate()));
                newConsumptionType.setConsumptionUUID(con.getUuid());
                newConsumptionType.setItemBatch(con.getBatchNumber());
                newConsumptionType.setItemCode(dictionaryMaps.getItemMappings().get(con.getItem().getUuid()));
                newConsumptionType.setTestingPointCode(dictionaryMaps.getDepartmentMappings()
                        .get(con.getDepartment().getUuid()));
                newConsumptionType.setTotalUsed(BigInteger.valueOf(con.getQuantity()));
                newConsumptionType.setTestPurposeCode(dictionaryMaps.getTestPurpose().get(con.getTestPurpose()));
                newConsumptionType.setTotalWastageLoses(BigInteger.valueOf(con.getWastage()));
                
                finalConsumptionReport.add(newConsumptionType);
            } catch (Exception ex) {
                LOG.warn(ex.getMessage());
            }
            
        });
        
        return finalConsumptionReport;
        
    }

	private List<ConsumptionSummaryType> convertToConsumptionSummaryType(List<ConsumptionSummary> consumptionSummarys) {
        
        List<ConsumptionSummaryType> consumptionSummaryTypes = new ArrayList<>();
        consumptionSummarys.stream().forEach(a -> {
            try {
                if (a.getTotalQuantityReceived() > 0 || a.getTotalQuantityConsumed() > 0) {
                    ConsumptionSummaryType consumptionSummaryType = new ConsumptionSummaryType();
                    consumptionSummaryType.setDepartmentCode(dictionaryMaps.getDepartmentMappings().
                            get(a.getDepartment().getUuid()));
                    consumptionSummaryType.setItemCode(dictionaryMaps.getItemMappings().get(a.getItem().getUuid()));
                    consumptionSummaryType.setStockBalance(BigInteger.valueOf(a.getStockBalance()));
                    consumptionSummaryType.setTotalQuantityConsumed(BigInteger.valueOf(a.getTotalQuantityConsumed()));
                    consumptionSummaryType.setTotalQuantityReceived(BigInteger.valueOf(a.getTotalQuantityReceived()));
                    
                    consumptionSummaryTypes.add(consumptionSummaryType);
                }
            } catch (Exception ex) {
                System.out.println("Error converting a record to summary: " + ex.getMessage());
            }
            
        });
        
        return consumptionSummaryTypes;
        
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
				distributionType.setOperationDate(dateFormat.format(st.getDateCreated()));
				distributionType.setSourceStockroomCode(dictionaryMaps.getSourceStockRoomMappings()
				        .get(st.getSource().getUuid()));

				List<ItemType> itemTypes = extractOPerationItems(st.getItems());

				OperationItemType op = new OperationItemType();
				if (!itemTypes.isEmpty()) {
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
				receiptType.setOperationDate(dateFormat.format(st.getDateCreated()));

				if (st.getCommoditySource() != null && !"".equals(st.getCommoditySource())) {
					receiptType.setStockSource(dictionaryMaps.getCommoditySource()
					        .get(st.getCommoditySource()));
				}

				try {
					receiptType.setDestinationStockroomCode(dictionaryMaps.getSourceStockRoomMappings()
					        .get(st.getDestination().getUuid()));
				} catch (Exception ex) {
					System.out.println("exception on receipt stockroom " + ex.getMessage());
				}

				List<ItemType> itemTypes = extractOPerationItems(st.getItems());

				OperationItemType op = new OperationItemType();
				if (!itemTypes.isEmpty()) {
					op.getItem().addAll(itemTypes);
					receiptType.getOperationItem().add(op);
				}

			} catch (Exception ex) {
				System.out.println("Error pulling a receipt " + ex.getMessage());
			}

			if (receiptType != null) {
				receipts.add(receiptType);
			}

		}

		return receipts;

	}

	private List<AdjustmentType> mapAndExtractAdjustment(List<StockOperation> stockOperations) {

		List<AdjustmentType> adjustments = new ArrayList<>();

		for (StockOperation st : stockOperations) {

			AdjustmentType adjustmentType = null;

			try {
				adjustmentType = new AdjustmentType();
				adjustmentType.setOperationID(st.getOperationNumber());
				if (st.getAdjustmentKind() != null) {
					adjustmentType.setAdjustmentTypeCode(dictionaryMaps.getAdjustmentTypeMappings()
					        .get(st.getAdjustmentKind().toLowerCase()));
				}
				adjustmentType.setOperationDate(dateFormat.format(st.getDateCreated()));
				adjustmentType.setSourceStockroomCode(dictionaryMaps.getSourceStockRoomMappings()
				        .get(st.getSource().getUuid()));

				OperationItemType op = new OperationItemType();
				List<ItemType> itemTypes = extractOPerationItems(st.getItems());
				if (!itemTypes.isEmpty()) {
					op.getItem().addAll(itemTypes);
					adjustmentType.getOperationItem().add(op);
				}

			} catch (Exception ex) {
				System.out.println("Error pulling an adjustment " + ex.getMessage());
			}

			if (adjustmentType != null) {
				adjustments.add(adjustmentType);
			}

		}

		return adjustments;

	}

	private List<ReturnType> mapAndExtractReturn(List<StockOperation> stockOperations) {

		List<ReturnType> returns = new ArrayList<>();

		for (StockOperation st : stockOperations) {

			ReturnType returnType = null;

			try {
				returnType = new ReturnType();
				returnType.setOperationID(st.getOperationNumber());
				if (st.getDepartment() != null) {
					returnType.setReturnTypeCode(dictionaryMaps.getReturnMappings()
					        .get(ConstantUtils.DEPARTMENT_STRING));
					returnType.setDepartmentCode(dictionaryMaps.getDepartmentMappings()
					        .get(st.getDepartment().getUuid()));
				}

				if (st.getInstitution() != null) {
					returnType.setReturnTypeCode(dictionaryMaps.getReturnMappings()
					        .get(ConstantUtils.INSTITUTION_STRING));
				}

				returnType.setOperationDate(dateFormat.format(st.getDateCreated()));
				returnType.setDestinationStockroomCode(dictionaryMaps.getSourceStockRoomMappings()
				        .get(st.getDestination().getUuid()));

				OperationItemType op = new OperationItemType();
				List<ItemType> itemTypes = extractOPerationItems(st.getItems());
				if (!itemTypes.isEmpty()) {
					op.getItem().addAll(itemTypes);
					returnType.getOperationItem().add(op);
				}

			} catch (Exception ex) {
				System.out.println("Error pulling a return " + ex.getMessage());
			}

			if (returnType != null) {
				returns.add(returnType);
			}

		}

		return returns;

	}

	private List<TransferType> mapAndExtractTransfer(List<StockOperation> stockOperations) {

		List<TransferType> transfers = new ArrayList<>();

		for (StockOperation st : stockOperations) {

			TransferType transferType = null;

			try {
				transferType = new TransferType();
				transferType.setOperationID(st.getOperationNumber());
				if (st.getDepartment() != null) {
					//                    transferType.setTransferCode(dictionaryMaps.getReturnMappings()
					//                            .get(ConstantUtils.DEPARTMENT_STRING));

				}

				if (st.getInstitution() != null) {
					transferType.setInstitutionType(st.getUuid());
				}

				transferType.setOperationDate(dateFormat.format(st.getDateCreated()));
				transferType.setSourceStockroomCode(dictionaryMaps.getSourceStockRoomMappings()
				        .get(st.getSource().getUuid()));

				OperationItemType op = new OperationItemType();
				List<ItemType> itemTypes = extractOPerationItems(st.getItems());
				if (!itemTypes.isEmpty()) {
					op.getItem().addAll(itemTypes);
					transferType.getOperationItem().add(op);
				}

			} catch (Exception ex) {
				System.out.println("Error pulling a transfer " + ex.getMessage());
			}

			if (transferType != null) {
				transfers.add(transferType);
			}

		}

		return transfers;

	}

	private List<ItemType> extractOPerationItems(Set<StockOperationItem> stockOperationItems) {
        List<ItemType> itemTypes = new ArrayList<>();
        stockOperationItems.forEach(a -> {
            
            ItemType operationItemType = new ItemType();
            operationItemType.setBatch(a.getItemBatch());
            try {
                operationItemType.setExpirationDate(dateFormat.format(a.getExpiration()));
            } catch (Exception ex) {
                System.out.println("Error occured while pulling ITEM " + ex.getMessage());
            }
            operationItemType.setItemCode(dictionaryMaps.getItemMappings().get(a.getItem().getUuid()));
            operationItemType.setQuantity(a.getQuantity().shortValue());

            //TODO: add checks for required feilds later
            itemTypes.add(operationItemType);
        });
        
        return itemTypes;
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

	private IStockOperationType getStockOperationType() {
		IStockOperationType stockOperationType = null;
		String stockOperationTypeUuid = ConstantUtils.DISTRIBUTION_TYPE_UUID;
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

}
