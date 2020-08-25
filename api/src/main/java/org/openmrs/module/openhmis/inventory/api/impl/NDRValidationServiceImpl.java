/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.openhmis.inventory.api.impl;

import com.umb.ndr.signer.Signer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.openmrs.module.openhmis.inventory.api.INDRValidationService;

/**
 * @author MORRISON.I
 */
public class NDRValidationServiceImpl implements INDRValidationService {

	@Override
	public String getValidation(String datimcode) {
		Map<String, String> initialMap = getInitialValidation();
		return retreiveValidation(datimcode, initialMap);

	}

	private String retreiveValidation(String datimCode, Map<String, String> staticEncrytionMap) {
		String textToEnc = staticEncrytionMap.get("initial");
		//     Version version = Utils.getNmrsVersion();
		try {

			//            textToEnc += "|";
			//            textToEnc += patientId;
			textToEnc += "|";
			textToEnc += datimCode;

			String returnVar = Signer.encryptText(textToEnc);
			String hashReturnVar = Base64.getEncoder().encodeToString(returnVar.getBytes());
			returnVar += "||";
			//add hash of encrpt string
			returnVar += hashReturnVar;
			returnVar += "||";
			returnVar += staticEncrytionMap.get("validation");
			//   returnVar += version.getValidator();
			return returnVar;
		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

		return null;
	}

	private static Map<String, String> getInitialValidation() {

		Runtime runtime = Runtime.getRuntime();
		//  RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

		String textToEnc = "";
		textToEnc += System.getProperty("os.arch");
		textToEnc += "|";
		textToEnc += System.getProperty("os.name");
		textToEnc += "|";
		textToEnc += System.getProperty("os.version");
		textToEnc += "|";
		textToEnc += System.getProperty("OPENMRS_APPLICATION_DATA_DIRECTORY");
		textToEnc += "|";
		textToEnc += System.getProperty("java.version");
		textToEnc += "|";
		textToEnc += System.getProperty("user.home");
		textToEnc += "|";
		textToEnc += System.getProperty("java.home");
		textToEnc += "|";
		textToEnc += System.getProperty("java.vendor");
		textToEnc += "|";
		textToEnc += System.getProperty("sun.arch.data.model");
		textToEnc += "|";
		textToEnc += runtime.availableProcessors();
		textToEnc += "|";
		textToEnc += runtime.totalMemory();

		Map<String, String> encMap = new HashMap<>();

		try {
			//  TODO: will read version from spring version
			textToEnc += "|";
			textToEnc += "3.3.0-SNAPSHOT";

			encMap.put("validation", "1.1");

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		encMap.put("initial", textToEnc);

		return encMap;

	}

}
