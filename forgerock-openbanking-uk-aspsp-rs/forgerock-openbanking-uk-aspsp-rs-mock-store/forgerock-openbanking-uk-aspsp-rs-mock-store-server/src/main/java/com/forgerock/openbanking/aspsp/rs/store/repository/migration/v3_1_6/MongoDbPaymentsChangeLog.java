/**
 * Copyright 2019 ForgeRock AS.
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.v3_1_6;

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRDomesticConsent;
import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.FRDomesticPaymentSubmission;
import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.payments.LegacySubDivisionDocument;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Data base migration changeSet
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

    /*
     * Entry method to execute the upgrade bulk operation
     * @param mongoTemplate
     * @param legacyClazz collection class
     * @param writeRiskParentField field name to get the write object that contains the risk object related with the collection class
     * @param <T> generic parameter to conforms legacyClazz object
     */
    private int upgrade(MongoTemplate mongoTemplate, Class<? extends LegacySubDivisionDocument> legacyDocumentClazz) {
        // execution time control - start
        StopWatch elapsedTime = start(legacyDocumentClazz);

        // number of docs updated
        int docsUpdated = 0;
        // prepare the json path to build the criteria filter
        String jsonPathCriteria = new StringBuffer(wildcard?//writeRiskField.getName())
                .append(SEPARATOR).append(RISK)
                .append(SEPARATOR).append(DELIVERY_ADDRESS)
                .append(SEPARATOR).append(COUNTRY_SUB_DIVISION).toString();

        // Criteria to query only documents where countrySubDivision not null and obVersion is 'v3_1_6'
        // { "${writeRiskParentField}.risk.deliveryAddress.countrySubDivision" : { "$ne" : null }, "$and" : [{ "obVersion" : "v3_1_6" }] }
        Criteria queryCriteria = new Criteria(jsonPathCriteria);
        //.ne(null).andOperator(new Criteria(OB_VERSION_FIELD).is(OB_VERSION_VALUE));

        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, legacyDocumentClazz);
        // get the documents filter by criteria
        Query query = new Query().addCriteria(queryCriteria);
        Update update = new Update();
        List<Pair<Query, Update>> updates = new ArrayList<>();

        List<? extends LegacySubDivisionDocument> legacyDocuments = mongoTemplate.find(query, legacyDocumentClazz);
        if (!legacyDocuments.isEmpty()) {
            legacyDocuments.forEach(
                    legacyDocument -> {
                        if (legacyDocument.getCountrySubDivision() != null) {
                            // we get only the first value of countrySubDivisions
                            Query queryUpdate = new Query().addCriteria(new Criteria("_id").is(legacyDocument.getId()));
                            updates.add(Pair.of(queryUpdate, update.set(jsonPathCriteria, legacyDocument.getCountrySubDivision())));
                        }
                    }
            );
        }
        // run the bulk operation
        if (!updates.isEmpty()) {
            docsUpdated = bulkOperations.updateMulti(updates).execute().getModifiedCount();
        }
        // execution time control - stop
        stop(legacyDocumentClazz, docsUpdated, elapsedTime);
        return docsUpdated;
    }

    private <T> StopWatch start(Class<? extends LegacySubDivisionDocument> legacyDocument) {
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();
        log.info("-----------------------------------------------------------------------");
        log.info(">>>>>>>>>> Upgrading {}", legacyDocument.getName());
        return elapsedTime;
    }

    private <T> void stop(Class<? extends LegacySubDivisionDocument> legacyDocument, int entriesModified, StopWatch elapsedTime) {
        elapsedTime.stop();
        log.info(">>>>>>>>>> Upgraded [{}] {} objects in {} seconds.", entriesModified, legacyDocument.getName(), elapsedTime.getTotalTimeSeconds());
        log.info("-----------------------------------------------------------------------");
    }

    /*
     * List of objects to update
     * @return list of objects to update
     */
    private List<Class<? extends LegacySubDivisionDocument>> getObjectsToUpdate() {
        List<Class<? extends LegacySubDivisionDocument>> objectsToUpdate = new ArrayList<>();
        // Domestic payments
        objectsToUpdate.add(FRDomesticConsent.class);

        objectsToUpdate.add(FRDomesticPaymentSubmission.class);
        // Domestic scheduled payments
//        objectsToUpdate.add(FRDomesticScheduledConsent.class);
//        objectsToUpdate.add(FRDomesticScheduledPaymentSubmission.class);
//        // Domestic standing orders
//        objectsToUpdate.add(FRDomesticStandingOrderConsent.class);
//        objectsToUpdate.add(FRDomesticStandingOrderPaymentSubmission.class);
//        // International payments
//        objectsToUpdate.add(FRInternationalConsent.class);
//        objectsToUpdate.add(FRInternationalPaymentSubmission.class);
//        // International scheduled payments
//        objectsToUpdate.add(FRInternationalScheduledConsent.class);
//        objectsToUpdate.add(FRInternationalScheduledPaymentSubmission.class);
//        // International standing Orders
//        objectsToUpdate.add(FRInternationalStandingOrderConsent.class);
//        objectsToUpdate.add(FRInternationalStandingOrderPaymentSubmission.class);

        return objectsToUpdate;
    }
}
