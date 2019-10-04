/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.rs.ui.model;

import com.forgerock.openbanking.common.model.openbanking.v3_0.account.data.FRUserData3;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String status;
    private String defaultProfile;

    private FRUserData3 data;
}
