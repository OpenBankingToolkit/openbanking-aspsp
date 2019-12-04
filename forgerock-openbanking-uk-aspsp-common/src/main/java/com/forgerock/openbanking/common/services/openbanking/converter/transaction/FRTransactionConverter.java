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
package com.forgerock.openbanking.common.services.openbanking.converter.transaction;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRTransaction1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRTransaction2;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRTransaction3;
import com.forgerock.openbanking.common.model.openbanking.v3_1.account.FRTransaction4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;

public class FRTransactionConverter {

    // Upgrade methods

    public static FRTransaction3 toTransaction3(FRTransaction2 transaction2) {
        FRTransaction3 frTransaction3 =  new FRTransaction3();
        frTransaction3.setAccountId(transaction2.getAccountId());
        frTransaction3.setId(transaction2.getId());
        frTransaction3.setCreated(transaction2.getCreated());
        frTransaction3.setUpdated(transaction2.getUpdated());
        frTransaction3.setBookingDateTime(transaction2.getBookingDateTime());
        frTransaction3.setTransaction(OBTransactionConverter.toTransaction3(transaction2.getTransaction()));
        frTransaction3.setStatementIds(transaction2.getStatementIds());
        return frTransaction3;
    }

    public static FRTransaction4 toTransaction4(FRTransaction3 transaction3) {
        FRTransaction4 frTransaction4 =  new FRTransaction4();
        frTransaction4.setAccountId(transaction3.getAccountId());
        frTransaction4.setId(transaction3.getId());
        frTransaction4.setCreated(transaction3.getCreated());
        frTransaction4.setUpdated(transaction3.getUpdated());
        frTransaction4.setBookingDateTime(transaction3.getBookingDateTime());
        frTransaction4.setTransaction(OBTransactionConverter.toTransaction4(transaction3.getTransaction()));
        frTransaction4.setStatementIds(transaction3.getStatementIds());
        return frTransaction4;
    }


    public static FRTransaction5 toTransaction5(FRTransaction4 frTransaction4) {
        FRTransaction5 frTransaction5 =  new FRTransaction5();
        frTransaction5.setAccountId(frTransaction4.getAccountId());
        frTransaction5.setId(frTransaction4.getId());
        frTransaction5.setCreated(frTransaction4.getCreated());
        frTransaction5.setUpdated(frTransaction4.getUpdated());
        frTransaction5.setBookingDateTime(frTransaction4.getBookingDateTime());
        frTransaction5.setTransaction(OBTransactionConverter.toOBTransaction5(frTransaction4.getTransaction()));
        frTransaction5.setStatementIds(frTransaction4.getStatementIds());
        return frTransaction5;
    }

    // Backwards compatibility methods

    public static FRTransaction4 toTransaction4(FRTransaction5 frTransaction5) {
        FRTransaction4 frTransaction4 =  new FRTransaction4();
        frTransaction4.setAccountId(frTransaction5.getAccountId());
        frTransaction4.setId(frTransaction5.getId());
        frTransaction4.setCreated(frTransaction5.getCreated());
        frTransaction4.setUpdated(frTransaction5.getUpdated());
        frTransaction4.setBookingDateTime(frTransaction5.getBookingDateTime());
        frTransaction4.setTransaction(OBTransactionConverter.toOBTransaction4(frTransaction5.getTransaction()));
        frTransaction4.setStatementIds(frTransaction5.getStatementIds());
        return frTransaction4;
    }

    public static FRTransaction3 toTransaction3(FRTransaction5 transaction5) {
        FRTransaction3 transaction3 =  new FRTransaction3();
        transaction3.setAccountId(transaction5.getAccountId());
        transaction3.setId(transaction5.getId());
        transaction3.setCreated(transaction5.getCreated());
        transaction3.setUpdated(transaction5.getUpdated());
        transaction3.setBookingDateTime(transaction5.getBookingDateTime());
        transaction3.setTransaction(OBTransactionConverter.toOBTransaction3(transaction5.getTransaction()));
        transaction3.setStatementIds(transaction5.getStatementIds());
        return transaction3;
    }

    public static FRTransaction2 toTransaction2(FRTransaction5 transaction5) {
        FRTransaction2 transaction2 =  new FRTransaction2();
        transaction2.setAccountId(transaction5.getAccountId());
        transaction2.setId(transaction5.getId());
        transaction2.setCreated(transaction5.getCreated());
        transaction2.setUpdated(transaction5.getUpdated());
        transaction2.setBookingDateTime(transaction5.getBookingDateTime());
        transaction2.setTransaction(OBTransactionConverter.toOBTransaction2(transaction5.getTransaction()));
        transaction2.setStatementIds(transaction5.getStatementIds());
        return transaction2;
    }

    public static FRTransaction1 toTransaction1(FRTransaction5 transaction5) {
        FRTransaction1 transaction1 =  new FRTransaction1();
        transaction1.setAccountId(transaction5.getAccountId());
        transaction1.setId(transaction5.getId());
        transaction1.setCreated(transaction5.getCreated());
        transaction1.setUpdated(transaction5.getUpdated());
        transaction1.setBookingDateTime(transaction5.getBookingDateTime());
        transaction1.setTransaction(OBTransactionConverter.toOBTransaction1(transaction5.getTransaction()));
        return transaction1;
    }

}
