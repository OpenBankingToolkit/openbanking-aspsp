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
package com.forgerock.openbanking.common.model.openbanking.v3_1_5.converter.payment;

import uk.org.openbanking.datamodel.payment.OBDebtorIdentification1;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2DataInitiationDebtorAccount;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationDebtorAccount;

public class DebtorIdentificationConverter {

    public static OBDebtorIdentification1 toDebtorIdentification1(OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return debtorAccount == null ? null : new OBDebtorIdentification1().name(debtorAccount.getName());
    }

    public static OBDebtorIdentification1 toDebtorIdentification1(OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount) {
        return null;
    }
}
