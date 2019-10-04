/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments;

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalPaymentSubmission2;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InternationalPaymentSubmission2Repository extends MongoRepository<FRInternationalPaymentSubmission2, String> {
}
