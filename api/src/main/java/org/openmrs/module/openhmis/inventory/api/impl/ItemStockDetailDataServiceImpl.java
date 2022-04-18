/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.annotation.Authorized;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseObjectDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.f.Action1;
import org.openmrs.module.openhmis.inventory.api.IItemStockDetailDataService;
import org.openmrs.module.openhmis.inventory.api.model.*;
import org.openmrs.module.openhmis.inventory.api.security.BasicObjectAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.primitives.Ints;

/**
 * Data service implementation class for {@link ItemStockDetail}.
 */
@Transactional
public class ItemStockDetailDataServiceImpl
        extends BaseObjectDataServiceImpl<ItemStockDetail, BasicObjectAuthorizationPrivileges>
        implements IItemStockDetailDataService {

	private static final int THREE = 3;
	private static final int TWO = 2;
	private static final int FOUR = 4;
	private static final int FIVE = 5;

	@Override
	protected BasicObjectAuthorizationPrivileges getPrivileges() {
		return new BasicObjectAuthorizationPrivileges();
	}

	@Override
	protected void validate(ItemStockDetail object) {

	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockDetail> getItemStockDetailsByStockroom(final Stockroom stockroom, PagingInfo pagingInfo) {
		if (stockroom == null) {
			throw new IllegalArgumentException("The stockroom must be defined.");
		}

		return executeCriteria(ItemStockDetail.class, pagingInfo, new Action1<Criteria>() {
			@Override
			public void apply(Criteria criteria) {
				criteria.createAlias("item", "i");
				criteria.add(Restrictions.eq("stockroom", stockroom));
			}
		}, Order.asc("i.name"));
	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockSummary> getItemStockSummaryByStockroom(final Stockroom stockroom, PagingInfo pagingInfo) {
		if (stockroom == null) {
			throw new IllegalArgumentException("The stockroom must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)
			String countHql = "select 1 "
			        + "from ItemStockDetail as detail "
			        + "where stockroom.id = " + stockroom.getId() + " "
			        + "group by item, expiration "
			        + "having sum(detail.quantity) <> 0";
			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging
		String hql = "select i, detail.expiration, sum(detail.quantity) as sumQty, detail.itemBatch "
		        + "from ItemStockDetail as detail inner join detail.item as i "
		        + "where detail.stockroom.id = " + stockroom.getId() + " "
		        + "group by i, detail.expiration "
		        + "having sum(detail.quantity) <> 0"
		        + "order by i.name asc, detail.expiration asc";
		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == THREE) {
				summary.setExpiration(null);
				Integer quantity = Ints.checkedCast((Long)row[1]);
				String batch = (String)row[2];
				summary.setItemBatch(batch);
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = Ints.checkedCast((Long)row[2]);
				String batch = (String)row[THREE];
				summary.setItemBatch(batch);
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			}

			results.add(summary);
		}

		// We done.
		return results;
	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ClosingbalanceUpdate> getItemStockSummaryByItemType(final Stockroom stockroom, PagingInfo pagingInfo) {
		if (stockroom == null) {
			throw new IllegalArgumentException("The stockroom must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)
			String countHql = "select 1 "
			        + "from Item as detail "
			        + "where itemType = '" + stockroom.getStockroomType() + "'";
			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging
		String hql = "select detail.id, detail.name, detail.uuid "
		        + "from Item as detail"
		        + "where detail.itemType = '" + stockroom.getStockroomType() + "' "
		        + "order by detail.name asc";
		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ClosingbalanceUpdate> results = new ArrayList<ClosingbalanceUpdate>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ClosingbalanceUpdate summary = new ClosingbalanceUpdate();
			Integer itemid = Ints.checkedCast((Long)row[0]);
			summary.setItemId(itemid);
			String itemname = (String)row[1];
			summary.setItemName(itemname);
			String itemuuid = (String)row[2];
			summary.setItemName(itemuuid);
			results.add(summary);
		}

		// We done.
		return results;
	}

	@Override
	@Transactional(readOnly = true)
	@Authorized({ PrivilegeConstants.VIEW_METADATA })
	public List<ItemStockSummary> getItemStockSummaryByDepartment(final Department department, PagingInfo pagingInfo) {
		if (department == null) {
			throw new IllegalArgumentException("The department must be defined.");
		}

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)	
			String countHql = "select 1 "
			        + "from ViewItemExpirationByDept as detail "
			        + "where department.id = " + department.getId();

			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging	
		String hql = "select i, detail.expiration, detail.quantity as sumQty "
		        + "from ViewItemExpirationByDept as detail inner join detail.item as i "
		        + "where detail.department.id = " + department.getId() + " "
		        + "order by i.name asc, detail.expiration asc";

		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == 2) {
				summary.setExpiration(null);
				Integer quantity = (int)row[1];
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = (int)row[2];
				if (quantity != 0) {
					summary.setQuantity(quantity);
				} else {
					continue;
				}
			}

			results.add(summary);
		}

		// We done.
		return results;
	}

	@Override
	@Transactional(readOnly = false)
	@Authorized({ PrivilegeConstants.MANAGE_OPERATIONS })
	public List<ItemStockSummary> getItemStockSummaryByDepartmentPharmacy(final Department department,
	        PagingInfo pagingInfo) {
		if (department == null) {
			throw new IllegalArgumentException("The department must be defined.");
		}

		//deduction implementation method
		deductionImplementation(department);

		// We cannot use a normal Criteria query here because criteria does not support a group by with a having statement
		// so HQL it is!

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			// Load the record count (for paging)	
			String countHql = "select 1 "
			        + "from ViewInvStockonhandPharmacyDispensary as detail "
			        + "where department.id = " + department.getId();

			Query countQuery = getRepository().createQuery(countHql);

			Integer count = countQuery.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		// Create the query and optionally add paging	
		String hql = "select i, detail.expiration, detail.updatableQuantity as sumQty, detail.id, detail.itemBatch "
		        + "from ViewInvStockonhandPharmacyDispensary as detail inner join detail.item as i "
		        + "where detail.department.id = " + department.getId() + " "
		        + "order by i.name asc, detail.expiration asc";

		Query query = getRepository().createQuery(hql);
		query = this.createPagingQuery(pagingInfo, query);

		List list = query.list();

		// Parse the aggregate query into an ItemStockSummary object
		List<ItemStockSummary> results = new ArrayList<ItemStockSummary>(list.size());
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			ItemStockSummary summary = new ItemStockSummary();
			summary.setItem((Item)row[0]);

			// If the expiration column is null it does not appear to be included in the row array
			if (row.length == FOUR) {
				summary.setExpiration(null);
				Integer quantity = (int)row[1];
				Integer pharmId = (int)row[2];
				String batch = (String)row[THREE];
				// skip record if the sum of item stock quantities == 0
				if (quantity != 0) {
					summary.setQuantity(quantity);
					summary.setPharmStockOnHandId(pharmId);
					summary.setItemBatch(batch);
				} else {
					continue;
				}
			} else {
				summary.setExpiration((Date)row[1]);
				Integer quantity = (int)row[2];
				Integer pharmId = (int)row[THREE];
				String batch = (String)row[FOUR];
				if (quantity != 0) {
					summary.setQuantity(quantity);
					summary.setPharmStockOnHandId(pharmId);
					summary.setItemBatch(batch);
				} else {
					continue;
				}
			}

			results.add(summary);
		}

		// We done.
		return results;
	}

	public void deductionImplementation(Department department) {

		Date startDate = null; //minimum distribution date
		Date endDate = new Date(); //current date time

		//get minimum distribution date query - start date
		String minDateDistributed = "select min(a.dateCreated) as startDate "
		        + "from ViewInvStockonhandPharmacyDispensary as a "
		        + "where a.department.id = " + department.getId()
		        + " and a.updatableQuantity != " + 0;
		Query minDateDistributedQuery = getRepository().createQuery(minDateDistributed);
		//minDateDistributedQuery.getQueryString();
		List<Timestamp> minDateDistributedList = (List<Timestamp>)minDateDistributedQuery.list();
		if (minDateDistributedList.size() >= 1) {
			System.out.println(">= 1 ");
			for (Timestamp obj : minDateDistributedList) {
				Timestamp row = obj;
				startDate = new Date(row.getTime());
			}
		} else {
			System.out.println("<1 ");
			startDate = endDate;
		}

		System.out.println("Start Date: " + startDate);
		System.out.println("End Date: " + endDate);

		//get all pharmacy encounter grouped within the period

		List<NewPharmacyConsumptionSummary> finalConsumptionSummarys =
		        getDrugDispenseSummary(startDate, endDate);

		System.out.println("finalConsumptionSummarys: " + finalConsumptionSummarys.size());

		//loop through each of the sum consumption
		for (NewPharmacyConsumptionSummary n : finalConsumptionSummarys) {
			getDistributionByConcept(n, department);
		}

	}

	public List<NewPharmacyConsumptionSummary> getDrugDispenseSummary(Date startDate, Date endDate) {
			
			System.out.println("Start Date: " + startDate);
			System.out.println("End Date: " + endDate);
			
	        String queryString = "select a from " + Encounter.class.getName() + " "
	                + "a where (a.dateCreated >= :startDate or a.dateChanged >= :startDate)"
	                + " and (a.dateCreated <= :endDate or a.dateChanged <= :endDate)"
	                + " and a.encounterType = 13 and a.voided = 0 ";

	        Query query = getRepository().createQuery(queryString);
	        //   Query query = getRepository().createQuery(queryString);
	        query.setDate("startDate", startDate);
	        query.setDate("endDate", endDate);
	        List<Encounter> encounters = (List<Encounter>) query.list();
	        System.out.println("found arv encounters");
	        System.out.println(encounters.size());

	        //   query = this.createPagingQuery(pagingInfo, query);
	        List<NewPharmacyConsumptionSummary> arvsuConsumptionSummarys = new ArrayList<>();
	        List<Obs> obsPerVisit = null;

	        for (Encounter enc : encounters) {
	            Obs obs = null;

	            String uuid = UUID.randomUUID().toString();
	            try {
	                obsPerVisit = new ArrayList<Obs>(enc.getAllObs());
	                Date visitDate = DateUtils.truncate(enc.getEncounterDatetime(), Calendar.DATE);

	                Set<ARVDispensedItem> aRVDispensedItems = null;

	                aRVDispensedItems = createARVDispenseItems(enc.getPatient(),
	                        visitDate, obsPerVisit, uuid);
	                System.out.println("arv count " + aRVDispensedItems.size());

	                List<NewPharmacyConsumptionSummary> collect = aRVDispensedItems.stream()
	                        .map(ItemStockDetailDataServiceImpl::mapARVDispensedItem)
	                        .collect(Collectors.toList());

	                if (!collect.isEmpty()) {
	                    arvsuConsumptionSummarys.addAll(collect);
	                }

	                aRVDispensedItems = retrieveOIMedicationItems(obsPerVisit, uuid);
	                List<NewPharmacyConsumptionSummary> collect1 = aRVDispensedItems.stream()
	                        .map(ItemStockDetailDataServiceImpl::mapARVDispensedItem)
	                        .collect(Collectors.toList());

	                if (!collect1.isEmpty()) {
	                    arvsuConsumptionSummarys.addAll(collect1);
	                }

	                aRVDispensedItems = retrieveTBMedicationItems(obsPerVisit, uuid);
	                List<NewPharmacyConsumptionSummary> collect2 = aRVDispensedItems.stream()
	                        .map(ItemStockDetailDataServiceImpl::mapARVDispensedItem)
	                        .collect(Collectors.toList());

	                if (!collect2.isEmpty()) {
	                    arvsuConsumptionSummarys.addAll(collect2);
	                }

	            } catch (DatatypeConfigurationException ex) {
	                Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
	            }

	        }

	        arvsuConsumptionSummarys = sumAndGroupConsumption(arvsuConsumptionSummarys);

	      
	        return arvsuConsumptionSummarys;
	    }

	public Set<ARVDispensedItem> createARVDispenseItems(Patient patient, Date visitDate, List<Obs> obsListForAVisit,
	        String uuid)
	        throws DatatypeConfigurationException {

		String visitID = "";
		Date stopDate = null;
		DateTime stopDateTime = null, startDateTime = null;
		Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();

		//	PatientIdentifier pepfarIdentifier = patient.getPatientIdentifier(Utils.PEPFAR_IDENTIFIER_INDEX);
		String pepfarID = "";
		String ndrCode = "";
		Obs obs = null;
		int valueCoded = 0, durationInDays = 0;

		if (!obsListForAVisit.isEmpty() && Utils.contains(obsListForAVisit, Utils.CURRENT_REGIMEN_LINE_CONCEPT)) {
			//	pepfarID = pepfarIdentifier.getIdentifier();

			visitID = Utils.getVisitId(pepfarID, visitDate);

			obs = Utils.extractObs(Utils.CURRENT_REGIMEN_LINE_CONCEPT, obsListForAVisit); //PrescribedRegimenLineCode
			if (obs != null && obs.getValueCoded() != null) {
				valueCoded = obs.getValueCoded().getConceptId();

				//  ndrCode = getRegimenMapValue(valueCoded);
				//  regimenType.setPrescribedRegimenLineCode(ndrCode);
				//   regimenType.setPrescribedRegimenTypeCode(Utils.ART_CODE);
				obs = Utils.extractObs(valueCoded, obsListForAVisit); // PrescribedRegimen

				if (obs != null) {
					//    valueCoded = obs.getValueCoded().getConceptId();
					//   ndrCode = getRegimenMapValue(valueCoded);  
					//     codedSimpleType.setCodeDescTxt(obs.getValueCoded().getName().getName());
					aRVDispensedItems = retrieveARVMedicationItems(visitDate, obsListForAVisit, uuid);

				}

			}

		}
		return aRVDispensedItems;
	}

	private Set<ARVDispensedItem> retrieveARVMedicationItems(Date visitDate, List<Obs> obsList, String uuid) {
		DateTime stopDateTime = null;
		DateTime startDateTime = null;
		int durationDays = 0;
		Obs obs = null;
		List<Obs> targetObsList = new ArrayList<Obs>();
		Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();
		String drugCategory = null;
		String serviceDeliveryModel = null;
		String deliveryType = null;

		obs = Utils.extractObs(Utils.SERVICE_DELIVERY_MODEL, obsList);
		if (obs != null && obs.getValueCoded() != null) {
			serviceDeliveryModel = obs.getValueCoded().getName().getName();
			System.out.println("service delivery " + obs.getValueCoded().getName().getName());

			if (obs.getValueCoded().getConceptId() == Utils.FACILITY_DISPENSING) {
				obs = Utils.extractObs(Utils.FACILITY_DISPENSING, obsList);
				if (obs != null && obs.getValueCoded() != null) {
					deliveryType = obs.getValueCoded().getName().getName();
					System.out.println("facility dispensing " + obs.getValueCoded().getName().getName());
				}

			} else if (obs.getValueCoded().getConceptId() == Utils.DECENTRALIZED_DRUG_DELIVERY) {
				obs = Utils.extractObs(Utils.DECENTRALIZED_DRUG_DELIVERY, obsList);
				if (obs != null && obs.getValueCoded() != null) {
					deliveryType = obs.getValueCoded().getName().getName();
					System.out.println("ddd " + obs.getValueCoded().getName().getName());
				}

			}
		}

		obs = Utils.extractObs(Utils.TREATMENT_CATEGORY_CONCEPT, obsList);
		if (obs != null && obs.getValueCoded() != null) {
			if (null == obs.getValueCoded().getConceptId()) {
				//rare scenario
				drugCategory = Utils.ADULT_ART_TEXT;
			} else {
				switch (obs.getValueCoded().getConceptId()) {
					case Utils.ADULT_TREATMENT_CATEGORY_CONCEPT:
						drugCategory = Utils.ADULT_ART_TEXT;
						break;
					case Utils.PEDIATRIC_TREATMENT_CATEGORY_CONCEPT:
						drugCategory = Utils.PEDIATRIC_ART_TEXT;
						break;
					default:
						//rare scenario
						drugCategory = Utils.ADULT_ART_TEXT;
						break;
				}
			}
		}

		Set<Obs> obsGroup = Utils.extractObsList(Utils.ARV_DRUGS_GROUPING_CONCEPT_SET, obsList);
		if (!obsGroup.isEmpty()) {

			for (Obs b : obsGroup) {
				ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
				aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

				List<Obs> filteredObs = b.getGroupMembers().stream().collect(Collectors.toList());

				obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
				if (obs != null) {
					durationDays = (int)obs.getValueNumeric().doubleValue();
					aRVDispensedItem.setDuration(durationDays);

				}

				obs = Utils.extractObs(Utils.ARV_DRUG, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
					aRVDispensedItem.setItemConceptId(obs.getValueCoded().getConceptId());
					System.out.println("Concept ID ARV Retrieve : " + obs.getValueCoded().getConceptId());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityPrescribed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityDispensed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_DRUG_STRENGHT, obsList);
				if (obs != null) {
					aRVDispensedItem.setDrugStrength(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.ARV_DRUG_SINGLE_DOSE, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setSingleDose((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_DRUG_FREQUENCY, obsList);
				if (obs != null) {
					aRVDispensedItem.setFrequency(obs.getValueCoded().getName().getName());
				}

				aRVDispensedItem.setDrugCategory(drugCategory);
				aRVDispensedItem.setDeliveryType(deliveryType);
				aRVDispensedItem.setServiceDeliveryModel(serviceDeliveryModel);

				aRVDispensedItems.add(aRVDispensedItem);

			}

		}

		return aRVDispensedItems;

	}

	private Set<ARVDispensedItem> retrieveOIMedicationItems(List<Obs> obsList, String uuid) {
		int durationDays = 0;
		Obs obs = null;
		List<Obs> targetObsList = new ArrayList<Obs>();
		Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();

		Set<Obs> obsGroup = Utils.extractObsList(Utils.OI_DRUGS_GROUPING_CONCEPT_SET, obsList);

		if (!obsGroup.isEmpty()) {

			for (Obs b : obsGroup) {

				ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
				aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

				List<Obs> filteredObs = b.getGroupMembers().stream().collect(Collectors.toList());

				obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
				if (obs != null) {
					durationDays = (int)obs.getValueNumeric().doubleValue();
					aRVDispensedItem.setDuration(durationDays);

				}

				obs = Utils.extractObs(Utils.OI_DRUG, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
					aRVDispensedItem.setItemConceptId(obs.getValueCoded().getConceptId());
					System.out.println("Concept ID OI Retrieve : " + obs.getValueCoded().getConceptId());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityPrescribed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityDispensed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_DRUG_STRENGHT, obsList);
				if (obs != null) {
					aRVDispensedItem.setDrugStrength(obs.getValueCoded().getName().getName());
				}

				aRVDispensedItem.setDrugCategory("OI Prophylaxis/Treatment");
				aRVDispensedItems.add(aRVDispensedItem);
			}

		}

		return aRVDispensedItems;

	}

	private Set<ARVDispensedItem> retrieveTBMedicationItems(List<Obs> obsList, String uuid) {
		int durationDays = 0;
		Obs obs = null;
		List<Obs> targetObsList = new ArrayList<Obs>();
		Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();

		Set<Obs> obsGroup = Utils.extractObsList(Utils.TB_DRUGS_GROUPING_CONCEPT_SET, obsList);
		if (obsGroup != null) {

			for (Obs b : obsGroup) {

				ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
				aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

				List<Obs> filteredObs = b.getGroupMembers().stream().collect(Collectors.toList());

				obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
				if (obs != null) {
					durationDays = (int)obs.getValueNumeric().doubleValue();
					aRVDispensedItem.setDuration(durationDays);

				}

				obs = Utils.extractObs(Utils.TB_DRUG, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
					aRVDispensedItem.setItemConceptId(obs.getValueCoded().getConceptId());
					System.out.println("Concept ID TB DRUG Retrieve : " + obs.getValueCoded().getConceptId());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityPrescribed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
				if (obs != null) {
					aRVDispensedItem.setQuantityDispensed((int)obs.getValueNumeric().doubleValue());
				}

				obs = Utils.extractObs(Utils.ARV_DRUG_STRENGHT, obsList);
				if (obs != null) {
					aRVDispensedItem.setDrugStrength(obs.getValueCoded().getName().getName());
				}

				aRVDispensedItem.setDrugCategory("Anti-TB Drugs");
				aRVDispensedItems.add(aRVDispensedItem);
			}

		}

		return aRVDispensedItems;

	}

	private static NewPharmacyConsumptionSummary mapARVDispensedItem(ARVDispensedItem arvItem) {
		NewPharmacyConsumptionSummary summary = null;
		if (arvItem.getDrugCategory() != null && arvItem.getItemName() != null) {
			summary = new NewPharmacyConsumptionSummary();
			summary.setGroupUuid(arvItem.getArvPharmacyDispenseUuid());
			summary.setItem(arvItem.getItemName());
			summary.setTotalQuantityReceived(arvItem.getQuantityDispensed());
			summary.setDrugCategory(arvItem.getDrugCategory());
			summary.setUuid(UUID.randomUUID().toString());
			summary.setDeliveryType(arvItem.getDeliveryType());
			summary.setServiceDeliveryModel(arvItem.getServiceDeliveryModel());
			summary.setItemConceptId(arvItem.getItemConceptId());

			System.out.println("Concept ID summary map : " + arvItem.getItemConceptId());
		}

		return summary;
	}

	private List<NewPharmacyConsumptionSummary>
		    sumAndGroupConsumption(List<NewPharmacyConsumptionSummary> arvsuConsumptionSummarys) {
		
				 List<NewPharmacyConsumptionSummary> summarys = new ArrayList<>();
				 List<NewPharmacyConsumptionSummary> newSummarys = new ArrayList<>();
				 
				 arvsuConsumptionSummarys.stream().filter(Objects::nonNull)
				         .collect(Collectors.groupingBy(NewPharmacyConsumptionSummary::getDrugCategory,
				                 Collectors.groupingBy(NewPharmacyConsumptionSummary::getItem,
				                         Collectors.summingInt(NewPharmacyConsumptionSummary::getTotalQuantityReceived 
				                         		))
				         )).entrySet()
				         .stream()
				         .map((a) -> {
				             return mapPharmacyConsumptionSummarys(a);
				         }).forEachOrdered((consumptions) -> {
				     summarys.addAll(consumptions);
				 });
				 
				 int conceptIdd = 0;
				 for(NewPharmacyConsumptionSummary s : summarys) {	       	
				    	 for(NewPharmacyConsumptionSummary nc : arvsuConsumptionSummarys) {
				    		 if(s.getItem().equalsIgnoreCase(nc.getItem())) {
				    			 conceptIdd = nc.getItemConceptId();
				    			 break;
				    		 }
				    	 }
				    	 s.setItemConceptId(conceptIdd);	       
				    	 newSummarys.add(s);
				 }
				
				 return newSummarys;

	}

	private static List<NewPharmacyConsumptionSummary>
	    mapPharmacyConsumptionSummarys(Map.Entry<String, Map<String, Integer>> entry) {
	
		 return entry.getValue().entrySet().stream().map((a) -> {
		 	
		     NewPharmacyConsumptionSummary summary = new NewPharmacyConsumptionSummary();
		     
		     summary.setDrugCategory(entry.getKey());
		     
		     summary.setItem(a.getKey());
		     
		     summary.setTotalQuantityReceived(a.getValue());
		     
		     summary.setUuid(UUID.randomUUID().toString());
		     
		     summary.setItemConceptId(a.getValue());
		     
		     return summary;
		     
		 }).collect(Collectors.toList());
	
	}

	@Transactional(readOnly = false)
	public void getDistributionByConcept(NewPharmacyConsumptionSummary newPharmacyConsumptionSummary,
	        Department department) {

		//get items in the department from inv_stockonhand_pharmacy_dispensary
		String allItemDistributedToThisDepartment =
		        "select distinct a.id, a.item.id, a.conceptId, a.dateCreated, "
		                + "a.expiration, a.quantity, a.updatableQuantity "
		                + "from ViewInvStockonhandPharmacyDispensary as a "
		                + "where a.department.id = " + department.getId() + " "
		                //+ "and a.updatableQuantity != " + 0 + " "
		                + "and a.conceptId = " + newPharmacyConsumptionSummary.getItemConceptId() + " "
		                + "order by a.expiration asc";

		Query query = getRepository().createQuery(allItemDistributedToThisDepartment);
		List list = query.list();
		int stockBalance = 0;
		int totalQuantityConsumed = newPharmacyConsumptionSummary.getTotalQuantityReceived();

		System.out.println("Item Concept: " + newPharmacyConsumptionSummary.getItemConceptId());
		int me = 0;
		for (Object obj : list) {
			Object[] row = (Object[])obj;

			int id = (int)row[0];
			int qtty = (int)row[FIVE];

			if (totalQuantityConsumed <= qtty) {
				System.out.println("Me: ");

				//subtract and update
				stockBalance = qtty - totalQuantityConsumed;

				System.out.println("totalQuantityConsumed: " + totalQuantityConsumed);
				System.out.println("qtty: " + qtty);
				System.out.println("stockBalance: " + stockBalance);

				updateStockBalanceAtDispensary(stockBalance, id);
				break;
			} else {
				System.out.println("You: ");

				stockBalance = totalQuantityConsumed - qtty;
				totalQuantityConsumed = stockBalance;

				System.out.println("totalQuantityConsumed: " + totalQuantityConsumed);
				System.out.println("qtty: " + qtty);
				System.out.println("stockBalance: " + stockBalance);

				updateStockBalanceAtDispensary(0, id);
			}

		}

	}

	@Override
	@Transactional(readOnly = false)
	public void updateStockBalanceAtDispensary(int stockBalance, int id) {

		String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
		        + "updatableQuantity = " + stockBalance + " "
		        + "where id = " + id;

		Query query = getRepository().createQuery(hql);
		int sql = query.executeUpdate();
		System.out.println("Updated Executed: " + sql);
	}

	@Override
	public void updatePharmacyAtDispensary(List<ViewInvStockonhandPharmacyDispensary> viewInvStockonhandPharmacyDispensary) {

		for (ViewInvStockonhandPharmacyDispensary obj : viewInvStockonhandPharmacyDispensary) {

			String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
			        + "updatableQuantity = " + obj.getUpdatableQuantity() + " "
			        + "where id = " + obj.getId();

			Query query = getRepository().createQuery(hql);
			int sql = query.executeUpdate();
			System.out.println("Updated Executed: " + sql);
		}
	}

	@Override
	public void addNewDistributionDataPharmacyAtDispensary(StockOperation operation) {

		//check that operation is not null
		if (operation == null) {
			throw new IllegalArgumentException("The operation must be defined.");
		}

		//get list of all stock operation items
		Iterator<StockOperationItem> operationItems = operation.getItems().iterator();

		while (operationItems.hasNext()) {

			StockOperationItem opItem = operationItems.next();

			ViewInvStockonhandPharmacyDispensary vs = new ViewInvStockonhandPharmacyDispensary();
			vs.setItem(opItem.getItem());
			vs.setExpiration(opItem.getExpiration());
			vs.setQuantity(opItem.getQuantity());
			vs.setItemBatch(opItem.getItemBatch());
			vs.setStockOperationId(operation.getId());
			vs.setOperationTypeId(operation.getInstanceType().getId());
			vs.setItemDrugType(opItem.getItemDrugType());
			vs.setCommodityType(operation.getCommodityType());
			vs.setUpdatableQuantity(opItem.getQuantity());
			vs.setDepartment(operation.getDepartment());
			vs.setDateCreated(operation.getDateCreated());
			if (operation.getCommodityType().equalsIgnoreCase(Utils.PHARMACY_COMMODITY_TYPE)) {
				vs.setConceptId(opItem.getItem().getConcept().getConceptId());
			} else {
				vs.setConceptId(0);
			}
			if (operation.getCommodityType().equalsIgnoreCase(Utils.PHARMACY_COMMODITY_TYPE)) {
				vs.setStrengthConceptId(opItem.getItem().getStrenghtConcept().getConceptId());
			} else {
				vs.setStrengthConceptId(0);
			}

			int updateAbleQuantity = 0;
			int finalQuantity = 0;
			int dispensaryId = 0;

			//check if there is existing record with same itemID, itemBatch, commodityType, expiration, departmentID
			String hql = "select detail.updatableQuantity as sumQty, detail.id, detail.quantity "
			        + "from ViewInvStockonhandPharmacyDispensary as detail "
			        + "where detail.department.id = " + vs.getDepartment().getId() + " and "
			        + "detail.itemBatch = '" + vs.getItemBatch() + "' and "
			        + "detail.commodityType = '" + vs.getCommodityType() + "' and "
			        + "detail.expiration = '" + vs.getExpiration() + "' and "
			        + "detail.item.id = " + vs.getItem().getId();

			Query query = getRepository().createQuery(hql);
			List list = query.list();

			// Parse the aggregate query into an ViewInvStockonhandPharmacyDispensary object
			List<ViewInvStockonhandPharmacyDispensary> results =
			        new ArrayList<ViewInvStockonhandPharmacyDispensary>(list.size());
			for (Object obj : list) {
				Object[] row = (Object[])obj;

				ViewInvStockonhandPharmacyDispensary summary = new ViewInvStockonhandPharmacyDispensary();
				summary.setUpdatableQuantity((Integer)row[0]);
				summary.setId((Integer)row[1]);
				summary.setQuantity((Integer)row[2]);

				results.add(summary);
			}

			if (results.size() == 1) {
				for (ViewInvStockonhandPharmacyDispensary viewInvStockonhandPharmacyDispensary : results) {
					updateAbleQuantity += viewInvStockonhandPharmacyDispensary.getUpdatableQuantity();
					finalQuantity += viewInvStockonhandPharmacyDispensary.getQuantity();
					dispensaryId = viewInvStockonhandPharmacyDispensary.getId();
				}
				updateAbleQuantity += vs.getUpdatableQuantity();
				finalQuantity += vs.getQuantity();
				String hql2 = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
				        + "updatableQuantity = " + updateAbleQuantity + ", "
				        + "quantity = " + finalQuantity + " "
				        + "where id = " + dispensaryId;

				Query query2 = getRepository().createQuery(hql2);
				query2.executeUpdate();

			} else if (results.size() > 1) {
				int id[] = new int[results.size()];
				int i = 0;
				for (ViewInvStockonhandPharmacyDispensary viewInvStockonhandPharmacyDispensary : results) {
					updateAbleQuantity += viewInvStockonhandPharmacyDispensary.getUpdatableQuantity();
					finalQuantity += viewInvStockonhandPharmacyDispensary.getQuantity();
					id[i] = viewInvStockonhandPharmacyDispensary.getId();
					i++;
				}
				int counter = 1;
				int index = 0;
				while (counter < results.size()) {
					dispensaryId = id[index];
					String hql4 = "DELETE FROM ViewInvStockonhandPharmacyDispensary as v "
					        + "where id = " + dispensaryId;

					Query query4 = getRepository().createQuery(hql4);
					query4.executeUpdate();
					counter++;
					index++;
				}
				updateAbleQuantity += vs.getUpdatableQuantity();
				finalQuantity += vs.getQuantity();
				dispensaryId = id[index];
				String hql3 = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
				        + "updatableQuantity = " + updateAbleQuantity + ", "
				        + "quantity = " + finalQuantity + " "
				        + "where id = " + dispensaryId;

				Query query3 = getRepository().createQuery(hql3);
				query3.executeUpdate();

			} else {
				//insert record to inv_stockonhand_pharmacy_dispensary		
				getRepository().save(vs);
			}

		}

	}

	@Override
	public List<CrrfDetails> getItemStockSummaryByPharmacy(Date startDate, Date endDate, PagingInfo pagingInfo) {
		System.out.println("Stock CRRF Tobe");
		return null;
	}

	@Override
	public void insertInvClosingBalanceUpdate(List<ClosingBalanceUpdateModel> closingBalanceUpdateModels) {
		for (ClosingBalanceUpdateModel obj : closingBalanceUpdateModels) {

			//			String hql = "UPDATE ViewInvStockonhandPharmacyDispensary as v set "
			//					+ "updatableQuantity = " + obj.getUpdatableQuantity() + " "
			//					+ "where id = " + obj.getId();
			//
			//			Query query = getRepository().createQuery(hql);
			//			int sql = query.executeUpdate();
			//			System.out.println("Updated Executed: " + sql);
		}
	}

}
