/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.account;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@RequestMapping(value = "/api/accounts")
public interface AccountsApi {

    @RequestMapping(value = "/search/findByUserId",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity<List<FRAccountWithBalance>> getAccounts(
            @RequestParam("userId") String userId,
            @RequestParam(value = "withBalance", required = false, defaultValue = "false") Boolean withBalance
    );

    @RequestMapping(value = "/permissions/{accountId}",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<FRAccount3> findByAccountId(
            @PathVariable("accountId") String accountId,
            @RequestParam("permissions") List<String> permissions
    );

    @RequestMapping(value = "/search/findByIdentification",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<Optional<FRAccount3>> findByIdentification(
            @RequestParam("identification") String identification
    );

    @RequestMapping(value = "/{accountId}",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<FRAccount3> getAccount(
            @PathVariable("accountId") String accountId
    );
}