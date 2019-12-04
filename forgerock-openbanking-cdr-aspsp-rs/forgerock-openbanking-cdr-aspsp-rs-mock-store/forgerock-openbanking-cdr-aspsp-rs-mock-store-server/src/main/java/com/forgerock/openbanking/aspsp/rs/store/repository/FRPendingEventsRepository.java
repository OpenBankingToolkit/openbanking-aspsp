/**
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
package com.forgerock.openbanking.aspsp.rs.store.repository;

import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
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
