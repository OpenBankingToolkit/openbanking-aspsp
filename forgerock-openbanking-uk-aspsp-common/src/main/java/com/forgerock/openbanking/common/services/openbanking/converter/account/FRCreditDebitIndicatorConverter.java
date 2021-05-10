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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRCreditDebitIndicator;
import uk.org.openbanking.datamodel.account.*;

public class FRCreditDebitIndicatorConverter {

    // FR to OB
    public static OBCreditDebitCode toOBCreditDebitCode(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBCreditDebitCode.valueOf(indicator.name());
    }

    public static OBCreditDebitCode1 toOBCreditDebitCode1(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBCreditDebitCode1.valueOf(indicator.name());
    }

    public static OBCreditDebitCode2 toOBCreditDebitCode2(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBCreditDebitCode2.valueOf(indicator.name());
    }

    public static OBTransaction5.CreditDebitIndicatorEnum toOBTransaction5CreditDebitIndicatorEnum(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBTransaction5.CreditDebitIndicatorEnum.valueOf(indicator.name());
    }

    public static OBStatementFee2.CreditDebitIndicatorEnum toOBStatementFee2CreditDebitIndicatorEnum(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBStatementFee2.CreditDebitIndicatorEnum.valueOf(indicator.name());
    }

    public static OBStatementInterest2.CreditDebitIndicatorEnum toOBStatementInterest2CreditDebitIndicatorEnum(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBStatementInterest2.CreditDebitIndicatorEnum.valueOf(indicator.name());
    }

    public static OBCreditDebitCode0 toOBCreditDebitCode0(FRCreditDebitIndicator indicator) {
        return indicator == null ? null : OBCreditDebitCode0.valueOf(indicator.name());
    }

    // OB to FR
    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBCreditDebitCode indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBCreditDebitCode1 indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBCreditDebitCode2 indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBStatementFee2.CreditDebitIndicatorEnum indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBStatementInterest2.CreditDebitIndicatorEnum indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBTransaction5.CreditDebitIndicatorEnum indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }

    public static FRCreditDebitIndicator toFRCreditDebitIndicator(OBCreditDebitCode0 indicator) {
        return indicator == null ? null : FRCreditDebitIndicator.valueOf(indicator.name());
    }
}
