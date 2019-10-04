/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.internationalscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.ExchangeRateVerifier;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.services.store.RsStoreGateway;
import com.forgerock.openbanking.commons.services.store.payment.InternationalScheduledPaymentService;
import org.springframework.stereotype.Controller;

@Controller("InternationalScheduledPaymentConsentsApiV3.1.2")
public class InternationalScheduledPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.internationalscheduledpayments.InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {

    public InternationalScheduledPaymentConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway, ExchangeRateVerifier exchangeRateVerifier, InternationalScheduledPaymentService paymentsService) {
        super(rsEndpointWrapperService, rsStoreGateway, exchangeRateVerifier, paymentsService);
    }
}
