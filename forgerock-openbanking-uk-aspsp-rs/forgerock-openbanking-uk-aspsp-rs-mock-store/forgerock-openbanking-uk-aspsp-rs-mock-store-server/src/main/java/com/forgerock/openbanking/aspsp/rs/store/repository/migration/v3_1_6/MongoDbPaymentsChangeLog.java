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

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledPaymentSubmission2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderPaymentSubmission3;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.CloseableIterator;

import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.getLegacyDocuments;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.migrate;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticConsentConverter.toFRDomesticConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticScheduledConsentConverter.toFRDomesticScheduledConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDomesticStandingOrderConsentConverter.toFRDomesticStandingOrderConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRFileConsentConverter.toFRFileConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalConsentConverter.toFRInternationalConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalPaymentSubmissionConverter.toFRInternationalPaymentSubmission4;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalScheduledConsentConverter.toFRInternationalScheduledConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalScheduledPaymentSubmissionConverter.toFRInternationalScheduledPaymentSubmission4;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalStandingOrderConsentConverter.toFRInternationalStandingOrderConsent5;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRInternationalStandingOrderPaymentSubmissionConverter.toFRInternationalStandingOrderPaymentSubmission4;

@ChangeLog
@Slf4j
public class MongoDbPaymentsChangeLog {

    @ChangeSet(order = "001", id = "payments-v3.1.2-to-v3.1.6", author = "Matt Wills")
    public void migratePaymentDocuments(MongoTemplate mongoTemplate) {
        log.info("Migrating payments API data from v3.1.2 to v3.1.6...");

        migrateDomesticPayments(mongoTemplate);
        migrateInternationalPayments(mongoTemplate);

        log.info("Finished migrating payments API data from v3.1.2 to v3.1.6");
    }

    private void migrateDomesticPayments(MongoTemplate mongoTemplate) {
        CloseableIterator<FRDomesticConsent2> frDomesticConsents = getLegacyDocuments(mongoTemplate, FRDomesticConsent2.class);
        frDomesticConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticConsent5(f)));

        CloseableIterator<FRDomesticScheduledConsent2> frDomesticScheduledConsents = getLegacyDocuments(mongoTemplate, FRDomesticScheduledConsent2.class);
        frDomesticScheduledConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticScheduledConsent5(f)));

        CloseableIterator<FRDomesticStandingOrderConsent3> frDomesticStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRDomesticStandingOrderConsent3.class);
        frDomesticStandingOrderConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticStandingOrderConsent5(f)));

        CloseableIterator<FRFileConsent2> frFileConsents = getLegacyDocuments(mongoTemplate, FRFileConsent2.class);
        frFileConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRFileConsent5(f)));
    }

    private void migrateInternationalPayments(MongoTemplate mongoTemplate) {
        CloseableIterator<FRInternationalConsent2> frInternationalConsents = getLegacyDocuments(mongoTemplate, FRInternationalConsent2.class);
        frInternationalConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalConsent5(f)));

        CloseableIterator<FRInternationalPaymentSubmission2> frInternationalPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalPaymentSubmission2.class);
        frInternationalPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalPaymentSubmission4(f)));

        CloseableIterator<FRInternationalScheduledConsent2> frInternationalScheduledConsents = getLegacyDocuments(mongoTemplate, FRInternationalScheduledConsent2.class);
        frInternationalScheduledConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalScheduledConsent5(f)));

        CloseableIterator<FRInternationalScheduledPaymentSubmission2> frInternationalScheduledPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalScheduledPaymentSubmission2.class);
        frInternationalScheduledPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalScheduledPaymentSubmission4(f)));

        CloseableIterator<FRInternationalStandingOrderConsent3> frInternationalStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRInternationalStandingOrderConsent3.class);
        frInternationalStandingOrderConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalStandingOrderConsent5(f)));

        CloseableIterator<FRInternationalStandingOrderPaymentSubmission3> frInternationalStandingOrderPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalStandingOrderPaymentSubmission3.class);
        frInternationalStandingOrderPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalStandingOrderPaymentSubmission4(f)));
    }
}
