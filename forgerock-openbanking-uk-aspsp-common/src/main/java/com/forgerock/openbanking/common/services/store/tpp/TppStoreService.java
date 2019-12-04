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
package com.forgerock.openbanking.common.services.store.tpp;

import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

public interface TppStoreService {

    Optional<Tpp> findByCn(String cn);

    Optional<Tpp> findByClientId(String clientId);

    Optional<Tpp> findById(String id);

    Tpp createTpp(Tpp tpp);

    Tpp save(Tpp tpp);

    void deleteTPP(Tpp tpp);

    List<Tpp> all();

    List<Tpp> findByDateBetween(DateTime fromDate, DateTime toDate);

    List<Tpp> findByCreatedBetween(DateTime fromDate, DateTime toDate);

    /**
     * Get the internal PISP Id from the provided TPP (client) ID from a token
     * @param tppId Client ID
     * @return PISP ID
     * @throws OBErrorException Not found
     */
    String findPispIdByTppId(String tppId) throws OBErrorException;
}
