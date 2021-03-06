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
 * Java class for AdjustmentType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdjustmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OperationID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OperationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AdjustmentTypeCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{}CodeType">
 *               &lt;enumeration value="P"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DestinationStockroomCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{}CodeType">
 *               &lt;enumeration value="M"/>
 *               &lt;enumeration value="p"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OperationItem" type="{}OperationItemType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdjustmentType", propOrder = {
        "operationID",
        "operationDate",
        "adjustmentTypeCode",
        "destinationStockroomCode",
        "operationItem"
})
public class AdjustmentType {

	@XmlElement(name = "OperationID", required = true)
	protected String operationID;
	@XmlElement(name = "OperationDate", required = true)
	protected String operationDate;
	@XmlElement(name = "AdjustmentTypeCode", required = true)
	protected String adjustmentTypeCode;
	@XmlElement(name = "DestinationStockroomCode", required = true)
	protected String destinationStockroomCode;
	@XmlElement(name = "OperationItem", required = true)
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
	 * Gets the value of the adjustmentTypeCode property.
	 * @return possible object is {@link String }
	 */
	public String getAdjustmentTypeCode() {
		return adjustmentTypeCode;
	}

	/**
	 * Sets the value of the adjustmentTypeCode property.
	 * @param value allowed object is {@link String }
	 */
	public void setAdjustmentTypeCode(String value) {
		this.adjustmentTypeCode = value;
	}

	/**
	 * Gets the value of the destinationStockroomCode property.
	 * @return possible object is {@link String }
	 */
	public String getDestinationStockroomCode() {
		return destinationStockroomCode;
	}

	/**
	 * Sets the value of the destinationStockroomCode property.
	 * @param value allowed object is {@link String }
	 */
	public void setDestinationStockroomCode(String value) {
		this.destinationStockroomCode = value;
	}

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
