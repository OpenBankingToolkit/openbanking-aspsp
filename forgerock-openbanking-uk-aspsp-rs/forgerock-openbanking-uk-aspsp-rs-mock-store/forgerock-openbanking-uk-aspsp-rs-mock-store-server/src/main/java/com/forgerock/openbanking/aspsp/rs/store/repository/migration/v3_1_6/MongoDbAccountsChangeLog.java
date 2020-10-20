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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts.*;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.CloseableIterator;

import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.getLegacyDocuments;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.migrate;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRAccountMigrator.toFRAccount;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRBalanceMigrator.toFRBalance;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRBeneficiaryMigrator.toFRBeneficiary;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRDirectDebitMigrator.toFRDirectDebit;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FROfferMigrator.toFROffer;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRPartyMigrator.toFRParty;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRProductMigrator.toFRProduct;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRScheduledPaymentMigrator.toFRScheduledPayment;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRStandingOrderMigrator.toFRStandingOrder;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRStatementMigrator.toFRStatement;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6.accounts.FRTransactionMigrator.toFRTransaction;

@ChangeLog
@Slf4j
public class MongoDbAccountsChangeLog {

    @ChangeSet(order = "002", id = "accounts-v3.1.2-to-v3.1.6", author = "Matt Wills")
    public void migrateAccountDocuments(MongoTemplate mongoTemplate) {
        log.info("Migrating accounts API data from v3.1.2 to v3.1.6...");

        CloseableIterator<FRAccount3> frAccounts = getLegacyDocuments(mongoTemplate, FRAccount3.class);
        frAccounts.forEachRemaining(f -> migrate(mongoTemplate, f, toFRAccount(f)));

        CloseableIterator<FRBalance1> frBalances = getLegacyDocuments(mongoTemplate, FRBalance1.class);
        frBalances.forEachRemaining(f -> migrate(mongoTemplate, f, toFRBalance(f)));

        CloseableIterator<FRBeneficiary3> frBeneficiaries = getLegacyDocuments(mongoTemplate, FRBeneficiary3.class);
        frBeneficiaries.forEachRemaining(f -> migrate(mongoTemplate, f, toFRBeneficiary(f)));

        CloseableIterator<FRDirectDebit1> frDirectDebits = getLegacyDocuments(mongoTemplate, FRDirectDebit1.class);
        frDirectDebits.forEachRemaining(f -> migrate(mongoTemplate, f, toFRDirectDebit(f)));

        CloseableIterator<FROffer1> frOffers = getLegacyDocuments(mongoTemplate, FROffer1.class);
        frOffers.forEachRemaining(f -> migrate(mongoTemplate, f, toFROffer(f)));

        CloseableIterator<FRParty2> frParties = getLegacyDocuments(mongoTemplate, FRParty2.class);
        frParties.forEachRemaining(f -> migrate(mongoTemplate, f, toFRParty(f)));

        CloseableIterator<FRProduct2> frProducts = getLegacyDocuments(mongoTemplate, FRProduct2.class);
        frProducts.forEachRemaining(f -> migrate(mongoTemplate, f, toFRProduct(f)));

        CloseableIterator<FRScheduledPayment2> frScheduledPayments = getLegacyDocuments(mongoTemplate, FRScheduledPayment2.class);
        frScheduledPayments.forEachRemaining(f -> migrate(mongoTemplate, f, toFRScheduledPayment(f)));

        CloseableIterator<FRStandingOrder5> frStandingOrders = getLegacyDocuments(mongoTemplate, FRStandingOrder5.class);
        frStandingOrders.forEachRemaining(f -> migrate(mongoTemplate, f, toFRStandingOrder(f)));

        CloseableIterator<FRStatement1> frStatements = getLegacyDocuments(mongoTemplate, FRStatement1.class);
        frStatements.forEachRemaining(f -> migrate(mongoTemplate, f, toFRStatement(f)));

        CloseableIterator<FRTransaction5> frTransactionsIterator = getLegacyDocuments(mongoTemplate, FRTransaction5.class);
        frTransactionsIterator.forEachRemaining(f -> migrate(mongoTemplate, f, toFRTransaction(f)));

        log.info("Finished migrating accounts API data from v3.1.2 to v3.1.6");
    }
}
