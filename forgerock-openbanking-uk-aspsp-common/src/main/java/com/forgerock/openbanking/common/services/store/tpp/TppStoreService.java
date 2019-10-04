/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
