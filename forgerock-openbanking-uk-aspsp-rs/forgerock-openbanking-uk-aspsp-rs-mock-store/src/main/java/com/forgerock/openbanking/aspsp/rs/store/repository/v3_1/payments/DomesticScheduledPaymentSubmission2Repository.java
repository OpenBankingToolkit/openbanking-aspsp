/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments;

import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRDomesticScheduledPayment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DomesticScheduledPaymentSubmission2Repository extends MongoRepository<FRDomesticScheduledPayment, String> {
}
