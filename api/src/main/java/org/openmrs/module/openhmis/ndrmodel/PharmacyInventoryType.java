//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.04.25 at 12:43:37 PM WAT 
//

package org.openmrs.module.openhmis.ndrmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for PharmacyInventoryType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PharmacyInventoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaskOperation" type="{}TaskOperationType" minOccurs="0"/>
 *         &lt;element name="ConsumptionReport" type="{}PharmacyConsumptionReportType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PharmacyInventoryType", propOrder = {
        "taskOperation",
        "consumptionReport"
})
public class PharmacyInventoryType {

	@XmlElement(name = "TaskOperation")
	protected TaskOperationType taskOperation;
	@XmlElement(name = "ConsumptionReport")
	protected PharmacyConsumptionReportType consumptionReport;

	/**
	 * Gets the value of the taskOperation property.
	 * @return possible object is {@link TaskOperationType }
	 */
	public TaskOperationType getTaskOperation() {
		return taskOperation;
	}

	/**
	 * Sets the value of the taskOperation property.
	 * @param value allowed object is {@link TaskOperationType }
	 */
	public void setTaskOperation(TaskOperationType value) {
		this.taskOperation = value;
	}

	/**
	 * Gets the value of the consumptionReport property.
	 * @return possible object is {@link PharmacyConsumptionReportType }
	 */
	public PharmacyConsumptionReportType getConsumptionReport() {
		return consumptionReport;
	}

	/**
	 * Sets the value of the consumptionReport property.
	 * @param value allowed object is {@link PharmacyConsumptionReportType }
	 */
	public void setConsumptionReport(PharmacyConsumptionReportType value) {
		this.consumptionReport = value;
	}

}
