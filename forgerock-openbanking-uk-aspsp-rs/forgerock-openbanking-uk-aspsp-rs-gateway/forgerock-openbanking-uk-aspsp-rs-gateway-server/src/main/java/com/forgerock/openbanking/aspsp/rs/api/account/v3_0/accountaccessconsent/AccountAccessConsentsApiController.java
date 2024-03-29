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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.accountaccessconsent;

import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentBasicAndDetailPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountRequestsEndpointWrapper;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadConsent1;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Controller("AccountAccessConsentsApiV3.0")
@Slf4j
public class AccountAccessConsentsApiController implements AccountAccessConsentsApi {

    private RsStoreGateway rsStoreGateway;
    private com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService RSEndpointWrapperService;
    private AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter;
    private AccountAccessConsentBasicAndDetailPermissionsFilter accountAccessConsentBasicAndDetailPermissionsFilter;
    private RSConfiguration rsConfiguration;
    private String RS_STORE_CUSTOMER_INFO_PATH = "/customer-info/v1.0/customer-info-consents";

    @Autowired
    public AccountAccessConsentsApiController(RsStoreGateway rsStoreGateway,
                                              com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService RSEndpointWrapperService,
                                              AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter,
                                              AccountAccessConsentBasicAndDetailPermissionsFilter accountAccessConsentBasicAndDetailPermissionsFilter,
                                              RSConfiguration rsConfiguration) {
        this.rsStoreGateway = rsStoreGateway;
        this.RSEndpointWrapperService = RSEndpointWrapperService;
        this.accountAccessConsentPermittedPermissionsFilter = accountAccessConsentPermittedPermissionsFilter;
        this.accountAccessConsentBasicAndDetailPermissionsFilter = accountAccessConsentBasicAndDetailPermissionsFilter;
        this.rsConfiguration = rsConfiguration;
    }

    @Override
    public ResponseEntity<OBReadConsentResponse1> createAccountAccessConsents(
            @ApiParam(value = "Create an Account Request", required = true)
            @Valid
            @RequestBody OBReadConsent1 body,

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
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        log.info("Receveid a new account access consent");

        HttpHeaders additionalHttpHeaders = new HttpHeaders();
        AccountRequestsEndpointWrapper accountRequestEndpoint = RSEndpointWrapperService.accountRequestEndpoint();
        accountRequestEndpoint.authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal);

        if(rsConfiguration.isCustomerInfoEnabled()){
            log.debug("createAccountAccessConsents() - CustomerInfo consent is enabled");
            @NotNull @Valid @Size(min = 1) List<OBExternalPermissions1Code> permissions = body.getData().getPermissions();

            accountRequestEndpoint.filters(
                f -> {
                    accountAccessConsentPermittedPermissionsFilter.filter(body.getData().getPermissions());
                    accountAccessConsentBasicAndDetailPermissionsFilter.filter(body.getData());
                    accountAccessConsentPermittedPermissionsFilter.filterByCustomerInfoPermissionRules(
                            body.getData().getPermissions());
                }
            );
        } else {
            accountRequestEndpoint.filters(
                f -> {
                    accountAccessConsentPermittedPermissionsFilter.filter(body.getData().getPermissions());
                    accountAccessConsentBasicAndDetailPermissionsFilter.filter(body.getData());
                }
            );
        }

        return accountRequestEndpoint.execute(
            (String aispId) -> {

                additionalHttpHeaders.add("x-ob-aisp_id", aispId);
                return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(),
                        OBReadConsentResponse1.class, body);
            }
        );

    }

    public String changePathToCustomerInfoEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        if(path.contains("account-access-consents")){
            path = path.replace("account-access-consents", "customer-info-consent");
        }

        return path;
    }

    @Override
    public ResponseEntity<Void> deleteAccountAccessConsent(
            @ApiParam(value = "ConsentId", required = true)
            @PathVariable("ConsentId") String consentId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

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
    public ResponseEntity<OBReadConsentResponse1> getAccountAccessConsentsConsent(
            @ApiParam(value = "ConsentId", required = true)
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
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

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
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadConsentResponse1.class);
                        }
                );
    }
}
