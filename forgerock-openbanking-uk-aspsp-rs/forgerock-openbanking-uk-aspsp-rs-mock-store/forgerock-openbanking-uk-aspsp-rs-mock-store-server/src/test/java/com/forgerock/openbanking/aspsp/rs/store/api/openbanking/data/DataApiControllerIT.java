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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRAccount4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.data.FRAccountData5;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.account.data.FRUserData5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.org.openbanking.datamodel.account.OBAccount6;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;
import uk.org.openbanking.datamodel.account.OBCashBalance1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"rs.data.upload.limit.accounts=10", "rs.data.upload.limit.documents=1"})
public class DataApiControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private FRAccount4Repository frAccountRepository;
    @Autowired
    private FRBalance1Repository frBalanceRepository;

    @Test
    public void shouldReturnPayloadTooLargeWhenCreatingNewData() throws Exception {
        // Given
        OBAccount6 account = new OBAccount6().accountId(UUID.randomUUID().toString());
        List<FRAccountData5> accountDatas = Collections.singletonList(FRAccountData5.builder()
                .account(account)
                .balances(Arrays.asList(new OBCashBalance1(),
                        new OBCashBalance1()))
                .build());
        FRUserData5 userData = new FRUserData5();
        userData.setAccountDatas(accountDatas);
        userData.setUserName(UUID.randomUUID().toString());

        // When
        mockMvc.perform(post("/api/data/user")
                .content(mapper.writeValueAsString(userData))
                .contentType("application/json"))

                // Then
                .andExpect(status()
                        .isPayloadTooLarge());
    }

    @Test
    public void shouldReturnPayloadTooLargeWhenCreatingNewDataWithDataAlreadySaved() throws Exception {
        // Given
        OBAccount6 account = new OBAccount6().accountId(UUID.randomUUID().toString());
        List<FRAccountData5> accountDatas = Collections.singletonList(FRAccountData5.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1().accountId(account.getAccountId()).type(OBBalanceType1Code.INTERIMAVAILABLE)))
                .build());
        FRAccount4 savedAccount = frAccountRepository.save(FRAccount4.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        frBalanceRepository.save(FRBalance1.builder().accountId(account.getAccountId()).build());
        FRUserData5 userData = new FRUserData5();
        userData.setAccountDatas(accountDatas);
        userData.setUserName(savedAccount.getUserID());

        // When
        mockMvc.perform(post("/api/data/user")
                .content(mapper.writeValueAsString(userData))
                .contentType("application/json"))

                // Then
                .andExpect(status()
                        .isPayloadTooLarge());
    }

    @Test
    public void shouldCreateNewData() throws Exception {
        // Given
        OBAccount6 account = new OBAccount6().accountId(UUID.randomUUID().toString());
        List<FRAccountData5> accountDatas = Collections.singletonList(FRAccountData5.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1()))
                .build());
        FRUserData5 userData = new FRUserData5();
        userData.setAccountDatas(accountDatas);
        userData.setUserName(UUID.randomUUID().toString());

        // When
        mockMvc.perform(post("/api/data/user")
                .content(mapper.writeValueAsString(userData))
                .contentType("application/json"))

                // Then
                .andExpect(status()
                        .isOk());
    }

    @Test
    public void shouldReturnPayloadTooLargeWhenCreatingNewDataUsingUpdate() throws Exception {
        // Given
        OBAccount6 account = new OBAccount6().accountId(UUID.randomUUID().toString());
        List<FRAccountData5> accountDatas = Collections.singletonList(FRAccountData5.builder()
                .account(account)
                .balances(Arrays.asList(new OBCashBalance1().type(OBBalanceType1Code.INTERIMAVAILABLE),
                        new OBCashBalance1().type(OBBalanceType1Code.INTERIMBOOKED)))
                .build());
        FRAccount4 savedAccount = frAccountRepository.save(FRAccount4.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData5 userData = new FRUserData5();
        userData.setAccountDatas(accountDatas);
        userData.setUserName(savedAccount.getUserID());

        // When
        mockMvc.perform(put("/api/data/user")
                .content(mapper.writeValueAsString(userData))
                .contentType("application/json"))

                // Then
                .andExpect(status()
                        .isPayloadTooLarge());
    }
    @Test
    public void shouldCreateNewDataUsingUpdate() throws Exception {
        // Given
        OBAccount6 account = new OBAccount6().accountId(UUID.randomUUID().toString());
        List<FRAccountData5> accountDatas = Collections.singletonList(FRAccountData5.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1()))
                .build());
        FRAccount4 savedAccount = frAccountRepository.save(FRAccount4.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData5 userData = new FRUserData5();
        userData.setAccountDatas(accountDatas);
        userData.setUserName(savedAccount.getUserID());

        // When
        mockMvc.perform(put("/api/data/user")
                .content(mapper.writeValueAsString(userData))
                .contentType("application/json"))

                // Then
                .andExpect(status()
                        .isOk());
    }
}