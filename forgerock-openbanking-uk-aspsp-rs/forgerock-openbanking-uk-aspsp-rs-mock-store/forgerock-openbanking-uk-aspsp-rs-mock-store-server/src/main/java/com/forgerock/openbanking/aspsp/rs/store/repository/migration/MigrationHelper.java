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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;

@Slf4j
public class MigrationHelper {

    public static <T> CloseableIterator<T> getLegacyDocuments(MongoTemplate mongoTemplate, Class<T> originatingDocumentClass) {
        long count = mongoTemplate.count(new Query(), originatingDocumentClass);
        log.info("Retrieving {} {} documents for conversion", count, originatingDocumentClass.getCanonicalName());
        return mongoTemplate.stream(new Query(), originatingDocumentClass);
    }

    public static <T, U> void migrate(MongoTemplate mongoTemplate, T originatingDocument, U newDocument) {
        mongoTemplate.remove(originatingDocument);
        mongoTemplate.save(newDocument);
    }
}
