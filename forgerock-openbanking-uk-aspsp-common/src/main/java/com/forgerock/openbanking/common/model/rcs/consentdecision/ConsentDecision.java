/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.rcs.consentdecision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Accounts consent decision bean to send the user decision on they accounts to the server side.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsentDecision {

    private String consentJwt;
    private String decision;
}
