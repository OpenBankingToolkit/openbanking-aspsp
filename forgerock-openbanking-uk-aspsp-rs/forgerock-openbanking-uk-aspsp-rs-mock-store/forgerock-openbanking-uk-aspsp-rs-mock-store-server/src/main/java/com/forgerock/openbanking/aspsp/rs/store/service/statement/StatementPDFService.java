/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
