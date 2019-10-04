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
 * <p>Java class for Garnishment2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Garnishment2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}GarnishmentType1"/>
 *         &lt;element name="Grnshee" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}PartyIdentification125" minOccurs="0"/>
 *         &lt;element name="GrnshmtAdmstr" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}PartyIdentification125" minOccurs="0"/>
 *         &lt;element name="RefNb" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}Max140Text" minOccurs="0"/>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}ISODate" minOccurs="0"/>
 *         &lt;element name="RmtdAmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}ActiveOrHistoricCurrencyAndAmount" minOccurs="0"/>
 *         &lt;element name="FmlyMdclInsrncInd" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="MplyeeTermntnInd" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}TrueFalseIndicator" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Garnishment2", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "tp",
    "grnshee",
    "grnshmtAdmstr",
    "refNb",
    "dt",
    "rmtdAmt",
    "fmlyMdclInsrncInd",
    "mplyeeTermntnInd"
})
public class Garnishment2 {

    @XmlElement(name = "Tp", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected GarnishmentType1 tp;
    @XmlElement(name = "Grnshee", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected PartyIdentification125 grnshee;
    @XmlElement(name = "GrnshmtAdmstr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected PartyIdentification125 grnshmtAdmstr;
    @XmlElement(name = "RefNb", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected String refNb;
    @XmlElement(name = "Dt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dt;
    @XmlElement(name = "RmtdAmt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected ActiveOrHistoricCurrencyAndAmount rmtdAmt;
    @XmlElement(name = "FmlyMdclInsrncInd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected Boolean fmlyMdclInsrncInd;
    @XmlElement(name = "MplyeeTermntnInd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected Boolean mplyeeTermntnInd;

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link GarnishmentType1 }
     *     
     */
    public GarnishmentType1 getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link GarnishmentType1 }
     *     
     */
    public void setTp(GarnishmentType1 value) {
        this.tp = value;
    }

    /**
     * Gets the value of the grnshee property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification125 }
     *     
     */
    public PartyIdentification125 getGrnshee() {
        return grnshee;
    }

    /**
     * Sets the value of the grnshee property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification125 }
     *     
     */
    public void setGrnshee(PartyIdentification125 value) {
        this.grnshee = value;
    }

    /**
     * Gets the value of the grnshmtAdmstr property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification125 }
     *     
     */
    public PartyIdentification125 getGrnshmtAdmstr() {
        return grnshmtAdmstr;
    }

    /**
     * Sets the value of the grnshmtAdmstr property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification125 }
     *     
     */
    public void setGrnshmtAdmstr(PartyIdentification125 value) {
        this.grnshmtAdmstr = value;
    }

    /**
     * Gets the value of the refNb property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefNb() {
        return refNb;
    }

    /**
     * Sets the value of the refNb property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefNb(String value) {
        this.refNb = value;
    }

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
     * Gets the value of the rmtdAmt property.
     * 
     * @return
     *     possible object is
     *     {@link ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public ActiveOrHistoricCurrencyAndAmount getRmtdAmt() {
        return rmtdAmt;
    }

    /**
     * Sets the value of the rmtdAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setRmtdAmt(ActiveOrHistoricCurrencyAndAmount value) {
        this.rmtdAmt = value;
    }

    /**
     * Gets the value of the fmlyMdclInsrncInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFmlyMdclInsrncInd() {
        return fmlyMdclInsrncInd;
    }

    /**
     * Sets the value of the fmlyMdclInsrncInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFmlyMdclInsrncInd(Boolean value) {
        this.fmlyMdclInsrncInd = value;
    }

    /**
     * Gets the value of the mplyeeTermntnInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMplyeeTermntnInd() {
        return mplyeeTermntnInd;
    }

    /**
     * Sets the value of the mplyeeTermntnInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMplyeeTermntnInd(Boolean value) {
        this.mplyeeTermntnInd = value;
    }

}
