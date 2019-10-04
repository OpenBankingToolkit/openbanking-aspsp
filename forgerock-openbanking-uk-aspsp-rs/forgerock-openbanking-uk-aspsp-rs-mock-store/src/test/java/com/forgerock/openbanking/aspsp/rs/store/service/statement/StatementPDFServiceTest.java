/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.service.statement;

import org.junit.Test;
import org.springframework.core.io.Resource;

import java.util.Optional;


public class StatementPDFServiceTest {

    @Test
    public void getPdfStatement_testProfile_found() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService("other,test");

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isTrue();
        assertThat(resource.orElseThrow(IllegalStateException::new).getFilename()).isEqualTo("statement.pdf");
    }

    @Test
    public void getPdfStatement_wrongProfile_notFound() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService("obri,other");

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isFalse();
    }

    @Test
    public void getPdfStatement_noProfile_notFound() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService("");

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isFalse();
    }

    @Test
    public void getPdfStatement_nullProfile_notFound() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService(null);

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isFalse();
    }

}