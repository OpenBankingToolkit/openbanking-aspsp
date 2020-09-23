/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_0.data;


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
