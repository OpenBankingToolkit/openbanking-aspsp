/**
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

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmation1;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
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
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationData1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationDataResponse1;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("FundsConfirmationsApiV3.0")
@Slf4j
public class FundsConfirmationsApiController implements FundsConfirmationsApi {

    private FundsConfirmationRepository fundsConfirmationRepository;
    private FundsConfirmationConsentRepository fundsConfirmationConsentRepository;
    private FundsAvailabilityService fundsAvailabilityService;
    private ResourceLinkService resourceLinkService;

    public FundsConfirmationsApiController(FundsConfirmationRepository fundsConfirmationRepository, FundsConfirmationConsentRepository fundsConfirmationConsentRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        this.fundsConfirmationRepository = fundsConfirmationRepository;
        this.fundsConfirmationConsentRepository = fundsConfirmationConsentRepository;
        this.fundsAvailabilityService = fundsAvailabilityService;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createFundsConfirmation(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBFundsConfirmation1 obFundsConfirmation,

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

            Principal principal
    ) {
        log.debug("Create funds confirmation: {}", obFundsConfirmation);

        String consentId = obFundsConfirmation.getData().getConsentId();
        Optional<FRFundsConfirmation1> isSubmission = fundsConfirmationRepository.findById(consentId);

        Optional<FRFundsConfirmationConsent1> isConsent = fundsConfirmationConsentRepository.findById(consentId);
        if (!isConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consent behind funds confirmation submission '" + consentId + "' can't be found");
        }

        // Check if funds are available on the account selected in consent
        boolean areFundsAvailable = fundsAvailabilityService.isFundsAvailable(
                isConsent.get().getAccountId(),
                obFundsConfirmation.getData().getInstructedAmount().getAmount());


        FRFundsConfirmation1 frFundsConfirmation = isSubmission
                .orElseGet(() ->
                        FRFundsConfirmation1.builder()
                                .id(consentId)
                                .created(DateTime.now())
                                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                                .build()
        );
        frFundsConfirmation.setFundsAvailable(areFundsAvailable);
        frFundsConfirmation.setFundsConfirmation(obFundsConfirmation);
        frFundsConfirmation = fundsConfirmationRepository.save(frFundsConfirmation);

        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(frFundsConfirmation, isConsent.get()));
    }

    @Override
    public ResponseEntity getFundsConfirmationId(
            @ApiParam(value = "FundsConfirmationId", required = true)
            @PathVariable("FundsConfirmationId") String fundsConfirmationId,

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

            Principal principal
    ) {
        Optional<FRFundsConfirmation1> isFundsConfirmation = fundsConfirmationRepository.findById(fundsConfirmationId);
        if (!isFundsConfirmation.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + fundsConfirmationId + "' can't be found");
        }
        FRFundsConfirmation1 frPaymentSubmission = isFundsConfirmation.get();

        Optional<FRFundsConfirmationConsent1> isSetup = fundsConfirmationConsentRepository.findById(frPaymentSubmission.getFundsConfirmation().getData().getConsentId());
        return isSetup
                .<ResponseEntity>map(frFundsConfirmationConsent1 -> ResponseEntity.ok(packageResponse(frPaymentSubmission, frFundsConfirmationConsent1)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + fundsConfirmationId + "' can't be found"));

    }

    private OBFundsConfirmationResponse1 packageResponse(FRFundsConfirmation1 fundsConfirmation, FRFundsConfirmationConsent1 consent) {
        final OBFundsConfirmationData1 obFundsConfirmationData = fundsConfirmation.getFundsConfirmation().getData();
        return new OBFundsConfirmationResponse1()
                .data(new OBFundsConfirmationDataResponse1()
                        .instructedAmount(obFundsConfirmationData.getInstructedAmount())
                        .creationDateTime(fundsConfirmation.getCreated())
                        .fundsConfirmationId(fundsConfirmation.getId())
                        .fundsAvailable(fundsConfirmation.isFundsAvailable())
                        .reference(obFundsConfirmationData.getReference())
                        .consentId(consent.getId()))
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(fundsConfirmation, discovery -> discovery.getV_3_0().getCreateFundsConfirmation()))
                ;
    }

}
