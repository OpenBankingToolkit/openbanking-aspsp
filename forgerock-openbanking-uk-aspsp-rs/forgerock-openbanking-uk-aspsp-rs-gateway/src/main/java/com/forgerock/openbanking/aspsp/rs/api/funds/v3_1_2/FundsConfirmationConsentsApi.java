/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.funds.v3_1_2;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "funds-confirmation-consents", description = "the funds-confirmation-consents API")
@OpenBankingAPI(
        obVersion = "3.1.2",
        obGroupName = OBGroupName.CBPII,
        obReference = OBReference.FUNDS
)
@RequestMapping(value = "/open-banking/v3.1.2/cbpii")
public interface FundsConfirmationConsentsApi extends com.forgerock.openbanking.aspsp.rs.api.funds.v3_1_1.FundsConfirmationConsentsApi {
}
