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

	private final Map<String, Integer> departmentMappings = new HashMap<>();
	private final Map<String, String> sourceStockRoomMappings = new HashMap<>();
	private final Map<String, String> returnMappings = new HashMap<>();
	private final Map<String, String> distributeToMappings = new HashMap<>();
	private final Map<String, String> adjustmentTypeMappings = new HashMap<>();
	private final Map<String, Integer> itemMappings = new HashMap<>();
	private final Map<String, String> testPurpose = new HashMap<>();
	private final Map<String, String> commoditySource = new HashMap<>();
	private final Map<String, String> disposedTypeMappings = new HashMap<>();

	public DictionaryMaps() {
		this.fillUpDepartments();
		this.fillUpSourceStockRoom();
		this.fillUpReturns();
		this.fillUpDistributeTo();
		this.fillUpAdjustment();
		this.fillUpItemMappings();
		this.fillUpCommoditySource();
		this.fillUpTestPurpose();
		this.fillUpDisposedType();
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
		this.departmentMappings.put("36ccbd54-ab90-4bab-88b9-eb7bd7e1df1a", 18); //EID
		this.departmentMappings.put("9e059670-5576-4e8a-b770-10f8cee9f36e", 19); //dispensary
		this.departmentMappings.put("4e72896f-f8d4-493d-bf2c-11afd0c32da3", 20); //com-pharmacy

	}

	private void fillUpSourceStockRoom() {
		this.sourceStockRoomMappings.put("2741bae2-c5de-43ef-891f-7ec2fd58f442", "M");
		this.sourceStockRoomMappings.put("5452ec3e-2fe1-46de-8a6e-28c6442e4cc0", "P");
	}

	private void fillUpReturns() {
		this.returnMappings.put(ConstantUtils.DEPARTMENT_STRING, "D");
		this.returnMappings.put(ConstantUtils.INSTITUTION_STRING, "I");
	}

	private void fillUpDistributeTo() {
		this.distributeToMappings.put(ConstantUtils.DEPARTMENT_STRING, "D");
		this.distributeToMappings.put(ConstantUtils.INSTITUTION_STRING, "I");
		this.distributeToMappings.put(ConstantUtils.PATIENT_STRING, "P");
	}

	private void fillUpAdjustment() {
		this.adjustmentTypeMappings.put("positive", "P");
	}

	private void fillUpDisposedType() {
		this.disposedTypeMappings.put("expired", "E");
		this.disposedTypeMappings.put("damaged", "D");
		this.disposedTypeMappings.put("others", "O");
	}

	private void fillUpItemMappings() {
		this.itemMappings.put("5f261815-f35c-4d34-914b-696a7b7f5eac", 1);
		this.itemMappings.put("06d5326c-b994-4d2f-a57a-4fe8755dc1a3", 2);
		this.itemMappings.put("cc97ad9f-efae-4fc3-81d6-a7813a2ec8fa", 3);
		this.itemMappings.put("41e9f6dd-5286-4961-8fcc-edf9a78e1467", 4);
		this.itemMappings.put("ac0226fa-6752-4fe5-8a27-3da7ec78012d", 5);
		this.itemMappings.put("db9484ff-a255-4e19-aee2-1b29fb69153b", 6);
		this.itemMappings.put("7559842c-0599-429d-90b5-e5935d0cd720", 7);
		this.itemMappings.put("e6a0df24-ca6c-47e6-a237-6c18482d1d50", 8); // DBS Kit(20)
		this.itemMappings.put("925d818a-3e3a-4c72-b566-1b94cc12a888", 9); //DBS Kits(50)
		this.itemMappings.put("b798dad9-d7d6-480d-8bd4-d2819145ea31", 10);
		this.itemMappings.put("55dfcd9b-e3b9-4164-a2fc-8faf8df25d58", 12);
		this.itemMappings.put("161937c6-c1b2-44a9-b2f3-1d7ad3da85da", 11);
		this.itemMappings.put("3d66d5eb-bbad-474b-9637-971f6a8e5b01", 13);
		this.itemMappings.put("e52a152b-1eb7-4189-8899-b91308f8b7b0", 14);
		this.itemMappings.put("d4c09c9b-bead-4578-85d8-0802f8bf1b4a", 15);
		this.itemMappings.put("f49501ee-637a-4115-87dc-bb2e72019a6c", 16);
		this.itemMappings.put("c2a303db-bcd4-46d9-b6d1-5c0c4eabcd2a", 17);
		this.itemMappings.put("91dc08f8-f7ae-4ea7-b2ea-0e483c0cb463", 18);
		this.itemMappings.put("0b17816e-d41a-4c8f-917a-8b55e3c345cc", 19);
		this.itemMappings.put("1860973c-1031-489c-b650-4c3d564a9844", 20);
		this.itemMappings.put("d7232d03-06a9-4915-9299-2fd511afb215", 21);
		this.itemMappings.put("5e949026-a650-423e-85c5-4c760e6b201b", 22);
		this.itemMappings.put("8bdc5767-636c-4a86-be12-a4dae87bbae3", 23);
		this.itemMappings.put("6102fcba-aec7-4b55-896b-606217a8de6b", 24);
		this.itemMappings.put("681b29bf-2ca0-48bb-8832-4eb701c5e070", 25);
		this.itemMappings.put("f85affcc-9e07-4303-aabf-a00c13bee5d1", 26);
		this.itemMappings.put("95800b9b-f334-4871-a383-13d663a99a2f", 27);
		this.itemMappings.put("c837ea4c-1aa5-4289-b8ea-033a4c2c793a", 28);
		this.itemMappings.put("68817326-9e61-4cb6-95a7-75193c377f7f", 29);
		this.itemMappings.put("cc047f31-8b44-4ca1-bb02-a85186adfe15", 30);
		this.itemMappings.put("291cf1a7-de8a-43a9-9c68-9c83b1c8cc4a", 31);
		this.itemMappings.put("b9261d1a-ee6c-4ae0-b03e-15f93a12db20", 32);
		this.itemMappings.put("c04a2ff5-a0df-40e8-865c-174a4004d7b9", 33);
		this.itemMappings.put("8ca997ff-b5f8-4386-bff4-6a98ce7377bf", 34);
		this.itemMappings.put("3e3fba6b-cdce-4b20-bae5-7b6916e691a0", 35);
		this.itemMappings.put("4c511821-236e-42d3-9272-9f12841b8e35", 36);
		this.itemMappings.put("1fc71936-ef5f-4105-b020-2d63f6704dc6", 39);

		this.itemMappings.put("7ac77300-767d-11eb-9439-0242ac130002", 37);
		this.itemMappings.put("83aac7a6-767d-11eb-9439-0242ac130002", 38);
		this.itemMappings.put("2b88c424-775e-11eb-9439-0242ac130002", 40);
		this.itemMappings.put("1860973c-1031-489c-b650-4c3d564a9844", 41);
		this.itemMappings.put("c0cdb086-767c-11eb-9439-0242ac130002", 42);
		this.itemMappings.put("cecd47f0-767c-11eb-9439-0242ac130002", 43);
		this.itemMappings.put("d7232d03-06a9-4915-9299-2fd511afb215", 44);
		this.itemMappings.put("0df10d0e-767d-11eb-9439-0242ac130002", 45);
		this.itemMappings.put("43804d68-767d-11eb-9439-0242ac130002", 46);
		this.itemMappings.put("7605d1cf-94de-44bf-9f6f-006d8361eab4", 47);
		this.itemMappings.put("8ca90610-a0f3-11eb-bcbc-0242ac130002", 48);
		this.itemMappings.put("8e346146-767d-11eb-9439-0242ac130002", 49);
		this.itemMappings.put("9729a86a-767d-11eb-9439-0242ac130002", 50);
		this.itemMappings.put("a53009cc-767d-11eb-9439-0242ac130002", 51);
		this.itemMappings.put("ae45d352-767d-11eb-9439-0242ac130002", 52);
		this.itemMappings.put("51524eaa-767d-11eb-9439-0242ac130002", 53);
		this.itemMappings.put("99a516e8-769f-11eb-9439-0242ac130002", 54);
		this.itemMappings.put("9379181e-769f-11eb-9439-0242ac130002", 55);
		this.itemMappings.put("890bda88-769f-11eb-9439-0242ac130002", 56);
		this.itemMappings.put("59494880-769f-11eb-9439-0242ac130002", 57);
		this.itemMappings.put("522ff756-769f-11eb-9439-0242ac130002", 58);
		this.itemMappings.put("4738a0aa-769f-11eb-9439-0242ac130002", 59);
		this.itemMappings.put("3fbbb56a-769f-11eb-9439-0242ac130002", 60);
		this.itemMappings.put("324698f0-769f-11eb-9439-0242ac130002", 61);
		this.itemMappings.put("2a889406-769f-11eb-9439-0242ac130002", 62);
		this.itemMappings.put("1d8ba658-769f-11eb-9439-0242ac130002", 63);
		this.itemMappings.put("15d689a0-769f-11eb-9439-0242ac130002", 64);
		this.itemMappings.put("06e5463e-769f-11eb-9439-0242ac130002", 65);
		this.itemMappings.put("00db558a-769f-11eb-9439-0242ac130002", 66);
		this.itemMappings.put("f6522990-769e-11eb-9439-0242ac130002", 67);
		this.itemMappings.put("efc9a24c-769e-11eb-9439-0242ac130002", 68);
		this.itemMappings.put("e44b779c-769e-11eb-9439-0242ac130002", 69);
		this.itemMappings.put("dd6d84c4-769e-11eb-9439-0242ac130002", 70);
		this.itemMappings.put("cfe69e58-769e-11eb-9439-0242ac130002", 71);
		this.itemMappings.put("c9385146-769e-11eb-9439-0242ac130002", 72);
		this.itemMappings.put("bf10cd06-769e-11eb-9439-0242ac130002", 73);
		this.itemMappings.put("b82f5c0a-769e-11eb-9439-0242ac130002", 74);
		this.itemMappings.put("a518de70-769e-11eb-9439-0242ac130002", 75);
		this.itemMappings.put("9ea69dca-769e-11eb-9439-0242ac130002", 76);
		this.itemMappings.put("92a49612-769e-11eb-9439-0242ac130002", 77);
		this.itemMappings.put("8a8abf9c-769e-11eb-9439-0242ac130002", 78);
		this.itemMappings.put("78541b34-769e-11eb-9439-0242ac130002", 79);
		this.itemMappings.put("71a7f968-769e-11eb-9439-0242ac130002", 80);
		this.itemMappings.put("64d874a6-769e-11eb-9439-0242ac130002", 81);
		this.itemMappings.put("5bdffaf4-769e-11eb-9439-0242ac130002", 82);
		this.itemMappings.put("57da5ab8-7698-11eb-9439-0242ac130002", 83);
		this.itemMappings.put("325c5192-7698-11eb-9439-0242ac130002", 84);
		this.itemMappings.put("1e4dd50e-7698-11eb-9439-0242ac130002", 85);
		this.itemMappings.put("990d766a-7697-11eb-9439-0242ac130002", 86);

		this.itemMappings.put("742aa79f-46bb-4bf3-8326-c27484379810", 87);
		this.itemMappings.put("4ab7f97d-fbf8-40a3-82bd-30e84d5a30c9", 88);
	}

	private void fillUpTestPurpose() {

		this.testPurpose.put("Initial", "I");
		this.testPurpose.put("Initial Screening", "I");
		this.testPurpose.put("Confirmation", "C");
		this.testPurpose.put("Controls", "CX");// 
		this.testPurpose.put("Tie Breaker", "TB");
		this.testPurpose.put("Recency", "R");
	}

	private void fillUpCommoditySource() {
		this.commoditySource.put("PEPFAR", "P");
		this.commoditySource.put("GF", "G");
		this.commoditySource.put("other donors", "O");
		this.commoditySource.put("GON", "GO");
	}

	public Map<String, Integer> getDepartmentMappings() {
		return departmentMappings;
	}

	public Map<String, String> getSourceStockRoomMappings() {
		return sourceStockRoomMappings;
	}

	public Map<String, String> getAdjustmentTypeMappings() {
		return adjustmentTypeMappings;
	}

	public Map<String, Integer> getItemMappings() {
		return itemMappings;
	}

	public Map<String, String> getReturnMappings() {
		return returnMappings;
	}

	public Map<String, String> getDistributeToMappings() {
		return distributeToMappings;
	}

	public Map<String, String> getTestPurpose() {
		return testPurpose;
	}

	public Map<String, String> getCommoditySource() {
		return commoditySource;
	}

	public Map<String, String> getDisposedTypeMappings() {
		return disposedTypeMappings;
	}

}
