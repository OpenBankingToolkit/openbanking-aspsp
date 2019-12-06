/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
