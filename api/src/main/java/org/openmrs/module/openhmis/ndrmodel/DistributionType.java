//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.12.30 at 03:49:03 PM WAT 
//

package org.openmrs.module.openhmis.ndrmodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for DistributionType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OperationID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OperationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SourceStockroomCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{}CodeType">
 *               &lt;enumeration value="M"/>
 *               &lt;enumeration value="p"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DistributeTypeCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{}CodeType">
 *               &lt;enumeration value="D"/>
 *               &lt;enumeration value="P"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DepartmentCode" type="{}DepartmentType"/>
 *         &lt;element name="PatientID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OperationItem" type="{}OperationItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionType", propOrder = {
        "operationID",
        "operationDate",
        "sourceStockroomCode",
        "distributeTypeCode",
        "departmentCode",
        //      "patientID",
        "operationItem"
})
public class DistributionType {

	@XmlElement(name = "OperationID", required = true)
	protected String operationID;
	@XmlElement(name = "OperationDate", required = true)
	protected String operationDate;
	@XmlElement(name = "SourceStockroomCode", required = true)
	protected String sourceStockroomCode;
	@XmlElement(name = "DistributeTypeCode", required = true)
	protected String distributeTypeCode;
	@XmlElement(name = "DepartmentCode")
	@XmlSchemaType(name = "positiveInteger")
	protected int departmentCode;
	//	@XmlElement(name = "PatientID", required = true)
	//	protected String patientID;
	@XmlElement(name = "OperationItem")
	protected List<OperationItemType> operationItem;

	/**
	 * Gets the value of the operationID property.
	 * @return possible object is {@link String }
	 */
	public String getOperationID() {
		return operationID;
	}

	/**
	 * Sets the value of the operationID property.
	 * @param value allowed object is {@link String }
	 */
	public void setOperationID(String value) {
		this.operationID = value;
	}

	/**
	 * Gets the value of the operationDate property.
	 * @return possible object is {@link String }
	 */
	public String getOperationDate() {
		return operationDate;
	}

	/**
	 * Sets the value of the operationDate property.
	 * @param value allowed object is {@link String }
	 */
	public void setOperationDate(String value) {
		this.operationDate = value;
	}

	/**
	 * Gets the value of the sourceStockroomCode property.
	 * @return possible object is {@link String }
	 */
	public String getSourceStockroomCode() {
		return sourceStockroomCode;
	}

	/**
	 * Sets the value of the sourceStockroomCode property.
	 * @param value allowed object is {@link String }
	 */
	public void setSourceStockroomCode(String value) {
		this.sourceStockroomCode = value;
	}

	/**
	 * Gets the value of the distributeTypeCode property.
	 * @return possible object is {@link String }
	 */
	public String getDistributeTypeCode() {
		return distributeTypeCode;
	}

	/**
	 * Sets the value of the distributeTypeCode property.
	 * @param value allowed object is {@link String }
	 */
	public void setDistributeTypeCode(String value) {
		this.distributeTypeCode = value;
	}

	/**
	 * Gets the value of the departmentCode property.
	 */
	public int getDepartmentCode() {
		return departmentCode;
	}

	/**
	 * Sets the value of the departmentCode property.
	 */
	public void setDepartmentCode(int value) {
		this.departmentCode = value;
	}

	/**
	 * Gets the value of the patientID property.
	 * @return possible object is {@link String }
	 */
	//	public String getPatientID() {
	//		return patientID;
	//	}
	//
	//	/**
	//	 * Sets the value of the patientID property.
	//	 * @param value allowed object is {@link String }
	//	 */
	//	public void setPatientID(String value) {
	//		this.patientID = value;
	//	}

	/**
	 * Gets the value of the operationItem property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
	 * operationItem property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
     *    getOperationItem().add(newItem);
     * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link OperationItemType }
	 */
	public List<OperationItemType> getOperationItem() {
		if (operationItem == null) {
			operationItem = new ArrayList<OperationItemType>();
		}
		return this.operationItem;
	}

}
