/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.payments.paymentsetup;

import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Collection;
import java.util.Optional;

public interface FRPaymentSetup1Repository extends MongoRepository<FRPaymentSetup1, String> {

    Collection<FRPaymentSetup1> findByStatus(@Param("status") OBTransactionIndividualStatus1Code status);
    Optional<FRPaymentSetup1> findByIdempotencyKeyAndPispId(@Param("idempotencyKey") String idempotencyKey, @Param("pispId") String pispId);
}
