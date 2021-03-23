/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.OpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.openhmis.commons.api.PagingInfo;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IMetadataAuthorizationPrivileges;
import org.openmrs.module.openhmis.commons.api.entity.security.IObjectAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.model.ARVDispensedItem;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.util.Utils;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispenseService;
import org.openmrs.module.openhmis.inventory.api.model.ConsumptionSummary;
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
			ARVPharmacyDispense arvDispense = new ARVPharmacyDispense();
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
    public List<NewPharmacyConsumptionSummary> getDrugDispenseSummary(Date startDate, Date endDate, PagingInfo pagingInfo) {
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

        arvsuConsumptionSummarys = sumAndGroupConsumption(arvsuConsumptionSummarys);

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

        arvsuConsumptionSummarys.stream().filter(Objects::nonNull)
                .collect(Collectors.groupingBy(NewPharmacyConsumptionSummary::getDrugCategory,
                        Collectors.groupingBy(NewPharmacyConsumptionSummary::getItem,
                                Collectors.summingInt(NewPharmacyConsumptionSummary::getTotalQuantityReceived))
                )).entrySet()
                .stream()
                .map((a) -> {
                    return mapPharmacyConsumptionSummarys(a);
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

        obs = Utils.extractObs(Utils.TREATMENT_CATEGORY_CONCEPT, obsList);
        if (obs != null && obs.getValueCoded() != null) {
            if (null == obs.getValueCoded().getConceptId()) {
                //rare scenario
                drugCategory = "Adult ART";
            } else {
                switch (obs.getValueCoded().getConceptId()) {
                    case Utils.ADULT_TREATMENT_CATEGORY_CONCEPT:
                        drugCategory = "Adult ART";
                        break;
                    case Utils.PEDIATRIC_TREATMENT_CATEGORY_CONCEPT:
                        drugCategory = "Pediatric ART";
                        break;
                    default:
                        //rare scenario
                        drugCategory = "Adult ART";
                        break;
                }
            }
        }

        Obs obsGroup = Utils.extractObs(Utils.ARV_DRUGS_GROUPING_CONCEPT_SET, obsList);
        if (obsGroup != null) {
            ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
            aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

            targetObsList.addAll(obsGroup.getGroupMembers());
            Set<Encounter> distinctObsGroupEncounter = targetObsList.stream()
                    .map(Obs::getEncounter)
                    .collect(Collectors.toSet());

            
//        for(Obs b: targetObsList){
//            List<Obs> filteredObs = b.getEncounter()
//                    .getAllObs().stream()
//                    .filter(a -> Objects.equals(a.getValueGroupId(), b.getId()))
//                    .collect(Collectors.toList());
//            
//            System.out.println("after filtering found "+filteredObs.size()+" obs");
//                   
//              obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
//                if (obs != null) {
//                    durationDays = (int) obs.getValueNumeric().doubleValue();
//                    aRVDispensedItem.setDuration(durationDays);
//
//                }
//
//                obs = Utils.extractObs(Utils.ARV_DRUG, filteredObs);
//                if (obs != null) {
//                    aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
//                }
//
//                obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
//                if (obs != null) {
//                    aRVDispensedItem.setQuantityPrescribed((int) obs.getValueNumeric().doubleValue());
//                }
//
//                obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
//                if (obs != null) {
//                    aRVDispensedItem.setQuantityDispensed((int) obs.getValueNumeric().doubleValue());
//                }
//
//                obs = Utils.extractObs(Utils.ARV_DRUG_STRENGHT, obsList);
//                if (obs != null) {
//                    aRVDispensedItem.setDrugStrength(obs.getValueCoded().getName().getName());
//                }
//                aRVDispensedItem.setDrugCategory(drugCategory);
//
//                aRVDispensedItems.add(aRVDispensedItem);
//
//        }    
            
            
            
            for (Encounter e : distinctObsGroupEncounter) {
                List<Obs> filteredObs = targetObsList.stream()
                        .filter(a -> a.getEncounter().equals(e))
                        .collect(Collectors.toList());

                obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
                if (obs != null) {
                    durationDays = (int) obs.getValueNumeric().doubleValue();
                    aRVDispensedItem.setDuration(durationDays);

                }

                obs = Utils.extractObs(Utils.ARV_DRUG, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityPrescribed((int) obs.getValueNumeric().doubleValue());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityDispensed((int) obs.getValueNumeric().doubleValue());
                }

                obs = Utils.extractObs(Utils.ARV_DRUG_STRENGHT, obsList);
                if (obs != null) {
                    aRVDispensedItem.setDrugStrength(obs.getValueCoded().getName().getName());
                }
                aRVDispensedItem.setDrugCategory(drugCategory);

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

        Obs obsGroup = Utils.extractObs(Utils.OI_DRUGS_GROUPING_CONCEPT_SET, obsList);
        if (obsGroup != null) {
            ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
            aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

            targetObsList.addAll(obsGroup.getGroupMembers());
            Set<Encounter> distinctObsGroupEncounter = targetObsList.stream()
                    .map(Obs::getEncounter)
                    .collect(Collectors.toSet());

            for (Encounter e : distinctObsGroupEncounter) {
                List<Obs> filteredObs = targetObsList.stream()
                        .filter(a -> a.getEncounter().equals(e))
                        .collect(Collectors.toList());

                obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
                if (obs != null) {
                    durationDays = (int) obs.getValueNumeric().doubleValue();
                    aRVDispensedItem.setDuration(durationDays);

                }

                obs = Utils.extractObs(Utils.OI_DRUG, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityPrescribed((int) obs.getValueNumeric().doubleValue());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityDispensed((int) obs.getValueNumeric().doubleValue());
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

        Obs obsGroup = Utils.extractObs(Utils.TB_DRUGS_GROUPING_CONCEPT_SET, obsList);
        if (obsGroup != null) {
            ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
            aRVDispensedItem.setArvPharmacyDispenseUuid(uuid);

            targetObsList.addAll(obsGroup.getGroupMembers());
            Set<Encounter> distinctObsGroupEncounter = targetObsList.stream()
                    .map(Obs::getEncounter)
                    .collect(Collectors.toSet());

            for (Encounter e : distinctObsGroupEncounter) {
                List<Obs> filteredObs = targetObsList.stream()
                        .filter(a -> a.getEncounter().equals(e))
                        .collect(Collectors.toList());

                obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, filteredObs);
                if (obs != null) {
                    durationDays = (int) obs.getValueNumeric().doubleValue();
                    aRVDispensedItem.setDuration(durationDays);

                }

                obs = Utils.extractObs(Utils.TB_DRUG, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setItemName(obs.getValueCoded().getName().getName());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_PRESCRIBED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityPrescribed((int) obs.getValueNumeric().doubleValue());
                }

                obs = Utils.extractObs(Utils.ARV_QTY_DISPENSED, filteredObs);
                if (obs != null) {
                    aRVDispensedItem.setQuantityDispensed((int) obs.getValueNumeric().doubleValue());
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
		}

		return summary;
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

}
