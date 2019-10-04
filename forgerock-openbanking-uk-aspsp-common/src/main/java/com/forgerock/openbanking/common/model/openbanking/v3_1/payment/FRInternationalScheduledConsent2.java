/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_1.payment;


import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.currency.CurrencyRateService;
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
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;
import uk.org.openbanking.datamodel.payment.OBInternationalScheduled2;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledConsent2;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FRInternationalScheduledConsent2 implements FRPaymentConsent, Persistable<String> {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBWriteInternationalScheduledConsent2 internationalScheduledConsent;

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
    public DateTime statusUpdate;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    @Override
    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    @Override
    public OBInternationalScheduled2 getInitiation() {
        return internationalScheduledConsent.getData().getInitiation();
    }

    @Override
    public OBRisk1 getRisk() {
        return internationalScheduledConsent.getRisk();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }

    /**
     * Note: we do not persist the calculated exchange rate fields (such as rate value and expiry date) as the exchange rate object in the initiation must match exactly what the user submitted. We could persist the
     * calculated exchange rate separately but currently it is easier just to generate dynamically as the rate it always the same value for testing purposes.
     * @return OBExchangeRate2 with rate nd expiry date fields populated where appropriate
     */
    public OBExchangeRate2 getCalculatedExchangeRate() {
        return CurrencyRateService.getCalculatedExchangeRate(getInitiation().getExchangeRateInformation(), created);
    }
}
