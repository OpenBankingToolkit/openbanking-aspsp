/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for CreditorReferenceType1Choice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreditorReferenceType1Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Cd" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}DocumentType3Code"/>
 *         &lt;element name="Prtry" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}Max35Text"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditorReferenceType1Choice", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "cd",
    "prtry"
})
public class CreditorReferenceType1Choice {

    @XmlElement(name = "Cd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    @XmlSchemaType(name = "string")
    protected DocumentType3Code cd;
    @XmlElement(name = "Prtry", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected String prtry;

    /**
     * Gets the value of the cd property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentType3Code }
     *     
     */
    public DocumentType3Code getCd() {
        return cd;
    }

    /**
     * Sets the value of the cd property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentType3Code }
     *     
     */
    public void setCd(DocumentType3Code value) {
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
    public void setPrtry(String value) {
        this.prtry = value;
    }

}
