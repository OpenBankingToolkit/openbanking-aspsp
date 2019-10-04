/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.register.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class ManualRegUser {
    @Id
    private String id;
    private String organisationId;
    private Collection<String> authorities = new ArrayList<>();

    private String directoryID;
    private String appId;
    private String psd2Roles;

}
