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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.internationalpayments;

import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBReference;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "international-payments", description = "the international-payments API")
@OpenBankingAPI(
        obVersion = "3.1.2",
        obGroupName = OBGroupName.PISP,
        obReference = OBReference.INTERNATIONAL_PAYMENTS
)
@RequestMapping(value = "/open-banking/v3.1.2/pisp")
public interface InternationalPaymentsApi extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.internationalpayments.InternationalPaymentsApi {

}
