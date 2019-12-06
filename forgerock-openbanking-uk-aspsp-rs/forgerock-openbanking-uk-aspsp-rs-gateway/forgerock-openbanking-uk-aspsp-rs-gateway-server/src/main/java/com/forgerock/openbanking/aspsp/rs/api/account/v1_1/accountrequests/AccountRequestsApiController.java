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
package com.forgerock.openbanking.aspsp.rs.api.account.v1_1.accountrequests;

import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentBasicAndDetailPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.OBReadRequest1;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Controller("AccountRequestsApiV1.1")
public class AccountRequestsApiController implements AccountRequestsApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestsApiController.class);

    @Autowired
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService RSEndpointWrapperService;
    @Autowired
    private AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter;
    @Autowired
    private AccountAccessConsentBasicAndDetailPermissionsFilter accountAccessConsentBasicAndDetailPermissionsFilter;

    @Override
    public ResponseEntity<OBReadResponse1> createAccountRequest(
            @ApiParam(value = "Create an Account Request" ,required=true )
            @Valid
            @RequestBody OBReadRequest1 body,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload." ,required=true)
            @RequestHeader(value="x-jws-signature", required=false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        LOGGER.info("Receveid a new account request");
        return RSEndpointWrapperService.accountRequestEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .filters(f ->
                        {
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            accountAccessConsentPermittedPermissionsFilter.filter(body.getData().getPermissions());
                            accountAccessConsentBasicAndDetailPermissionsFilter.filter(body.getData());
                        }
                )
                .execute(
                        (String aispId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-aisp_id", aispId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBReadResponse1.class, body);
                        }
                );
    }

    @Override
    public ResponseEntity<Void> deleteAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account request resource.",required=true )
            @PathVariable("AccountRequestId") String accountRequestId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {

        return RSEndpointWrapperService.accountRequestEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .execute(
                        (String aispId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-aisp_id", aispId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Void.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadResponse1> getAccountRequest(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify the account request resource.",required=true )
            @PathVariable("AccountRequestId") String accountRequestId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return RSEndpointWrapperService.accountRequestEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .execute(
                        (String aispId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add("x-ob-aisp_id", aispId);
                            ParameterizedTypeReference<OBReadResponse1> ptr = new ParameterizedTypeReference<OBReadResponse1>() {};
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadResponse1.class);
                        }
                );
    }


}
