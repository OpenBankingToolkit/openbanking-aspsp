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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.customerinfo.v1_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.forgerock.openbanking.aspsp.rs.store.utils.FRCustomerInfoTestHelper;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.repositories.customerinfo.FRCustomerInfoRepository;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InfoApiControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private SpringSecForTest springSecForTest;

    @Autowired
    RSConfiguration rsConfiguration;

    @Autowired
    private FRCustomerInfoRepository frCustomerInfoRepository;

    @Before
    public void setUp(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper(objectMapper);
        Unirest.config().setObjectMapper(jacksonObjectMapper).verifySsl(false);
    }

    @After
    public void tearDown(){

    }

    @Test
    public void testGetCustomerInfo() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        FRCustomerInfo customerInfo = FRCustomerInfoTestHelper.aValidFRCustomerInfo();
        FRCustomerInfo createdInfo = frCustomerInfoRepository.save(customerInfo);

        try {
            // When
            HttpResponse<ReadCustomerInfo> response = Unirest.get("https://rs-store:" + port
                            + "/customer-info/v1.0/info")
                    .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.getFinancialId())
                    .header(OBHeaders.AUTHORIZATION, "token")
                    .header("x-ob-psu-user-id", customerInfo.getUserID())
                    .asObject(ReadCustomerInfo.class);

            // Then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getBody().getData()).isNotNull();
        } finally {
            frCustomerInfoRepository.deleteById(createdInfo.getId());
        }
    }

    @Test
    public void shouldReturnNoContentIfNoData_testGetCustomerInfo() throws UnirestException {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_AISP);
        FRCustomerInfo customerInfo = FRCustomerInfoTestHelper.aValidFRCustomerInfo();

        // When
        HttpResponse<ReadCustomerInfo> response = Unirest.get("https://rs-store:" + port
                        + "/customer-info/v1.0/info")
                .header(OBHeaders.X_FAPI_FINANCIAL_ID, rsConfiguration.getFinancialId())
                .header(OBHeaders.AUTHORIZATION, "token")
                .header("x-ob-psu-user-id", customerInfo.getUserID())
                .asObject(ReadCustomerInfo.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
