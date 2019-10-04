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
package com.forgerock.openbanking.aspsp.rs.api.payment.v2_0.paymentsubmissions;

import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBReference;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "v2.0-Payments", description = "the payment-submissions API")
@OpenBankingAPI(
        obVersion = "2.0",
        obGroupName = OBGroupName.PISP,
        obReference = OBReference.SINGLE_PAYMENTS_SUBMISSION
)
@RequestMapping(value = "/open-banking/v2.0")
public interface PaymentSubmissionsApi extends com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.paymentsubmissions.PaymentSubmissionsApi {
}
