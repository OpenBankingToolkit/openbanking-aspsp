/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRAccountData4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.data.FRUserData4;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.org.openbanking.datamodel.account.OBAccount3;
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
    private FRAccount3Repository frAccountRepository;
    @Autowired
    private FRBalance1Repository frBalanceRepository;

    @Test
    public void shouldReturnPayloadTooLargeWhenCreatingNewData() throws Exception {
        // Given
        OBAccount3 account = new OBAccount3().accountId(UUID.randomUUID().toString());
        List<FRAccountData4> accountDatas = Collections.singletonList(FRAccountData4.builder()
                .account(account)
                .balances(Arrays.asList(new OBCashBalance1(),
                        new OBCashBalance1()))
                .build());
        FRUserData4 userData = new FRUserData4();
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
        OBAccount3 account = new OBAccount3().accountId(UUID.randomUUID().toString());
        List<FRAccountData4> accountDatas = Collections.singletonList(FRAccountData4.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1().accountId(account.getAccountId()).type(OBBalanceType1Code.INTERIMAVAILABLE)))
                .build());
        FRAccount3 savedAccount = frAccountRepository.save(FRAccount3.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        frBalanceRepository.save(FRBalance1.builder().accountId(account.getAccountId()).build());
        FRUserData4 userData = new FRUserData4();
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
        OBAccount3 account = new OBAccount3().accountId(UUID.randomUUID().toString());
        List<FRAccountData4> accountDatas = Collections.singletonList(FRAccountData4.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1()))
                .build());
        FRUserData4 userData = new FRUserData4();
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
        OBAccount3 account = new OBAccount3().accountId(UUID.randomUUID().toString());
        List<FRAccountData4> accountDatas = Collections.singletonList(FRAccountData4.builder()
                .account(account)
                .balances(Arrays.asList(new OBCashBalance1().type(OBBalanceType1Code.INTERIMAVAILABLE),
                        new OBCashBalance1().type(OBBalanceType1Code.INTERIMBOOKED)))
                .build());
        FRAccount3 savedAccount = frAccountRepository.save(FRAccount3.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData4 userData = new FRUserData4();
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
        OBAccount3 account = new OBAccount3().accountId(UUID.randomUUID().toString());
        List<FRAccountData4> accountDatas = Collections.singletonList(FRAccountData4.builder()
                .account(account)
                .balances(Collections.singletonList(new OBCashBalance1()))
                .build());
        FRAccount3 savedAccount = frAccountRepository.save(FRAccount3.builder().id(account.getAccountId()).userID(UUID.randomUUID().toString()).build());
        FRUserData4 userData = new FRUserData4();
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