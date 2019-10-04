/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.payments;

import com.forgerock.openbanking.commons.model.openbanking.v3_0.payment.FRFilePaymentSubmission1;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilePaymentSubmission1Repository extends MongoRepository<FRFilePaymentSubmission1, String> {
}
