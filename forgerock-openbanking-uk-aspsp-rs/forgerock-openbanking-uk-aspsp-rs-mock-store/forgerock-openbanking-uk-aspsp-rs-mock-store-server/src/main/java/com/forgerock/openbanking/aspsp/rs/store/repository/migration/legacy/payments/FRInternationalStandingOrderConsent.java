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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.version.OBVersion;
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

import java.util.Date;
import java.util.List;

@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FRInternationalStandingOrderConsent implements LegacyCountrySubDivision {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public FRWriteInternationalStandingOrderConsent internationalStandingOrderConsent;

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

    public OBVersion version;

    @Override
    public String getDocumentId() {
        return this.id;
    }

    @Override
    public String getCountrySubDivision() {
        if(this.internationalStandingOrderConsent.getRisk()!=null){
            if(this.internationalStandingOrderConsent.getRisk().getDeliveryAddress()!=null){
                List<String> countrySubDivision = this.internationalStandingOrderConsent.getRisk().getDeliveryAddress().getCountrySubDivision();
                if(countrySubDivision!=null && !countrySubDivision.isEmpty()){
                    return countrySubDivision.get(0);
                }
            }
        }
        return null;
    }
}
