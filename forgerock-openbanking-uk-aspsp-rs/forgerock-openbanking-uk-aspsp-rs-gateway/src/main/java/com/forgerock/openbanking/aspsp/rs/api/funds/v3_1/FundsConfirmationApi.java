/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.funds.v3_1;

import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBReference;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "funds-confirmation", description = "the funds-confirmation API")
@OpenBankingAPI(
        obVersion = "3.1",
        obGroupName = OBGroupName.CBPII,
        obReference = OBReference.FUNDS
)
@RequestMapping(value = "/open-banking/v3.1/cbpii")
public interface FundsConfirmationApi extends com.forgerock.openbanking.aspsp.rs.api.funds.v3_0.FundsConfirmationApi {

}
