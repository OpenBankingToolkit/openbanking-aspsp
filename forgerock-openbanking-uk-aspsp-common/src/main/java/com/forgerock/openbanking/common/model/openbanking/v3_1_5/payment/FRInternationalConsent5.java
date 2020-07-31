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
package com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.currency.CurrencyRateService;
import com.forgerock.openbanking.model.Tpp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent5;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse4DataExchangeRateInformation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsentResponse6DataExchangeRateInformation;

import java.util.Date;

import static com.forgerock.openbanking.common.services.openbanking.converter.OBWriteInternationalExchangeRateInformationConverter.toOBWriteInternationalConsentResponse6DataExchangeRateInformation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FRInternationalConsent5 implements FRPaymentConsent, Persistable<String> {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBWriteInternationalConsent5 internationalConsent;

    @Indexed
    public String accountId;
    @Indexed
    public String userId;
    @Indexed
    public String pispId;
    public String pispName;
    public String idempotencyKey;

    @CreatedDate
    public DateTime created;
    public DateTime statusUpdate;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    /**
     * Note: we do not persist the calculated exchange rate fields (such as rate value and expiry date) as the exchange rate object in the initiation must match exactly what the user submitted. We could persist the
     * calculated exchange rate separately but currently it is easier just to generate dynamically as the rate it always the same value for testing purposes.
     *
     * @return OBExchangeRate2 with rate nd expiry date fields populated where appropriate
     */
    public OBWriteInternationalConsentResponse6DataExchangeRateInformation getCalculatedExchangeRate() {
        OBWriteInternationalConsentResponse4DataExchangeRateInformation exchangeRate = CurrencyRateService.getCalculatedExchangeRate(internationalConsent.getData().getInitiation().getExchangeRateInformation(), created);
        return toOBWriteInternationalConsentResponse6DataExchangeRateInformation(exchangeRate);
    }

    @Override
    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    @Override
    public OBWriteInternational3DataInitiation getInitiation() {
        return internationalConsent.getData().getInitiation();
    }

    @Override
    public OBRisk1 getRisk() {
        return internationalConsent.getRisk();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }
}
