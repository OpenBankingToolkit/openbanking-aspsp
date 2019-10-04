/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_2;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "funds-confirmation", description = "the funds-confirmation API")

@RequestMapping(value = "/open-banking/v3.1.2/cbpii")
public interface FundsConfirmationsApi extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_1.FundsConfirmationsApi {
}
