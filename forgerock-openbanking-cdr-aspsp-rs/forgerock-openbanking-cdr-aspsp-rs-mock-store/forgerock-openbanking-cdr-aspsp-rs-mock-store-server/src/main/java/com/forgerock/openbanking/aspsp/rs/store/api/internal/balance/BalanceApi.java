/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.balance;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.util.Optional;

@RequestMapping(value = "/api/balances")
public interface BalanceApi {

    @RequestMapping(value = "/search/findByAccountId",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET)
    ResponseEntity<Optional<FRBalance1>> findByAcountId(
            @RequestParam("accountId") String accountId,
            @RequestParam("type") OBBalanceType1Code type
    );

    @RequestMapping(value = "/",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.PUT)
    ResponseEntity save(
            @RequestBody FRBalance1 balance1
    );
}
