/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.payment.OBInitiation1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;

import java.util.Date;

/**
 * FRPaymentConsent setup entity for storing in DB
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FRPaymentSetup1 implements FRPaymentConsent, Persistable<String> {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBPaymentSetup1 paymentSetupRequest;

    @Indexed
    public String accountId;
    @Indexed
    public String userId;
    @Indexed
    public String pispId;
    public String pispName;
    public String idempotencyKey;

    @CreatedDate
    public DateTime created;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    @Override
    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();

    }

    @Override
    public OBInitiation1 getInitiation() {
        return paymentSetupRequest.getData().getInitiation();
    }

    @Override
    public OBRisk1 getRisk() {
        return paymentSetupRequest.getRisk();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }
}
