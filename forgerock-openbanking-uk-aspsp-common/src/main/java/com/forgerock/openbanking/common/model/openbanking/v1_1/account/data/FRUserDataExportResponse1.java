/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account.data;


import java.util.ArrayList;
import java.util.List;

public class FRUserDataExportResponse1 {

    public String userName;
    public List<FRAccountDataExportResponse1> accountDatas = new ArrayList<>();

    public FRUserDataExportResponse1() {}

    public FRUserDataExportResponse1(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FRAccountDataExportResponse1> getAccountDatas() {
        return accountDatas;
    }

    public void setAccountDatas(List<FRAccountDataExportResponse1> accountDatas) {
        this.accountDatas = accountDatas;
    }

    public void addAccountData(FRAccountDataExportResponse1 accountData) {
        accountDatas.add(accountData);
    }
}
