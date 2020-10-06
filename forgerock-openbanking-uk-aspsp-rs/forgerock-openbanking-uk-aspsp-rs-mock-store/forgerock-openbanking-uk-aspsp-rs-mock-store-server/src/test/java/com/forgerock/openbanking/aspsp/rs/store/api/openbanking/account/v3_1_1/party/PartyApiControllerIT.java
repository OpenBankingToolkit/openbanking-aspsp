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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.party;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.party.FRPartyRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRParty;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBParty2;
import uk.org.openbanking.datamodel.account.OBReadParty2;
import uk.org.openbanking.datamodel.account.OBReadParty3;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PartyApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private FRPartyRepository frPartyRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;


    private FRParty accountParty;
    private FRParty userParty;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
        accountParty = JMockData.mock(FRParty.class);
        accountParty.setAccountId(UUID.randomUUID().toString());
        accountParty.setParty(new OBParty2().partyId("accountParty"));
        frPartyRepository.save(accountParty);

        userParty = JMockData.mock(FRParty.class);
        userParty.setUserId(UUID.randomUUID().toString());
        userParty.setParty(new OBParty2().partyId("userParty"));
        frPartyRepository.save(userParty);
    }

    @Test
    public void getAccountParties_returnTwo() {
        // Given
        //springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);


        // When
        HttpResponse<OBReadParty3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/aisp/accounts/"+accountParty.getAccountId()+"/parties")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "http://localhost")
                .header("x-ob-permissions", OBExternalPermissions1Code.READPARTY.name())
                .header("x-ob-user-id", userParty.getUserId())
                .asObject(OBReadParty3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getParty().size()).isEqualTo(2);
        assertThat(response.getBody().getData().getParty().get(0)).isEqualTo(accountParty.getParty());
        assertThat(response.getBody().getData().getParty().get(1)).isEqualTo(userParty.getParty());
    }

    @Test
    public void getAccountParty() {
        // Given
       // springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);

        // When
        HttpResponse<OBReadParty2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/aisp/accounts/"+accountParty.getAccountId()+"/party")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "http://localhost")
                .header("x-ob-permissions", OBExternalPermissions1Code.READPARTY.name())
                .asObject(OBReadParty2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getParty()).isEqualTo(accountParty.getParty());
    }

    @Test
    public void getParty() {
        // Given
        String accountId = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        //springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);

        FRParty userParty = JMockData.mock(FRParty.class);
        userParty.setUserId(username);
        userParty.setParty(new OBParty2().partyId("2"));
        frPartyRepository.save(userParty);

        // When
        HttpResponse<OBReadParty2> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/aisp/party")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "http://localhost")
                .header("x-ob-permissions", OBExternalPermissions1Code.READPARTY.name())
                .header("x-ob-user-id", userParty.getUserId())
                .asObject(OBReadParty2.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getParty()).isEqualTo(userParty.getParty());
    }
}