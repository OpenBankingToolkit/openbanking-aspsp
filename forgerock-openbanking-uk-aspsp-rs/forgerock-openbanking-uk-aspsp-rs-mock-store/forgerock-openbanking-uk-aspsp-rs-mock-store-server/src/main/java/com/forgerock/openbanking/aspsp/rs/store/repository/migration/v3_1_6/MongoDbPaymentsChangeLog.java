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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.*;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.CloseableIterator;

import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.getLegacyDocuments;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.migrate;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticConsentMigrator.toFRDomesticConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticPaymentSubmissionMigrator.toFRDomesticPaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticScheduledConsentMigrator.toFRDomesticScheduledConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticScheduledPaymentSubmissionMigrator.toFRDomesticScheduledPaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticStandingOrderConsentMigrator.toFRDomesticStandingOrderConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRDomesticStandingOrderPaymentSubmissionConverter.toFRDomesticStandingOrderPaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRFileConsentMigrator.toFRFileConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRFilePaymentSubmissionMigrator.toFRFilePaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalConsentMigrator.toFRInternationalConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalPaymentSubmissionMigrator.toFRInternationalPaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalScheduledConsentMigrator.toFRInternationalScheduledConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalScheduledPaymentSubmissionMigrator.toFRInternationalScheduledPaymentSubmission;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalStandingOrderConsentMigrator.toFRInternationalStandingOrderConsent;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.payments.FRInternationalStandingOrderPaymentSubmissionMigrator.toFRInternationalStandingOrderPaymentSubmission;

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
        frDomesticConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticConsent(f)));

        CloseableIterator<FRDomesticPaymentSubmission2> frDomesticPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRDomesticPaymentSubmission2.class);
        frDomesticPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticPaymentSubmission(f)));

        CloseableIterator<FRDomesticScheduledConsent2> frDomesticScheduledConsents = getLegacyDocuments(mongoTemplate, FRDomesticScheduledConsent2.class);
        frDomesticScheduledConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticScheduledConsent(f)));

        CloseableIterator<FRDomesticScheduledPayment> frDomesticScheduledPayments = getLegacyDocuments(mongoTemplate, FRDomesticScheduledPayment.class);
        frDomesticScheduledPayments.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticScheduledPaymentSubmission(f)));

        CloseableIterator<FRDomesticStandingOrderConsent3> frDomesticStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRDomesticStandingOrderConsent3.class);
        frDomesticStandingOrderConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticStandingOrderConsent(f)));

        CloseableIterator<FRDomesticStandingOrderPaymentSubmission3> frDomesticStandingOrderSubmissions = getLegacyDocuments(mongoTemplate, FRDomesticStandingOrderPaymentSubmission3.class);
        frDomesticStandingOrderSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDomesticStandingOrderPaymentSubmission(f)));

        CloseableIterator<FRFileConsent2> frFileConsents = getLegacyDocuments(mongoTemplate, FRFileConsent2.class);
        frFileConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRFileConsent(f)));

        CloseableIterator<FRFilePaymentSubmission2> frFilePaymentSubmissions = getLegacyDocuments(mongoTemplate, FRFilePaymentSubmission2.class);
        frFilePaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRFilePaymentSubmission(f)));
    }

    private void migrateInternationalPayments(MongoTemplate mongoTemplate) {
        CloseableIterator<FRInternationalConsent2> frInternationalConsents = getLegacyDocuments(mongoTemplate, FRInternationalConsent2.class);
        frInternationalConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalConsent(f)));

        CloseableIterator<InternationalPaymentSubmission2> frInternationalPaymentSubmissions = getLegacyDocuments(mongoTemplate, InternationalPaymentSubmission2.class);
        frInternationalPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalPaymentSubmission(f)));

        CloseableIterator<FRInternationalScheduledConsent2> frInternationalScheduledConsents = getLegacyDocuments(mongoTemplate, FRInternationalScheduledConsent2.class);
        frInternationalScheduledConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalScheduledConsent(f)));

        CloseableIterator<InternationalScheduledPaymentSubmission2> frInternationalScheduledPaymentSubmissions = getLegacyDocuments(mongoTemplate, InternationalScheduledPaymentSubmission2.class);
        frInternationalScheduledPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalScheduledPaymentSubmission(f)));

        CloseableIterator<FRInternationalStandingOrderConsent3> frInternationalStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRInternationalStandingOrderConsent3.class);
        frInternationalStandingOrderConsents.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalStandingOrderConsent(f)));

        CloseableIterator<InternationalStandingOrderPaymentSubmission3> frInternationalStandingOrderPaymentSubmissions = getLegacyDocuments(mongoTemplate, InternationalStandingOrderPaymentSubmission3.class);
        frInternationalStandingOrderPaymentSubmissions.forEachRemaining(f -> migrate(mongoTemplate, f, toFRInternationalStandingOrderPaymentSubmission(f)));
    }
}
