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
