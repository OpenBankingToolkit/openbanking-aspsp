package com.forgerock.openbanking.aspsp.as.api.registration.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.openbanking.aspsp.as.TestHelperFunctions;
import com.forgerock.openbanking.aspsp.as.service.SSAService;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientException;
import com.forgerock.openbanking.aspsp.as.service.apiclient.ApiClientIdentityFactory;
import com.forgerock.openbanking.aspsp.as.service.apiclient.RegistrationRequestFactory;
import com.forgerock.openbanking.common.error.exception.dynamicclientregistration.DynamicClientRegistrationException;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ManualRegistrationApiControllerTest {

    private ManualRegistrationApiController manualRegistrationApiController;

    @Mock
    private TppRegistrationService tppRegistrationService;

    @Mock
    private TppStoreService tppStoreService;

    @Mock
    SSAService ssaService;

    ApiClientIdentityFactory apiClientIdentityFactory;

    RegistrationRequestFactory registrationRequestFactory;

    ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        String registrationRequest = "{\n" +
                "  \"scope\": \"openid accounts payments fundsconfirmations\",\n" +
                "  \"grant_types\": [\"authorization_code\", \"client_credentials\"],\n" +
                "  \"response_types\": [\"code id_token\"],\n" +
                "  \"token_endpoint_auth_method\": \"client_secret_basic\",\n" +
                "  \"id_token_signed_response_alg\": \"PS256\",\n" +
                "  \"subject_type\": \"pairwise\"\n" +
                "}";
        InputStream stream = new ByteArrayInputStream(registrationRequest.getBytes(StandardCharsets.UTF_8));
        Resource registrationRequestResource = new InputStreamResource(stream);
        apiClientIdentityFactory = new ApiClientIdentityFactory();
        registrationRequestFactory = new RegistrationRequestFactory(this.tppRegistrationService);
        manualRegistrationApiController = new ManualRegistrationApiController(tppStoreService, objectMapper,
                tppRegistrationService, ssaService, apiClientIdentityFactory, registrationRequestFactory,
                registrationRequestResource);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void registerApplication() throws ApiClientException, DynamicClientRegistrationException, OBErrorException,
            CertificateException, IOException, InvalidPsd2EidasCertificate {
        // Given
        ManualRegistrationRequest manualRegistrationRequest = getValidManualRegistrationRequest();
        X509Certificate[] certsChain = TestHelperFunctions.getCertChainFromFile("src/test/resources/certificates" +
                "/OBWac.pem");
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>(List.of(OBRIRole.UNKNOWN_CERTIFICATE)); ;
        PSD2Authentication authentication = new PSD2Authentication("testname",authorities, certsChain ,
                new Psd2CertInfo(certsChain));
        // When
        ResponseEntity<OIDCRegistrationResponse> response = manualRegistrationApiController.registerApplication(
                manualRegistrationRequest, authentication);
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private ManualRegistrationRequest getValidManualRegistrationRequest() {
        String validSSA =
                "eyJhbGciOiJQUzI1NiIsImtpZCI6Imo4SFdZMDBhSUJtS0ExT1c3WW50dnRLVU0ycnVueDdvQWdiS2hJRE1IM0k9IiwidHlwIjo" +
                        "iSldUIn0.eyJpc3MiOiJPcGVuQmFua2luZyBMdGQiLCJpYXQiOjE2MjU2NjA1MTIsImp0aSI6ImUzMWY2Nzc1NzQ0Mz" +
                        "QxZDUiLCJzb2Z0d2FyZV9lbnZpcm9ubWVudCI6InNhbmRib3giLCJzb2Z0d2FyZV9tb2RlIjoiVGVzdCIsInNvZnR3Y" +
                        "XJlX2lkIjoiSXdEaFBIMnFNczA3WHYxNnZYcG1WYyIsInNvZnR3YXJlX2NsaWVudF9pZCI6Ikl3RGhQSDJxTXMwN1h2" +
                        "MTZ2WHBtVmMiLCJzb2Z0d2FyZV9jbGllbnRfbmFtZSI6IkphbWllc1RQUCIsInNvZnR3YXJlX2NsaWVudF9kZXNjcml" +
                        "wdGlvbiI6IlRlc3Rpbmcgd2l0aCBlaWRhcyBjZXJ0IG9uYm9hcmRpbmcgd2l0aCBGUiBzYW5kYm94Iiwic29mdHdhcm" +
                        "VfdmVyc2lvbiI6MS4xLCJzb2Z0d2FyZV9jbGllbnRfdXJpIjoiaHR0cHM6Ly9qYW1pZS1kZXYuZm9yZ2Vyb2NrLmZpb" +
                        "mFuY2lhbCIsInNvZnR3YXJlX3JlZGlyZWN0X3VyaXMiOlsiaHR0cHM6Ly9yZWRpcmVjdC5qYW1pZS1kZXYuZm9yZ2Vy" +
                        "b2NrLmZpbmFuY2lhbC8iXSwic29mdHdhcmVfcm9sZXMiOlsiQUlTUCIsIlBJU1AiXSwib3JnYW5pc2F0aW9uX2NvbXB" +
                        "ldGVudF9hdXRob3JpdHlfY2xhaW1zIjp7ImF1dGhvcml0eV9pZCI6Ik9CR0JSIiwicmVnaXN0cmF0aW9uX2lkIjoiVW" +
                        "5rbm93bjAwMTU4MDAwMDEwNDFSRUFBWSIsInN0YXR1cyI6IkFjdGl2ZSIsImF1dGhvcmlzYXRpb25zIjpbeyJtZW1iZ" +
                        "XJfc3RhdGUiOiJHQiIsInJvbGVzIjpbIlBJU1AiLCJBSVNQIiwiQVNQU1AiXX0seyJtZW1iZXJfc3RhdGUiOiJJRSIs" +
                        "InJvbGVzIjpbIkFTUFNQIiwiQUlTUCIsIlBJU1AiXX0seyJtZW1iZXJfc3RhdGUiOiJOTCIsInJvbGVzIjpbIkFTUFN" +
                        "QIiwiUElTUCIsIkFJU1AiXX1dfSwic29mdHdhcmVfbG9nb191cmkiOiJodHRwczovL2xvZ28uamFtaWUtZGV2LmZvcm" +
                        "dlcm9jay5maW5hbmNpYWwvIiwib3JnX3N0YXR1cyI6IkFjdGl2ZSIsIm9yZ19pZCI6IjAwMTU4MDAwMDEwNDFSRUFBW" +
                        "SIsIm9yZ19uYW1lIjoiRk9SR0VST0NLIExJTUlURUQiLCJvcmdfY29udGFjdHMiOlt7Im5hbWUiOiJUZWNobmljYWwi" +
                        "LCJlbWFpbCI6ImphbWllLmJvd2VuQGZvcmdlcm9jay5jb20iLCJwaG9uZSI6IjQ0Nzc2NTEwOTUyNyIsInR5cGUiOiJ" +
                        "UZWNobmljYWwifSx7Im5hbWUiOiJCdXNpbmVzcyIsImVtYWlsIjoiam9obi5wcm91ZGZvb3RAZm9yZ2Vyb2NrLmNvbS" +
                        "IsInBob25lIjoiNDQ3NzEwMzUwMjY2IiwidHlwZSI6IkJ1c2luZXNzIn1dLCJvcmdfandrc19lbmRwb2ludCI6Imh0d" +
                        "HBzOi8va2V5c3RvcmUub3BlbmJhbmtpbmd0ZXN0Lm9yZy51ay8wMDE1ODAwMDAxMDQxUkVBQVkvMDAxNTgwMDAwMTA0" +
                        "MVJFQUFZLmp3a3MiLCJvcmdfandrc19yZXZva2VkX2VuZHBvaW50IjoiaHR0cHM6Ly9rZXlzdG9yZS5vcGVuYmFua2l" +
                        "uZ3Rlc3Qub3JnLnVrLzAwMTU4MDAwMDEwNDFSRUFBWS9yZXZva2VkLzAwMTU4MDAwMDEwNDFSRUFBWS5qd2tzIiwic2" +
                        "9mdHdhcmVfandrc19lbmRwb2ludCI6Imh0dHBzOi8va2V5c3RvcmUub3BlbmJhbmtpbmd0ZXN0Lm9yZy51ay8wMDE1O" +
                        "DAwMDAxMDQxUkVBQVkvSXdEaFBIMnFNczA3WHYxNnZYcG1WYy5qd2tzIiwic29mdHdhcmVfandrc19yZXZva2VkX2Vu" +
                        "ZHBvaW50IjoiaHR0cHM6Ly9rZXlzdG9yZS5vcGVuYmFua2luZ3Rlc3Qub3JnLnVrLzAwMTU4MDAwMDEwNDFSRUFBWS9" +
                        "yZXZva2VkL0l3RGhQSDJxTXMwN1h2MTZ2WHBtVmMuandrcyIsInNvZnR3YXJlX3BvbGljeV91cmkiOiJodHRwczovL3" +
                        "BvbGljeS5qYW1pZS1kZXYuZm9yZ2Vyb2NrLmZpbmFuY2lhbCIsInNvZnR3YXJlX3Rvc191cmkiOiJodHRwczovL3Bvb" +
                        "GljeS5qYW1pZS1kZXYuZm9yZ2Vyb2NrLmZpbmFuY2lhbCIsInNvZnR3YXJlX29uX2JlaGFsZl9vZl9vcmciOm51bGx9" +
                        ".VH70iQPzkd3Ka6k8L4qhtdqV6764L2p4pHBelpyY03XFEmuFqWBhlDWRzXpGw7VmWw68SmdCkqDo8heSyT8o7XXrO_" +
                        "HoqFBaQaVgeZOsxuKofWrDlA7qowGYl6bm_FB67AJlFW4NinFb_9HIB0NMe1DQYZshXxT0eDVyn-w3fBELvRnlugTAN" +
                        "wEdStK5J_8l_v4-NmmdDNuKWp-tIFwX1goWJ4B1gkt_VLKDe5iytaG0CLYiNEBVdIHyaTJqRs8PtmgYOR2SR0rjyCrU" +
                        "RFlbDiZ0_vGw4GreHz0yMpAQuXF3RbG4YAUwakOYcPPYUjfw9AvCPDeECpUokXhbcxi9Ew";
        ManualRegistrationRequest manualRegistrationRequest = new ManualRegistrationRequest();
        manualRegistrationRequest.setAppId("Manual Registration Test Application");
        manualRegistrationRequest.setApplicationDescription("description");
        manualRegistrationRequest.setSoftwareStatementAssertion(validSSA);
        return manualRegistrationRequest;
    }

    @Test
    public void unregisterApplication() {
    }

    @Test
    public void getRegistrationRequest() {
    }
}