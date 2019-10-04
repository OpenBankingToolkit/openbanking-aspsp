/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_0.accounts;


import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification4Code;
import uk.org.openbanking.datamodel.service.converter.OBExternalAccountIdentificationConverter;

import java.util.Optional;

@Controller("AccountsApiV3.0")
public class AccountsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.accounts.AccountsApiController implements AccountsApi {

    /**
     * Because we always use latest model in persistent store, older API controller may need to do conversions on some of values e.g. the Account identifier codes.
     * This can be overidden is later versions of controller.
     */
    protected FRAccount2 convertAccounts(FRAccount2 account) {
        if (account.getAccount().getAccount() != null) {
            account.getAccount().getAccount()
                    .forEach(this::checkAndConvertV2SchemeNameToV3);
        }
        return account;
    }

    // This is a special case because V2.0 used enum and V3.x uses String so we cannot do simple type conversion - we need to map the V2.0 strings into a corresponding V3.x type if possible
    private void checkAndConvertV2SchemeNameToV3(OBCashAccount3 obCashAccount3) {
        OBExternalAccountIdentification3Code code3 = OBExternalAccountIdentification3Code.fromValue(obCashAccount3.getSchemeName());
        if (code3 == null) {
            // Not a V2.0 OBExternalAccountIdentification3Code scheme name so no action required
            return;
        }
        Optional<OBExternalAccountIdentification4Code> code4 = Optional.ofNullable(OBExternalAccountIdentificationConverter.toOBExternalAccountIdentification4(code3));
        obCashAccount3.setSchemeName(code4.map(OBExternalAccountIdentification4Code::toString).orElse(""));
    }

}
