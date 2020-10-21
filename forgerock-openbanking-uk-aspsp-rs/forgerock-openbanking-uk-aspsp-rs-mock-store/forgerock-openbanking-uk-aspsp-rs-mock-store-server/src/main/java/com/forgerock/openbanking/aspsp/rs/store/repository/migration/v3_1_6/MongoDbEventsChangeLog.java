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

import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.events.FRCallbackUrl1;
import com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.events.FREventSubscription1;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventSubscription;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.util.CloseableIterator;

import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.getLegacyDocuments;
import static com.forgerock.openbanking.aspsp.rs.store.repository.migration.MigrationHelper.migrate;
import static com.forgerock.openbanking.common.services.openbanking.converter.event.FRCallbackUrlConverter.toFRCallbackUrlData;
import static com.forgerock.openbanking.common.services.openbanking.converter.event.FREventSubscriptionConverter.toFREventSubscriptionData;

@ChangeLog
@Slf4j
public class MongoDbEventsChangeLog {

    @ChangeSet(order = "003", id = "events-v3.1.2-to-v3.1.6", author = "Matt Wills")
    public void migrateAccountDocuments(MongoTemplate mongoTemplate) {
        log.info("Migrating Events API data from v3.1.2 to v3.1.6...");

        CloseableIterator<FRCallbackUrl1> frCallbackUrls = getLegacyDocuments(mongoTemplate, FRCallbackUrl1.class);
        frCallbackUrls.forEachRemaining(d -> migrate(mongoTemplate, d, toFRCallbackUrl(d)));

        CloseableIterator<FREventSubscription1> frEventSubscriptions = getLegacyDocuments(mongoTemplate, FREventSubscription1.class);
        frEventSubscriptions.forEachRemaining(d -> migrate(mongoTemplate, d, toFREventSubscription(d)));

        log.info("Finished migrating Events API data from v3.1.2 to v3.1.6");
    }

    private FRCallbackUrl toFRCallbackUrl(FRCallbackUrl1 frCallbackUrl1) {
        return frCallbackUrl1 == null ? null : FRCallbackUrl.builder()
                .id(frCallbackUrl1.getId())
                .callbackUrl(toFRCallbackUrlData(frCallbackUrl1.getObCallbackUrl()))
                .tppId(frCallbackUrl1.getTppId())
                .created(frCallbackUrl1.getCreated())
                .updated(frCallbackUrl1.getUpdated())
                .build();
    }

    public static FREventSubscription toFREventSubscription(FREventSubscription1 frEventSubscription1) {
        return frEventSubscription1 == null ? null : FREventSubscription.builder()
                .id(frEventSubscription1.getId())
                .eventSubscription(toFREventSubscriptionData(frEventSubscription1.getObEventSubscription1()))
                .tppId(frEventSubscription1.getTppId())
                .created(frEventSubscription1.getCreated())
                .updated(frEventSubscription1.getUpdated())
                .version(frEventSubscription1.getVersion())
                .build();
    }
}
