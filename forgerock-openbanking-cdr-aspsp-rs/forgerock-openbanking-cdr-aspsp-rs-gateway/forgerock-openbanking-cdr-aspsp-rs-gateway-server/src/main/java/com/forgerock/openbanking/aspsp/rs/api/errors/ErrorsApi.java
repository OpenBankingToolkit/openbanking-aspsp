/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.errors;


import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

@RequestMapping(value = "/open-banking/api/errors")
public interface ErrorsApi {

    @RequestMapping(value = "/",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<Set<OBRIErrorType>> getAllErrorTypes();
}
