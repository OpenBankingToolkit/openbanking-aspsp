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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.DirectorySoftwareStatementFactory;
import com.forgerock.openbanking.model.DirectorySoftwareStatement;
import com.forgerock.openbanking.model.Tpp;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.List;

@ChangeLog
@Slf4j
public class MongoTppSchemaChangeLog {

    private ObjectMapper objectMapper;

    @Autowired
    public MongoTppSchemaChangeLog(){
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }



    @ChangeSet(order = "001", id = "tpp-to-multi-software-statement-tpp", author = "Jamie Bowen")
    public void migrateTpps(MongoTemplate mongoTemplate) throws IOException {
        StopWatch elapsedTime = new StopWatch();
        elapsedTime.start();
        long docsUpdated = 0;
        long docsWithNoAuthorisationNumber = 0;
        log.info("-----------------------------------------------------------------------");
        log.info("Migrating Tpp data to have full softwareStatement info");

        OpenBankingDirectoryConfiguration openBankingDirectoryConfiguration =
                new OpenBankingDirectoryConfiguration();
        openBankingDirectoryConfiguration.issuerId = "OpenBanking Ltd";
        DirectorySoftwareStatementFactory directorySoftwareStatementFactory =
                new DirectorySoftwareStatementFactory(openBankingDirectoryConfiguration);

        Query query = new Query();
        List<Tpp> tpps = mongoTemplate.find(query, Tpp.class);
        log.info("Found {} tpps", tpps.size());
        for(Tpp tpp: tpps){
            String ssa = tpp.getSsa();
            DirectorySoftwareStatement directorySoftwareStatement =
                    directorySoftwareStatementFactory.getSoftwareStatementFromJsonString(ssa, objectMapper);
            String authorisationNumber = directorySoftwareStatement.getAuthorisationNumber();
            if(authorisationNumber == null || authorisationNumber.isBlank()){
                log.error("Failed to set authorisation number of document id '{}'", tpp.getId());
                docsWithNoAuthorisationNumber++;
            } else {
                tpp.setAuthorisationNumber(authorisationNumber);
            }
            tpp.setSoftwareId(directorySoftwareStatement.getSoftware_client_id());
            tpp.setDirectorySoftwareStatement(directorySoftwareStatement);
            mongoTemplate.save(tpp);
            docsUpdated++;
        }

        elapsedTime.stop();

        log.info("Upgraded {} documents in {} seconds.", docsUpdated, elapsedTime.getTotalTimeSeconds());
        log.info("Failed to create authorisationNumbers for {} documents", docsWithNoAuthorisationNumber);
        log.info("-----------------------------------------------------------------------");
        log.info("Finished updating Tpps to have full software statement information");
    }
}
