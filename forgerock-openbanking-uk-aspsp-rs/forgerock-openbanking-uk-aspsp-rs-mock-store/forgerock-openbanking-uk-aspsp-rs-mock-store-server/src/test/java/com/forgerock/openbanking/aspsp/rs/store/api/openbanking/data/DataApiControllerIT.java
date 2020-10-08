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
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalanceRepository;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRCashBalance;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRAccountData;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.data.FRUserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    private FRAccountRepository frAccountRepository;
    @Autowired
    private FRBalanceRepository frBalanceRepository;

    @Test
    public void shouldReturnPayloadTooLargeWhenCreatingNewData() throws Exception {
        // Given
        FRFinancialAccount account = FRFinancialAccount.builder().accountId(UUID.randomUUID().toString()).build();
        List<FRAccountData> accountDatas = Collections.singletonList(FRAccountData.builder()
                .account(account)
                .balances(Arrays.asList(
                        FRCashBalance.builder().build(),
                        FRCashBalance.builder().build()))
                .build());
        FRUserData userData = new FRUserData();
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
        FRFinancialAccount account = FRFinancialAccount.builder().accountId(UUID.randomUUID().toString()).build();
        List<FRAccountData> accountDatas = Collections.singletonList(FRAccountData.builder()
                .account(account)
                .balances(Collections.singletonList(FRCashBalance.builder()
                        .accountId(account.getAccountId())
                        .type(FRBalanceType.INTERIMAVAILABLE)
                        .build()))
                .build());
        FRAccount savedAccount = frAccountRepository.save(FRAccount.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        frBalanceRepository.save(FRBalance.builder().accountId(account.getAccountId()).build());
        FRUserData userData = new FRUserData();
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
        FRFinancialAccount account = FRFinancialAccount.builder().accountId(UUID.randomUUID().toString()).build();
        List<FRAccountData> accountDatas = Collections.singletonList(FRAccountData.builder()
                .account(account)
                .balances(Collections.singletonList(FRCashBalance.builder().build()))
                .build());
        FRUserData userData = new FRUserData();
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
        FRFinancialAccount account = FRFinancialAccount.builder().accountId(UUID.randomUUID().toString()).build();
        List<FRAccountData> accountDatas = Collections.singletonList(FRAccountData.builder()
                .account(account)
                .balances(Arrays.asList(
                        FRCashBalance.builder().type(FRBalanceType.INTERIMAVAILABLE).build(),
                        FRCashBalance.builder().type(FRBalanceType.INTERIMBOOKED).build()))
                .build());
        FRAccount savedAccount = frAccountRepository.save(FRAccount.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData userData = new FRUserData();
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
        FRFinancialAccount account = FRFinancialAccount.builder().accountId(UUID.randomUUID().toString()).build();
        List<FRAccountData> accountDatas = Collections.singletonList(FRAccountData.builder()
                .account(account)
                .balances(Collections.singletonList(FRCashBalance.builder().build()))
                .build());
        FRAccount savedAccount = frAccountRepository.save(FRAccount.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData userData = new FRUserData();
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