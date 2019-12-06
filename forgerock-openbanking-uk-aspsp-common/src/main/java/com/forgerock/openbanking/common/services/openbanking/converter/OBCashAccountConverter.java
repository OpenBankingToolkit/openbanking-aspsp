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
package com.forgerock.openbanking.common.services.openbanking.converter;

import uk.org.openbanking.datamodel.account.OBCashAccount1;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.account.OBCashAccount6;
import uk.org.openbanking.datamodel.payment.OBCashAccountCreditor3;
import uk.org.openbanking.datamodel.payment.OBCashAccountDebtor4;

public class OBCashAccountConverter {
    public static OBCashAccount3 toOBCashAccount3(OBCashAccount6 obCashAccount6) {
        return FRModelMapper.map(obCashAccount6, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccountCreditor3 creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccountDebtor4 creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount5 obCashAccount5) {
        return FRModelMapper.map(obCashAccount5, OBCashAccount3.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccount5.class);
    }

    public static OBCashAccount6 toOBCashAccount6(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccount6.class);
    }

    public static OBCashAccount1 toOBCashAccount1(OBCashAccount5 obCashAccount5) {
        return FRModelMapper.map(obCashAccount5, OBCashAccount1.class);
    }

    public static OBCashAccountDebtor4 toOBCashAccountDebtor4(OBCashAccount3 debtorAccount) {
        return FRModelMapper.map(debtorAccount, OBCashAccountDebtor4.class);
    }

    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(OBCashAccount3 creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccountCreditor3.class);
    }
}
