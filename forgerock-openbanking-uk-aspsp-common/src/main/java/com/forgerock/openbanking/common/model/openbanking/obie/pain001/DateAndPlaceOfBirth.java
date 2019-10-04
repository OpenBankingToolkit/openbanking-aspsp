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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DateAndPlaceOfBirth complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DateAndPlaceOfBirth">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BirthDt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}ISODate"/>
 *         &lt;element name="PrvcOfBirth" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}Max35Text" minOccurs="0"/>
 *         &lt;element name="CityOfBirth" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}Max35Text"/>
 *         &lt;element name="CtryOfBirth" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.08}CountryCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateAndPlaceOfBirth", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", propOrder = {
    "birthDt",
    "prvcOfBirth",
    "cityOfBirth",
    "ctryOfBirth"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
public class DateAndPlaceOfBirth {

    @XmlElement(name = "BirthDt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", required = true)
    @XmlSchemaType(name = "date")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected XMLGregorianCalendar birthDt;
    @XmlElement(name = "PrvcOfBirth", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String prvcOfBirth;
    @XmlElement(name = "CityOfBirth", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String cityOfBirth;
    @XmlElement(name = "CtryOfBirth", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.001.001.08", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    protected String ctryOfBirth;

    /**
     * Gets the value of the birthDt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public XMLGregorianCalendar getBirthDt() {
        return birthDt;
    }

    /**
     * Sets the value of the birthDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setBirthDt(XMLGregorianCalendar value) {
        this.birthDt = value;
    }

    /**
     * Gets the value of the prvcOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getPrvcOfBirth() {
        return prvcOfBirth;
    }

    /**
     * Sets the value of the prvcOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setPrvcOfBirth(String value) {
        this.prvcOfBirth = value;
    }

    /**
     * Gets the value of the cityOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getCityOfBirth() {
        return cityOfBirth;
    }

    /**
     * Sets the value of the cityOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setCityOfBirth(String value) {
        this.cityOfBirth = value;
    }

    /**
     * Gets the value of the ctryOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public String getCtryOfBirth() {
        return ctryOfBirth;
    }

    /**
     * Sets the value of the ctryOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2019-01-07T09:44:17+00:00", comments = "JAXB RI v2.2.8-b130911.1802")
    public void setCtryOfBirth(String value) {
        this.ctryOfBirth = value;
    }

}
