/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.callbackurl;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.events.CallbackUrlsRepository;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
@Slf4j
public class CallbackUrlApiController implements CallbackUrlsApi {

    private final CallbackUrlsRepository callbackUrlsRepository;

    public CallbackUrlApiController(CallbackUrlsRepository callbackUrlsRepository) {
        this.callbackUrlsRepository = callbackUrlsRepository;
    }

    @Override
    public ResponseEntity<Collection<FRCallbackUrl1>> findByTppId(
            @RequestParam("tppId")  String tppId) {
        log.debug("Find all callback URLs for TPP: {}", tppId);
        return ResponseEntity.ok(callbackUrlsRepository.findByTppId(tppId));
    }
}
