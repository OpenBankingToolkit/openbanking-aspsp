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
package com.forgerock.openbanking.aspsp.as.migration.tppschema;

import com.forgerock.openbanking.model.Tpp;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StopWatch;

import java.util.List;

@ChangeLog
@Slf4j
public class MongoTppSchemaChangeLog {

    @ChangeSet(order = "001", id = "tpp-to-multi-software-statement-tpp", author = "Jamie Bowen", runAlways = true)
    public void migrateTpps(MongoTemplate mongoTemplate){
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();
        long docsUpdated = 0;
        log.info("-----------------------------------------------------------------------");
        log.info("Migrating Tpp data to have full softwareStatement info");

        Criteria queryCriteria = new Criteria("directoryId").is("ForgeRock");
        Query query = new Query().addCriteria(queryCriteria);
        List<Tpp> tpps = mongoTemplate.find(query, Tpp.class);
        for(Tpp tpp: tpps){
            //ApiClientOrganisation =
        }

        elapsedTime.stop();

        log.info("Upgraded {} documents in {} seconds.", docsUpdated, elapsedTime.getTotalTimeSeconds());
        log.info("-----------------------------------------------------------------------");
        log.info("Finished updating Tpps to have full software statement information");
    }



}
