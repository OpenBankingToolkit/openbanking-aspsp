/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.account.data;


import uk.org.openbanking.datamodel.account.OBParty1;

import java.util.ArrayList;
import java.util.List;

public class FRUserData3 {

    public String userName;
    public OBParty1 party;
    public List<FRAccountData3> accountDatas = new ArrayList<>();

    public OBParty1 getParty() {
        return party;
    }

    public void setParty(OBParty1 party) {
        this.party = party;
    }

    public FRUserData3() {}

    public FRUserData3(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FRAccountData3> getAccountDatas() {
        return accountDatas;
    }

    public void setAccountDatas(List<FRAccountData3> accountDatas) {
        this.accountDatas = accountDatas;
    }

    public void addAccountData(FRAccountData3 accountData) {
        accountDatas.add(accountData);
    }
}
