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
 * http://www.apache.org/licenses/LICENSE-2.0
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
import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.data.OBEventNotification2;
import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.org.openbanking.datamodel.event.OBEvent1;
import uk.org.openbanking.datamodel.event.OBEventLink1;
import uk.org.openbanking.datamodel.event.OBEventResourceUpdate1;
import uk.org.openbanking.datamodel.event.OBEventSubject1;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataEventsApiControllerIT {

    @MockBean(name = "cryptoApiClient")
    private CryptoApiClient cryptoApiClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Captures JWT claims passed for signing so we can check them
    private ArgumentCaptor<JWTClaimsSet> claimsCaptor;

    // constants for FRDataEvent object
    private static final String ISSUER_ID_VALUE = "https://as.aspsp.domain.forgerock.financial/oauth2";
    private static final String FAKE_SIGNED_JWT = "lijlkjlkjlkjlkjlkjlkjlkjlkjl";
    private static final String TPP = "3ffb98cc-be98-4b10-a405-bde41e88c2c7";
    private static final String JTI = "b460a07c-4962-43d1-85ee-9dc10fbb8f6c";
    private static final String PAYMENT_ID = "PAY_001";

    @Before
    public void setUp() throws Exception {
        claimsCaptor = ArgumentCaptor.forClass(JWTClaimsSet.class);
        // mock jwk signing client
        given(cryptoApiClient.signClaims(eq(ISSUER_ID_VALUE), claimsCaptor.capture(), anyBoolean())).willReturn(FAKE_SIGNED_JWT);
    }

    @Test
    public void whenValidInput_thenReturnEventsImported() throws Exception {
        mockMvc.perform(post("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createFRDataEvent()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        // delete event created
        mockMvc.perform(delete("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FRDataEvent().tppId(TPP)))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenValidInput_thenReturnEventsUpdated() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createFRDataEvent()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].tppId").value(TPP))
                .andReturn();

        String jti = objectMapper.readValue(result.getResponse().getContentAsString(), FREventNotification[].class)[0].getJti();
        FRDataEvent frDataEvent = createFRDataEvent();
        frDataEvent.getObEventNotification2List().get(0).setJti(jti);
        mockMvc.perform(put("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(frDataEvent))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        // delete event created
        mockMvc.perform(delete("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FRDataEvent().tppId(TPP)))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenValidInput_thenReturnExportEventsByTppId() throws Exception {

        mockMvc.perform(post("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createFRDataEvent()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        mockMvc.perform(get("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tppId", TPP)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        // delete event created
        mockMvc.perform(delete("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FRDataEvent().tppId(TPP)))
        )
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenValidInput_thenReturnExportAllEvents() throws Exception {

        mockMvc.perform(post("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createFRDataEvent()))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        mockMvc.perform(get("/api/data/events/all")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tppId").value(TPP));

        // delete event created
        mockMvc.perform(delete("/api/data/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new FRDataEvent().tppId(TPP)))
        )
                .andExpect(status().isNoContent());
    }

    FRDataEvent createFRDataEvent() {
        return new FRDataEvent()
                .tppId(TPP)
                .addOBEventNotification2Item(createOBEventNotification2());
    }

    OBEventNotification2 createOBEventNotification2() {

        OBEventLink1 obEventLink1 = new OBEventLink1()
                .link("https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/" + PAYMENT_ID)
                .version("v3.0");

        OBEventLink1 obEventLink2 = new OBEventLink1()
                .link("https://examplebank.com/api/open-banking/v1.1/payment-submissions/" + PAYMENT_ID)
                .version("v1.1");


        OBEvent1 obEvent1 = new OBEvent1()
                .urnukorgopenbankingeventsresourceUpdate(new OBEventResourceUpdate1()
                        .subject(new OBEventSubject1()
                                .subjectType("http://openbanking.org.uk/rid_http://openbanking.org.uk/rty")
                                .httpopenbankingOrgUkrid(PAYMENT_ID)
                                .httpopenbankingOrgUkrty("domestic-payment")
                                .httpopenbankingOrgUkrlk(Arrays.asList(obEventLink1, obEventLink2))
                        )
                );

        return createOBEventNotification2(obEvent1, JTI);
    }

    private OBEventNotification2 createOBEventNotification2(OBEvent1 obEvent1, String jti) {
        return new OBEventNotification2()
                .aud("7umx5nTR33811QyQfi")
                .iat(1516239022)
                .events(obEvent1)
                .iss("https://examplebank.com/")
                .jti(jti)
                .sub("https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/" + PAYMENT_ID)
                .toe(1516239022)
                .txn("dfc51628-3479-4b81-ad60-210b43d02306");
    }
}
