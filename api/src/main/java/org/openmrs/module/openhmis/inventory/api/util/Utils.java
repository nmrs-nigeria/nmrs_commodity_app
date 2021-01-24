/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openmrs.Obs;

/**
 * @author MORRISON.I
 */
public class Utils {

	public static final int CURRENT_REGIMEN_LINE_CONCEPT = 165708; // From Pharmacy Form
	public static final int PEPFAR_IDENTIFIER_INDEX = 4;
	public static final int VISIT_TYPE_CONCEPT = 164181; // Visit Type concept from Pharmacy Forms
	public static final int ARV_DRUGS_GROUPING_CONCEPT_SET = 162240;// 
	public static final int MEDICATION_DURATION_CONCEPT = 159368;// Medication Duration Concept From Pharmacy Form
	public static final int TREATMENT_TYPE = 165945;
	public static final int TREATMENT_TYPE_ART_THERAPHY = 165303;
	public static final int TREATMENT_TYPE_NON_ART = 165941;
	public static final int TREATMENT_TYPE_OCCUP_PEP = 165942;
	public static final int TREATMENT_TYPE_NON_OCCUP_PEP = 165943;
	public static final int TREATMENT_TYPE_HEI = 165658;
	public static final int TREATMENT_TYPE_PREP = 165944;
	public static final int TREATMENT_TYPE_PMTCT = 165685;
	public static final int VISIT_TYPE_INITIAL_CONCEPT = 164180; // Initial Visit from Pharmacy Forms	
	public static final int VISIT_TYPE_FOLLOWUP_CONCEPT = 160530; // Follow up Visit from Pharmacy Forms
	public static final int PICKUP_REASON_CONCEPT = 165774; // From Pharmacy Form
	public static final int PICKUP_REASON_CONCEPT_SUBSTITUTE_VALUE = 165665; // From Pharmacy Form
	public static final int PICKUP_REASON_CONCEPT_SWITCH_VALUE = 165772;// From Pharmacy Order Form
	public static final int PICKUP_REASON_CONCEPT_REFILL = 165662;
	public static final int PICKUP_REASON_CONCEPT_NEW = 165773;

	public static final int SERVICE_DELIVERY_MODEL = 166148;
	public static final int SERVICE_DELIVERY_MODEL_NOT_DIFERENCIATED = 166153;
	public static final int SERVICE_DELIVERY_MODEL_COMMUNITY_PHARMACY = 166134;
	public static final int SERVICE_DELIVERY_MODEL_COMMUNITY_ART = 166135;
	public static final int SERVICE_DELIVERY_MODEL_REFILL_FAST_TRACK = 166151;
	public static final int SERVICE_DELIVERY_MODEL_MULTIMONTH_SCRIPTING = 166149;
	public static final int SERVICE_DELIVERY_MODEL_FAMILY_DRUG_PICKUP = 166150;

	public static boolean contains(List<Obs> obsList, int conceptID) {
		boolean ans = false;
		for (Obs ele : obsList) {
			if (ele.getConcept().getConceptId() == conceptID) {
				ans = true;
			}
		}
		return ans;
	}

	public static String getVisitId(String identifier, Date visitDate) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String visitID = dateFormat.format(visitDate);// + "-" + identifier;
		//DateTime dateTime=new DateTime(visitDate);
		//StringUtils.leftPad(String.valueOf(dateTime), DEAD_CONCEPT, identifier)
		//String visitID = String.valueOf(visitDate.getTime());
		return visitID;
	}

	public static XMLGregorianCalendar getXmlDate(Date date) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = null;
		if (date != null) {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format(date));
		}
		return cal;
	}

	public static Obs extractObs(int conceptID, List<Obs> obsList) {

        if (obsList == null) {
            return null;
        }
        return obsList.stream().filter(ele -> ele.getConcept().getConceptId() == conceptID).findFirst().orElse(null);
    }
}