/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRPaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmission1;

import java.util.Date;

/**
 * FRPaymentConsent submission entity for storing in DB
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FRPaymentSubmission1 implements FRPaymentSubmission {
    @Id
    @Indexed
    public String id;

    public OBPaymentSubmission1 paymentSubmission;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;
    public String idempotencyKey;

    public OBVersion obVersion;

}
