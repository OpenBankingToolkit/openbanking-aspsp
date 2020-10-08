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

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Stream;

/**
 * Represents {@link uk.org.openbanking.datamodel.account.OBTransaction6} in the OB data model. It is stored within mongo (instead of the OB object),
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
public class FRTransactionData {

    private String accountId;
    private String transactionId;
    private String transactionReference;
    private List<String> statementReference;
    private FRCreditDebitIndicator creditDebitIndicator;
    private FREntryStatus status;
    private FRTransactionMutability transactionMutability;
    private DateTime bookingDateTime;
    private DateTime valueDateTime;
    private String transactionInformation;
    private String addressLine;
    private FRAmount amount;
    private FRAmount chargeAmount;
    private FRCurrencyExchange currencyExchange;
    private FRBankTransactionCodeStructure bankTransactionCode;
    private FRProprietaryBankTransactionCodeStructure proprietaryBankTransactionCode;
    private FRTransactionCashBalance balance;
    private FRMerchantDetails merchantDetails;
    private FRFinancialAgent creditorAgent;
    private FRAccountIdentifier creditorAccount;
    private FRFinancialAgent debtorAgent;
    private FRAccountIdentifier debtorAccount;
    private FRTransactionCardInstrument cardInstrument;
    private FRSupplementaryData supplementaryData;

    public enum FREntryStatus {
        BOOKED("Booked"),
        PENDING("Pending");

        private String value;

        FREntryStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FREntryStatus fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    public enum FRTransactionMutability {
        MUTABLE("Mutable"),
        IMMUTABLE("Immutable");

        private String value;

        FRTransactionMutability(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRTransactionMutability fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRBankTransactionCodeStructure {
        private String code;
        private String subCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRProprietaryBankTransactionCodeStructure {
        private String code;
        private String issuer;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRTransactionCashBalance {
        private FRAmount amount;
        private FRCreditDebitIndicator creditDebitIndicator;
        private FRBalanceType type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRMerchantDetails {
        private String merchantName;
        private String merchantCategoryCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRTransactionCardInstrument {
        private FRCardScheme cardSchemeName = null;
        private FRCardAuthorisationType authorisationType = null;
        private String name = null;
        private String identification = null;
    }

    public enum FRCardScheme {
        AMERICANEXPRESS("AmericanExpress"),
        DINERS("Diners"),
        DISCOVER("Discover"),
        MASTERCARD("MasterCard"),
        VISA("VISA");

        private String value;

        FRCardScheme(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRCardScheme fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }

    public enum FRCardAuthorisationType {
        CONSUMERDEVICE("ConsumerDevice"),
        CONTACTLESS("Contactless"),
        NONE("None"),
        PIN("PIN");

        private String value;

        FRCardAuthorisationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRCardAuthorisationType fromValue(String value) {
            return Stream.of(values())
                    .filter(type -> type.getValue().equals(value))
                    .findFirst()
                    .orElse(null);
        }
    }
}
