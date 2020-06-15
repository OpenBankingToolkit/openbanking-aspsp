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
import com.forgerock.openbanking.common.model.data.FRDataEvent;
import com.forgerock.openbanking.common.model.data.OBEventNotification2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.org.openbanking.datamodel.event.*;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DataEventsApiController.class)
public class DataEventsApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidInput_thenReturnEventsImported() throws Exception {

        mockMvc.perform(post("/api/data/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createFRDataEvent()))
        )
                .andExpect(status().isCreated());
    }

    FRDataEvent createFRDataEvent(){
        return new FRDataEvent()
                .tppId("")
                .addOBEventNotification2Item(createOBEventNotification2());
    }

    OBEventNotification2 createOBEventNotification2(){

        final String _paymentID = "PAY_001";
        OBEventLink1 obEventLink1 = new OBEventLink1();
        obEventLink1.setLink("https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/" + _paymentID);
        obEventLink1.setVersion("v3.0");
        OBEventLink1 obEventLink2 = new OBEventLink1();
        obEventLink1.setLink("https://examplebank.com/api/open-banking/v1.1/payment-submissions/" + _paymentID);
        obEventLink1.setVersion("v1.1");
        Arrays.asList(obEventLink1,obEventLink2);

        OBEventSubject1 obEventSubject1 = new OBEventSubject1();
        obEventSubject1.setSubjectType("http://openbanking.org.uk/rid_http://openbanking.org.uk/rty");
        obEventSubject1.setHttpopenbankingOrgUkrid(_paymentID);
        obEventSubject1.setHttpopenbankingOrgUkrty("domestic-payment");
        obEventSubject1.setHttpopenbankingOrgUkrlk(Arrays.asList(obEventLink1,obEventLink2));

        OBEventResourceUpdate1 obEventResourceUpdate1 = new OBEventResourceUpdate1();
        obEventResourceUpdate1.setSubject(obEventSubject1);

        OBEvent1 obEvent1 = new OBEvent1();
        obEvent1.setUrnukorgopenbankingeventsresourceUpdate(obEventResourceUpdate1);

        OBEventNotification2 obEventNotification2 = new OBEventNotification2();
        obEventNotification2.setAud("7umx5nTR33811QyQfi");
        obEventNotification2.setIat(1516239022);
        obEventNotification2.setEvents(obEvent1);
        obEventNotification2.setIss("https://examplebank.com/");
        obEventNotification2.setJti("b460a07c-4962-43d1-85ee-9dc10fbb8f6c");
        obEventNotification2.setSub("https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/"+_paymentID);
        obEventNotification2.setToe(1516239022);
        obEventNotification2.setTxn("dfc51628-3479-4b81-ad60-210b43d02306");

        return obEventNotification2;
    }
}
