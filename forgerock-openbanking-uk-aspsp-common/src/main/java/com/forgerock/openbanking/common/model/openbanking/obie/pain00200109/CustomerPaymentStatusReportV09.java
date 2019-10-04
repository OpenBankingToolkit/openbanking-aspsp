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
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for CustomerPaymentStatusReportV09 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPaymentStatusReportV09">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GrpHdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}GroupHeader74"/>
 *         &lt;element name="OrgnlGrpInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}OriginalGroupHeader13"/>
 *         &lt;element name="OrgnlPmtInfAndSts" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}OriginalPaymentInstruction27" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SplmtryData" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}SupplementaryData1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPaymentStatusReportV09", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "grpHdr",
    "orgnlGrpInfAndSts",
    "orgnlPmtInfAndSts",
    "splmtryData"
})
public class CustomerPaymentStatusReportV09 {

    @XmlElement(name = "GrpHdr", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected GroupHeader74 grpHdr;
    @XmlElement(name = "OrgnlGrpInfAndSts", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected OriginalGroupHeader13 orgnlGrpInfAndSts;
    @XmlElement(name = "OrgnlPmtInfAndSts", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected List<OriginalPaymentInstruction27> orgnlPmtInfAndSts;
    @XmlElement(name = "SplmtryData", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected List<SupplementaryData1> splmtryData;

    /**
     * Gets the value of the grpHdr property.
     * 
     * @return
     *     possible object is
     *     {@link GroupHeader74 }
     *     
     */
    public GroupHeader74 getGrpHdr() {
        return grpHdr;
    }

    /**
     * Sets the value of the grpHdr property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupHeader74 }
     *     
     */
    public void setGrpHdr(GroupHeader74 value) {
        this.grpHdr = value;
    }

    /**
     * Gets the value of the orgnlGrpInfAndSts property.
     * 
     * @return
     *     possible object is
     *     {@link OriginalGroupHeader13 }
     *     
     */
    public OriginalGroupHeader13 getOrgnlGrpInfAndSts() {
        return orgnlGrpInfAndSts;
    }

    /**
     * Sets the value of the orgnlGrpInfAndSts property.
     * 
     * @param value
     *     allowed object is
     *     {@link OriginalGroupHeader13 }
     *     
     */
    public void setOrgnlGrpInfAndSts(OriginalGroupHeader13 value) {
        this.orgnlGrpInfAndSts = value;
    }

    /**
     * Gets the value of the orgnlPmtInfAndSts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orgnlPmtInfAndSts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrgnlPmtInfAndSts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OriginalPaymentInstruction27 }
     * 
     * 
     */
    public List<OriginalPaymentInstruction27> getOrgnlPmtInfAndSts() {
        if (orgnlPmtInfAndSts == null) {
            orgnlPmtInfAndSts = new ArrayList<OriginalPaymentInstruction27>();
        }
        return this.orgnlPmtInfAndSts;
    }

    /**
     * Gets the value of the splmtryData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the splmtryData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSplmtryData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupplementaryData1 }
     * 
     * 
     */
    public List<SupplementaryData1> getSplmtryData() {
        if (splmtryData == null) {
            splmtryData = new ArrayList<SupplementaryData1>();
        }
        return this.splmtryData;
    }

}
