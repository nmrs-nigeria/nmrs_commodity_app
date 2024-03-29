/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;

/**
 * @author MORRISON.I
 */
public class Utils {

	public static final int OI_DRUGS_GROUPING_CONCEPT_SET = 165726;//
	public static final int TB_DRUGS_GROUPING_CONCEPT_SET = 165728;

	public static final int TREATMENT_CATEGORY_CONCEPT = 165720; // From Pharmacy Form
	public static final int ADULT_TREATMENT_CATEGORY_CONCEPT = 165709; // From Pharmacy Form
	public static final int PEDIATRIC_TREATMENT_CATEGORY_CONCEPT = 1528; // From Pharmacy Form
	public static final int CURRENT_REGIMEN_LINE_CONCEPT = 165708; // From Pharmacy Form
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
	public static final int FACILITY_DISPENSING = 166276;
	public static final int DECENTRALIZED_DRUG_DELIVERY = 166363;

	public static final int FACILITY_SDM_MODEL_NOT_DIFERENCIATED = 166153;
	public static final int FACILITY_SDM_REFILL_FAST_TRACK = 166151;
	public static final int FACILITY_SDM_ADOLECENT_CLINIC = 166279;

	public static final int DECENTRALIZED_SDM_COMMUNITY_PHARMACY = 166134;
	public static final int DECENTRALIZED_SDM_MODEL_COMMUNITY_ART = 166135;
	public static final int DECENTRALIZED_SDM_HOME_DELIVERY = 166280;
	public static final int DECENTRALIZED_SDM_PRIVATE_CLINICS = 166364;
	public static final int DECENTRALIZED_SDM_PATENT_MEDICINE_STORES = 166365;
	public static final int DECENTRALIZED_SDM_ATM = 166366;
	public static final int DECENTRALIZED_SDM_OTHERS = 5622;
	public static final int DECENTRALIZED_SDM_OTHERS_DISPENSARY_VALUE = 166367;

	public static final int SERVICE_DELIVERY_MODEL_MULTIMONTH_SCRIPTING = 166149;
	public static final int SERVICE_DELIVERY_MODEL_FAMILY_DRUG_PICKUP = 166150;

	public static final int TB_DRUG = 165304;
	public static final int OI_DRUG = 165727;
	public static final int ARV_DRUG = 165724;
	public static final int ARV_DRUG_STRENGHT = 165725;
	public static final int ARV_QTY_PRESCRIBED = 160856;
	public static final int ARV_QTY_DISPENSED = 1443;

	public static final int ARV_DRUG_SINGLE_DOSE = 166120;
	public static final int ARV_DRUG_FREQUENCY = 165723;

	/* Identifier IDs */
	public static final int PEPFAR_IDENTIFIER_INDEX = 4;

	public static final int HOSPITAL_IDENTIFIER_INDEX = 5;

	public static final int OTHER_IDENTIFIER_INDEX = 3;

	public static final int HTS_IDENTIFIER_INDEX = 8;

	public static final int PMTCT_IDENTIFIER_INDEX = 6;

	public static final int EXPOSE_INFANT_IDENTIFIER_INDEX = 7;

	public static final int PEP_IDENTIFIER_INDEX = 9;

	public static final int RECENCY_INDENTIFIER_INDEX = 10;

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

	public static Map<Object, List<Obs>> groupedByConceptIdsOnly(List<Obs> obsList) {
		Map<Object, List<Obs>> groupedByConceptIds = new HashMap<>();

		if (obsList != null && obsList.size() > 0) {
			for (Obs obs : obsList) {
				// group by conceptId
				if (obs.getConcept() != null && obs.getConcept().getConceptId() != null) {
					if (groupedByConceptIds.get(obs.getConcept().getConceptId()) == null) {
						List<Obs> obsChildList = new ArrayList<>();
						obsChildList.add(obs);
						groupedByConceptIds.put(obs.getConcept().getConceptId(), obsChildList);
					} else {
						List<Obs> groupedObs = groupedByConceptIds.get(obs.getConcept().getConceptId());
						groupedObs.add(obs);
						groupedByConceptIds.put(obs.getConcept().getConceptId(), groupedObs);
					}
				}
			}
		}
		return groupedByConceptIds;
	}

	public static Obs extractObsMap(int conceptID, Map<Object, List<Obs>> obsList) {
		List<Obs> obss = obsList.get(conceptID);
		if (obss != null && obss.size() > 0) {
			return obss.get(0);
		}
		return null;
	}

	public static Set<Obs> extractObsList(int conceptID, List<Obs> obsList) {

        if (obsList == null) {
            return null;
        }
        return obsList.stream().filter(ele -> ele.getConcept().getConceptId() == conceptID)
                .collect(Collectors.toSet());
    }

	public static String getPatientPEPFARId(Patient patient) {

		PatientIdentifier patientId = patient.getPatientIdentifier(PEPFAR_IDENTIFIER_INDEX);

		if (patientId != null) {
			return patientId.getIdentifier();
		} else {
			patientId = patient.getPatientIdentifier(HOSPITAL_IDENTIFIER_INDEX); //hospital number
			if (patientId != null) {
				return patientId.getIdentifier();
			}
			return "";
		}
	}

	public static Map<Integer, String> getRegimenDescription() {
		Map<Integer, String> regimenCodeDescTextMap = new HashMap<>();
		regimenCodeDescTextMap.put(REGIMEN_166092, "ABC-3TC-ATV/r");
		regimenCodeDescTextMap.put(REGIMEN_160124, "AZT-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_1652, "AZT-3TC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_160104, "D4T-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_166179, "ABC-FTC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_166181, "ABC-3TC-TDF");
		regimenCodeDescTextMap.put(REGIMEN_166183, "D4T-3TC-ABC");
		regimenCodeDescTextMap.put(REGIMEN_166185, "AZT-TDF-NVP");
		regimenCodeDescTextMap.put(REGIMEN_166186, "DDI-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_166187, "AZT-3TC-DTG");
		regimenCodeDescTextMap.put(REGIMEN_162564, "ABC-AZT-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_166188, "DDI-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_162559, "ABC-DDI-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_164854, "TDF-FTC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_164505, "TDF-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_162565, "TDF-3TC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_165522, "AZT-3TC-TDF");
		regimenCodeDescTextMap.put(REGIMEN_162563, "ABC-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_165681, "TDF-3TC-DTG");
		regimenCodeDescTextMap.put(REGIMEN_165686, "TDF-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_165682, "TDF-FTC-DTG");
		regimenCodeDescTextMap.put(REGIMEN_165687, "TDF-FTC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_165523, "TDF-FTC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_162201, "TDF-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_165524, "TDF-FTC-ATV/r");
		regimenCodeDescTextMap.put(REGIMEN_164512, "TDF-3TC-ATV/r");
		regimenCodeDescTextMap.put(REGIMEN_162561, "AZT-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_164511, "AZT-3TC-ATV/r");
		regimenCodeDescTextMap.put(REGIMEN_165530, "AZT-TDF-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_165537, "TDF-AZT-3TC-ATV/r");
		regimenCodeDescTextMap.put(REGIMEN_165688, "DRV/r-DTG + 1-2 NRTIs");
		regimenCodeDescTextMap.put(REGIMEN_1652, "AZT-3TC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_162199, "ABC-3TC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_817, "AZT-3TC-ABC");
		regimenCodeDescTextMap.put(REGIMEN_792, "d4T-3TC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_165691, "ABC-3TC-DTG");
		regimenCodeDescTextMap.put(REGIMEN_165693, "ABC-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_162200, "ABC-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_165692, "ABC-FTC-DTG");
		regimenCodeDescTextMap.put(REGIMEN_165694, "ABC-FTC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_165690, "ABC-FTC-NVP");
		regimenCodeDescTextMap.put(REGIMEN_162561, "AZT-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_165695, "AZT-3TC-RAL");
		regimenCodeDescTextMap.put(REGIMEN_165686, "TDF-3TC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_104565, "TDF-FTC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_165687, "TDF-FTC-EFV");
		regimenCodeDescTextMap.put(REGIMEN_162561, "AZT-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_162560, "d4T-3TC-LPV/r");
		regimenCodeDescTextMap.put(REGIMEN_165526, "ABC-3TC-ddi");
		regimenCodeDescTextMap.put(REGIMEN_165696, "ABC-3TC-RAL");
		regimenCodeDescTextMap.put(REGIMEN_165695, "AZT-3TC-RAL");
		regimenCodeDescTextMap.put(REGIMEN_165698, "DRV/r + 2 NRTIs + 2 NNRTI");
		regimenCodeDescTextMap.put(REGIMEN_165700, "DRV/r +2NRTIs");
		regimenCodeDescTextMap.put(REGIMEN_165701, "DRV/r-RAL + 1-2 NRTIs");
		regimenCodeDescTextMap.put(REGIMEN_165697, "DTG+2 NRTIs");
		regimenCodeDescTextMap.put(REGIMEN_165699, "RAL + 2 NRTIs");
		regimenCodeDescTextMap.put(REGIMEN_86663, "AZT");
		regimenCodeDescTextMap.put(REGIMEN_78643, "3TC");
		regimenCodeDescTextMap.put(REGIMEN_165544, "AZT-NVP");

		return regimenCodeDescTextMap;
	}

	//reportId
	public static final String DISPENSARY_CONSUMPTION_REPORT = "dispensary_consumption";

	//report header
	public static final String DISPENSARY_CONSUMPTION_REPORT_SHEET_NAME = "Consumption Report";
	public static final String DCR_ITEM_HEADER = "Item";
	public static final String DCR_DISPENSARY_HEADER = "Dispensary";
	public static final String DCR_TOTAL_QUANTITY_RECEIVED_HEADER = "Total Quantity Received";
	public static final String DCR_TOTAL_QUANTITY_CONSUMED_HEADER = "Total Quantity Consumed";
	public static final String DCR_TOTAL_QUANTITY_ISSUED_HEADER = "Total Quantity Issued";
	public static final String DCR_TOTAL_WASTAGE_HEADER = "Total wastage";
	public static final String DCR_STOCK_BALANCE_HEADER = "Stock balance";
	public static final String DCR_DRUG_CATEGORY_HEADER = "Drug Category";
	public static final String DCR_SERVICE_DELIVERY_MODEL = "Service Delivery Model";
	public static final String DCR_DELIVERY_TYPE = "Delivery Type";

	public static final String DISPENSARY_STOCKONHAND_REPORT_SHEET_NAME = "Dispensary Stock on Hand Report";
	public static final String SSR_DEPARTMENT_HEADER = "Department";
	public static final String SSR_ITEM_HEADER = "Item";
	public static final String SSR_BATCH_HEADER = "Batch";
	public static final String SSR_EXPIRATION_HEADER = "Expiration";
	public static final String SSR_QUANTITY_HEADER = "Quantity";

	//integer constant
	public static final int ZERO_INTEGER = 0;
	public static final int ONE_INTEGER = 1;
	public static final int TWO_INTEGER = 2;
	public static final int THREE_INTEGER = 3;
	public static final int FOUR_INTEGER = 4;
	public static final int FIVE_INTEGER = 5;
	public static final int SIX_INTEGER = 6;
	public static final int SEVEN_INTEGER = 7;
	public static final int EIGHT_INTEGER = 8;
	public static final int NINE_INTEGER = 9;

	public static final String PHARMACY_COMMODITY_TYPE = "pharmacy";
	public static final String LAB_COMMODITY_TYPE = "lab";

	public static final String ADULT_ART_TEXT = "Adult ART";
	public static final String PEDIATRIC_ART_TEXT = "Pediatric ART";

	public static final int REGIMEN_166092 = 166092;
	public static final int REGIMEN_160124 = 160124;
	public static final int REGIMEN_1652 = 1652;
	public static final int REGIMEN_160104 = 160104;
	public static final int REGIMEN_166179 = 166179;
	public static final int REGIMEN_166181 = 166181;
	public static final int REGIMEN_166183 = 166183;
	public static final int REGIMEN_166185 = 166185;
	public static final int REGIMEN_166186 = 166186;
	public static final int REGIMEN_166187 = 166187;
	public static final int REGIMEN_162564 = 162564;
	public static final int REGIMEN_166188 = 166188;
	public static final int REGIMEN_162559 = 162559;
	public static final int REGIMEN_164854 = 164854;
	public static final int REGIMEN_164505 = 164505;
	public static final int REGIMEN_162565 = 162565;
	public static final int REGIMEN_165522 = 165522;
	public static final int REGIMEN_162563 = 162563;
	public static final int REGIMEN_165681 = 165681;
	public static final int REGIMEN_165686 = 165686;
	public static final int REGIMEN_165682 = 165682;
	public static final int REGIMEN_165687 = 165687;
	public static final int REGIMEN_165523 = 165523;
	public static final int REGIMEN_162201 = 162201;
	public static final int REGIMEN_165524 = 165524;
	public static final int REGIMEN_164512 = 164512;
	public static final int REGIMEN_162561 = 162561;
	public static final int REGIMEN_164511 = 164511;
	public static final int REGIMEN_165530 = 165530;
	public static final int REGIMEN_165537 = 165537;
	public static final int REGIMEN_165688 = 165688;
	public static final int REGIMEN_162199 = 162199;
	public static final int REGIMEN_817 = 817;
	public static final int REGIMEN_792 = 792;
	public static final int REGIMEN_165691 = 165691;
	public static final int REGIMEN_165693 = 165693;
	public static final int REGIMEN_162200 = 162200;
	public static final int REGIMEN_165692 = 165692;
	public static final int REGIMEN_165694 = 165694;
	public static final int REGIMEN_165690 = 165690;
	public static final int REGIMEN_165695 = 165695;
	public static final int REGIMEN_104565 = 104565;
	public static final int REGIMEN_162560 = 162560;
	public static final int REGIMEN_165526 = 165526;
	public static final int REGIMEN_165696 = 165696;
	public static final int REGIMEN_165698 = 165698;
	public static final int REGIMEN_165700 = 165700;
	public static final int REGIMEN_165701 = 165701;
	public static final int REGIMEN_165697 = 165697;
	public static final int REGIMEN_165699 = 165699;
	public static final int REGIMEN_86663 = 86663;
	public static final int REGIMEN_78643 = 78643;
	public static final int REGIMEN_165544 = 165544;
}
