/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.account.OBParty2;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FRUserData4 {

    public String userName;
    public OBParty2 party;
    public List<FRAccountData4> accountDatas = new ArrayList<>();

    public FRUserData4(String userName) {
        this.userName = userName;
    }

    public void addAccountData(FRAccountData4 accountData) {
        accountDatas.add(accountData);
    }
}
