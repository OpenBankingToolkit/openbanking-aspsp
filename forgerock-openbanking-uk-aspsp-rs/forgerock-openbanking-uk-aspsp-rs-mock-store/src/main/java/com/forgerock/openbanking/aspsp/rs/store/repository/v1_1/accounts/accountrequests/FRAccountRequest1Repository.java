/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.accountrequests;

import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRAccountRequest1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FRAccountRequest1Repository extends MongoRepository<FRAccountRequest1, String> {

    Optional<FRAccountRequest1> findByAccountRequestId(@Param("accountRequestId") String accountRequestId);
    Collection<FRAccountRequest1> findByUserId(@Param("userId") String userId);
    Collection<FRAccountRequest1> findByUserIdAndClientId(@Param("userId") String userId, @Param("clientId") String clientId);
    Collection<FRAccountRequest1> findByUserIdAndAccountRequestDataStatusIn(@Param("userId") String userId, @Param("statuses") List<OBExternalRequestStatus1Code> status);
}
