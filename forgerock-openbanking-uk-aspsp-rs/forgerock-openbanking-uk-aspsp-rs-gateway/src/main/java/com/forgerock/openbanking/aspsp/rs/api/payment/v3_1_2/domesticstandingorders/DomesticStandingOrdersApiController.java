/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.services.openbanking.frequency.FrequencyService;
import com.forgerock.openbanking.commons.services.store.RsStoreGateway;
import com.forgerock.openbanking.commons.services.store.account.standingorder.StandingOrderService;
import com.forgerock.openbanking.commons.services.store.payment.DomesticStandingOrderService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Controller("DomesticStandingOrdersApiV3.1.2")
@Slf4j
public class DomesticStandingOrdersApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.domesticstandingorders.DomesticStandingOrdersApiController implements DomesticStandingOrdersApi {

    public DomesticStandingOrdersApiController(DomesticStandingOrderService paymentsService, RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway, StandingOrderService standingOrderService, FrequencyService frequencyService, TppStoreService tppStoreService) {
        super(paymentsService, rsEndpointWrapperService, rsStoreGateway, standingOrderService, frequencyService, tppStoreService);
    }
}
