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
package com.forgerock.openbanking.aspsp.as.service.headless;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUriUtils {
    public static final Pattern PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");


    public static Map<String, String> parseQueryOrFragment(String query) {
        Matcher matcher = ParseUriUtils.PARAM_PATTERN.matcher(query);
        Map<String, String> queries = new HashMap<>();
        while (matcher.find()) {
            String name = matcher.group(1);
            String eq = matcher.group(2);
            String value = matcher.group(3);
            queries.put(name, (value != null ? value : (StringUtils.hasLength(eq) ? "" : null)));
        }
        return queries;
    }
}