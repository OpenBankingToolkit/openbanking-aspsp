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


/**
 * <p>Java class for Party35Choice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Party35Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Pty" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}PartyIdentification125"/>
 *         &lt;element name="Agt" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}BranchAndFinancialInstitutionIdentification5"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party35Choice", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "pty",
    "agt"
})
public class Party35Choice {

    @XmlElement(name = "Pty", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected PartyIdentification125 pty;
    @XmlElement(name = "Agt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
    protected BranchAndFinancialInstitutionIdentification5 agt;

    /**
     * Gets the value of the pty property.
     * 
     * @return
     *     possible object is
     *     {@link PartyIdentification125 }
     *     
     */
    public PartyIdentification125 getPty() {
        return pty;
    }

    /**
     * Sets the value of the pty property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartyIdentification125 }
     *     
     */
    public void setPty(PartyIdentification125 value) {
        this.pty = value;
    }

    /**
     * Gets the value of the agt property.
     * 
     * @return
     *     possible object is
     *     {@link BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public BranchAndFinancialInstitutionIdentification5 getAgt() {
        return agt;
    }

    /**
     * Sets the value of the agt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setAgt(BranchAndFinancialInstitutionIdentification5 value) {
        this.agt = value;
    }

}
