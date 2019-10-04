/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.funds.v3_1;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.services.store.RsStoreGateway;
import org.springframework.stereotype.Controller;

@Controller("FundsConfirmationConsentsApiV3.1")
public class FundsConfirmationConsentsApiController extends com.forgerock.openbanking.aspsp.rs.api.funds.v3_0.FundsConfirmationConsentsApiController implements FundsConfirmationConsentsApi {

    public FundsConfirmationConsentsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        super(rsEndpointWrapperService, rsStoreGateway);
    }
}
