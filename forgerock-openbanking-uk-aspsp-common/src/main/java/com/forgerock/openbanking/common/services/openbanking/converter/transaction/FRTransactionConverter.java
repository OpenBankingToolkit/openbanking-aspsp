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
package com.forgerock.openbanking.common.services.openbanking.converter.transaction;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1.FRTransaction1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0.FRTransaction2;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_0.FRTransaction3;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1.FRTransaction4;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_5.FRTransaction6;

import static com.forgerock.openbanking.common.services.openbanking.converter.transaction.OBTransactionConverter.toOBTransaction1;
import static com.forgerock.openbanking.common.services.openbanking.converter.transaction.OBTransactionConverter.toOBTransaction2;
import static com.forgerock.openbanking.common.services.openbanking.converter.transaction.OBTransactionConverter.toOBTransaction3;
import static com.forgerock.openbanking.common.services.openbanking.converter.transaction.OBTransactionConverter.toOBTransaction4;


public class FRTransactionConverter {

    public static FRTransaction1 toTransaction1(FRTransaction6 transaction6) {
        FRTransaction1 transaction1 = new FRTransaction1();
        transaction1.setAccountId(transaction6.getAccountId());
        transaction1.setId(transaction6.getId());
        transaction1.setCreated(transaction6.getCreated());
        transaction1.setUpdated(transaction6.getUpdated());
        transaction1.setBookingDateTime(transaction6.getBookingDateTime());
        transaction1.setTransaction(toOBTransaction1(transaction6.getTransaction()));
        return transaction1;
    }

    public static FRTransaction2 toTransaction2(FRTransaction6 transaction6) {
        FRTransaction2 frTransaction2 = new FRTransaction2();
        frTransaction2.setAccountId(transaction6.getAccountId());
        frTransaction2.setId(transaction6.getId());
        frTransaction2.setCreated(transaction6.getCreated());
        frTransaction2.setUpdated(transaction6.getUpdated());
        frTransaction2.setBookingDateTime(transaction6.getBookingDateTime());
        frTransaction2.setTransaction(toOBTransaction2(transaction6.getTransaction()));
        frTransaction2.setStatementIds(transaction6.getStatementIds());
        return frTransaction2;
    }

    public static FRTransaction3 toTransaction3(FRTransaction2 transaction2) {
        FRTransaction3 frTransaction3 = new FRTransaction3();
        frTransaction3.setAccountId(transaction2.getAccountId());
        frTransaction3.setId(transaction2.getId());
        frTransaction3.setCreated(transaction2.getCreated());
        frTransaction3.setUpdated(transaction2.getUpdated());
        frTransaction3.setBookingDateTime(transaction2.getBookingDateTime());
        frTransaction3.setTransaction(OBTransactionConverter.toTransaction3(transaction2.getTransaction()));
        frTransaction3.setStatementIds(transaction2.getStatementIds());
        return frTransaction3;
    }

    public static FRTransaction3 toTransaction3(FRTransaction6 transaction6) {
        FRTransaction3 transaction3 = new FRTransaction3();
        transaction3.setAccountId(transaction6.getAccountId());
        transaction3.setId(transaction6.getId());
        transaction3.setCreated(transaction6.getCreated());
        transaction3.setUpdated(transaction6.getUpdated());
        transaction3.setBookingDateTime(transaction6.getBookingDateTime());
        transaction3.setTransaction(toOBTransaction3(transaction6.getTransaction()));
        return transaction3;
    }

    public static FRTransaction4 toTransaction4(FRTransaction3 transaction3) {
        FRTransaction4 frTransaction4 = new FRTransaction4();
        frTransaction4.setAccountId(transaction3.getAccountId());
        frTransaction4.setId(transaction3.getId());
        frTransaction4.setCreated(transaction3.getCreated());
        frTransaction4.setUpdated(transaction3.getUpdated());
        frTransaction4.setBookingDateTime(transaction3.getBookingDateTime());
        frTransaction4.setTransaction(OBTransactionConverter.toTransaction4(transaction3.getTransaction()));
        frTransaction4.setStatementIds(transaction3.getStatementIds());
        return frTransaction4;
    }

    public static FRTransaction4 toTransaction4(FRTransaction6 transaction6) {
        FRTransaction4 transaction4 = new FRTransaction4();
        transaction4.setAccountId(transaction6.getAccountId());
        transaction4.setId(transaction6.getId());
        transaction4.setCreated(transaction6.getCreated());
        transaction4.setUpdated(transaction6.getUpdated());
        transaction4.setBookingDateTime(transaction6.getBookingDateTime());
        transaction4.setTransaction(toOBTransaction4(transaction6.getTransaction()));
        return transaction4;
    }
}
