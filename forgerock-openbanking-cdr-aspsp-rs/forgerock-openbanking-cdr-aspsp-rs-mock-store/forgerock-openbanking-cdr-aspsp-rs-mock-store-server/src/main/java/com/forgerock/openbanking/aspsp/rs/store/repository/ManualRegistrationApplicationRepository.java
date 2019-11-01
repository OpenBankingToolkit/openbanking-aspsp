/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository;

import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface ManualRegistrationApplicationRepository extends MongoRepository<ManualRegistrationApplication, String> {

    Collection<ManualRegistrationApplication> findByUserId(@Param("userId") String userId);
    ManualRegistrationApplication findBySoftwareClientId(@Param("softwareClientId") String softwareClientId);
}
