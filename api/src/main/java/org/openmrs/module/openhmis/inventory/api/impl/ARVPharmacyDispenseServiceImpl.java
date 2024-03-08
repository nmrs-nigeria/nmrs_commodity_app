/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.model.ARVDispensedItem;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.model.NewPharmacyConsumptionSummary;
import org.openmrs.module.openhmis.inventory.api.util.PrivilegeConstants;

/**
 * @author MORRISON.I
 */
public class ARVPharmacyDispenseServiceImpl extends BaseMetadataDataServiceImpl<ARVPharmacyDispense>
        implements IARVPharmacyDispenseService, IMetadataAuthorizationPrivileges {

	@Override
	public List<ARVPharmacyDispense> getARVs(Date startDate, Date endDate, PagingInfo pagingInfo) {
		String queryString = "select  a from " + Encounter.class.getName() + " "
		        + "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
		        + "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) and a.encounterType = 13 and a.voided = 0 ";

		Query query = getRepository().createQuery(queryString);
		//   Query query = getRepository().createQuery(queryString);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		List<Encounter> encounters = (List<Encounter>)query.list();
		System.out.println("found arv encounters");
		System.out.println(encounters.size());

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			Integer count = query.list().size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		//      query = this.createPagingQuery(pagingInfo, query);
		List<ARVPharmacyDispense> aRVPharmacyDispenses = new ArrayList<>();
		List<Obs> obsPerVisit = null;

		for (Encounter enc : encounters) {
			Obs obs = null;
			Obs obsMap = null;
			int valueCoded = 0;
			String regimenCode = "";
			ARVPharmacyDispense arvDispense = new ARVPharmacyDispense();
			String uuid = UUID.randomUUID().toString();
			try {
				obsPerVisit = new ArrayList<Obs>(enc.getAllObs());
				Date visitDate = DateUtils.truncate(enc.getEncounterDatetime(), Calendar.DATE);

				Map<Object, List<Obs>> map = Utils.groupedByConceptIdsOnly(obsPerVisit);
				obsMap = Utils.extractObsMap(Utils.CURRENT_REGIMEN_LINE_CONCEPT, map);
				if (obsMap != null && obsMap.getValueCoded() != null) {

					valueCoded = obsMap.getValueCoded().getConceptId();

					Obs valueObs = Utils.extractObsMap(valueCoded, map);
					if (valueObs != null) {
						valueCoded = valueObs.getValueCoded().getConceptId();
						if (valueCoded > 0) {
							//regimenCode = getRegimenMapValueDesc(valueCoded);
							arvDispense.setCurrentRegimen(getRegimenMapValueDesc(valueCoded));
						}
					}

				}
				System.out.println("Regimen Name - PHIS3 " + arvDispense.getCurrentRegimen());

				obs = Utils.extractObs(Utils.TREATMENT_TYPE, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setTreatmentType(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.VISIT_TYPE_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setVisitType(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.PICKUP_REASON_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setPickupReason(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.SERVICE_DELIVERY_MODEL, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setPatientCategory(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.TREATMENT_CATEGORY_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setTreatmentAge(obs.getValueCoded().getName().getName());
				}

				obs = Utils.extractObs(Utils.CURRENT_REGIMEN_LINE_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setCurrentLine(obs.getValueCoded().getName().getName());
				}

				java.util.Date date = new Date(visitDate.toString().replaceAll("WAT", "GMT"));
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				String strDate = formatter.format(date);

				arvDispense.setDateOfDrugDispensed(strDate);

				arvDispense.setDateOfDispensed(visitDate);
				arvDispense.setUuid(uuid);
				arvDispense.setPatientID(Utils.getPatientPEPFARId(enc.getPatient()));
				arvDispense.setPatientDBId(enc.getPatient().getId());
				arvDispense.setEncounterId(enc.getEncounterId());

				System.out.println("Encounter ID: " + enc.getEncounterId());
				System.out.println("Patient DB ID: " + enc.getPatient().getId());

				Set<ARVDispensedItem> aRVDispensedItems = createARVDispenseItems(enc.getPatient(),
				    visitDate, obsPerVisit, uuid);
				arvDispense.setItems(aRVDispensedItems);

				aRVPharmacyDispenses.add(arvDispense);
			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		return aRVPharmacyDispenses;

	}

	@Override
	public ARVPharmacyDispense getARVsByUuid(Integer encounterId) {

		String queryString = "select  a from " + Encounter.class.getName() + " "
		        + "a where a.encounterId = " + encounterId;

		System.out.println("encounterId: " + encounterId);

		Query query = getRepository().createQuery(queryString);

		Encounter enc = (Encounter)query;

		System.out.println("found arv encounters");
		System.out.println(enc);

		ARVPharmacyDispense arvDispense = new ARVPharmacyDispense();
		List<Obs> obsPerVisit = null;
		Obs obs = null;

		String uuid = UUID.randomUUID().toString();
		try {
			obsPerVisit = new ArrayList<Obs>(enc.getAllObs());
			Date visitDate = DateUtils.truncate(enc.getEncounterDatetime(), Calendar.DATE);

			obs = Utils.extractObs(Utils.TREATMENT_TYPE, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setTreatmentType(obs.getValueCoded().getName().getName());
			}

			obs = Utils.extractObs(Utils.VISIT_TYPE_CONCEPT, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setVisitType(obs.getValueCoded().getName().getName());
			}

			obs = Utils.extractObs(Utils.PICKUP_REASON_CONCEPT, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setPickupReason(obs.getValueCoded().getName().getName());
			}

			obs = Utils.extractObs(Utils.SERVICE_DELIVERY_MODEL, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setPatientCategory(obs.getValueCoded().getName().getName());
			}

			obs = Utils.extractObs(Utils.TREATMENT_CATEGORY_CONCEPT, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setTreatmentAge(obs.getValueCoded().getName().getName());
			}

			obs = Utils.extractObs(Utils.CURRENT_REGIMEN_LINE_CONCEPT, obsPerVisit);
			if (obs != null && obs.getValueCoded() != null) {
				arvDispense.setCurrentLine(obs.getValueCoded().getName().getName());
			}

			arvDispense.setDateOfDispensed(visitDate);
			arvDispense.setUuid(uuid);
			arvDispense.setPatientID(Utils.getPatientPEPFARId(enc.getPatient()));
			arvDispense.setPatientDBId(enc.getPatient().getId());
			arvDispense.setEncounterId(enc.getEncounterId());

			System.out.println("Encounter ID: " + enc.getEncounterId());
			System.out.println("Patient DB ID: " + enc.getPatient().getId());

			Set<ARVDispensedItem> aRVDispensedItems = createARVDispenseItems(enc.getPatient(),
			    visitDate, obsPerVisit, uuid);
			arvDispense.setItems(aRVDispensedItems);

		} catch (DatatypeConfigurationException ex) {
			Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}

		return arvDispense;
	}

	@Override
	public List<NewPharmacyConsumptionSummary> getDrugDispenseSummary(Date startDate, Date endDate, PagingInfo pagingInfo) {

		System.out.println("Start Date: " + startDate);
		System.out.println("End Date: " + endDate);

		String queryString = "select a from " + Encounter.class.getName() + " "
				+ "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
				+ "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) and a.encounterType = 13 and a.voided = 0 ";

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
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect);
				}

				aRVDispensedItems = retrieveOIMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect1 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect1.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect1);
				}

				aRVDispensedItems = retrieveTBMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect2 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect2.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect2);
				}

			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		arvsuConsumptionSummarys = sumAndGroupConsumption(arvsuConsumptionSummarys);

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			Integer count = arvsuConsumptionSummarys.size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		return arvsuConsumptionSummarys;
	}

	public List<NewPharmacyConsumptionSummary>
	getDrugDispenseSummaryBeginingBalance(Date startDate, Date endDate, PagingInfo pagingInfo) {

		System.out.println("Start Date: " + startDate);
		System.out.println("End Date: " + endDate);

		String queryString = "select a from " + Encounter.class.getName() + " "
				+ "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
				+ "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) "
				+ "and a.encounterType = 13 and a.voided = 0 ";

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
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect);
				}

				aRVDispensedItems = retrieveOIMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect1 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect1.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect1);
				}

				aRVDispensedItems = retrieveTBMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect2 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect2.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect2);
				}

			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		arvsuConsumptionSummarys = sumAndGroupConsumption(arvsuConsumptionSummarys);

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			Integer count = arvsuConsumptionSummarys.size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		return arvsuConsumptionSummarys;
	}

	@Override
	public List<NewPharmacyConsumptionSummary>
	getAdultDrugDispenseSummaryByModalities(Date startDate, Date endDate,
											PagingInfo pagingInfo, String treatmentCategory) {
		String queryString = "select  a from " + Encounter.class.getName() + " "
				+ "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
				+ "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) and a.encounterType = 13 and a.voided = 0 ";

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
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect);
				}

				aRVDispensedItems = retrieveOIMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect1 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect1.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect1);
				}

				aRVDispensedItems = retrieveTBMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect2 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect2.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect2);
				}

			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		arvsuConsumptionSummarys = sumAndGroupConsumptionByModalities(arvsuConsumptionSummarys, treatmentCategory);

		if (pagingInfo != null && pagingInfo.shouldLoadRecordCount()) {
			Integer count = arvsuConsumptionSummarys.size();

			pagingInfo.setTotalRecordCount(count.longValue());
			pagingInfo.setLoadRecordCount(false);
		}

		return arvsuConsumptionSummarys;
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
		int strengthConceptIdd = 0;
		for(NewPharmacyConsumptionSummary s : summarys) {
			for(NewPharmacyConsumptionSummary nc : arvsuConsumptionSummarys) {
				if(s.getItem().equalsIgnoreCase(nc.getItem())) {
					conceptIdd = nc.getItemConceptId();
					strengthConceptIdd = nc.getStrengthConceptId();
					break;
				}
			}
			s.setItemConceptId(conceptIdd);
			s.setStrengthConceptId(strengthConceptIdd);
			newSummarys.add(s);
		}

		return newSummarys;

	}

	private List<NewPharmacyConsumptionSummary>
	sumAndGroupConsumptionByModalities(List<NewPharmacyConsumptionSummary> arvsuConsumptionSummarys,
									   String treatmentCategory) {

		List<NewPharmacyConsumptionSummary> summarys = new ArrayList<>();

		arvsuConsumptionSummarys.stream()
				.filter(Objects::nonNull)
				.filter(a -> Objects.nonNull(a.getDeliveryType()) && a.getDrugCategory().equals(treatmentCategory))
				.collect(Collectors.groupingBy(NewPharmacyConsumptionSummary::getDeliveryType,
						Collectors.groupingBy(NewPharmacyConsumptionSummary::getItem,
								Collectors.summingInt(NewPharmacyConsumptionSummary::getTotalQuantityReceived))
				)).entrySet()
				.stream()
				.map((a) -> {
					return mapModalitiesPharmacyConsumptionSummarys(a);
				}).forEachOrdered((consumptions) -> {
			summarys.addAll(consumptions);
		});
		return summarys;

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

	private static List<NewPharmacyConsumptionSummary>
	mapModalitiesPharmacyConsumptionSummarys(Map.Entry<String, Map<String, Integer>> entry) {

		return entry.getValue().entrySet().stream().map((a) -> {
			NewPharmacyConsumptionSummary summary = new NewPharmacyConsumptionSummary();
			summary.setDeliveryType(entry.getKey());
			summary.setItem(a.getKey());
			summary.setTotalQuantityReceived(a.getValue());
			summary.setUuid(UUID.randomUUID().toString());
			summary.setDrugCategory(Utils.ADULT_ART_TEXT);
			return summary;
		}).collect(Collectors.toList());

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

			//            obsGroup.stream()
			//                .map((a) -> {
			//                    return a.getGroupMembers();
			//                }).forEachOrdered((groupMembers) -> {
			//            targetObsList.addAll(groupMembers);
			//        });
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
					//set concept strength
					aRVDispensedItem.setStrengthConceptId(obs.getValueCoded().getConceptId());
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

				aRVDispensedItem.setDuration(0);
				aRVDispensedItem.setItemName("OI Medication");
				aRVDispensedItem.setItemConceptId(165257);
				aRVDispensedItem.setQuantityPrescribed(0);
				aRVDispensedItem.setQuantityDispensed(0);

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
					//set concept strength
					aRVDispensedItem.setStrengthConceptId(obs.getValueCoded().getConceptId());
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
					//set concept strength
					aRVDispensedItem.setStrengthConceptId(obs.getValueCoded().getConceptId());
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
			summary.setItem(arvItem.getItemName() + " (" + arvItem.getDrugStrength() + ")");
			summary.setTotalQuantityReceived(arvItem.getQuantityDispensed());
			summary.setDrugCategory(arvItem.getDrugCategory());
			summary.setUuid(UUID.randomUUID().toString());
			summary.setDeliveryType(arvItem.getDeliveryType());
			summary.setServiceDeliveryModel(arvItem.getServiceDeliveryModel());
			summary.setItemConceptId(arvItem.getItemConceptId());
			summary.setStrengthConceptId(arvItem.getStrengthConceptId());

			System.out.println("Concept ID summary map : " + arvItem.getItemConceptId());
			System.out.println("Strength Concept ID summary map : " + arvItem.getStrengthConceptId());
			System.out.println("Drug Strength summary map : " + arvItem.getDrugStrength());
		}

		return summary;
	}

	@Override
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
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect);
				}

				aRVDispensedItems = retrieveOIMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect1 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
						.collect(Collectors.toList());

				if (!collect1.isEmpty()) {
					arvsuConsumptionSummarys.addAll(collect1);
				}

				aRVDispensedItems = retrieveTBMedicationItems(obsPerVisit, uuid);
				List<NewPharmacyConsumptionSummary> collect2 = aRVDispensedItems.stream()
						.map(ARVPharmacyDispenseServiceImpl::mapARVDispensedItem)
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

	@Override
	protected void validate(ARVPharmacyDispense object) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	protected IMetadataAuthorizationPrivileges getPrivileges() {
		return this;
	}

	@Override
	public String getRetirePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getSavePrivilege() {
		return PrivilegeConstants.MANAGE_CONSUMPTION;
	}

	@Override
	public String getPurgePrivilege() {
		return PrivilegeConstants.PURGE_CONSUMPTION;
	}

	@Override
	public String getGetPrivilege() {
		return PrivilegeConstants.VIEW_CONSUMPTIONS;
	}

	public String getRegimenMapValueDesc(int valueCoded) {
		//old implementation return regimenMap.get(value_coded);
		if (Utils.getRegimenDescription().containsKey(valueCoded)) {
			return Utils.getRegimenDescription().get(valueCoded);
		}
		return null;
	}
}
