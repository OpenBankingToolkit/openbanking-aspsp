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
        log.info("Migrating Payments API data from v3.1.2 to v3.1.6...");

        migrateDomesticPayments(mongoTemplate);
        migrateInternationalPayments(mongoTemplate);

        log.info("Finished migrating Payments API data from v3.1.2 to v3.1.6");
    }

    private void migrateDomesticPayments(MongoTemplate mongoTemplate) {
        CloseableIterator<FRDomesticConsent2> frDomesticConsents = getLegacyDocuments(mongoTemplate, FRDomesticConsent2.class);
        frDomesticConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticConsent(d)));

        CloseableIterator<FRDomesticPaymentSubmission2> frDomesticPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRDomesticPaymentSubmission2.class);
        frDomesticPaymentSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticPaymentSubmission(d)));

        CloseableIterator<FRDomesticScheduledConsent2> frDomesticScheduledConsents = getLegacyDocuments(mongoTemplate, FRDomesticScheduledConsent2.class);
        frDomesticScheduledConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticScheduledConsent(d)));

        CloseableIterator<FRDomesticScheduledPayment> frDomesticScheduledPayments = getLegacyDocuments(mongoTemplate, FRDomesticScheduledPayment.class);
        frDomesticScheduledPayments.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticScheduledPaymentSubmission(d)));

        CloseableIterator<FRDomesticStandingOrderConsent3> frDomesticStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRDomesticStandingOrderConsent3.class);
        frDomesticStandingOrderConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticStandingOrderConsent(d)));

        CloseableIterator<FRDomesticStandingOrderPaymentSubmission3> frDomesticStandingOrderSubmissions = getLegacyDocuments(mongoTemplate, FRDomesticStandingOrderPaymentSubmission3.class);
        frDomesticStandingOrderSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDomesticStandingOrderPaymentSubmission(d)));

        CloseableIterator<FRFileConsent2> frFileConsents = getLegacyDocuments(mongoTemplate, FRFileConsent2.class);
        frFileConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRFileConsent(d)));

        CloseableIterator<FRFilePaymentSubmission2> frFilePaymentSubmissions = getLegacyDocuments(mongoTemplate, FRFilePaymentSubmission2.class);
        frFilePaymentSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRFilePaymentSubmission(d)));
    }

    private void migrateInternationalPayments(MongoTemplate mongoTemplate) {
        CloseableIterator<FRInternationalConsent2> frInternationalConsents = getLegacyDocuments(mongoTemplate, FRInternationalConsent2.class);
        frInternationalConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalConsent(d)));

        CloseableIterator<FRInternationalPaymentSubmission2> frInternationalPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalPaymentSubmission2.class);
        frInternationalPaymentSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalPaymentSubmission(d)));

        CloseableIterator<FRInternationalScheduledConsent2> frInternationalScheduledConsents = getLegacyDocuments(mongoTemplate, FRInternationalScheduledConsent2.class);
        frInternationalScheduledConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalScheduledConsent(d)));

        CloseableIterator<FRInternationalScheduledPaymentSubmission2> frInternationalScheduledPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalScheduledPaymentSubmission2.class);
        frInternationalScheduledPaymentSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalScheduledPaymentSubmission(d)));

        CloseableIterator<FRInternationalStandingOrderConsent3> frInternationalStandingOrderConsents = getLegacyDocuments(mongoTemplate, FRInternationalStandingOrderConsent3.class);
        frInternationalStandingOrderConsents.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalStandingOrderConsent(d)));

        CloseableIterator<FRInternationalStandingOrderPaymentSubmission3> frInternationalStandingOrderPaymentSubmissions = getLegacyDocuments(mongoTemplate, FRInternationalStandingOrderPaymentSubmission3.class);
        frInternationalStandingOrderPaymentSubmissions.forEachRemaining(d -> migrate(mongoTemplate, d, toFRInternationalStandingOrderPaymentSubmission(d)));
    }
}
