/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;


import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.common.services.openbanking.converter.FRAccountConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.account.OBCashBalance1;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FRAccountWithBalance extends FRAccount3 {

    private List<OBCashBalance1> balances;

    public FRAccountWithBalance(FRAccount2 account, List<OBCashBalance1> balances) {
        this.id = account.id;
        this.userID = account.userID;
        this.account = FRAccountConverter.toOBAccount3(account.getAccount());
        this.latestStatementId = account.latestStatementId;
        this.created = account.created;
        this.updated = account.updated;
        this.balances = balances;
    }

    public FRAccountWithBalance(FRAccount3 account, List<OBCashBalance1> balances) {
        this.id = account.id;
        this.userID = account.userID;
        this.account = account.account;
        this.latestStatementId = account.latestStatementId;
        this.created = account.created;
        this.updated = account.updated;
        this.balances = balances;
    }
}
