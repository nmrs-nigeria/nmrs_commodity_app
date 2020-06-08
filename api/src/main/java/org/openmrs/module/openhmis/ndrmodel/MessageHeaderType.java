//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.08 at 09:37:53 PM WAT 
//

package org.openmrs.module.openhmis.ndrmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for MessageHeaderType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MessageStatusCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageCreationDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="MessageUniqueID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MessageVersion" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="ExportStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ExportEndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="MessageSendingOrganisation" type="{}MessageSendingOrganisationType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageHeaderType", propOrder = {
        "messageStatusCode",
        "messageCreationDateTime",
        "messageUniqueID",
        "messageVersion",
        "exportStartDate",
        "exportEndDate",
        "messageSendingOrganisation"
})
public class MessageHeaderType {

	@XmlElement(name = "MessageStatusCode", required = true)
	protected String messageStatusCode;
	@XmlElement(name = "MessageCreationDateTime", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar messageCreationDateTime;
	@XmlElement(name = "MessageUniqueID", required = true)
	protected String messageUniqueID;
	@XmlElement(name = "MessageVersion")
	protected float messageVersion;
	@XmlElement(name = "ExportStartDate", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar exportStartDate;
	@XmlElement(name = "ExportEndDate", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar exportEndDate;
	@XmlElement(name = "MessageSendingOrganisation", required = true)
	protected MessageSendingOrganisationType messageSendingOrganisation;

	/**
	 * Gets the value of the messageStatusCode property.
	 * @return possible object is {@link String }
	 */
	public String getMessageStatusCode() {
		return messageStatusCode;
	}

	/**
	 * Sets the value of the messageStatusCode property.
	 * @param value allowed object is {@link String }
	 */
	public void setMessageStatusCode(String value) {
		this.messageStatusCode = value;
	}

	/**
	 * Gets the value of the messageCreationDateTime property.
	 * @return possible object is {@link XMLGregorianCalendar }
	 */
	public XMLGregorianCalendar getMessageCreationDateTime() {
		return messageCreationDateTime;
	}

	/**
	 * Sets the value of the messageCreationDateTime property.
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 */
	public void setMessageCreationDateTime(XMLGregorianCalendar value) {
		this.messageCreationDateTime = value;
	}

	/**
	 * Gets the value of the messageUniqueID property.
	 * @return possible object is {@link String }
	 */
	public String getMessageUniqueID() {
		return messageUniqueID;
	}

	/**
	 * Sets the value of the messageUniqueID property.
	 * @param value allowed object is {@link String }
	 */
	public void setMessageUniqueID(String value) {
		this.messageUniqueID = value;
	}

	/**
	 * Gets the value of the messageVersion property.
	 */
	public float getMessageVersion() {
		return messageVersion;
	}

	/**
	 * Sets the value of the messageVersion property.
	 */
	public void setMessageVersion(float value) {
		this.messageVersion = value;
	}

	/**
	 * Gets the value of the exportStartDate property.
	 * @return possible object is {@link XMLGregorianCalendar }
	 */
	public XMLGregorianCalendar getExportStartDate() {
		return exportStartDate;
	}

	/**
	 * Sets the value of the exportStartDate property.
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 */
	public void setExportStartDate(XMLGregorianCalendar value) {
		this.exportStartDate = value;
	}

	/**
	 * Gets the value of the exportEndDate property.
	 * @return possible object is {@link XMLGregorianCalendar }
	 */
	public XMLGregorianCalendar getExportEndDate() {
		return exportEndDate;
	}

	/**
	 * Sets the value of the exportEndDate property.
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 */
	public void setExportEndDate(XMLGregorianCalendar value) {
		this.exportEndDate = value;
	}

	/**
	 * Gets the value of the messageSendingOrganisation property.
	 * @return possible object is {@link MessageSendingOrganisationType }
	 */
	public MessageSendingOrganisationType getMessageSendingOrganisation() {
		return messageSendingOrganisation;
	}

	/**
	 * Sets the value of the messageSendingOrganisation property.
	 * @param value allowed object is {@link MessageSendingOrganisationType }
	 */
	public void setMessageSendingOrganisation(MessageSendingOrganisationType value) {
		this.messageSendingOrganisation = value;
	}

}
