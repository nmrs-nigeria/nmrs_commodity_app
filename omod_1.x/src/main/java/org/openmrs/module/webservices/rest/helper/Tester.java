/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.webservices.rest.helper;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import org.openmrs.module.openhmis.ndrmodel.NewConsumptionType;

/**
 * @author MORRISON.I
 */
public class Tester {

	public static void main(String args[]) {
		try {
			XMLGregorianCalendar xmldate = RestUtils.getXmlDate(new Date());
			NewConsumptionType newConsumptionType = new NewConsumptionType();
			newConsumptionType.setConsumptionDate(xmldate);
			System.out.println(xmldate);
		} catch (Exception ex) {
			Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
