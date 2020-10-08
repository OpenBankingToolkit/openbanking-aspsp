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

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents {@link uk.org.openbanking.datamodel.account.OBStatement2} in the OB data model. It is stored within mongo (instead of the OB object),
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
public class FRStatementData {

    private String accountId;
    private String statementId;
    private String statementReference;
    private FRStatementType type;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private DateTime creationDateTime;
    private List<String> statementDescription;
    private List<FRStatementBenefit> statementBenefit;
    private List<FRStatementFee> statementFee;
    private List<FRStatementInterest> statementInterest;
    private List<FRStatementDateTime> statementDateTime;
    private List<FRStatementRate> statementRate;
    private List<FRStatementValue> statementValue;
    private List<FRStatementAmount> statementAmount;

    public enum FRStatementType {
        ACCOUNTCLOSURE("AccountClosure"),
        ACCOUNTOPENING("AccountOpening"),
        ANNUAL("Annual"),
        INTERIM("Interim"),
        REGULARPERIODIC("RegularPeriodic");

        private String value;

        FRStatementType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return value;
        }

        public static FRStatementType fromValue(String value) {
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
    public static class FRStatementBenefit {
        private String type;
        private FRAmount amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementFee {
        private String description;
        private FRCreditDebitIndicator creditDebitIndicator;
        private String type;
        private BigDecimal rate;
        private String rateType;
        private String frequency;
        private FRAmount amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementInterest {
        private String description;
        private FRCreditDebitIndicator creditDebitIndicator;
        private String type;
        private BigDecimal rate;
        private String rateType;
        private String frequency;
        private FRAmount amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementDateTime {
        private DateTime dateTime;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementRate {
        private String rate;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementValue {
        private Integer value;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRStatementAmount {
        private FRCreditDebitIndicator creditDebitIndicator;
        private String type;
        private FRAmount amount;
    }
}
