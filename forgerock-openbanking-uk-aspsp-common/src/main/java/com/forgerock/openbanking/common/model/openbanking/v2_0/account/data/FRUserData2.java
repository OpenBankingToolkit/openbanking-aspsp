/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v2_0.account.data;


import uk.org.openbanking.datamodel.account.OBParty1;

import java.util.ArrayList;
import java.util.List;

public class FRUserData2 {

    public String userName;
    public OBParty1 party;
    public List<FRAccountData2> accountDatas = new ArrayList<>();

    public OBParty1 getParty() {
        return party;
    }

    public void setParty(OBParty1 party) {
        this.party = party;
    }

    public FRUserData2() {}

    public FRUserData2(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FRAccountData2> getAccountDatas() {
        return accountDatas;
    }

    public void setAccountDatas(List<FRAccountData2> accountDatas) {
        this.accountDatas = accountDatas;
    }

    public void addAccountData(FRAccountData2 accountData) {
        accountDatas.add(accountData);
    }
}
