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
 * <p>Java class for RemittanceLocationDetails1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemittanceLocationDetails1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Mtd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}RemittanceLocationMethod2Code"/>
 *         &lt;element name="ElctrncAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}Max2048Text" minOccurs="0"/>
 *         &lt;element name="PstlAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}NameAndAddress10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemittanceLocationDetails1", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", propOrder = {
    "mtd",
    "elctrncAdr",
    "pstlAdr"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class RemittanceLocationDetails1 {

    @XmlElement(name = "Mtd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", required = true)
    @XmlSchemaType(name = "string")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected RemittanceLocationMethod2Code mtd;
    @XmlElement(name = "ElctrncAdr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String elctrncAdr;
    @XmlElement(name = "PstlAdr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected NameAndAddress10 pstlAdr;

    /**
     * Gets the value of the mtd property.
     * 
     * @return
     *     possible object is
     *     {@link RemittanceLocationMethod2Code }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public RemittanceLocationMethod2Code getMtd() {
        return mtd;
    }

    /**
     * Sets the value of the mtd property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemittanceLocationMethod2Code }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setMtd(RemittanceLocationMethod2Code value) {
        this.mtd = value;
    }

    /**
     * Gets the value of the elctrncAdr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getElctrncAdr() {
        return elctrncAdr;
    }

    /**
     * Sets the value of the elctrncAdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setElctrncAdr(String value) {
        this.elctrncAdr = value;
    }

    /**
     * Gets the value of the pstlAdr property.
     * 
     * @return
     *     possible object is
     *     {@link NameAndAddress10 }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public NameAndAddress10 getPstlAdr() {
        return pstlAdr;
    }

    /**
     * Sets the value of the pstlAdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameAndAddress10 }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setPstlAdr(NameAndAddress10 value) {
        this.pstlAdr = value;
    }

}
