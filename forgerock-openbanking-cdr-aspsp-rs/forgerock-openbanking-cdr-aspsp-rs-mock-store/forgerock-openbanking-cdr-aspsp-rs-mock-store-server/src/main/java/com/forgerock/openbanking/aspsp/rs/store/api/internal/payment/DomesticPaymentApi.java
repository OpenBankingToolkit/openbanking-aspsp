/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping(value = "/api/domestic-payments")
public interface DomesticPaymentApi {

    @RequestMapping(value = "/{paymentId}",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity get(
            @PathVariable("paymentId") String paymentId
    );

    @RequestMapping(value = "/search/findByStatus",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<Collection<FRDomesticConsent2>> findByStatus(
            @RequestParam("status") ConsentStatusCode status
    );

    @RequestMapping(value = "/",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.PUT)
    ResponseEntity<FRDomesticConsent2> update(
            @RequestBody FRDomesticConsent2 payment1
    );
}
