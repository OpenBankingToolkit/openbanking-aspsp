/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.onboarding;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ManualRegistrationRequest {

    public String applicationName;
    public String organisationId;
    public String directoryId;
    public String applicationDescription;
    public List<String> redirectUris;
    public String softwareStatementAssertion;
    public String qsealPem;
    public String appId;
    public String psd2Roles;

}
