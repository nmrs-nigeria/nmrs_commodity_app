//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.25 at 12:43:37 PM WAT 
//

package org.openmrs.module.openhmis.ndrmodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ConsumptionReportType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConsumptionReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NewConsumption" type="{}NewConsumptionType" maxOccurs="unbounded"/>
 *         &lt;element name="ConsumptionSummary" type="{}ConsumptionSummaryType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsumptionReportType", propOrder = {
        "newConsumption",
        "consumptionSummary"
})
public class ConsumptionReportType {

	@XmlElement(name = "NewConsumption", required = true)
	protected List<NewConsumptionType> newConsumption;
	@XmlElement(name = "ConsumptionSummary", required = true)
	protected List<ConsumptionSummaryType> consumptionSummary;

	/**
	 * Gets the value of the newConsumption property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
	 * newConsumption property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
     *    getNewConsumption().add(newItem);
     * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link NewConsumptionType }
	 */
	public List<NewConsumptionType> getNewConsumption() {
		if (newConsumption == null) {
			newConsumption = new ArrayList<NewConsumptionType>();
		}
		return this.newConsumption;
	}

	/**
	 * Gets the value of the consumptionSummary property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
	 * consumptionSummary property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
     *    getConsumptionSummary().add(newItem);
     * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link ConsumptionSummaryType }
	 */
	public List<ConsumptionSummaryType> getConsumptionSummary() {
		if (consumptionSummary == null) {
			consumptionSummary = new ArrayList<ConsumptionSummaryType>();
		}
		return this.consumptionSummary;
	}

}
