/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.accountaccessconsents;

import com.forgerock.openbanking.commons.model.openbanking.v3_0.account.FRAccountAccessConsent1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FRAccountAccessConsent1Repository extends MongoRepository<FRAccountAccessConsent1, String> {

    Optional<FRAccountAccessConsent1> findByConsentId(@Param("consentId") String consentId);
    Collection<FRAccountAccessConsent1> findByUserId(@Param("userId") String userId);
    Collection<FRAccountAccessConsent1> findByUserIdAndClientId(@Param("userId") String userId, @Param("clientId") String clientId);
    Collection<FRAccountAccessConsent1> findByUserIdAndAccountAccessConsentDataStatusIn(@Param("userId") String userId, @Param("statuses") List<OBExternalRequestStatus1Code> status);
}
