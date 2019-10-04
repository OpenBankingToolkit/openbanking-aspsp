/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.payment;

import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import uk.org.openbanking.datamodel.payment.OBWriteFile1;

import java.util.Date;

@Builder
@Data
public class FRFilePaymentSubmission1 {

    @Id
    @Indexed
    public String id;

    public OBWriteFile1 filePayment;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    public OBVersion version;
}
