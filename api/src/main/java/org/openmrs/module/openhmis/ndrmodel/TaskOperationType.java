//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.06.30 at 11:28:50 AM WAT 
//

package org.openmrs.module.openhmis.ndrmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TaskOperationType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaskOperationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ReturnOperation" type="{}ReturnOperationType"/>
 *         &lt;element name="AdjustmentOperation" type="{}AdjustmentOperationType"/>
 *         &lt;element name="DisposedOperation" type="{}DisposedOperationType"/>
 *         &lt;element name="DistributionOperation" type="{}DistributionOperationType"/>
 *         &lt;element name="ReceiptOperation" type="{}ReceiptOperationType"/>
 *         &lt;element name="TransferOperation" type="{}TransferOperationType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaskOperationType", propOrder = {
        "returnOperation",
        "adjustmentOperation",
        "disposedOperation",
        "distributionOperation",
        "receiptOperation",
        "transferOperation"
})
public class TaskOperationType {

	@XmlElement(name = "ReturnOperation", required = true)
	protected ReturnOperationType returnOperation;
	@XmlElement(name = "AdjustmentOperation", required = true)
	protected AdjustmentOperationType adjustmentOperation;
	@XmlElement(name = "DisposedOperation", required = true)
	protected DisposedOperationType disposedOperation;
	@XmlElement(name = "DistributionOperation", required = true)
	protected DistributionOperationType distributionOperation;
	@XmlElement(name = "ReceiptOperation", required = true)
	protected ReceiptOperationType receiptOperation;
	@XmlElement(name = "TransferOperation", required = true)
	protected TransferOperationType transferOperation;

	/**
	 * Gets the value of the returnOperation property.
	 * @return possible object is {@link ReturnOperationType }
	 */
	public ReturnOperationType getReturnOperation() {
		return returnOperation;
	}

	/**
	 * Sets the value of the returnOperation property.
	 * @param value allowed object is {@link ReturnOperationType }
	 */
	public void setReturnOperation(ReturnOperationType value) {
		this.returnOperation = value;
	}

	/**
	 * Gets the value of the adjustmentOperation property.
	 * @return possible object is {@link AdjustmentOperationType }
	 */
	public AdjustmentOperationType getAdjustmentOperation() {
		return adjustmentOperation;
	}

	/**
	 * Sets the value of the adjustmentOperation property.
	 * @param value allowed object is {@link AdjustmentOperationType }
	 */
	public void setAdjustmentOperation(AdjustmentOperationType value) {
		this.adjustmentOperation = value;
	}

	/**
	 * Gets the value of the disposedOperation property.
	 * @return possible object is {@link DisposedOperationType }
	 */
	public DisposedOperationType getDisposedOperation() {
		return disposedOperation;
	}

	/**
	 * Sets the value of the disposedOperation property.
	 * @param value allowed object is {@link DisposedOperationType }
	 */
	public void setDisposedOperation(DisposedOperationType value) {
		this.disposedOperation = value;
	}

	/**
	 * Gets the value of the distributionOperation property.
	 * @return possible object is {@link DistributionOperationType }
	 */
	public DistributionOperationType getDistributionOperation() {
		return distributionOperation;
	}

	/**
	 * Sets the value of the distributionOperation property.
	 * @param value allowed object is {@link DistributionOperationType }
	 */
	public void setDistributionOperation(DistributionOperationType value) {
		this.distributionOperation = value;
	}

	/**
	 * Gets the value of the receiptOperation property.
	 * @return possible object is {@link ReceiptOperationType }
	 */
	public ReceiptOperationType getReceiptOperation() {
		return receiptOperation;
	}

	/**
	 * Sets the value of the receiptOperation property.
	 * @param value allowed object is {@link ReceiptOperationType }
	 */
	public void setReceiptOperation(ReceiptOperationType value) {
		this.receiptOperation = value;
	}

	/**
	 * Gets the value of the transferOperation property.
	 * @return possible object is {@link TransferOperationType }
	 */
	public TransferOperationType getTransferOperation() {
		return transferOperation;
	}

	/**
	 * Sets the value of the transferOperation property.
	 * @param value allowed object is {@link TransferOperationType }
	 */
	public void setTransferOperation(TransferOperationType value) {
		this.transferOperation = value;
	}

}
