/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.payment;


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
import org.springframework.data.mongodb.core.index.Indexed;
import uk.org.openbanking.datamodel.payment.OBInternationalStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderConsent1;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRInternationalStandingOrderConsent1 implements FRPaymentConsent {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBWriteInternationalStandingOrderConsent1 internationalStandingOrderConsent;

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

    public OBVersion version;

    @Override
    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    @Override
    public OBInternationalStandingOrder1 getInitiation() {
        return internationalStandingOrderConsent.getData().getInitiation();
    }

    @Override
    public OBRisk1 getRisk() {
        return internationalStandingOrderConsent.getRisk();
    }
}
