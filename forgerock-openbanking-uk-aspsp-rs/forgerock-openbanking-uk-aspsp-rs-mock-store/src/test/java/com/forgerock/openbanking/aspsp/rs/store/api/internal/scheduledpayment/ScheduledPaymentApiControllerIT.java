/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.scheduledpayment;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments.FRScheduledPayment2Repository;
import com.forgerock.openbanking.commons.auth.Authenticator;
import com.forgerock.openbanking.commons.configuration.applications.RSConfiguration;
import com.forgerock.openbanking.commons.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduledPaymentApiControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private FRScheduledPayment2Repository scheduledPaymentRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @MockBean
    private Authenticator authenticator;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testInternalGetScheduledPayments() throws UnirestException {
        // Given
        mockAuthentication(authenticator, "ROLE_AISP");
        FRScheduledPayment2 payment = JMockData.mock(FRScheduledPayment2.class);
        payment.setStatus(ScheduledPaymentStatus.PENDING);
        payment.setCreated(new java.util.Date(0));
        scheduledPaymentRepository.save(payment);

        // When
        HttpResponse<List> response = Unirest.get("https://rs-store:" + port + "/api/accounts/scheduled-payments/search/find")
                .queryString("status", ScheduledPaymentStatus.PENDING)
                .queryString("toDateTime", DateTime.now().toString(ISODateTimeFormat.dateTimeNoMillis()))
                .asObject(List.class);
        log.debug("Response: {}", response.getBody());

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().isEmpty()).isEqualTo(false);
    }


}
