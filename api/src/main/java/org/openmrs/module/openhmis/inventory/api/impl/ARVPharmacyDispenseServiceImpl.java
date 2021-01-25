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

				Set<ARVDispensedItem> aRVDispensedItems = createRegimenType(enc.getPatient(), visitDate, obsPerVisit, uuid);
				arvDispense.setItems(aRVDispensedItems);

				aRVPharmacyDispenses.add(arvDispense);
			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		return aRVPharmacyDispenses;

	}

	public Set<ARVDispensedItem> createRegimenType(Patient patient, Date visitDate, List<Obs> obsListForAVisit, String uuid)
	        throws DatatypeConfigurationException {

		String visitID = "";
		Date stopDate = null;
		DateTime stopDateTime = null, startDateTime = null;

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
					Set<ARVDispensedItem> aRVDispensedItems = retrieveMedicationDuration(visitDate, obsListForAVisit, uuid);

					return aRVDispensedItems;
				}

			}

		}
		return null;
	}

	public Set<ARVDispensedItem> retrieveMedicationDuration(Date visitDate, List<Obs> obsList, String uuid) {
        DateTime stopDateTime = null;
        DateTime startDateTime = null;
        int durationDays = 0;
        Obs obs = null;
        List<Obs> targetObsList = new ArrayList<Obs>();
        Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();

        Obs obsGroup = Utils.extractObs(Utils.ARV_DRUGS_GROUPING_CONCEPT_SET, obsList);
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

                aRVDispensedItems.add(aRVDispensedItem);
            }

        }

        return aRVDispensedItems;

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
