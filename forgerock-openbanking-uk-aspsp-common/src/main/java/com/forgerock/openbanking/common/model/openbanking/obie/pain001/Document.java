/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain001;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CstmrCdtTrfInitn" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}CustomerCreditTransferInitiationV08"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", propOrder = {
    "cstmrCdtTrfInitn"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class Document {

    @XmlElement(name = "CstmrCdtTrfInitn", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected CustomerCreditTransferInitiationV08 cstmrCdtTrfInitn;

    /**
     * Gets the value of the cstmrCdtTrfInitn property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerCreditTransferInitiationV08 }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public CustomerCreditTransferInitiationV08 getCstmrCdtTrfInitn() {
        return cstmrCdtTrfInitn;
    }

    /**
     * Sets the value of the cstmrCdtTrfInitn property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerCreditTransferInitiationV08 }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setCstmrCdtTrfInitn(CustomerCreditTransferInitiationV08 value) {
        this.cstmrCdtTrfInitn = value;
    }

}
