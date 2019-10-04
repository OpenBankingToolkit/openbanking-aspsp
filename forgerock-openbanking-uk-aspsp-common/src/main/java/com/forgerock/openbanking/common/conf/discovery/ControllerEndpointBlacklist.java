/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf.discovery;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Stores methods that were not in the configuration when calculating discovery endpoints. This blacklist can be used to accurately filter precise controller methods without using regex matches (that may false match different HTTP method types or similar endpoints with URL vars)
 */
@Slf4j
public class ControllerEndpointBlacklist {

    /**
     * Set of all controller methods that are not in discovery
     */
    private final Set<String> blackList = new HashSet<>();

    public boolean contains(Class clazz, Method method) {
        return  (clazz != null &&
                method != null &&
                blackList.contains(formatString(clazz, method)));
    }

    public void add(Class clazz, Method method) {
        if (clazz != null && method != null) {
            String methodName = String.format("%s.%s", clazz.getName(), method.getName());
            blackList.add(methodName);
            log.info("Disabled Controller method: {}", methodName);
        }
    }

    private static String formatString(Class clazz, Method method) {
        return String.format("%s.%s", clazz.getName(), method.getName());
    }
}
