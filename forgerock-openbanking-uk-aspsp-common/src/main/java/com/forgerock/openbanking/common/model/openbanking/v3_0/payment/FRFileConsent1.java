/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.payment;


import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRFileConsent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFileType;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import lombok.*;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import uk.org.openbanking.datamodel.payment.OBFile1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent1;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRFileConsent1 implements FRPaymentConsent, FRFileConsent {
    @Id
    @Indexed
    public String id;

    @Indexed
    public ConsentStatusCode status;

    public OBWriteFileConsent1 writeFileConsent;

    @Indexed
    public String accountId;

    @Indexed
    public String userId;

    @Indexed
    public String pispId;

    public String pispName;

    @CreatedDate
    public DateTime created;

    public DateTime statusUpdate;

    @LastModifiedDate
    public Date updated;

    @ToString.Exclude // Makes the log too verbose if large files are uploaded
    public List<FRFilePayment> payments;

    @ToString.Exclude // Makes the log too verbose if large files are uploaded
    public String fileContent;

    public OBVersion version;

    @Override
    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    @Override
    public OBFile1 getInitiation() {
        return writeFileConsent.getData().getInitiation();
    }

    @Override
    public OBRisk1 getRisk() { return null; }

    public PaymentFileType getFileType() {
        return PaymentFileType.fromFileType(writeFileConsent.getData().getInitiation().getFileType());
    }

    public String getFileHash() {
        return writeFileConsent.getData().getInitiation().getFileHash();
    }
}
