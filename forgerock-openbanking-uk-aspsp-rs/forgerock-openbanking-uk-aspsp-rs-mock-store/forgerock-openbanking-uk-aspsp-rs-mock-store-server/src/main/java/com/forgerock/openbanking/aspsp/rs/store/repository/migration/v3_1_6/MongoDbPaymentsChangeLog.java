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
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/*
 * MongoDB migration changeSet
 * Describe what changes need to be done for objects with version v3_1_6
 * For that case we will change the dataType from the field 'countrySubDivision' from Array to String
 * Fix of issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/27, ticket: 59553
 */
@ChangeLog
@Slf4j
public class MongoDbPaymentsChangeLog {

    private static final String RISK = "risk";
    private static final String DELIVERY_ADDRESS = "deliveryAddress";
    private static final String COUNTRY_SUB_DIVISION = "countrySubDivision";
    private static final String SEPARATOR = ".";


    @ChangeSet(order = "001", id = "payments-v3.1.6-to-v3.1.6-patch-27", author = "Jordi Sanchez")
    public void migratePaymentDocuments(MongoTemplate mongoTemplate) {
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();

        log.info("-----------------------------------------------------------------------");
        log.info("Migrating Payments API data from v3.1.6 to v3.1.6 patch...");

        int docsUpdated = getObjectsToUpdate().stream()
                .mapToInt(objectToUpdate -> upgrade(mongoTemplate, objectToUpdate))
                .sum();

        elapsedTime.stop();

        log.info("Upgraded {} documents in {} seconds.", docsUpdated, elapsedTime.getTotalTimeSeconds());
        log.info("-----------------------------------------------------------------------");
        log.info("Finished migrating Payments API data from v3.1.6 to v3.1.6 patch");
    }

    /**
     * Entry method to execute the upgrade bulk operation
     * @param mongoTemplate
     * @param legacyClazz collection class
     * @param <T> generic parameter to conforms legacyClazz object
     */
    private <T> int upgrade(MongoTemplate mongoTemplate, Class<? extends LegacyCountrySubDivision> legacyClazz) {
        // execution time control - start
        StopWatch elapsedTime = start(legacyClazz);

        // number of docs updated
        int docsUpdated = 0;
        // prepare the json path to build the criteria filter
        String jsonPathCriteria = "*" + // TODO test wildcard of document field name
                SEPARATOR + RISK +
                SEPARATOR + DELIVERY_ADDRESS +
                SEPARATOR + COUNTRY_SUB_DIVISION;

        Criteria queryCriteria = new Criteria(jsonPathCriteria)
                .ne(null);

        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, legacyClazz);
        // get the documents filter by criteria
        Query query = new Query().addCriteria(queryCriteria);
        List<Pair<Query, Update>> updates = new ArrayList<>();
        List<? extends LegacyCountrySubDivision> legacyDocument = mongoTemplate.find(query, legacyClazz);
        legacyDocument.forEach(
                legacyDoc -> {
                    // getting the id and the first element of countrySubDivision from the document
                    String id = legacyDoc.getDocumentId();
                    String division = legacyDoc.getCountrySubDivision();
                    if (id != null && division != null) {
                        // Prepare the updates
                        updates.add(Pair.of(query(new Criteria("_id").is(id)), update(jsonPathCriteria, division)));
                    }
                }
        );
        // run the bulk operation
        if (!updates.isEmpty()) {
            docsUpdated = bulkOperations.updateMulti(updates).execute().getModifiedCount();
        }
        // execution time control - stop
        stop(legacyClazz, docsUpdated, elapsedTime);
        return docsUpdated;
    }

    private <T> StopWatch start(Class<T> clazz) {
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();
        log.info("-----------------------------------------------------------------------");
        log.info(">>>>>>>>>> Upgrading {}", clazz.getName());
        return elapsedTime;
    }

    private <T> void stop(Class<T> clazz, int entriesModified, StopWatch elapsedTime) {
        elapsedTime.stop();
        log.info(">>>>>>>>>> Upgraded [{}] {} objects in {} seconds.", entriesModified, clazz.getName(), elapsedTime.getTotalTimeSeconds());
        log.info("-----------------------------------------------------------------------");
    }

    /**
     * List of objects to update
     * @return list of objects to update
     */
    private List<Class<? extends LegacyCountrySubDivision>> getObjectsToUpdate(){
        List<Class<? extends LegacyCountrySubDivision>> objectsToUpdate = new ArrayList<>();
        // Domestic payments
        objectsToUpdate.add(FRDomesticConsent.class);

        objectsToUpdate.add(FRDomesticPaymentSubmission.class);
        // Domestic scheduled payments
        objectsToUpdate.add(FRDomesticScheduledConsent.class);
        objectsToUpdate.add(FRDomesticScheduledPaymentSubmission.class);
        // Domestic standing orders
        objectsToUpdate.add(FRDomesticStandingOrderConsent.class);
        objectsToUpdate.add(FRDomesticStandingOrderPaymentSubmission.class);
        // International payments
        objectsToUpdate.add(FRInternationalConsent.class);
        objectsToUpdate.add(FRInternationalPaymentSubmission.class);
        // International scheduled payments
        objectsToUpdate.add(FRInternationalScheduledConsent.class);
        objectsToUpdate.add(FRInternationalScheduledPaymentSubmission.class);
        // International standing Orders
        objectsToUpdate.add(FRInternationalStandingOrderConsent.class);
        objectsToUpdate.add(FRInternationalStandingOrderPaymentSubmission.class);

        objectsToUpdate.add(FRPaymentSetup.class);

        return objectsToUpdate;
    }
}
