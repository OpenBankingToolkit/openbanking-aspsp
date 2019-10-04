/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository;

import com.forgerock.openbanking.model.Tpp;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;

@RepositoryRestResource
public interface TppRepository extends MongoRepository<Tpp, String> {

    Collection<Tpp> findByCertificateCn(@Param("certificateCn") String certificateCn);

    Tpp findByClientId(@Param("clientId") String clientId);

    Collection<Tpp> findByCreatedBetween(
            @Param("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime fromDate,
            @Param("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) DateTime toDate);
}
