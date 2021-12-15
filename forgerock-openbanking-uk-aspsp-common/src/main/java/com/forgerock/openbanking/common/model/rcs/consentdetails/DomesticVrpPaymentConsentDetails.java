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
package com.forgerock.openbanking.common.model.rcs.consentdetails;

import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPControlParameters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models the consent data that is used for
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomesticVrpPaymentConsentDetails extends ConsentDetails {
    protected String decisionApiUri;
    protected String username;
    protected String logo;
    protected String clientId;
    protected String merchantName;
    protected String pispName;
    protected String aspspName;
    protected FRAccountIdentifier debtorAccount;
    protected FRAccountIdentifier creditorAccount;
    protected String paymentReference;
    protected String debtorReference;
    protected FRDomesticVRPControlParameters controlParameters;

    @Override
    public IntentType getIntentType() {
        return IntentType.DOMESTIC_VRP_PAYMENT_CONSENT;
    }
}
