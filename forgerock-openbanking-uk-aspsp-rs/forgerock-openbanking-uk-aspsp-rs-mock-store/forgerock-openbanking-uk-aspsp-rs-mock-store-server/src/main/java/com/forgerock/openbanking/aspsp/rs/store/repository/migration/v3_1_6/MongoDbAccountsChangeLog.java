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

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog
@Slf4j
public class MongoDbAccountsChangeLog {

    @ChangeSet(order = "002", id = "accounts-v3.1.2-to-v3.1.6", author = "Matt Wills")
    public void migrateAccountDocuments(MongoTemplate mongoTemplate) {
        log.info("Migrating accounts API data from v3.1.2 to v3.1.6...");

        // TODO #296 - add legacy account objects to legacy.accounts package (and deprecate them)
//        CloseableIterator<FRAccount3> frAccounts = getLegacyDocuments(mongoTemplate, FRAccount3.class);
//        frAccounts.forEachRemaining(f -> migrate(mongoTemplate, f, toAccount4(f)));
//
//        CloseableIterator<FRBeneficiary3> frBeneficiaries = getLegacyDocuments(mongoTemplate, FRBeneficiary3.class);
//        frBeneficiaries.forEachRemaining(f -> migrate(mongoTemplate, f, toFRBeneficiary5(f)));
//
//        CloseableIterator<FRStandingOrder5> frStandingOrders = getLegacyDocuments(mongoTemplate, FRStandingOrder5.class);
//        frStandingOrders.forEachRemaining(f -> migrate(mongoTemplate, f, toFRStandingOrder6(f)));
//
//        CloseableIterator<FRTransaction5> frTransactionsIterator = getLegacyDocuments(mongoTemplate, FRTransaction5.class);
//        frTransactionsIterator.forEachRemaining(f -> migrate(mongoTemplate, f, toFRTransaction6(f)));
//
//        CloseableIterator<FRStatement1> frStatements = getLegacyDocuments(mongoTemplate, FRStatement1.class);
//        frStatements.forEachRemaining(f -> migrate(mongoTemplate, f, toFRStatement4(f)));
//
//        CloseableIterator<FRScheduledPayment2> frScheduledPayments = getLegacyDocuments(mongoTemplate, FRScheduledPayment2.class);
//        frScheduledPayments.forEachRemaining(f -> migrate(mongoTemplate, f, toFRScheduledPayment4(f)));
//
//        CloseableIterator<FRDirectDebit1> frDirectDebits = getLegacyDocuments(mongoTemplate, FRDirectDebit1.class);
//        frDirectDebits.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDirectDebit4(f)));

        log.info("Finished migrating accounts API data from v3.1.2 to v3.1.6");
    }
}
