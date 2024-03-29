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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_9.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
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
import uk.org.openbanking.datamodel.account.OBAccount6;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount6;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRFinancialAccountConverter.toOBExternalAccountSubType1Code;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRFinancialAccountConverter.toOBExternalAccountType1Code;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * SpringBoot test for {@link com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_9.accounts.AccountsApiController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsApiControllerIT {

    @LocalServerPort
    private int port;
    @Autowired
    private SpringSecForTest springSecForTest;

    @Autowired
    private FRAccountRepository frAccountRepository;
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
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        FRAccount account = JMockData.mock(FRAccount.class);
        frAccountRepository.save(account);

        // When
        HttpResponse<OBReadAccount6> response = Unirest.get("https://rs-store:" + port + "/open-banking/v3.1.9/aisp/accounts/" + account.getId())
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.financialId)
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-url", "http://localhost")
                .header("x-ob-permissions", OBExternalPermissions1Code.READACCOUNTSDETAIL.name() + "," + OBExternalPermissions1Code.READPAN.name())
                .asObject(OBReadAccount6.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        OBAccount6 returnedAccount = response.getBody().getData().getAccount().get(0);
        assertThat(returnedAccount).isNotNull();
        assertThat(returnedAccount.getAccountId()).isEqualTo(account.getAccount().getAccountId());
        assertThat(returnedAccount.getCurrency()).isEqualTo(account.getAccount().getCurrency());
        assertThat(returnedAccount.getAccountType()).isEqualTo(toOBExternalAccountType1Code(account.getAccount().getAccountType()));
        assertThat(returnedAccount.getAccountSubType()).isEqualTo(toOBExternalAccountSubType1Code(account.getAccount().getAccountSubType()));
        assertThat(returnedAccount.getDescription()).isEqualTo(account.getAccount().getDescription());
        assertThat(returnedAccount.getNickname()).isEqualTo(account.getAccount().getNickname());
        assertThat(returnedAccount.getServicer().getSchemeName()).isEqualTo(account.getAccount().getServicer().getSchemeName());
        assertThat(returnedAccount.getServicer().getIdentification()).isEqualTo(account.getAccount().getServicer().getIdentification());
        assertThat(returnedAccount.getAccount().get(0).getIdentification()).isEqualTo(account.getAccount().getAccounts().get(0).getIdentification());
    }

}
