/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.domesticpayments;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "domestic-payments", description = "the domestic-payments API")
@OpenBankingAPI(
        obVersion = "3.1.1",
        obGroupName = OBGroupName.PISP,
        obReference = OBReference.DOMESTIC_PAYMENTS
)
@RequestMapping(value = "/open-banking/v3.1.1/pisp")
public interface DomesticPaymentsApi extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1.domesticpayments.DomesticPaymentsApi {

}
