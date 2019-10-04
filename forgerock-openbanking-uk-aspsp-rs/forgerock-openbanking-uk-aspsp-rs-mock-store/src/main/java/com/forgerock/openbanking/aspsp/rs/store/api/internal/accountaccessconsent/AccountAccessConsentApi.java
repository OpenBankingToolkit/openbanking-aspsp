/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.accountaccessconsent;

import com.forgerock.openbanking.commons.model.openbanking.v3_0.account.FRAccountAccessConsent1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/api/account-access-consents")
public interface AccountAccessConsentApi {

    @RequestMapping(value = "/",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT)
    ResponseEntity<FRAccountAccessConsent1> save(
            @RequestBody FRAccountAccessConsent1 accountAccessConsent1
    );


    @RequestMapping(value = "/{consentId}",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity read(
            @PathVariable("consentId") String consentId
    );
}
