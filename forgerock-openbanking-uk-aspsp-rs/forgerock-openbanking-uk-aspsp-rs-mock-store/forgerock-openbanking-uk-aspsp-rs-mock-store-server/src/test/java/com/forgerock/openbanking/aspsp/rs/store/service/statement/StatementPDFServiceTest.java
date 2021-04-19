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

import org.junit.Test;
import org.springframework.core.io.Resource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class StatementPDFServiceTest {

    @Test
    public void getPdfStatement_test_found() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService();
        statementPDFService.setBucketName("ob-sandbox-storage");
        statementPDFService.setResource("statements/default/fr-statement.pdf");

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isTrue();
        assertThat(resource.orElseThrow(IllegalStateException::new).getDescription())
                .containsIgnoringCase("[Resource ob-sandbox-storage/statements/default/fr-statement.pdf loaded through InputStream]");
    }

    @Test
    public void getPdfStatement_notFound() {
        // Given
        StatementPDFService statementPDFService = new StatementPDFService();
        statementPDFService.setBucketName("ob-sandbox-storage");
        statementPDFService.setResource("account/statements/folder/folder/fr-statement.pdf");

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isFalse();
    }

}
