/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for DateAndDateTime2Choice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DateAndDateTime2Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}ISODate"/>
 *         &lt;element name="DtTm" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}ISODateTime"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DateAndDateTime2Choice", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "dt",
    "dtTm"
})
public class DateAndDateTime2Choice {

    @XmlElement(name = "Dt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dt;
    @XmlElement(name = "DtTm", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dtTm;

    /**
     * Gets the value of the dt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDt() {
        return dt;
    }

    /**
     * Sets the value of the dt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDt(XMLGregorianCalendar value) {
        this.dt = value;
    }

    /**
     * Gets the value of the dtTm property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDtTm() {
        return dtTm;
    }

    /**
     * Sets the value of the dtTm property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDtTm(XMLGregorianCalendar value) {
        this.dtTm = value;
    }

}
