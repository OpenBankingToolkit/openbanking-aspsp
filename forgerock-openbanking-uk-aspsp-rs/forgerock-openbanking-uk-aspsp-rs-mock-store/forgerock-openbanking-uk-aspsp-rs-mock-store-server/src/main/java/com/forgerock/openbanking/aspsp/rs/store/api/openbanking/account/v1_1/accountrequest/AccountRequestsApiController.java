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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v1_1.accountrequest;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accountrequests.FRAccountRequestRepository;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountRequest;
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
import uk.org.openbanking.datamodel.account.OBReadDataResponse1;
import uk.org.openbanking.datamodel.account.OBReadRequest1;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toFRReadResponse;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRReadResponseConverter.toOBReadResponse1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("AccountRequestsApiV1.1")
public class AccountRequestsApiController implements AccountRequestsApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestsApiController.class);

    @Autowired
    private TppRepository tppRepository;
    @Autowired
    private FRAccountRequestRepository frAccountRequestRepository;
    @Autowired
    private ConsentMetricService consentMetricService;

    @Override
    public ResponseEntity<OBReadResponse1> createAccountRequest(
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
        LOGGER.info("Received a new account request");
        String accountRequestId = IntentType.ACCOUNT_REQUEST.generateIntentId();
        LOGGER.info("Create a new account request ID {}", accountRequestId);

        OBReadResponse1 response = new OBReadResponse1()
                .data(new OBReadDataResponse1()
                        .accountRequestId(accountRequestId)
                        .status(OBExternalRequestStatus1Code.AWAITINGAUTHORISATION)
                        .creationDateTime(DateTime.now())
                        .permissions(body.getData().getPermissions())
                        .expirationDateTime(body.getData().getExpirationDateTime())
                        .transactionFromDateTime(body.getData().getTransactionFromDateTime())
                        .transactionToDateTime(body.getData().getTransactionToDateTime()))
                .risk(body.getRisk());

        FRAccountRequest accountRequest = new FRAccountRequest();
        accountRequest.setId(accountRequestId);
        accountRequest.setAccountRequestId(accountRequestId);
        accountRequest.setAccountRequest(toFRReadResponse(response));
        accountRequest.setAisp(tppRepository.findByClientId(aispId));
        accountRequest.setObVersion(VersionPathExtractor.getVersionFromPath(request));
        accountRequest = frAccountRequestRepository.save(accountRequest);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(accountRequest.getId(), accountRequest.getStatus().name()));

        LOGGER.debug("Account request created {}",
                accountRequest.getAccountRequest());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity deleteAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account request resource.", required = true)
            @PathVariable("AccountRequestId") String accountRequestId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent
    ) throws OBErrorResponseException {

        Optional<FRAccountRequest> accountRequest = frAccountRequestRepository.findByAccountRequestId(accountRequestId);
        if (!accountRequest.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Account request '" + accountRequestId + "' not found");

        }
        LOGGER.debug("Account request revoked with id {}", accountRequestId);
        FRAccountRequest frAccountRequest = accountRequest.get();
        frAccountRequest.getAccountRequest().getData().setStatus(FRExternalRequestStatusCode.REVOKED);
        consentMetricService.sendConsentActivity(
                new ConsentStatusEntry(frAccountRequest.getAccountRequest().getData().getAccountRequestId(),
                frAccountRequest.getAccountRequest().getData().getStatus().name()));
        frAccountRequestRepository.save(frAccountRequest);
        LOGGER.debug("Account request revoked");

        return ResponseEntity.ok("Account request '" + accountRequestId + "' deleted");
    }

    @Override
    public ResponseEntity<OBReadResponse1> getAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account request resource.", required = true)
            @PathVariable("AccountRequestId") String accountRequestId,

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
        LOGGER.debug("Read account request with id {}", accountRequestId);
        Optional<FRAccountRequest> accountRequest = frAccountRequestRepository.findById(accountRequestId);
        LOGGER.debug("Read successful with id {}", accountRequestId);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put(OBHeaders.X_FAPI_INTERACTION_ID, Arrays.asList(xFapiInteractionId));
        return new ResponseEntity<>(toOBReadResponse1(accountRequest.get().getAccountRequest()), headers, HttpStatus.OK);
    }

}
