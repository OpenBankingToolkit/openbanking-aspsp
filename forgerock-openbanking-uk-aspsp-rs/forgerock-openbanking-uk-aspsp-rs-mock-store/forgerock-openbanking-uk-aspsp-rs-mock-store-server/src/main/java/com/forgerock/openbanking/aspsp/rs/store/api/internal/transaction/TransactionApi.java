/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.transaction;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/api/transactions")
public interface TransactionApi {

    @RequestMapping(value = "/",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.POST)
    ResponseEntity<FRTransaction5> create(
            @RequestBody FRTransaction5 transaction
    );
}
