/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.statements;

import com.forgerock.openbanking.aspsp.rs.store.service.statement.StatementPDFService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class StatementsApiControllerIT {
    private static final String TEST_PDF = "account/statements/test/statement.pdf";

    @Mock
    private StatementPDFService statementPDFService;

    @InjectMocks
    private StatementsApiController statementsApiController;

    @Before
    public void setUp() {
    }

    @Test
    public void getStatementsFile_acceptsPDF_hasPdfForProfile_returnPdf() throws Exception
    {
        given(statementPDFService.getPdfStatement()).willReturn(Optional.of(new ClassPathResource(TEST_PDF)));

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
    public void getStatementsFile_wrongAcceptHeader_501() throws Exception
    {
        ResponseEntity<Resource> response = statementsApiController.getAccountStatementFile("a12345",
                0,
                "s12345",
                "f12345",
                "token",
                DateTime.now(),
                "",
                "interaction1234",
                "application/xml");

        assertThat(response.getStatusCodeValue()).isEqualTo(501);
        assertThat(response.getBody()).isNull();
        verifyZeroInteractions(statementPDFService);
    }

    @Test
    public void getStatementsFile_noPdfForProfile_501() throws Exception
    {
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

        assertThat(response.getStatusCodeValue()).isEqualTo(501);
        assertThat(response.getBody()).isNull();
    }

}