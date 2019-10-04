/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds;

import com.forgerock.openbanking.commons.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

import java.util.Collection;

public interface FundsConfirmationConsentRepository extends MongoRepository<FRFundsConfirmationConsent1, String> {

    Collection<FRFundsConfirmationConsent1> findByStatus(@Param("status") OBTransactionIndividualStatus1Code status);

}
