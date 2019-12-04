/**
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
package com.forgerock.openbanking.aspsp.rs.api.discovery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.conf.discovery.GenericOBDiscoveryAPILinks;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;

import java.net.URI;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscoveryTestIT {
    @LocalServerPort
    private int port;


    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void discovery() {
        // When
        HttpResponse<DiscoveryResponse> response = get("https://rs-api:" + port + "/open-banking/discovery");

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        DiscoveryResponse.DiscoveryData discoveryData = Objects.requireNonNull(response.getBody()).getData();
        assertThat(discoveryData.getFinancialId()).isNotNull();

        // Account
        verifyLinksDoNotContain(discoveryData.getAccountAndTransactionByVersion("v1.1"), "aisp");
        verifyLinksDoNotContain(discoveryData.getAccountAndTransactionByVersion("v2.0"), "aisp");

        verifyLinksAllContain(discoveryData.getAccountAndTransactionByVersion("v3.0"), "aisp");
        verifyLinksAllContain(discoveryData.getAccountAndTransactionByVersion("v3.1"), "aisp");
        verifyLinksAllContain(discoveryData.getAccountAndTransactionByVersion("v3.1.1"), "aisp");

        // Payment
        verifyLinksDoNotContain(discoveryData.getPaymentInitiationByVersion("v1.1"), "pisp");
        verifyLinksDoNotContain(discoveryData.getPaymentInitiationByVersion("v2.0"), "pisp");

        verifyLinksAllContain(discoveryData.getPaymentInitiationByVersion("v3.0"), "pisp");
        verifyLinksAllContain(discoveryData.getPaymentInitiationByVersion("v3.1"), "pisp");
        verifyLinksAllContain(discoveryData.getPaymentInitiationByVersion("v3.1.1"), "pisp");

        // Funds
        verifyLinksAllContain(discoveryData.getFundsConfirmationByVersion("v3.0"), "cbpii");
        verifyLinksAllContain(discoveryData.getFundsConfirmationByVersion("v3.1"), "cbpii");
        verifyLinksAllContain(discoveryData.getFundsConfirmationByVersion("v3.1.1"), "cbpii");
    }

    private void verifyLinksDoNotContain(OBDiscoveryAPI<GenericOBDiscoveryAPILinks> api, String strToMatch) {
        assertThat(api.getLinks()
                .getLinkValues()
                .stream()
                .noneMatch(link -> link.contains(strToMatch))
        ).isEqualTo(true);
    }

    private void verifyLinksAllContain(OBDiscoveryAPI<GenericOBDiscoveryAPILinks> api, String strToMatch) {
        assertThat(api.getLinks()
                .getLinkValues()
                .stream()
                .allMatch(link -> link.contains(strToMatch))
        ).isEqualTo(true);
    }

    public HttpResponse<DiscoveryResponse> get(String url) {
        ParameterizedTypeReference<DiscoveryResponse> ptr = new ParameterizedTypeReference<DiscoveryResponse>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        return Unirest.get(uri.toString()).asObject(DiscoveryResponse.class);

    }
}