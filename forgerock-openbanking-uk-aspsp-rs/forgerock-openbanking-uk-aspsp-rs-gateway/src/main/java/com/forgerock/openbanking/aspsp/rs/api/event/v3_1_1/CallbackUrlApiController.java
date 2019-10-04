/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.event.v3_1_1;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("CallbackUrlApiV3.1.1")
@Slf4j
public class CallbackUrlApiController extends com.forgerock.openbanking.aspsp.rs.api.event.v3_1.CallbackUrlApiController implements CallbackUrlApi {

    public CallbackUrlApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        super(rsEndpointWrapperService, rsStoreGateway);
    }
}
