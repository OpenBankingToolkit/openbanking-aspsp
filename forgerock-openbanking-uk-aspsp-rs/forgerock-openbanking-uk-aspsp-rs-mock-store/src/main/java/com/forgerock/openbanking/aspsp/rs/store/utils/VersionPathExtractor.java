/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.utils;

import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts the version component (e.g. v3.1 ) from an Open Banking resource path (e.g. /open-banking/v3.1/pisp/domestic-payment-consent)
 */
@Slf4j
public class VersionPathExtractor {

    private static final Pattern VERSION_PATH_MATCHER = Pattern.compile(".*/(v(\\d+\\.)?(\\d+\\.)?(\\*|\\d+))/.*");

    /**
     * Work out the OB version from the URL path used in request.
     * @param request HTTP request
     * @return OBVersion or null if no version present in path
     */
    public static OBVersion getVersionFromPath(HttpServletRequest request) {
        return getVersionFromPath((String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
    }

    static OBVersion getVersionFromPath(String path) {
        if (StringUtils.isEmpty(path)) {
            log.warn("Path not defined");
            return null;
        }
        Matcher matcher = VERSION_PATH_MATCHER.matcher(path);
        if (!matcher.matches()) {
            log.warn("Version not found in path: " + path);
            return null;
        }
        String version = matcher.group(1);
        return OBVersion.fromString(version);
    }
}
