/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.accounts;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRAccount2;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;
import java.util.Optional;



public interface FRAccount2Repository extends MongoRepository<FRAccount2, String>, FRAccount2RepositoryCustom {

    Collection<FRAccount2> findByUserID(@Param("userID") String userID);
    Optional<FRAccount2> findByAccountAccountIdentification(@Param("identification") String identification);
    Collection<FRAccount2> findAllUserIDDistinctByCreatedBetween(
            @Param("from")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime fromDate,
            @Param("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime toDate);


}
