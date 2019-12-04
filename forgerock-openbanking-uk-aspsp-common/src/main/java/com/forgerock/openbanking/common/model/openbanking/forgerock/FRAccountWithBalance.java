/**
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
