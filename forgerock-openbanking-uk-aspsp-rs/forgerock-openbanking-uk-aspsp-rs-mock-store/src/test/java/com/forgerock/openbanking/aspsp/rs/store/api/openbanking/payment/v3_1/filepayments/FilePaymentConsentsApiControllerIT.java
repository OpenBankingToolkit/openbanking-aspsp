/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.filepayments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFileType;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.github.jsonzou.jmockdata.JMockData;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.assertj.jodatime.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.payment.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Function;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.PaymentTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilePaymentConsentsApiControllerIT {
    private static final Logger log = LoggerFactory.getLogger(FilePaymentConsentsApiControllerIT.class);

    @LocalServerPort
    private int port;

    @Autowired
    private FileConsent2Repository repository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;

    @MockBean
    private TppRepository tppRepository;



    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetFileConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        FRFileConsent2 consent = JMockData.mock(FRFileConsent2.class);
        consent.setStatus(ConsentStatusCode.AWAITINGUPLOAD);
        consent.getInitiation().supplementaryData(new OBSupplementaryData1());
        repository.save(consent);

        // When
        HttpResponse<OBWriteFileConsentResponse2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payment-consents/" + consent.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(CONTENT_TYPE, "application/json")
                .header(ACCEPT, "application/json")
                .asObject(OBWriteFileConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getConsentId()).isEqualTo(consent.getId());
        assertThat(response.getBody().getData().getInitiation().getFileType()).isEqualTo(consent.getInitiation().getFileType());
        assertThat(response.getBody().getData().getInitiation().getFileHash()).isEqualTo(consent.getInitiation().getFileHash());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(consent.getStatus().toOBExternalConsentStatus2Code());
        Assertions.assertThat(response.getBody().getData().getCreationDateTime()).isEqualToIgnoringMillis(consent.getCreated());
        Assertions.assertThat(response.getBody().getData().getStatusUpdateDateTime()).isEqualToIgnoringMillis(consent.getStatusUpdate());
    }

    @Test
    public void testGetFileConsentReturnNotFound() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");

        // When
        HttpResponse<String> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payment-consents/12345")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateFileConsent() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_PISP");
        setupMockTpp(tppRepository);
        OBWriteFileConsent2 consentRequest = JMockData.mock(OBWriteFileConsent2.class);
        consentRequest.getData().getInitiation().fileHash("dslkjdslkfhsdlkfjlskdj");
        consentRequest.getData().getInitiation().fileReference("123");
        consentRequest.getData().getInitiation().fileType("UK.OBIE.pain.001.001.08");
        consentRequest.getData().getInitiation().numberOfTransactions("100");
        consentRequest.getData().getInitiation().controlSum(new BigDecimal("3000.0"));
        consentRequest.getData().getInitiation().supplementaryData(new OBSupplementaryData1());

        // When
        HttpResponse<OBWriteFileConsentResponse2> response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payment-consents/")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, "x-jws-signature")
                .header(CONTENT_TYPE, "application/json")
                .header(ACCEPT, "application/json")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .body(consentRequest)
                .asObject(OBWriteFileConsentResponse2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        OBWriteFileConsentResponse2 consentResponse = response.getBody();
        FRFileConsent2 consent = repository.findById(consentResponse.getData().getConsentId()).get();
        assertThat(consent.getPispName()).isEqualTo(MOCK_PISP_NAME);
        assertThat(consent.getPispId()).isEqualTo(MOCK_PISP_ID);
        assertThat(consent.getId()).isEqualTo(consentResponse.getData().getConsentId());
        assertThat(consent.getInitiation()).isEqualTo(consentResponse.getData().getInitiation());
        assertThat(consent.getStatus().toOBExternalConsentStatus2Code()).isEqualTo(consentResponse.getData().getStatus());
        assertThat(consent.getObVersion()).isEqualTo(OBVersion.v3_1);
    }

    @Test
    public void testCreateFilePaymentConsentsFile() throws UnirestException {
        // Given
        String fileConsentId = UUID.randomUUID().toString();
        //mockAuthentication(authenticator, "ROLE_PISP");
        setupMockTpp(tppRepository);

        String fileContent = utf8FileToString.apply("OBIEPaymentInitiation_3_0.json");

        FRFileConsent2 existingConsent = JMockData.mock(FRFileConsent2.class);
        existingConsent.setStatus(ConsentStatusCode.AWAITINGUPLOAD);
        existingConsent.setId(fileConsentId);
        existingConsent.setFileContent(null);
        existingConsent.setPayments(Collections.emptyList());
        existingConsent.setWriteFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(new OBFile2()
                .fileHash("kdjfklsdjflksjf")
                .numberOfTransactions("3")
                .controlSum(new BigDecimal("66.0"))
                .fileType(PaymentFileType.UK_OBIE_PAYMENT_INITIATION_V3_0.getFileType())
        )));
        repository.save(existingConsent);

        // When
        HttpResponse response = Unirest.post("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payment-consents/"+fileConsentId+"/file")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(OBHeaders.X_IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .header(OBHeaders.X_JWS_SIGNATURE, UUID.randomUUID().toString())
                .header(CONTENT_TYPE, "plain/xml")
                .header(ACCEPT, "application/json")
                .header("x-ob-client-id", MOCK_CLIENT_ID)
                .body(fileContent)
                .asObject(String.class);

        // Then
        log.debug("Response: {}",response);
        assertThat(response.getStatus()).isEqualTo(200);
        FRFileConsent2 consent = repository.findById(fileConsentId).get();
        assertThat(consent.getId()).isEqualTo(fileConsentId);
        assertThat(consent.getStatus().toOBExternalConsentStatus2Code()).isEqualTo(OBExternalConsentStatus2Code.AWAITINGAUTHORISATION);
        assertThat(consent.getFileContent()).isEqualTo(fileContent);
    }

    @Test
    public void testGetFileConsentFile() throws Exception {
        // Given
        String fileConsentId = UUID.randomUUID().toString();
        String fileContent = "<sample>test</sample>";
        //mockAuthentication(authenticator, "ROLE_PISP");
        FRFileConsent2 consent = JMockData.mock(FRFileConsent2.class);
        consent.setStatus(ConsentStatusCode.AWAITINGAUTHORISATION);
        consent.setId(fileConsentId);
        consent.setFileContent(fileContent);
        repository.save(consent);

        // When
        HttpResponse response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1/pisp/file-payment-consents/" + fileConsentId+"/file")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header(ACCEPT, "plain/xml")
                .header(CONTENT_TYPE, "application/json")
                .asString();

        // Then
        log.debug("Response: {} {}",response.getStatus(), response.getStatusText());
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().toString()).isEqualTo(fileContent);
    }

    private static final Function<String, String> utf8FileToString = fileName -> {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    };

}