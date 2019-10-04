/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_2.events;

import com.forgerock.openbanking.commons.model.openbanking.v3_1_2.event.FREventSubscription1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface EventSubscriptionsRepository extends MongoRepository<FREventSubscription1, String> {

        Collection<FREventSubscription1> findByTppId(@Param("tppId") String tppId);
}
