/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.forgerock.openbanking.common.model.openbanking.obie.pain00200109;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressType2Code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AddressType2Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADDR"/>
 *     &lt;enumeration value="PBOX"/>
 *     &lt;enumeration value="HOME"/>
 *     &lt;enumeration value="BIZZ"/>
 *     &lt;enumeration value="MLTO"/>
 *     &lt;enumeration value="DLVY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AddressType2Code", namespace = "urn:iso:std:iso:20022:tech:xsd:pain.002.001.09")
@XmlEnum
public enum AddressType2Code {

    ADDR,
    PBOX,
    HOME,
    BIZZ,
    MLTO,
    DLVY;

    public String value() {
        return name();
    }

    public static AddressType2Code fromValue(String v) {
        return valueOf(v);
    }

}
