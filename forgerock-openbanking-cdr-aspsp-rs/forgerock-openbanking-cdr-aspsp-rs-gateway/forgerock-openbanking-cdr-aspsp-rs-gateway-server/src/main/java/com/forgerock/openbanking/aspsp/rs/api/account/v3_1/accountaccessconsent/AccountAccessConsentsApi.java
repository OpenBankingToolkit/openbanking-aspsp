/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1.accountaccessconsent;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Api(tags = "v3.1-account-access-consents", description = "the account-access-consents API")
@OpenBankingAPI(
        obVersion = "3.1",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.ACCOUNT_ACCESS_CONSENT
)
@RequestMapping(value = "/open-banking/v3.1/aisp")
public interface AccountAccessConsentsApi extends com.forgerock.openbanking.aspsp.rs.api.account.v3_0.accountaccessconsent.AccountAccessConsentsApi {
}