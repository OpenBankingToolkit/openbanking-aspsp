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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent4Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent5Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent5Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent5Data;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent6Data;

public class FRReadRefundAccountConverter {

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteDomesticConsent4Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteDomesticScheduledConsent4Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteDomesticStandingOrderConsent5Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteInternationalConsent5Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteInternationalScheduledConsent5Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }

    public static FRReadRefundAccount toFRReadRefundAccount(OBWriteInternationalStandingOrderConsent6Data.ReadRefundAccountEnum readRefundAccount) {
        return readRefundAccount == null ? null : FRReadRefundAccount.valueOf(readRefundAccount.name());
    }
}