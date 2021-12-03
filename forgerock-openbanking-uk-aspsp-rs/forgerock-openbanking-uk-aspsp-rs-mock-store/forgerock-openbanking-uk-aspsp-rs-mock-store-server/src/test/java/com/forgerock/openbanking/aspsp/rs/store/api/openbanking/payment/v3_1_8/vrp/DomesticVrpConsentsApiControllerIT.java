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

package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_8.vrp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.repositories.TppRepository;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponseData;

import java.util.UUID;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.*;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRiskTestDataFactory.aValidFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toFRRisk;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBDomesticVRPInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory.aValidOBDomesticVRPConsentRequest;

/**
 * Integration test for {@link DomesticVrpConsentsApiController}.
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DomesticVrpConsentsApiControllerIT {

    private static final String RS_STORE_URL = "https://rs-store:";
    private static final String CONTEXT_PATH = "/open-banking/v3.1.8/pisp/domestic-vrp-consents/";

    @LocalServerPort
    private int port;

    @Autowired
    private DomesticVRPConsentRepository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;
    @Autowired
    private SpringSecForTest springSecForTest;

//    @MockBean
//    private FundsAvailabilityService fundsAvailabilityService;

    @MockBean
    private TppRepository tppRepository;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testCreateDomesticVrpPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        setupMockTpp(tppRepository);

        // When
        HttpResponse<OBDomesticVRPConsentResponse> response = Unirest.post(RS_STORE_URL + port + CONTEXT_PATH)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(aValidOBDomesticVRPConsentRequest())
                .asObject(OBDomesticVRPConsentResponse.class);

        // Then
        log.error("The response: {}", response);
        assertThat(response.getStatus()).isEqualTo(201);
        OBDomesticVRPConsentResponse consentResponse = response.getBody();
        FRDomesticVRPConsent consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(toOBDomesticVRPInitiation(consent.getInitiation())).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus1Code().toString()).isEqualTo(consentResponse.getData().getStatus().getValue());
        assertThat(consent.getRisk()).isEqualTo(toFRRisk(consentResponse.getRisk()));
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1_8);
    }

    @Test
    public void testGetDomesticVrpPaymentConsent() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);
        FRDomesticVRPConsent consent = saveFRConsent(FRReadRefundAccount.NO, ConsentStatusCode.EXPIRED);

        // When
        HttpResponse<OBDomesticVRPConsentResponse> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(OBDomesticVRPConsentResponse.class);

        log.debug("Response {}:{}  {}", response.getStatus(), response.getStatusText(), response.getBody());
        if (response.getParsingError().isPresent()) {
            log.error("Parsing error", response.getParsingError().get());
        }

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(toFRWriteDomesticVRPDataInitiation(response.getBody().getData().getInitiation())).isEqualTo(consent.getInitiation());
        assertThat(response.getBody().getData().getStatus().getValue()).isEqualTo(consent.getStatus().getValue());

    }

    @Test
    public void testGetDomesticVrpPaymentConsentReturnNotFound() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse<String> response = Unirest.get(RS_STORE_URL + port + CONTEXT_PATH + "DVRP_not_exist")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400); // seems odd this isn't a 404
    }

    private FRDomesticVRPConsent saveFRConsent(FRReadRefundAccount frReadRefundAccount, ConsentStatusCode consentStatusCode) {
        FRDomesticVRPConsentDetails details = toFRDomesticVRPConsentDetails(aValidOBDomesticVRPConsentRequest());
        FRDomesticVRPConsent consent = JMockData.mock(FRDomesticVRPConsent.class);
        consent.setVrpDetails(details);
        consent.getVrpDetails().getData().setReadRefundAccount(frReadRefundAccount);
        consent.setId(IntentType.DOMESTIC_VRP_PAYMENT_CONSENT.generateIntentId());
        consent.setIdempotencyKey(UUID.randomUUID().toString());
        consent.setStatus(consentStatusCode);
        consent.getRisk().setMerchantCategoryCode(aValidFRRisk().getMerchantCategoryCode());
        consent.getRisk().setDeliveryAddress(aValidFRRisk().getDeliveryAddress());
        consent.setObVersion(OBVersion.v3_1_8);
        repository.save(consent);
        return consent;
    }

}
