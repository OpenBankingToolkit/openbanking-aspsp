/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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