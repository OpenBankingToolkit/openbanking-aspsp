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
package com.forgerock.openbanking.common.model.openbanking.domain.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRAccountServicer;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents {@link uk.org.openbanking.datamodel.account.OBAccount6} in the OB data model. It is stored within mongo (instead of the OB object),
 * in order to make it easier to introduce new versions of the Read/Write API.
 *
 * <p>
 * Note that this object is used across multiple versions of the Read/Write API, meaning that some values won't be populated. For this reason it is
 * a mutable {@link lombok.Data} rather than an immutable {@link lombok.Value} one.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRFinancialAccount {
    @JsonProperty("AccountId") // JSON format required for RCS UI (see AccountWithBalance within ConsentDetails objects)
    private String accountId;
    @JsonProperty("Status")
    private FRAccountStatusCode status;
    @JsonProperty("StatusUpdateDateTime")
    private DateTime statusUpdateDateTime;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("AccountType")
    private FRAccountTypeCode accountType;
    @JsonProperty("AccountSubType")
    private FRAccountSubTypeCode accountSubType;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Nickname")
    private String nickname;
    @JsonProperty("OpeningDate")
    private DateTime openingDate;
    @JsonProperty("MaturityDate")
    private DateTime maturityDate;
    @JsonProperty("Account")
    private List<FRAccountIdentifier> accounts;
    @JsonProperty("Servicer")
    private FRAccountServicer servicer;

    public enum FRAccountStatusCode {
        DELETED("Deleted"),
        DISABLED("Disabled"),
        ENABLED("Enabled"),
        PENDING("Pending"),
        PROFORMA("ProForma");

        private String value;

        FRAccountStatusCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @JsonValue
        public String toString() {
            return value;
        }

        @JsonCreator
        public static FRAccountStatusCode fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    public enum FRAccountTypeCode {
        BUSINESS("Business"),
        PERSONAL("Personal");

        private String value;

        FRAccountTypeCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @JsonValue
        public String toString() {
            return value;
        }

        @JsonCreator
        public static FRAccountTypeCode fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    public enum FRAccountSubTypeCode {
        CHARGECARD("ChargeCard"),
        CREDITCARD("CreditCard"),
        CURRENTACCOUNT("CurrentAccount"),
        EMONEY("EMoney"),
        LOAN("Loan"),
        MORTGAGE("Mortgage"),
        PREPAIDCARD("PrePaidCard"),
        SAVINGS("Savings");

        private String value;

        FRAccountSubTypeCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @JsonValue
        public String toString() {
            return value;
        }

        @JsonCreator
        public static FRAccountSubTypeCode fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

}
