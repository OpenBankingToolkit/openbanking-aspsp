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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1.data;


import java.util.ArrayList;
import java.util.List;

public class FRUserDataImportRequest1 {

    public String userName;
    public List<FRAccountDataImportRequest1> accountDatas = new ArrayList<>();

    public FRUserDataImportRequest1() {}

    public FRUserDataImportRequest1(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<FRAccountDataImportRequest1> getAccountDatas() {
        return accountDatas;
    }

    public void setAccountDatas(List<FRAccountDataImportRequest1> accountDatas) {
        this.accountDatas = accountDatas;
    }

    public void addAccountData(FRAccountDataImportRequest1 accountData) {
        accountDatas.add(accountData);
    }
}