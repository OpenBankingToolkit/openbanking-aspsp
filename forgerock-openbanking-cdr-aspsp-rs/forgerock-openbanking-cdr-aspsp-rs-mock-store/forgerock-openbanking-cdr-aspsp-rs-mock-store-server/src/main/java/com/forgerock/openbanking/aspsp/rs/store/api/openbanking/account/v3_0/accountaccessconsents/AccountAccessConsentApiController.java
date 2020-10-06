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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_0.accountaccessconsents;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.accountaccessconsents.FRAccountAccessConsent1Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.OBHeaders;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1Data;
import uk.org.openbanking.datamodel.account.OBReadRequest1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("AccountAccessConsentsApiV3.0")
public class AccountAccessConsentApiController implements AccountAccessConsentApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountAccessConsentApiController.class);

    @Autowired
    private TppRepository tppRepository;
    @Autowired
    private FRAccountAccessConsent1Repository frAccountAccessConsent1Repository;
    @Autowired
    private ConsentMetricService consentMetricService;

    @Override
    public ResponseEntity<OBReadConsentResponse1> createAccountAccessConsent(
            @ApiParam(value = "Create an Account Request", required = true)
            @Valid
            @RequestBody OBReadRequest1 body,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The AISP ID")
            @RequestHeader(value = "x-ob-aisp_id", required = false) String aispId,

            HttpServletRequest request
            ) throws OBErrorResponseException {
        LOGGER.info("Received a new account access consent");
        String consentId = IntentType.ACCOUNT_ACCESS_CONSENT.generateIntentId();
        LOGGER.info("Create a new Account access consent ID {}", consentId);

        OBReadConsentResponse1 response = new OBReadConsentResponse1()
                .data(new OBReadConsentResponse1Data()
                        .consentId(consentId)
                        .status(OBExternalRequestStatus1Code.AWAITINGAUTHORISATION)
                        .creationDateTime(DateTime.now())
                        .permissions(body.getData().getPermissions())
                        .expirationDateTime(body.getData().getExpirationDateTime())
                        .transactionFromDateTime(body.getData().getTransactionFromDateTime())
                        .transactionToDateTime(body.getData().getTransactionToDateTime()))
                .risk(body.getRisk());

        FRAccountAccessConsent accountAccessConsent = new FRAccountAccessConsent();
        accountAccessConsent.setId(consentId);
        accountAccessConsent.setConsentId(consentId);
        accountAccessConsent.setAccountAccessConsent(response);
        accountAccessConsent.setAisp(tppRepository.findByClientId(aispId));
        accountAccessConsent.setObVersion(VersionPathExtractor.getVersionFromPath(request));

        consentMetricService.sendConsentActivity(new ConsentStatusEntry(accountAccessConsent.getId(), accountAccessConsent.getStatus().name()));
        accountAccessConsent = frAccountAccessConsent1Repository.save(accountAccessConsent);

        LOGGER.debug("Account access consent created {}",
                accountAccessConsent.getAccountAccessConsent());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity deleteAccountConsent(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account request resource.", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent
    ) throws OBErrorResponseException {

        Optional<FRAccountAccessConsent> accountAccessConsent = frAccountAccessConsent1Repository.findByConsentId(consentId);
        if (!accountAccessConsent.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Account access consent '" + consentId + "' not found");

        }
        LOGGER.debug("Account access consent revoked with id {}", consentId);
        FRAccountAccessConsent frAccountAccessConsent = accountAccessConsent.get();
        frAccountAccessConsent.getAccountAccessConsent().getData().setStatus(OBExternalRequestStatus1Code.REVOKED);
        consentMetricService.sendConsentActivity(
                new ConsentStatusEntry(frAccountAccessConsent.getAccountAccessConsent().getData().getConsentId(),
                        frAccountAccessConsent.getAccountAccessConsent().getData().getStatus().name()));
        frAccountAccessConsent1Repository.save(frAccountAccessConsent);
        LOGGER.debug("Account access consent revoked");

        return ResponseEntity.ok("Account access consent '" + consentId + "' deleted");
    }

    @Override
    public ResponseEntity<OBReadConsentResponse1> getAccountConsent(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the Account access consent resource.", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent
    ) throws OBErrorResponseException {
        LOGGER.debug("Read Account access consent with id {}", consentId);
        Optional<FRAccountAccessConsent> accountAccessConsent = frAccountAccessConsent1Repository.findById(consentId);
        LOGGER.debug("Read successful with id {}", consentId);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(OBHeaders.X_FAPI_INTERACTION_ID, Arrays.asList(xFapiInteractionId));
        return new ResponseEntity<>(accountAccessConsent.get().getAccountAccessConsent(), headers, HttpStatus.OK);
    }

}
