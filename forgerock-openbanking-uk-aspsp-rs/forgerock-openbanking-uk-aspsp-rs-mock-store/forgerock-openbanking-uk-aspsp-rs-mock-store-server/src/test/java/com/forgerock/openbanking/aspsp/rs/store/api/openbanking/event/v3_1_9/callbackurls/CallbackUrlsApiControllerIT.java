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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_9.callbackurls;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.events.CallbackUrlsRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FRCallbackUrlData;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.repositories.TppRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.event.OBCallbackUrl1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlData1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponse1;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CallbackUrlsApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CallbackUrlsRepository callbackUrlsRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;



    @MockBean
    private TppRepository tppRepository;

    private String clientId;

    private Tpp tpp;


    @Before
    public void setUp() {
        tpp = new Tpp();
        tpp.setId(UUID.randomUUID().toString());
        given(tppRepository.findByClientId(any())).willReturn(tpp);

        clientId = UUID.randomUUID().toString();
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void createCallbackUrls_urlDoesNotExistForTpp_created() {
        // Given
        String url = "http://callback"+ UUID.randomUUID();
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url(url)
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse<OBCallbackUrlResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getData().getCallbackUrlId()).isNotNull();
        assertThat(response.getBody().getData().getUrl()).isEqualTo(url);
        assertThat(response.getBody().getData().getVersion()).isEqualTo(OBVersion.v3_1_9.getCanonicalVersion());

        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(response.getBody().getData().getCallbackUrlId());
        assertThat(byId.orElseThrow(AssertionError::new).getCallbackUrl().getUrl()).isEqualTo(url);
    }

    @Test
    public void createCallbackUrls_urlAlreadyExistsForTpp_conflict() throws Exception {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        String callbackId = UUID.randomUUID().toString();
        callbackUrlsRepository.save(newFRCallbackUrl(callbackId)); // Existing URL
        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url("http://callback-"+callbackId) // Already exists
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse response = Unirest.post("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());

        final Collection<FRCallbackUrl> byClientId = callbackUrlsRepository.findByTppId(tpp.getId());
        assertThat(byClientId.size()).isEqualTo(1); // Should still be just 1
    }

    @Test
    public void readCallBackUrls_noResults_emptyData() {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_AISP.name());

        // When
        HttpResponse<OBCallbackUrlResponse1> response = Unirest.get("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getCallbackUrlId()).isNull();
    }

    @Test
    public void readCallBackUrls_accessedByNewerApiVersion() {
        // Given
        String url = "http://callback"+ UUID.randomUUID();
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url(url)
                        .version(OBVersion.v3_1_8.getCanonicalVersion())
                );

        // When
        HttpResponse<OBCallbackUrlResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getData().getCallbackUrlId()).isNotNull();
        assertThat(response.getBody().getData().getUrl()).isEqualTo(url);
        assertThat(response.getBody().getData().getVersion()).isEqualTo(OBVersion.v3_1_8.getCanonicalVersion());

        HttpResponse<OBCallbackUrlsResponse1> getResponse = Unirest.get("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBCallbackUrlsResponse1.class);

        assertThat(getResponse.getStatus()).isEqualTo(200);
        assertThat(getResponse.getBody().getData().getCallbackUrl()).isNotNull();
    }

    @Test
    public void readCallBackUrls_accessedByOlderApiVersion() {
        // Given
        String url = "http://callback"+ UUID.randomUUID();
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url(url)
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse<OBCallbackUrlResponse1> response = Unirest.post("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getBody().getData().getCallbackUrlId()).isNotNull();
        assertThat(response.getBody().getData().getUrl()).isEqualTo(url);
        assertThat(response.getBody().getData().getVersion()).isEqualTo(OBVersion.v3_1_9.getCanonicalVersion());

        HttpResponse<OBCallbackUrlsResponse1> getResponse = Unirest.get("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_8.getCanonicalName()+"/callback-urls")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(OBCallbackUrlsResponse1.class);

        assertThat(getResponse.getStatus()).isEqualTo(200);
        assertThat(getResponse.getBody().getData().getCallbackUrl()).isNullOrEmpty();
    }

    @Test
    public void updateCallbackUrl_exists_updated() {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_AISP.name());
        String callbackId = UUID.randomUUID().toString();

        FRCallbackUrl frCallbackUrl1 = FRCallbackUrl.builder()
                .id(callbackId)
                .callbackUrl(FRCallbackUrlData.builder()
                                .url("http://callback-update")
                                .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build();
        callbackUrlsRepository.save(frCallbackUrl1);

        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url("http://callback-"+callbackId+"-update")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse<OBCallbackUrlResponse1> response = Unirest.put("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls/"+callbackId)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(OBCallbackUrlResponse1.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getCallbackUrlId()).isNotNull();
        assertThat(response.getBody().getData().getUrl()).isEqualTo("http://callback-"+callbackId+"-update");
        assertThat(response.getBody().getData().getVersion()).isEqualTo(OBVersion.v3_1_9.getCanonicalVersion());

        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackId);
        assertThat(byId.orElseThrow(AssertionError::new).getCallbackUrl().getUrl()).isEqualTo("http://callback-"+callbackId+"-update");
    }

    @Test
    public void updateCallbackUrl_doesNotExist_notFound() {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        OBCallbackUrl1 obCallbackUrl = new OBCallbackUrl1()
                .data(new OBCallbackUrlData1()
                        .url("http://callback-update")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                );

        // When
        HttpResponse response = Unirest.put("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls/"+UUID.randomUUID())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(obCallbackUrl)
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);

    }

    @Test
    public void deleteCallbackUrl_exists_deleted() {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());
        String callbackId = UUID.randomUUID().toString();
        FRCallbackUrl frCallbackUrl1 = FRCallbackUrl.builder()
                .id(callbackId)
                .callbackUrl(FRCallbackUrlData.builder()
                        .url("http://callback-update")
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build();
        callbackUrlsRepository.save(frCallbackUrl1);

        // When
        HttpResponse response = Unirest.delete("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls/"+callbackId)
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(204);

        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackId);
        assertThat(byId.isPresent()).isFalse();

    }

    @Test
    public void deleteCallbackUrl_doesNotExist_notFound() {
        // Given
        //mockAuthentication(authenticator, OBRIRole.ROLE_PISP.name());

        // When
        HttpResponse response = Unirest.delete("https://rs-store:" + port + "/open-banking/"+OBVersion.v3_1_9.getCanonicalName()+"/callback-urls/"+ UUID.randomUUID())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-client-id", clientId)
                .header(OBHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .asObject(String.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(400);
    }

    private FRCallbackUrl newFRCallbackUrl(String id) {
        return FRCallbackUrl.builder()
                .id(id)
                .created(DateTime.now())
                .tppId(tpp.getId())
                .callbackUrl(FRCallbackUrlData.builder()
                        .url("http://callback-"+id)
                        .version(OBVersion.v3_1_9.getCanonicalVersion())
                        .build())
                .build();

    }
}
