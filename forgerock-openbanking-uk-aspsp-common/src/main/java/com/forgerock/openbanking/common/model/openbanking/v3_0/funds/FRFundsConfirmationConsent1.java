/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.funds;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
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
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRFundsConfirmationConsent1 implements Persistable<String> {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBFundsConfirmationConsent1 fundsConfirmationConsent;

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

    public OBVersion obVersion;

    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    public OBCashAccount3 getDebtorAccount() {
        return fundsConfirmationConsent.getData().getDebtorAccount();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }
}
