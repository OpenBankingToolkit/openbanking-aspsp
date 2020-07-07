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
package com.forgerock.openbanking.aspsp.rs.api.payment;

import com.forgerock.openbanking.common.model.version.OBVersion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ApiVersionMatcher {

    private static final Pattern VERSION_PATTERN = Pattern.compile("v[0-9][0-9]?\\.[0-9][0-9]?\\.?[0-9]?[0-9]?");

    /**
     * Provides the version of the API supported by the implementing controller (as specified in the request URI).
     *
     * @param requestURI the Request URI containing the API version number (e.g. v3.1.3).
     * @return The {@link OBVersion} supported in this instance.
     */
    public static OBVersion getOBVersion(String requestURI) {
        Matcher matcher = VERSION_PATTERN.matcher(requestURI);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to determine version from request URI: " + requestURI);
        }
        OBVersion version = OBVersion.fromString(matcher.group());
        if (version == null) {
            throw new IllegalArgumentException("Unknown version in request URI: " + requestURI);
        }
        return version;
    }
}
