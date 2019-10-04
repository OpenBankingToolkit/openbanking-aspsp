/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.version;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Represents the API versions on the OB sandbox.
 */
@Slf4j
public enum OBVersion {
    v1_1,
    v2_0,
    v3_0,
    v3_1,
    v3_1_1,
    v3_1_2;

    public static OBVersion fromString(String version) {
        try {
            if (!StringUtils.isEmpty(version)) {
                return OBVersion.valueOf(
                        version.replace(".", "_")
                                .toLowerCase()
                );
            }
        } catch (IllegalArgumentException e ) {
            log.debug("No match found for {} in enum: {}", version, OBVersion.class.getName(), e);
        }
        return null;
    }
}
