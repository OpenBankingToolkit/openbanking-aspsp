/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository;

import com.forgerock.openbanking.commons.model.openbanking.forgerock.event.FREventNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

/**
 * Persistence for pending events generated within the sandbox so that we can track acknowledgement, error status and event history.
 *
 * Events sent to a callback URL will not be stored here. If TPP does not have a callback URL then all events will be stored here until they are polled AND acknowledged.
 * Event polled but not acknowledged will remain here. Events with TPP reported errors against them will remain here for ever (for audit/investigation)
 */
public interface FRPendingEventsRepository extends MongoRepository<FREventNotification, String> {

    Collection<FREventNotification> findByTppId(@Param("tppId") String tppId);

    Optional<FREventNotification> findByTppIdAndJti(@Param("tppId") String tppId, @Param("jti") String jti);

    void deleteByTppIdAndJti(@Param("tppId") String tppId, @Param("jti") String jti);
}
