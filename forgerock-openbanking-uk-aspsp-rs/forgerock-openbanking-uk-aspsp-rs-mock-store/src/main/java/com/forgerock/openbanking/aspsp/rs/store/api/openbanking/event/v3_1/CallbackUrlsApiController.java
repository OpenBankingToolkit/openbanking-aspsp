/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1;

import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.events.CallbackUrlsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("CallbackUrlsApiV3.1")
@Slf4j
public class CallbackUrlsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_0.CallbackUrlsApiController implements CallbackUrlsApi {

    public CallbackUrlsApiController(CallbackUrlsRepository callbackUrlsRepository, TppRepository tppRepository) {
        super(callbackUrlsRepository, tppRepository);
    }
}
