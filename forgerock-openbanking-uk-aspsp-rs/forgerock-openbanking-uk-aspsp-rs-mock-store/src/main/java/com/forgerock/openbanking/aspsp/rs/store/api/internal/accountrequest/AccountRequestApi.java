/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.accountrequest;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccountRequest1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/api/account-requests")
public interface AccountRequestApi {

    @RequestMapping(value = "/",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT)
    ResponseEntity<FRAccountRequest1> save(
            @RequestBody FRAccountRequest1 accountRequest1
    );


    @RequestMapping(value = "/{accountRequestId}",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity read(
            @PathVariable("accountRequestId") String accountRequestId
    );
}
