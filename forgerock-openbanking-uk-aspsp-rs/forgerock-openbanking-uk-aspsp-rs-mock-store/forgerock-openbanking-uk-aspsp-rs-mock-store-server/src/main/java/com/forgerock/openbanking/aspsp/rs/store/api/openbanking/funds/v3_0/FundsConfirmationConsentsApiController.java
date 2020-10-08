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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_0;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.funds.FRFundsConfirmationConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentDataResponse1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsentResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount3;
import static com.forgerock.openbanking.common.services.openbanking.converter.fund.FRFundsConfirmationConsentConverter.toFRFundsConfirmationConsentData;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("FundsConfirmationConsentsApiV3.0")
@Slf4j
public class FundsConfirmationConsentsApiController implements FundsConfirmationConsentsApi {

    private FundsConfirmationConsentRepository fundsConfirmationConsentRepository;
    private TppRepository tppRepository;
    private ConsentMetricService consentMetricService;
    private ResourceLinkService resourceLinkService;

    public FundsConfirmationConsentsApiController(FundsConfirmationConsentRepository fundsConfirmationConsentRepository, TppRepository tppRepository, ConsentMetricService consentMetricService, ResourceLinkService resourceLinkService) {
        this.fundsConfirmationConsentRepository = fundsConfirmationConsentRepository;
        this.tppRepository = tppRepository;
        this.consentMetricService = consentMetricService;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity<OBFundsConfirmationConsentResponse1> createFundsConfirmationConsent(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBFundsConfirmationConsent1 obFundsConfirmationConsent,

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

            @ApiParam(value = "The PISP Client ID" )
            @RequestHeader(value="x-ob-client-id", required=false) String clientId,

            HttpServletRequest request,

            Principal principal
    ) {
        log.debug("Received '{}'.", obFundsConfirmationConsent);

        final Tpp tpp = tppRepository.findByClientId(clientId);
        FRFundsConfirmationConsent consent = FRFundsConfirmationConsent.builder()
                .id(IntentType.FUNDS_CONFIRMATION_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGAUTHORISATION)
                .fundsConfirmationConsent(toFRFundsConfirmationConsentData(obFundsConfirmationConsent))
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();

        consentMetricService.sendConsentActivity(new ConsentStatusEntry(consent.getId(), consent.getStatus().name()));

        consent = fundsConfirmationConsentRepository.save(consent);

        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consent));
    }

    @Override
    public ResponseEntity getFundsConfirmationConsentsConsentId(
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

            @ApiParam(value = "The PISP ID" )
            @RequestHeader(value="x-ob-pisp-id", required=false) String pispId,

            HttpServletRequest request,

            Principal principal

    ) {
        return fundsConfirmationConsentRepository.findById(consentId)
                .<ResponseEntity>map(frFundsConfirmationConsent1 -> ResponseEntity.ok(packageResponse(frFundsConfirmationConsent1)))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funds Confirmation consent '" + consentId + "' can't be found"));
    }

    @Override
    public ResponseEntity deleteFundsConfirmationConsentsConsentId(
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

            @ApiParam(value = "The PISP ID" )
            @RequestHeader(value="x-ob-pisp-id", required=false) String pispId,

            HttpServletRequest request,

            Principal principal
            ) {
        Optional<FRFundsConfirmationConsent> existingConsent = fundsConfirmationConsentRepository.findById(consentId);
        if (existingConsent.isPresent()) {
            log.debug("Deleting fund confirmation consent: {}", consentId);
            fundsConfirmationConsentRepository.deleteById(consentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Funds Confirmation consent '" + consentId + "' can't be found");
        }

    }

    private OBFundsConfirmationConsentResponse1 packageResponse(FRFundsConfirmationConsent consent) {
        return new OBFundsConfirmationConsentResponse1()
                .data(new OBFundsConfirmationConsentDataResponse1()
                        .debtorAccount(toOBCashAccount3(consent.getFundsConfirmationConsent().getDebtorAccount()))
                        .creationDateTime(consent.getCreated())
                        .status(consent.getStatus().toOBExternalRequestStatus1Code())
                        .statusUpdateDateTime(consent.getStatusUpdate())
                        .expirationDateTime(consent.getFundsConfirmationConsent().getExpirationDateTime())
                        .consentId(consent.getId())
                )
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(consent, discovery -> discovery.getV_3_0().getGetFundsConfirmationConsent()))
                ;
    }
}
