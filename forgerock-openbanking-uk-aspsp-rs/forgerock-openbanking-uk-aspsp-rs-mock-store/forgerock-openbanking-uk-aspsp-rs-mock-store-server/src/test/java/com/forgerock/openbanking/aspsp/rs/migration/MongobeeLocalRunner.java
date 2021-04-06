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
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.migration;

import com.github.mongobee.Mongobee;
import com.github.mongobee.exception.MongobeeException;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongobeeLocalRunner {

    /*
     * After run this in your local environment you can check the collection 'mongo.dbchangelog' with the entrance of changeSets.
     * To run it again you will need delete the entrance of your changeSet to avoid 'passed over' from the collection 'mongo.dbchangelog'
     */
    public static void main(String[] args) {
        MongoTemplate mongoTemplate = new MongoTemplate(MongoClients.create("mongodb://localhost:27017/mongo"),"mongo");
        Mongobee runner = new Mongobee("mongodb://localhost:27017/mongo");
        runner.setDbName("mongo");
        runner.setChangeLogsScanPackage("com.forgerock.openbanking.aspsp.rs.store.repository.migration");
        runner.setMongoTemplate(mongoTemplate);
        try {
            runner.execute();
        } catch (MongobeeException e) {
            e.printStackTrace();
        }
    }

    /* restore data for test */
    /*
    # changeSet 'payments-v3.1.6-to-v3.1.6-patch-27'
    mongorestore --drop -d mongo -c fRDomesticConsent fRDomesticConsent.bson \
    && mongorestore --drop -d mongo -c fRDomesticPaymentSubmission fRDomesticPaymentSubmission.bson \
    && mongorestore --drop -d mongo -c fRDomesticScheduledConsent fRDomesticScheduledConsent.bson \
    && mongorestore --drop -d mongo -c fRDomesticScheduledPaymentSubmission fRDomesticScheduledPaymentSubmission.bson \
    && mongorestore --drop -d mongo -c fRInternationalConsent fRInternationalConsent.bson \
    && mongorestore --drop -d mongo -c fRInternationalPaymentSubmission fRInternationalPaymentSubmission.bson \
    && mongorestore --drop -d mongo -c fRInternationalScheduledConsent fRInternationalScheduledConsent.bson \
    && mongorestore --drop -d mongo -c fRInternationalScheduledPaymentSubmission fRInternationalScheduledPaymentSubmission.bson \
    && mongorestore --drop -d mongo -c fRInternationalStandingOrderConsent fRInternationalStandingOrderConsent.bson \
    && mongorestore --drop -d mongo -c fRInternationalStandingOrderPaymentSubmission fRInternationalStandingOrderPaymentSubmission.bson \
    && mongorestore --drop -d mongo -c fRPaymentSetup fRPaymentSetup.bson
    */
    /* example filter: {"domesticConsent.risk.deliveryAddress.countrySubDivision": {$ne: null}, $and: [{"obVersion": "v3_1_6"}]} */
}
