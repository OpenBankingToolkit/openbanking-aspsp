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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_0.accounts;

import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRAccount4;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBAccount3Account;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification4Code;
import uk.org.openbanking.datamodel.service.converter.account.OBExternalAccountIdentificationConverter;

import java.util.Optional;

@Controller("AccountsApiV3.0")
public class AccountsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.accounts.AccountsApiController implements AccountsApi {

    /**
     * Because we always use latest model in persistent store, older API controller may need to do conversions on some of values e.g. the Account identifier codes.
     * This can be overidden is later versions of controller.
     */
    protected FRAccount4 convertAccounts(FRAccount4 account) {
        if (account.getAccount().getAccount() != null) {
            account.getAccount().getAccount()
                    .forEach(this::checkAndConvertV2SchemeNameToV3);
        }
        return account;
    }

    // This is a special case because V2.0 used enum and V3.x uses String so we cannot do simple type conversion - we need to map the V2.0 strings into a corresponding V3.x type if possible
    private void checkAndConvertV2SchemeNameToV3(OBAccount3Account account) {
        OBExternalAccountIdentification3Code code3 = OBExternalAccountIdentification3Code.fromValue(account.getSchemeName());
        if (code3 == null) {
            // Not a V2.0 OBExternalAccountIdentification3Code scheme name so no action required
            return;
        }
        Optional<OBExternalAccountIdentification4Code> code4 = Optional.ofNullable(OBExternalAccountIdentificationConverter.toOBExternalAccountIdentification4(code3));
        account.setSchemeName(code4.map(OBExternalAccountIdentification4Code::toString).orElse(""));
    }

}
