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

package com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_5;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import uk.org.openbanking.datamodel.payment.*;

public class ResponseReadRefundAccountConverter {
    public static OBWriteDomesticConsentResponse5Data.ReadRefundAccountEnum toOBWriteDomesticConsentResponse5DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticConsentResponse5Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticConsentResponse5Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteDomesticScheduledConsentResponse5Data.ReadRefundAccountEnum toOBWriteDomesticScheduledConsentResponse5DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticScheduledConsentResponse5Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticScheduledConsentResponse5Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteDomesticStandingOrderConsentResponse6Data.ReadRefundAccountEnum toOBWriteDomesticStandingOrderConsentResponse6DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteDomesticStandingOrderConsentResponse6Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteDomesticStandingOrderConsentResponse6Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalConsentResponse6Data.ReadRefundAccountEnum toOBWriteInternationalConsentResponse6DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalConsentResponse6Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalConsentResponse6Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalScheduledConsentResponse6Data.ReadRefundAccountEnum toOBWriteInternationalScheduledConsentResponse6DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalScheduledConsentResponse6Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalScheduledConsentResponse6Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }

    public static OBWriteInternationalStandingOrderConsentResponse7Data.ReadRefundAccountEnum toOBWriteInternationalStandingOrderConsentResponse7DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount) {
        if (frReadRefundAccount != null) {
            switch (frReadRefundAccount) {
                case YES:
                    return OBWriteInternationalStandingOrderConsentResponse7Data.ReadRefundAccountEnum.YES;
                default:
                    return OBWriteInternationalStandingOrderConsentResponse7Data.ReadRefundAccountEnum.NO;
            }
        }
        return null;
    }
}
