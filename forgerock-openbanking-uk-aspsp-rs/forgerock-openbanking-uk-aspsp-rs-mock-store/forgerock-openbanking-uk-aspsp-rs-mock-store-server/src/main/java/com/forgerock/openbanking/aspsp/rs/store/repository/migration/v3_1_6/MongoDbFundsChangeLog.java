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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.funds.FRFundsConfirmation1;
import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmation;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmationConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.CloseableIterator;

import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.getLegacyDocuments;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.migrate;
import static com.forgerock.openbanking.common.services.openbanking.converter.fund.FRFundsConfirmationConsentConverter.toFRFundsConfirmationConsentData;
import static com.forgerock.openbanking.common.services.openbanking.converter.fund.FRFundsConfirmationConverter.toFRFundsConfirmationData;

@ChangeLog
@Slf4j
public class MongoDbFundsChangeLog {

    @ChangeSet(order = "004", id = "funds-v3.1.2-to-v3.1.6", author = "Matt Wills")
    public void migrateAccountDocuments(MongoTemplate mongoTemplate) {
        log.info("Migrating Confirmation of Funds API data from v3.1.2 to v3.1.6...");

        CloseableIterator<FRFundsConfirmation1> frFundConfirmations = getLegacyDocuments(mongoTemplate, FRFundsConfirmation1.class);
        frFundConfirmations.forEachRemaining(d -> migrate(mongoTemplate, d, toFRFundsConfirmation(d)));

        CloseableIterator<FRFundsConfirmationConsent1> frFundsConfirmationConsents = getLegacyDocuments(mongoTemplate, FRFundsConfirmationConsent1.class);
        frFundsConfirmationConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRFundsConfirmationConsent(d)));

        log.info("Finished migrating Confirmation of Funds API data from v3.1.2 to v3.1.6");
    }

    public static FRFundsConfirmation toFRFundsConfirmation(FRFundsConfirmation1 frFundsConfirmation1) {
        return frFundsConfirmation1 == null ? null : FRFundsConfirmation.builder()
                .id(frFundsConfirmation1.getId())
                .fundsConfirmation(toFRFundsConfirmationData(frFundsConfirmation1.getFundsConfirmation()))
                .fundsAvailable(frFundsConfirmation1.isFundsAvailable())
                .created(frFundsConfirmation1.getCreated())
                .updated(frFundsConfirmation1.getUpdated())
                .obVersion(frFundsConfirmation1.getObVersion())
                .build();
    }

    public static FRFundsConfirmationConsent toFRFundsConfirmationConsent(FRFundsConfirmationConsent1 frFundsConfirmationConsent1) {
        return frFundsConfirmationConsent1 == null ? null : FRFundsConfirmationConsent.builder()
                .id(frFundsConfirmationConsent1.getId())
                .status(toConsentStatusCode(frFundsConfirmationConsent1.getStatus()))
                .fundsConfirmationConsent(toFRFundsConfirmationConsentData(frFundsConfirmationConsent1.getFundsConfirmationConsent()))
                .accountId(frFundsConfirmationConsent1.getAccountId())
                .userId(frFundsConfirmationConsent1.getUserId())
                .pispId(frFundsConfirmationConsent1.getPispId())
                .pispName(frFundsConfirmationConsent1.getPispName())
                .created(frFundsConfirmationConsent1.getCreated())
                .statusUpdate(frFundsConfirmationConsent1.getStatusUpdate())
                .updated(frFundsConfirmationConsent1.getUpdated())
                .obVersion(frFundsConfirmationConsent1.getObVersion())
                .build();
    }

    private static ConsentStatusCode toConsentStatusCode(com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.funds.ConsentStatusCode status) {
        return status == null ? null : ConsentStatusCode.valueOf(status.name());
    }
}
