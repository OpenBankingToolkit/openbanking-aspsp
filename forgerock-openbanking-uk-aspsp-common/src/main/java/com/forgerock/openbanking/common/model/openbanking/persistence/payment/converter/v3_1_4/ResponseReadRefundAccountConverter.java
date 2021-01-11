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

package com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_4;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import uk.org.openbanking.datamodel.payment.*;

public class ResponseReadRefundAccountConverter {
    public static OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum toOBWriteDomesticConsentResponse4DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteDomesticScheduledConsentResponse4Data.ReadRefundAccountEnum toOBWriteDomesticScheduledConsentResponse4DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticScheduledConsentResponse4Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticScheduledConsentResponse4Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteDomesticStandingOrderConsentResponse5Data.ReadRefundAccountEnum toOBWriteDomesticStandingOrderConsentResponse5DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticStandingOrderConsentResponse5Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticStandingOrderConsentResponse5Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalConsentResponse5Data.ReadRefundAccountEnum toOBWriteInternationalConsentResponse5DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalConsentResponse5Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalConsentResponse5Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalScheduledConsentResponse5Data.ReadRefundAccountEnum toOBWriteInternationalScheduledConsentResponse5DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalScheduledConsentResponse5Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalScheduledConsentResponse5Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalStandingOrderConsentResponse6Data.ReadRefundAccountEnum toOBWriteInternationalStandingOrderConsentResponse6DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalStandingOrderConsentResponse6Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalStandingOrderConsentResponse6Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

}
