/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MORRISON.I
 */
public class DictionaryMaps {

	private Map<String, Integer> departmentMappings = new HashMap<>();
	private Map<String, String> sourceStockRoomMappings = new HashMap<>();
	private Map<String, String> returnMappings = new HashMap<>();
	private Map<String, String> distributeToMappings = new HashMap<>();
	private Map<String, String> adjustmentTypeMappings = new HashMap<>();
	private Map<String, String> itemMappings = new HashMap<>();

	public DictionaryMaps() {

	}

	private void fillUpDepartments() {

		this.departmentMappings.put("9a0a3f68-d3cc-4319-8b11-2f0af9a13493", 1); //ANC
		this.departmentMappings.put("63dfb487-4410-43fa-9ef5-1d98937b0828", 2); //Com
		this.departmentMappings.put("bc5efa90-30f6-4e2c-89c1-d0fdae4cddea", 3); //Emerg
		this.departmentMappings.put("bc22078e-c5be-4cdb-ad9f-17376a6fe9f4", 4); //FP
		this.departmentMappings.put("e0add9d4-24c5-447d-b9b5-39c30f3f93f2", 5); //In-Pat
		this.departmentMappings.put("3816323a-bbfa-4cbf-ae29-1397ad0f6c62", 6); // L-and-D
		this.departmentMappings.put("8eb2330d-7970-41b7-8240-b07d4534c923", 7); // Lab
		this.departmentMappings.put("e2b1f92a-5af3-411a-b114-c9b310cb9f8c", 8); // Mal
		this.departmentMappings.put("57c54916-668c-41bf-a089-5257fdb358a3", 9); //Mob
		this.departmentMappings.put("a5e899da-27c8-47a3-9ea6-5c0df9dee87d", 10); //OPD
		this.departmentMappings.put("34b14c5c-2824-4d70-9637-9d5792dd700e", 11); //OSS
		this.departmentMappings.put("43dd09f9-2773-4f67-b694-0b37d66db406", 12); //Oth
		this.departmentMappings.put("759f4329-6420-4cf6-b65c-ee8ea0cd5e37", 13); //Paed
		this.departmentMappings.put("53dcc49a-89ca-4076-8507-3f43a345cf1e", 14); //PP
		this.departmentMappings.put("1952ff56-a3dc-40cb-954a-c05810259b03", 15); //STI
		this.departmentMappings.put("7d4b8a26-9cf8-4d7a-8767-aaac21aca521", 16); //TB
		this.departmentMappings.put("1f2ac9f2-d2db-4933-b04d-0b85455a141b", 17); //VCT

	}

	public Map<String, Integer> getDepartmentMappings() {
		return departmentMappings;
	}

	public Map<String, String> getSourceStockRoomMappings() {
		return sourceStockRoomMappings;
	}

}
