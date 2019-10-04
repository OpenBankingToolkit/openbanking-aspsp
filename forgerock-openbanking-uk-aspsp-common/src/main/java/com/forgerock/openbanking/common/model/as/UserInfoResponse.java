/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.as;

import java.util.List;

public class UserInfoResponse {

    public String sub;
    public List<String> group;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }
}
