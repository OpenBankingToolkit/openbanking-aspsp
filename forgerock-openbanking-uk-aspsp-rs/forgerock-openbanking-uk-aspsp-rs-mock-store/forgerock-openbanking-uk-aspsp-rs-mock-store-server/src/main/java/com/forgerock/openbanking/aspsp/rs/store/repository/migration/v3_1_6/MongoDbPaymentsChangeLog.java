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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String OB_VERSION_FIELD = "obVersion";
    private static final String OB_VERSION_VALUE = "v3_1_6";
    private static final String SEPARATOR = ".";


    @ChangeSet(order = "001", id = "payments-v3.1.6-to-v3.1.6-patch-27", author = "Jordi Sanchez")
    public void migratePaymentDocuments(MongoTemplate mongoTemplate) {
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();

        log.info("-----------------------------------------------------------------------");
        log.info("Migrating Payments API data from v3.1.6 to v3.1.6 patch...");

        int docsUpdated = getObjectsToUpdate().stream().mapToInt(objectToUpdate -> upgrade(mongoTemplate, objectToUpdate.legacyClass, objectToUpdate.writeConsentField)).sum();

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
    private <T> int upgrade(MongoTemplate mongoTemplate, Class<T> legacyClazz, String writeRiskParentField) {
        // execution time control - start
        StopWatch elapsedTime = start(legacyClazz);

        // number of docs updated
        int docsUpdated = 0;
        // prepare the json path to build the criteria filter
        String jsonPathCriteria = new StringBuffer(writeRiskParentField)
                .append(SEPARATOR).append(RISK)
                .append(SEPARATOR).append(DELIVERY_ADDRESS)
                .append(SEPARATOR).append(COUNTRY_SUB_DIVISION).toString();

        // Criteria to query only documents where countrySubDivision not null and obVersion is 'v3_1_6'
        // { "${writeRiskParentField}.risk.deliveryAddress.countrySubDivision" : { "$ne" : null }, "$and" : [{ "obVersion" : "v3_1_6" }] }
        Criteria queryCriteria = new Criteria(jsonPathCriteria)
                .ne(null).andOperator(new Criteria(OB_VERSION_FIELD).is(OB_VERSION_VALUE));

        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, legacyClazz);
        // get the documents filter by criteria
        Query query = new Query().addCriteria(queryCriteria);
        Update update = new Update();
        List<Pair<Query, Update>> updates = new ArrayList<>();
        mongoTemplate.find(query, legacyClazz).forEach(
                legacyClassDocInstance -> {
                    try {
                        // return a Optional Pair [id, countrySubDivision] for each document
                        Optional<Pair<String, String>> optIdAndCountrySubDivision = getPair(legacyClazz, legacyClassDocInstance, writeRiskParentField);
                        if (optIdAndCountrySubDivision.isPresent()) {
                            if (optIdAndCountrySubDivision.get().getFirst() != null && optIdAndCountrySubDivision.get().getSecond() != null) {
                                // we get only the first value of countrySubDivisions
                                Query queryUpdate = new Query().addCriteria(new Criteria("_id").is(optIdAndCountrySubDivision.get().getFirst()));
                                updates.add(Pair.of(queryUpdate, update.set(jsonPathCriteria, optIdAndCountrySubDivision.get().getSecond())));
                            }
                        }
                    } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                        log.error("Error getting the id and countrySubDivision from the document [{}]", legacyClassDocInstance.toString(), e);
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

    /*
     * Introspect the legacyClassDocInstance to get the id and countrySubDivision values to prepare the bulk update operation
     * @param legacyClazz collection class
     * @param legacyClassDocInstance document legacyClass instance
     * @param writeRiskParentField field name to get the write object that contains the risk object related with the collection class
     * @param <T> generic parameter to conforms legacyClazz object
     * @return a optional pair with the id and the first value of countrySubDivision
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private <T> Optional<Pair<String, String>> getPair(Class<T> legacyClazz, Object legacyClassDocInstance, String writeRiskParentField) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Optional<Method> optionalMethod = Arrays.stream(legacyClazz.getDeclaredMethods()).filter(m -> m.getName().toLowerCase().endsWith(writeRiskParentField.toLowerCase())).findFirst();
        if (optionalMethod.isPresent()) {
            String division = null;
            String id;
            // get the id value
            PropertyDescriptor pd = new PropertyDescriptor("id", legacyClassDocInstance.getClass());
            id = (String) pd.getReadMethod().invoke(legacyClassDocInstance);

            //String fieldName = Arrays.stream(legacyClazz.getFields()).filter(field -> field.getName().equals(writeConsentField)).findFirst().get().getName();
            // get the write risk parent object
            pd = new PropertyDescriptor(writeRiskParentField, legacyClassDocInstance.getClass());
            Object obj = pd.getReadMethod().invoke(legacyClassDocInstance);

            // get the risk object
            pd = new PropertyDescriptor(RISK, obj.getClass());
            obj = pd.getReadMethod().invoke(obj);
            log.info(obj.toString());

            // get the delivery adress object
            pd = new PropertyDescriptor(DELIVERY_ADDRESS, obj.getClass());
            obj = pd.getReadMethod().invoke(obj);
            log.info(obj.toString());

            // get the country sub division object
            pd = new PropertyDescriptor(COUNTRY_SUB_DIVISION, obj.getClass());
            obj = pd.getReadMethod().invoke(obj);

            // get the first value of country sub division object list
            if (obj instanceof List) {
                division = (String) ((List) obj).get(0);
            }
            // build the optional pair with the id and the country sub division
            return Optional.of(Pair.of(id, division));
        }
        // empty if doesn't exist the proper objects to extract the values
        return Optional.empty();
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

    /*
     * List of objects to update
     * @return list of objects to update
     */
    private List<ObjectToUpdate> getObjectsToUpdate(){
        List<ObjectToUpdate> objectsToUpdate = new ArrayList<>();
        // Domestic payments
        objectsToUpdate.add(new ObjectToUpdate(FRDomesticConsent.class, "domesticConsent"));

        objectsToUpdate.add(new ObjectToUpdate(FRDomesticPaymentSubmission.class, "domesticPayment"));
        // Domestic scheduled payments
        objectsToUpdate.add(new ObjectToUpdate(FRDomesticScheduledConsent.class, "domesticScheduledConsent"));
        objectsToUpdate.add(new ObjectToUpdate(FRDomesticScheduledPaymentSubmission.class, "domesticScheduledPayment"));
        // Domestic standing orders
        objectsToUpdate.add(new ObjectToUpdate(FRDomesticStandingOrderConsent.class, "domesticStandingOrderConsent"));
        objectsToUpdate.add(new ObjectToUpdate(FRDomesticStandingOrderPaymentSubmission.class, "domesticStandingOrder"));
        // International payments
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalConsent.class,"internationalConsent"));
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalPaymentSubmission.class, "internationalPayment"));
        // International scheduled payments
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalScheduledConsent.class, "internationalScheduledConsent"));
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalScheduledPaymentSubmission.class, "internationalScheduledPayment"));
        // International standing Orders
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalStandingOrderConsent.class, "internationalStandingOrderConsent"));
        objectsToUpdate.add(new ObjectToUpdate(FRInternationalStandingOrderPaymentSubmission.class, "internationalStandingOrder"));

        objectsToUpdate.add(new ObjectToUpdate(FRPaymentSetup.class, "paymentSetupRequest"));


        return objectsToUpdate;
    }

    class ObjectToUpdate{
        Class legacyClass; // class from legacy.payments
        String writeConsentField; // name of the field to get FRWriteConsent object and root field of document to path risk elements, stored in mongodb

        public ObjectToUpdate(Class legacyClass, String writeConsentField) {
            this.legacyClass = legacyClass;
            this.writeConsentField = writeConsentField;
        }
    }
}
