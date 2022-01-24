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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.model.rcs.consentdetails.CustomerInfoConsentDetails;
import com.forgerock.openbanking.common.repositories.customerinfo.FRCustomerInfoRepository;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class RCSCustomerInfoDetailsApiTest {

    @Mock
    private RCSErrorService rcsErrorService;

    @Mock
    private TppStoreService tppStoreService;

    @Mock
    private AccountRequestStoreService accountRequestStoreService;

    @Mock
    private FRCustomerInfoRepository customerInfoRepository;

    @InjectMocks
    private RCSCustomerInfoDetailsApi api;

    @Test
    public void shouldReturnCustomerInfoDetails() throws OBErrorException {
        FRAccountAccessConsent frAccountAccessConsent = JMockData.mock(FRAccountAccessConsent.class);
        frAccountAccessConsent.setConsentId(IntentType.CUSTOMER_INFO_CONSENT.generateIntentId());
        FRCustomerInfo customerInfo = JMockData.mock(FRCustomerInfo.class);

        given(tppStoreService.findById(frAccountAccessConsent.getAispId())).willReturn(
                Optional.of(Tpp.builder().clientId(frAccountAccessConsent.getClientId()).build())
        );

        given(accountRequestStoreService.get(any())).willReturn(
                Optional.ofNullable(frAccountAccessConsent)
        );

        given(customerInfoRepository.findByUserID(any())).willReturn(customerInfo);

        ResponseEntity<CustomerInfoConsentDetails> response = api.consentDetails(
                "asdfas",
                Collections.EMPTY_LIST,
                frAccountAccessConsent.getUserId(),
                frAccountAccessConsent.getConsentId(),
                frAccountAccessConsent.getClientId()
        );

        CustomerInfoConsentDetails details = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(details.getCustomerInfo()).isNotNull();
    }

    @Test
    public void shouldReturnRedirectActionWhenCustomerInfoNotFound() throws OBErrorException {
        FRAccountAccessConsent frAccountAccessConsent = JMockData.mock(FRAccountAccessConsent.class);
        frAccountAccessConsent.setConsentId(IntentType.CUSTOMER_INFO_CONSENT.generateIntentId());
        frAccountAccessConsent.setCustomerInfo(null);
        given(tppStoreService.findById(frAccountAccessConsent.getAispId())).willReturn(
                Optional.of(Tpp.builder().clientId(frAccountAccessConsent.getClientId()).build())
        );

        given(accountRequestStoreService.get(any())).willReturn(
                Optional.ofNullable(frAccountAccessConsent)
        );

        given(customerInfoRepository.findByUserID(any())).willReturn(null);

        given(rcsErrorService.invalidConsentError(any(), any())).willReturn(
                ResponseEntity.ok(
                        RedirectionAction.builder()
                                .redirectUri("redirect_uri_value")
                                .build())
        );

        ResponseEntity<RedirectionAction> response = api.consentDetails(
                "asdfasdc",
                Collections.EMPTY_LIST,
                frAccountAccessConsent.getUserId(),
                frAccountAccessConsent.getConsentId(),
                frAccountAccessConsent.getClientId()
        );

        RedirectionAction redirectionAction = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(redirectionAction.getRedirectUri()).isNotNull();
    }
}
