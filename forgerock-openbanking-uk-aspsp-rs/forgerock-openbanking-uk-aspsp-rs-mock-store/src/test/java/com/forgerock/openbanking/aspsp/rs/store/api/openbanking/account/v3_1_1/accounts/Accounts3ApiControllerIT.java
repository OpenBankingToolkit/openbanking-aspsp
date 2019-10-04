/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import com.github.jsonzou.jmockdata.JMockData;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount3;

import static org.assertj.core.api.Assertions.assertThat;




@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Accounts3ApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private FRAccount3Repository frAccountRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RSConfiguration rsConfiguration;



    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void testGetAnAccount() throws UnirestException {
        // Given
        //mockAuthentication(authenticator, "ROLE_AISP");
        FRAccount3 account = JMockData.mock(FRAccount3.class);
        frAccountRepository.save(account);

        // When
        HttpResponse<OBReadAccount3> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.1/aisp/accounts/" + account.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "http://localhost")
                .header("x-ob-permissions", OBExternalPermissions1Code.READACCOUNTSDETAIL.name() + "," + OBExternalPermissions1Code.READPAN.name())
                .asObject(OBReadAccount3.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getData().getAccount().get(0)).isEqualTo(account.getAccount());
    }

}