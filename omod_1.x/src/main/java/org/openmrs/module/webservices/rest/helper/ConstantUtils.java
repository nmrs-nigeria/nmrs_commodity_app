/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

/**
 * @author MORRISON.I
 */
public class ConstantUtils {

	public static final String DISTRIBUTION_TYPE_UUID = "c264f34b-c795-4576-9928-454d1fa20e09";
	public static final String ADJUSTMENT_TYPE_UUID = "288fd7fe-1374-4f7a-89e6-d5f1ac97d4a5";
	public static final String DISPOSED_TYPE_UUID = "84be0aaf-70cf-4ebb-83e3-088e5d375905";
	public static final String RECEIPT_TYPE_UUID = "fce0b4fc-9402-424a-aacb-f99599e51a9f";
	public static final String RETURN_TYPE_UUID = "128924d7-72ee-414e-ae40-52f1f89d3e7d";
	public static final String TRANSFER_TYPE_UUID = "db40707f-9175-4199-8df2-a5702f41ec7d";

	public static final int EXPIRYDAYSCOUNT = 120;
	public static final String DEPARTMENT_STRING = "department";
	public static final String INSTITUTION_STRING = "institution";
	public static final String PATIENT_STRING = "patient";
	public static final String PHARMACY_COMMODITY_TYPE = "pharmacy";
	public static final String LAB_COMMIDITY_TYPE = "lab";

	//reportId
	public static final String DISPENSARY_CONSUMPTION_REPORT = "dispensary_consumption";
	public static final String STOCK_ROOM_CONSUMPTION_REPORT = "stockroom_consumption";
	public static final String STORE_STOCKONHAND_REPORT = "stockroom_stockonhand";
	public static final String DISPENSARY_STOCKONHAND_REPORT = "dispensary_stockonhand";

	//report header
	public static final String DISPENSARY_CONSUMPTION_REPORT_SHEET_NAME = "Consumption Report";
	public static final String DCR_ITEM_HEADER = "Item";
	public static final String DCR_TOTAL_QUANTITY_RECEIVED_HEADER = "Total Quantity Received";
	public static final String DCR_TOTAL_QUANTITY_CONSUMED_HEADER = "Total Quantity Consumed";
	public static final String DCR_TOTAL_WASTAGE_HEADER = "Total wastage";
	public static final String DCR_STOCK_BALANCE_HEADER = "Stock balance";

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
}
