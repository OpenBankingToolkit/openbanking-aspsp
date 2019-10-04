/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.internationalpayments;

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.ExchangeRateVerifier;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.services.store.RsStoreGateway;
import com.forgerock.openbanking.commons.services.store.payment.InternationalPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("InternationalPaymentConsentsApiV3.1.1")
public class InternationalPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1.internationalpayments.InternationalPaymentConsentsApiController implements InternationalPaymentConsentsApi {

    @Autowired
    public InternationalPaymentConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway, ExchangeRateVerifier exchangeRateVerifier, InternationalPaymentService paymentsService) {
        super(rsEndpointWrapperService, rsStoreGateway, exchangeRateVerifier, paymentsService);
    }
}
