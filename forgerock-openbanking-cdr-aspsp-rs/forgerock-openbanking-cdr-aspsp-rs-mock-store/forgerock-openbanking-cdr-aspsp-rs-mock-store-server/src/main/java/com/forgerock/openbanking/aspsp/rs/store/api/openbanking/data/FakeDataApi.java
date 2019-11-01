/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.exceptions.OBErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "/api/fake-data")
public interface FakeDataApi {

    @RequestMapping(value = "/generate",
            method = RequestMethod.POST)
    ResponseEntity generateFakeData(
            @RequestParam("userId") String userId,
            @RequestParam("username") String username,
            @RequestParam(name = "profile", required = false) String profile
    ) throws OBErrorException;
}
