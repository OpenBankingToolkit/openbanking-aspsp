/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRAccountData4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRUserData4;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/data")
public interface DataApi {

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    ResponseEntity<Page<FRAccountData4>> exportAccountData(
            @PageableDefault Pageable pageable
    );

    @RequestMapping(value = "/user/has-data", method = RequestMethod.GET)
    ResponseEntity<Boolean> hasData(
            @RequestParam("userId") String userId
    );

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    ResponseEntity<FRUserData4> exportUserData(
            @RequestParam("userId") String userId
    );

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    ResponseEntity updateUserData(
            @RequestBody FRUserData4 userData
    );

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    ResponseEntity importUserData(
            @RequestBody FRUserData4 userData
    );

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    ResponseEntity<Boolean> deleteUserData(
            @RequestParam("userId") String userId
    );
}
