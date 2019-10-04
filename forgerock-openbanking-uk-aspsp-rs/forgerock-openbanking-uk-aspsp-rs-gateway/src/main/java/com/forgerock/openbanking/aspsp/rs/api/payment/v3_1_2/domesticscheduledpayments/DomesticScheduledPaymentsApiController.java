/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.domesticscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.account.scheduledpayment.ScheduledPaymentService;
import com.forgerock.openbanking.common.services.store.payment.DomesticScheduledPaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller("DomesticScheduledPaymentsApiV3.1.2")
public class DomesticScheduledPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.domesticscheduledpayments.DomesticScheduledPaymentsApiController implements DomesticScheduledPaymentsApi {

    @Autowired
    public DomesticScheduledPaymentsApiController(DomesticScheduledPaymentService paymentsService, RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway, ScheduledPaymentService scheduledPaymentService, TppStoreService tppStoreService) {
        super(paymentsService, rsEndpointWrapperService, rsStoreGateway, scheduledPaymentService, tppStoreService);
    }
}
