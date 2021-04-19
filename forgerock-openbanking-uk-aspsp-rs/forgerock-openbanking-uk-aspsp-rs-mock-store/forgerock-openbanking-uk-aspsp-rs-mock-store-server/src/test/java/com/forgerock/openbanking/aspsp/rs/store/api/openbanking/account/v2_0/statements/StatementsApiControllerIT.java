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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.statements;

import com.forgerock.openbanking.aspsp.rs.store.service.statement.StatementPDFService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StatementsApiControllerIT {
    private static final String TEST_PDF = "account/statements/test/fr-statement.pdf";

    @Mock
    private StatementPDFService statementPDFService;

    @InjectMocks
    private StatementsApiController statementsApiController;

    @Before
    public void setUp() {
    }

    @Test
    public void getStatementsFile_acceptsPDF_hasPdfForProfile_returnPdf() throws Exception {

        given(statementPDFService.getPdfStatement()).willReturn(
                Optional.of(Mockito.mock(InputStreamResource.class))
        );

        ResponseEntity<Resource> response = statementsApiController.getAccountStatementFile("a12345",
                0,
                "s12345",
                "f12345",
                "token",
                DateTime.now(),
                "",
                "interaction1234",
                "application/pdf");

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(Resource.class);
    }

    @Test
    public void getStatementsFile_wrongAcceptHeader_BadRequest() throws Exception {
        Throwable thrown = catchThrowable(() -> {
            statementsApiController.getAccountStatementFile("a12345",
                    0,
                    "s12345",
                    "f12345",
                    "token",
                    DateTime.now(),
                    "",
                    "interaction1234",
                    "application/xml");
        });

        assertThat(thrown).isInstanceOf(OBErrorResponseException.class)
                .satisfies(t -> assertThat(((OBErrorResponseException) t).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST))
                .satisfies(t -> assertThat(((OBErrorResponseException) t).getErrors().get(0).getMessage()).isEqualTo("Invalid header 'Accept' the only supported value for this operation is '" + MediaType.APPLICATION_PDF_VALUE + "'"));
    }

    @Test
    public void getStatementsFile_noPdfFound() throws Exception {
        given(statementPDFService.getPdfStatement()).willReturn(Optional.empty());

        ResponseEntity<Resource> response = statementsApiController.getAccountStatementFile("a12345",
                0,
                "s12345",
                "f12345",
                "token",
                DateTime.now(),
                "",
                "interaction1234",
                "application/pdf");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isNull();
    }

}
