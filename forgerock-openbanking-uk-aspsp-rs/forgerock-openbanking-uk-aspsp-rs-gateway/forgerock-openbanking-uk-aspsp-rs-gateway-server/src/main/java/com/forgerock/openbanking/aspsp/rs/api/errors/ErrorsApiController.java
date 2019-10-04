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
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ErrorsApiController implements ErrorsApi {

    @Override
    public ResponseEntity<Set<OBRIErrorType>> getAllErrorTypes() {
        return ResponseEntity.ok(new HashSet<>(Arrays.asList(OBRIErrorType.values())));
    }
}
