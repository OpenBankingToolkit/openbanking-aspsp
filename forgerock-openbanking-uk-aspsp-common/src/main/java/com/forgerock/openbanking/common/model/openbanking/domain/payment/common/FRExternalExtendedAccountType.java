/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.common.model.openbanking.domain.payment.common;

/**
 * Represents OBExternalExtendedAccountType1Code
 */
public enum FRExternalExtendedAccountType {

    BUSINESS("Business"),

    BUSINESSSAVINGSACCOUNT("BusinessSavingsAccount"),

    CHARITY("Charity"),

    COLLECTION("Collection"),

    CORPORATE("Corporate"),

    EWALLET("Ewallet"),

    GOVERNMENT("Government"),

    INVESTMENT("Investment"),

    ISA("ISA"),

    JOINTPERSONAL("JointPersonal"),

    PENSION("Pension"),

    PERSONAL("Personal"),

    PERSONALSAVINGSACCOUNT("PersonalSavingsAccount"),

    PREMIER("Premier"),

    WEALTH("Wealth");

    private String value;

    FRExternalExtendedAccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static FRExternalExtendedAccountType fromValue(String value) {
        for (FRExternalExtendedAccountType b : FRExternalExtendedAccountType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
