/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Document complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CstmrPmtStsRpt" type="{urn:iso:std:iso:20022:tech:xsd:pain.002.001.09}CustomerPaymentStatusReportV09"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", propOrder = {
    "cstmrPmtStsRpt"
})
public class Document {

    @XmlElement(name = "CstmrPmtStsRpt", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09", required = true)
    protected CustomerPaymentStatusReportV09 cstmrPmtStsRpt;

    /**
     * Gets the value of the cstmrPmtStsRpt property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerPaymentStatusReportV09 }
     *     
     */
    public CustomerPaymentStatusReportV09 getCstmrPmtStsRpt() {
        return cstmrPmtStsRpt;
    }

    /**
     * Sets the value of the cstmrPmtStsRpt property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerPaymentStatusReportV09 }
     *     
     */
    public void setCstmrPmtStsRpt(CustomerPaymentStatusReportV09 value) {
        this.cstmrPmtStsRpt = value;
    }

}
