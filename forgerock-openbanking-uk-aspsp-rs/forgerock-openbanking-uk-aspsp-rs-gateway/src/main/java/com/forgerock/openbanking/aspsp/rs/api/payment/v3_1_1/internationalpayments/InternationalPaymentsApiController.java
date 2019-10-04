/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.internationalpayments;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("InternationalPaymentsApiV3.1.1")
@RequestMapping(value = "/open-banking/v3.1.1/pisp")
public class InternationalPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1.internationalpayments.InternationalPaymentsApiController implements InternationalPaymentsApi {

}
