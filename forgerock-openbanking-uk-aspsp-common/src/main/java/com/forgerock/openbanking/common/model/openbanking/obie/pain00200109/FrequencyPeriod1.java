/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


/**
 * <p>Java class for FrequencyPeriod1 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FrequencyPeriod1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}Frequency6Code"/>
 *         &lt;element name="CntPerPrd" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}DecimalNumber"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FrequencyPeriod1", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "tp",
    "cntPerPrd"
})
public class FrequencyPeriod1 {

    @XmlElement(name = "Tp", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    @XmlSchemaType(name = "string")
    protected Frequency6Code tp;
    @XmlElement(name = "CntPerPrd", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected BigDecimal cntPerPrd;

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link Frequency6Code }
     *     
     */
    public Frequency6Code getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frequency6Code }
     *     
     */
    public void setTp(Frequency6Code value) {
        this.tp = value;
    }

    /**
     * Gets the value of the cntPerPrd property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCntPerPrd() {
        return cntPerPrd;
    }

    /**
     * Sets the value of the cntPerPrd property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCntPerPrd(BigDecimal value) {
        this.cntPerPrd = value;
    }

}
