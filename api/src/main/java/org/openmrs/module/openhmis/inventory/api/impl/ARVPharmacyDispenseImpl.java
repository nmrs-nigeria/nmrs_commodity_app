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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.OpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.ConceptNameType;
import org.openmrs.module.openhmis.commons.api.entity.impl.BaseMetadataDataServiceImpl;
import org.openmrs.module.openhmis.commons.api.entity.security.IObjectAuthorizationPrivileges;
import org.openmrs.module.openhmis.inventory.api.IARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.model.ARVDispensedItem;
import org.openmrs.module.openhmis.inventory.api.model.ARVPharmacyDispense;
import org.openmrs.module.openhmis.inventory.api.model.Item;
import org.openmrs.module.openhmis.inventory.api.model.ItemConceptSuggestion;
import org.openmrs.module.openhmis.inventory.api.util.Utils;

/**
 * @author MORRISON.I
 */
public class ARVPharmacyDispenseImpl extends BaseMetadataDataServiceImpl implements IARVPharmacyDispense {

	@Override
	public List<ARVPharmacyDispense> getARVs(Date startDate, Date endDate) {
		String queryString = "select  a from " + Encounter.class.getName() + " "
		        + "a where (a.dateCreated >= :startDate or a.dateChanged >=:startDate)"
		        + "and (a.dateCreated <= :endDate or a.dateChanged <= :endDate) and a.encounterType = 13 ";

		Query query = getRepository().createQuery(queryString);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		List<Encounter> encounters = (List<Encounter>)query.list();
		List<ARVPharmacyDispense> aRVPharmacyDispenses = new ArrayList<>();
		List<Obs> obsPerVisit = null;

		for (Encounter enc : encounters) {
			Obs obs = null;
			ARVPharmacyDispense arvDispense = new ARVPharmacyDispense();
			try {
				obsPerVisit = new ArrayList<Obs>(enc.getAllObs());
				Date visitDate = DateUtils.truncate(enc.getEncounterDatetime(), Calendar.DATE);

				obs = Utils.extractObs(Utils.TREATMENT_TYPE, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setTreatmentType(obs.getValueCodedName().getName());
				}

				obs = Utils.extractObs(Utils.VISIT_TYPE_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setVisitType(obs.getValueCodedName().getName());
				}

				obs = Utils.extractObs(Utils.PICKUP_REASON_CONCEPT, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setPickupReason(obs.getValueCodedName().getName());
				}

				obs = Utils.extractObs(Utils.SERVICE_DELIVERY_MODEL, obsPerVisit);
				if (obs != null && obs.getValueCoded() != null) {
					arvDispense.setPatientCategory(obs.getValueCodedName().getName());
				}

				arvDispense.setDateOfDispensed(visitDate);

				ARVPharmacyDispense aRVPharmacyDispense = createRegimenType(enc.getPatient(), visitDate, obsPerVisit);
				aRVPharmacyDispenses.add(aRVPharmacyDispense);
			} catch (DatatypeConfigurationException ex) {
				Logger.getLogger(ARVPharmacyDispenseImpl.class.getName()).log(Level.SEVERE, null, ex);
			}

		}

		return aRVPharmacyDispenses;

	}

	public ARVPharmacyDispense createRegimenType(Patient patient, Date visitDate, List<Obs> obsListForAVisit)
	        throws DatatypeConfigurationException {

		String visitID = "";
		Date stopDate = null;
		DateTime stopDateTime = null, startDateTime = null;

		PatientIdentifier pepfarIdentifier = patient.getPatientIdentifier(Utils.PEPFAR_IDENTIFIER_INDEX);

		String pepfarID = "";
		String ndrCode = "";
		Obs obs = null;
		int valueCoded = 0, durationInDays = 0;

		if (!obsListForAVisit.isEmpty() && Utils.contains(obsListForAVisit, Utils.CURRENT_REGIMEN_LINE_CONCEPT)) {
			pepfarID = pepfarIdentifier.getIdentifier();

			visitID = Utils.getVisitId(pepfarID, visitDate);

			obs = Utils.extractObs(Utils.CURRENT_REGIMEN_LINE_CONCEPT, obsListForAVisit); //PrescribedRegimenLineCode
			if (obs != null && obs.getValueCoded() != null) {
				valueCoded = obs.getValueCoded().getConceptId();

				//                ndrCode = getRegimenMapValue(valueCoded);
				//                regimenType.setPrescribedRegimenLineCode(ndrCode);
				//                regimenType.setPrescribedRegimenTypeCode(Utils.ART_CODE);
				//                obs = Utils.extractObs(valueCoded, obsListForAVisit); // PrescribedRegimen
				//                if (obs != null) {
				//                    valueCoded = obs.getValueCoded().getConceptId();
				//                    ndrCode = getRegimenMapValue(valueCoded);
				//                    codedSimpleType = new CodedSimpleType();
				//                    codedSimpleType.setCode(ndrCode);
				//                    codedSimpleType.setCodeDescTxt(obs.getValueCoded().getName().getName());
				//                    regimenType.setPrescribedRegimen(codedSimpleType);
				//                }
				//                regimenType.setPrescribedRegimenDispensedDate(getXmlDate(visitDate));//PrescribedRegimenDispensedDate
				//                stopDateTime = retrieveMedicationDuration(visitDate, obsListForAVisit,obs.getValueCodedName().getName());
				//                startDateTime = new DateTime(visitDate);
				//                if (stopDateTime != null) {
				//                    durationInDays = Utils.getDateDiffInDays(startDateTime.toDate(), stopDateTime.toDate());
				//                    regimenType.setPrescribedRegimenDuration(String.valueOf(durationInDays));//PrescribedRegimenDuration
				//                    stopDate = stopDateTime.toDate();

				//                    regimenType.setDateRegimenEnded(getXmlDate(stopDate));
				//                    regimenType.setDateRegimenEndedDD(Utils.getDayDD(stopDate));
				//                    regimenType.setDateRegimenEndedMM(Utils.getMonthMM(stopDate));
				//                    regimenType.setDateRegimenEndedYYYY(Utils.getYearYYYY(stopDate));

			}
		}

		//            regimenType.setDateRegimenStarted(getXmlDate(visitDate));
		//            regimenType.setDateRegimenStartedDD(Utils.getDayDD(visitDate));
		//            regimenType.setDateRegimenStartedMM(Utils.getMonthMM(visitDate));
		//            regimenType.setDateRegimenStartedYYYY(Utils.getYearYYYY(visitDate));

		return null;
	}

	public void retrieveMedicationDuration(Date visitDate, List<Obs> obsList, String drugName) {
		DateTime stopDateTime = null;
		DateTime startDateTime = null;
		int durationDays = 0;
		Obs obs = null;
		List<Obs> targetObsList = new ArrayList<Obs>();
		Set<ARVDispensedItem> aRVDispensedItems = new HashSet<>();

		Obs obsGroup = Utils.extractObs(Utils.ARV_DRUGS_GROUPING_CONCEPT_SET, obsList);
		if (obsGroup != null) {
			ARVDispensedItem aRVDispensedItem = new ARVDispensedItem();
			aRVDispensedItem.setItemName(drugName);

			targetObsList.addAll(obsGroup.getGroupMembers());
			obs = Utils.extractObs(Utils.MEDICATION_DURATION_CONCEPT, targetObsList);
			if (obs != null) {
				durationDays = (int)obs.getValueNumeric().doubleValue();
				aRVDispensedItem.setDuration(durationDays);

			}

			//     obs = Utils.extractObs(NAME_LENGTH, obsList)

		}

		/*if (stopDateTime == null) {
		    obs = Utils.extractObs(Utils.NEXT_APPOINTMENT_DATE_CONCEPT, obsList);
		    if (obs != null) {
		        stopDateTime = new DateTime(obs.getValueDate());
		    }
		}*/

	}

	@Override
	protected void validate(OpenmrsObject object) {
		return;
	}

	@Override
	protected IObjectAuthorizationPrivileges getPrivileges() {
		return null;
	}

}
