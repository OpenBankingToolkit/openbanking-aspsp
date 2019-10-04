/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.party;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRParty2;
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
    private FRParty2Repository frPartyRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;


    private FRParty2 accountParty;
    private FRParty2 userParty;

    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
        accountParty = JMockData.mock(FRParty2.class);
        accountParty.setAccountId(UUID.randomUUID().toString());
        accountParty.setParty(new OBParty2().partyId("accountParty"));
        frPartyRepository.save(accountParty);

        userParty = JMockData.mock(FRParty2.class);
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

        FRParty2 userParty = JMockData.mock(FRParty2.class);
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