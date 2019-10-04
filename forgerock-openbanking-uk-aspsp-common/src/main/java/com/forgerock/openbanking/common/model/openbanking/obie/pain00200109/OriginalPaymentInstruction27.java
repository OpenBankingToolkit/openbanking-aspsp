/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for OriginalPaymentInstruction27 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OriginalPaymentInstruction27">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrgnlPmtInfId" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}Max35Text"/>
 *         &lt;element name="OrgnlNbOfTxs" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}Max15NumericText" minOccurs="0"/>
 *         &lt;element name="OrgnlCtrlSum" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}DecimalNumber" minOccurs="0"/>
 *         &lt;element name="PmtInfSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}ExternalPaymentGroupStatus1Code" minOccurs="0"/>
 *         &lt;element name="StsRsnInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}StatusReasonInformation11" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="NbOfTxsPerSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}NumberOfTransactionsPerStatus5" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TxInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}PaymentTransaction92" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OriginalPaymentInstruction27", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "orgnlPmtInfId",
    "orgnlNbOfTxs",
    "orgnlCtrlSum",
    "pmtInfSts",
    "stsRsnInf",
    "nbOfTxsPerSts",
    "txInfAndSts"
})
public class OriginalPaymentInstruction27 {

    @XmlElement(name = "OrgnlPmtInfId", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected String orgnlPmtInfId;
    @XmlElement(name = "OrgnlNbOfTxs", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected String orgnlNbOfTxs;
    @XmlElement(name = "OrgnlCtrlSum", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected BigDecimal orgnlCtrlSum;
    @XmlElement(name = "PmtInfSts", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected String pmtInfSts;
    @XmlElement(name = "StsRsnInf", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected List<StatusReasonInformation11> stsRsnInf;
    @XmlElement(name = "NbOfTxsPerSts", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected List<NumberOfTransactionsPerStatus5> nbOfTxsPerSts;
    @XmlElement(name = "TxInfAndSts", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected List<PaymentTransaction92> txInfAndSts;

    /**
     * Gets the value of the orgnlPmtInfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgnlPmtInfId() {
        return orgnlPmtInfId;
    }

    /**
     * Sets the value of the orgnlPmtInfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgnlPmtInfId(String value) {
        this.orgnlPmtInfId = value;
    }

    /**
     * Gets the value of the orgnlNbOfTxs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgnlNbOfTxs() {
        return orgnlNbOfTxs;
    }

    /**
     * Sets the value of the orgnlNbOfTxs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgnlNbOfTxs(String value) {
        this.orgnlNbOfTxs = value;
    }

    /**
     * Gets the value of the orgnlCtrlSum property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrgnlCtrlSum() {
        return orgnlCtrlSum;
    }

    /**
     * Sets the value of the orgnlCtrlSum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrgnlCtrlSum(BigDecimal value) {
        this.orgnlCtrlSum = value;
    }

    /**
     * Gets the value of the pmtInfSts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPmtInfSts() {
        return pmtInfSts;
    }

    /**
     * Sets the value of the pmtInfSts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPmtInfSts(String value) {
        this.pmtInfSts = value;
    }

    /**
     * Gets the value of the stsRsnInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stsRsnInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStsRsnInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StatusReasonInformation11 }
     * 
     * 
     */
    public List<StatusReasonInformation11> getStsRsnInf() {
        if (stsRsnInf == null) {
            stsRsnInf = new ArrayList<StatusReasonInformation11>();
        }
        return this.stsRsnInf;
    }

    /**
     * Gets the value of the nbOfTxsPerSts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nbOfTxsPerSts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNbOfTxsPerSts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NumberOfTransactionsPerStatus5 }
     * 
     * 
     */
    public List<NumberOfTransactionsPerStatus5> getNbOfTxsPerSts() {
        if (nbOfTxsPerSts == null) {
            nbOfTxsPerSts = new ArrayList<NumberOfTransactionsPerStatus5>();
        }
        return this.nbOfTxsPerSts;
    }

    /**
     * Gets the value of the txInfAndSts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the txInfAndSts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTxInfAndSts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PaymentTransaction92 }
     * 
     * 
     */
    public List<PaymentTransaction92> getTxInfAndSts() {
        if (txInfAndSts == null) {
            txInfAndSts = new ArrayList<PaymentTransaction92>();
        }
        return this.txInfAndSts;
    }

}
