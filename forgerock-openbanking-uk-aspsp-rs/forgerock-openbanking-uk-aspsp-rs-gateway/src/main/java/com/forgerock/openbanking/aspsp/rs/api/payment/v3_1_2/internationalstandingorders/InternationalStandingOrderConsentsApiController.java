/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_2.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import org.springframework.stereotype.Controller;

@Controller("InternationalStandingOrderConsentsApiV3.1.2")
public class InternationalStandingOrderConsentsApiController extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_1.internationalstandingorders.InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {

    public InternationalStandingOrderConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        super(rsEndpointWrapperService, rsStoreGateway);
    }
}
