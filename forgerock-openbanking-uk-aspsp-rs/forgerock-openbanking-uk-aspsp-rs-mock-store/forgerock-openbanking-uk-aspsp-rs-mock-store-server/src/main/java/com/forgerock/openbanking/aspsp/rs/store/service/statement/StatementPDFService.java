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
package com.forgerock.openbanking.aspsp.rs.store.service.statement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service to fetch a PSU statement in PDF format
 */
@Service
@Slf4j
public class StatementPDFService {

    private static final String STATEMENT_PDF_PATH = "account/statements/%s/statement.pdf";

    private final List<String> activeProfiles;

    public StatementPDFService(@Value("${spring.profiles.active:}") String activeProfileStr) {
        if (StringUtils.isEmpty(activeProfileStr)) {
            log.warn("No active profiles found");
            activeProfiles = Collections.emptyList();
        } else {
            activeProfiles = Arrays.asList(activeProfileStr.split(","));
            log.debug("activeProfiles={}", activeProfileStr);
        }

    }

    public Optional<Resource> getPdfStatement() {
        return activeProfiles.stream()
                .map(profile -> new ClassPathResource(String.format(STATEMENT_PDF_PATH, profile)))
                .filter(ClassPathResource::exists)
                .peek(r -> log.debug("Found statement PDF {} for profiles: {}", r.getPath(), activeProfiles))
                .map(resource -> (Resource) resource)
                .findFirst()
        ;
    }

}
