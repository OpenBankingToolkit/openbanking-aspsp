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
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.account.OBStandingOrder6;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.util.List;

/**
 * Models the consent data that is used for
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomesticStandingOrderPaymentConsentDetails extends ConsentDetails {

    private OBStandingOrder6 standingOrder;
    
    private OBActiveOrHistoricCurrencyAndAmount instructedAmount; 
    
    protected String decisionApiUri;

    protected List<FRAccountWithBalance> accounts;
    protected String username;
    protected String logo;
    protected String clientId;
    protected String merchantName;
    protected DateTime expiredDate;
    protected String paymentReference;

    @Override
    public IntentType getIntentType() {
        return IntentType.PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT;
    }
}
