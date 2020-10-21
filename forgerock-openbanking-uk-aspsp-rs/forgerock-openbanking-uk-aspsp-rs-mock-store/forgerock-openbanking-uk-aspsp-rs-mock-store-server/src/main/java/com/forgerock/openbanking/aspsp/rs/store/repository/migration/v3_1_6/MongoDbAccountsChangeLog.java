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
        log.info("Migrating Accounts API data from v3.1.2 to v3.1.6...");

        CloseableIterator<FRAccount3> frAccounts = getLegacyDocuments(mongoTemplate, FRAccount3.class);
        frAccounts.forEachRemaining(d -> migrate(mongoTemplate, d, toFRAccount(d)));

        CloseableIterator<FRBalance1> frBalances = getLegacyDocuments(mongoTemplate, FRBalance1.class);
        frBalances.forEachRemaining(d -> migrate(mongoTemplate, d, toFRBalance(d)));

        CloseableIterator<FRBeneficiary3> frBeneficiaries = getLegacyDocuments(mongoTemplate, FRBeneficiary3.class);
        frBeneficiaries.forEachRemaining(d -> migrate(mongoTemplate, d, toFRBeneficiary(d)));

        CloseableIterator<FRDirectDebit1> frDirectDebits = getLegacyDocuments(mongoTemplate, FRDirectDebit1.class);
        frDirectDebits.forEachRemaining(d -> migrate(mongoTemplate, d, toFRDirectDebit(d)));

        CloseableIterator<FROffer1> frOffers = getLegacyDocuments(mongoTemplate, FROffer1.class);
        frOffers.forEachRemaining(d -> migrate(mongoTemplate, d, toFROffer(d)));

        CloseableIterator<FRParty2> frParties = getLegacyDocuments(mongoTemplate, FRParty2.class);
        frParties.forEachRemaining(d -> migrate(mongoTemplate, d, toFRParty(d)));

        CloseableIterator<FRProduct2> frProducts = getLegacyDocuments(mongoTemplate, FRProduct2.class);
        frProducts.forEachRemaining(d -> migrate(mongoTemplate, d, toFRProduct(d)));

        CloseableIterator<FRScheduledPayment2> frScheduledPayments = getLegacyDocuments(mongoTemplate, FRScheduledPayment2.class);
        frScheduledPayments.forEachRemaining(d -> migrate(mongoTemplate, d, toFRScheduledPayment(d)));

        CloseableIterator<FRStandingOrder5> frStandingOrders = getLegacyDocuments(mongoTemplate, FRStandingOrder5.class);
        frStandingOrders.forEachRemaining(d -> migrate(mongoTemplate, d, toFRStandingOrder(d)));

        CloseableIterator<FRStatement1> frStatements = getLegacyDocuments(mongoTemplate, FRStatement1.class);
        frStatements.forEachRemaining(d -> migrate(mongoTemplate, d, toFRStatement(d)));

        CloseableIterator<FRTransaction5> frTransactionsIterator = getLegacyDocuments(mongoTemplate, FRTransaction5.class);
        frTransactionsIterator.forEachRemaining(d -> migrate(mongoTemplate, d, toFRTransaction(d)));

        log.info("Finished migrating Accounts API data from v3.1.2 to v3.1.6");
    }
}
