/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments;

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Collection;
import java.util.Optional;

public interface DomesticConsent2Repository extends MongoRepository<FRDomesticConsent2, String> {

    Collection<FRDomesticConsent2> findByStatus(@Param("status") OBTransactionIndividualStatus1Code status);

    Optional<FRDomesticConsent2> findByIdempotencyKeyAndPispId(@Param("idempotencyKey") String idempotencyKey, @Param("pispId") String pispId);
}