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
package com.forgerock.openbanking.common.model.openbanking.data.payment.common;

import lombok.Builder;
import lombok.Data;

import java.util.stream.Stream;

@Data
@Builder
public class FRDataSCASupportData {

    private FRRequestedSCAExemptionType requestedSCAExemptionType;
    private FRAppliedAuthenticationApproach appliedAuthenticationApproach;
    private String referencePaymentOrderId;

    public enum FRRequestedSCAExemptionType {
        BILLPAYMENT("BillPayment"),
        CONTACTLESSTRAVEL("ContactlessTravel"),
        ECOMMERCEGOODS("EcommerceGoods"),
        ECOMMERCESERVICES("EcommerceServices"),
        KIOSK("Kiosk"),
        PARKING("Parking"),
        PARTYTOPARTY("PartyToParty");

        private String value;

        FRRequestedSCAExemptionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRRequestedSCAExemptionType fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    public enum FRAppliedAuthenticationApproach {
        CA("CA"),
        SCA("SCA");

        private String value;

        FRAppliedAuthenticationApproach(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRAppliedAuthenticationApproach fromValue(String value) {
            return Stream.of(values())
                    .filter(approach -> approach.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }
}
