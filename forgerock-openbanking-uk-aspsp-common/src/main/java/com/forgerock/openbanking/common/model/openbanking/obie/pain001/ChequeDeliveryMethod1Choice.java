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
 * <p>Java class for ChequeDeliveryMethod1Choice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChequeDeliveryMethod1Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Cd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}ChequeDelivery1Code"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}Max35Text"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChequeDeliveryMethod1Choice", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", propOrder = {
    "cd",
    "prtry"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class ChequeDeliveryMethod1Choice {

    @XmlElement(name = "Cd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08")
    @XmlSchemaType(name = "string")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected ChequeDelivery1Code cd;
    @XmlElement(name = "Prtry", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String prtry;

    /**
     * Gets the value of the cd property.
     * 
     * @return
     *     possible object is
     *     {@link ChequeDelivery1Code }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public ChequeDelivery1Code getCd() {
        return cd;
    }

    /**
     * Sets the value of the cd property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChequeDelivery1Code }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setCd(ChequeDelivery1Code value) {
        this.cd = value;
    }

    /**
     * Gets the value of the prtry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getPrtry() {
        return prtry;
    }

    /**
     * Sets the value of the prtry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setPrtry(String value) {
        this.prtry = value;
    }

}
