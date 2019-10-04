/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.onboarding;

import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Document
public class ManualRegistrationApplication {

    @Id
    @Indexed
    public String id;
    @Indexed
    public String userId;
    @Indexed
    public String softwareClientId;
    public String description;
    public ManualRegistrationRequest manualRegistrationRequest;
    public OIDCRegistrationResponse oidcRegistrationResponse;
}
