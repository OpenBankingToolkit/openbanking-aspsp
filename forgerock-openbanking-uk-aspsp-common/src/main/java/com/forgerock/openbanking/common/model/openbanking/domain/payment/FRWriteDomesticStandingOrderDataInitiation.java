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
package com.forgerock.openbanking.common.model.openbanking.domain.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRWriteDomesticStandingOrderDataInitiation {

    private String frequency;
    private String reference;
    private String numberOfPayments;
    private DateTime firstPaymentDateTime;
    private DateTime recurringPaymentDateTime;
    private DateTime finalPaymentDateTime;
    private FRAmount firstPaymentAmount;
    private FRAmount recurringPaymentAmount;
    private FRAmount finalPaymentAmount;
    private FRAccount debtorAccount;
    private FRAccount creditorAccount;
    private FRSupplementaryData supplementaryData;
}
