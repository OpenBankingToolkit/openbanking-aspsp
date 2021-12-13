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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_8.vrp;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.FRDomesticVrpPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVrpPaymentSubmission;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksVrpPayment;
import uk.org.openbanking.datamodel.vrp.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toFRDomesticVRPRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toOBDomesticVRPRequest;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpsApiV3.1.8")
@Slf4j
public class DomesticVrpsApiController implements DomesticVrpsApi {

    private final FRDomesticVrpPaymentSubmissionRepository paymentSubmissionRepository;
    private final DomesticVRPConsentRepository domesticVRPConsentRepository;
    private final ResourceLinkService resourceLinkService;

    public DomesticVrpsApiController(FRDomesticVrpPaymentSubmissionRepository paymentSubmissionRepository, DomesticVRPConsentRepository domesticVRPConsentRepository, ResourceLinkService resourceLinkService) {
        this.paymentSubmissionRepository = paymentSubmissionRepository;
        this.domesticVRPConsentRepository = domesticVRPConsentRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity domesticVrpGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        Optional<FRDomesticVrpPaymentSubmission> optionalVrpPayment = paymentSubmissionRepository.findById(domesticVRPId);
        if (!optionalVrpPayment.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic VRP payment '" + domesticVRPId + "' " +
                    "can't be found");
        }
        Optional<FRDomesticVRPConsent> frDomesticVRPConsent = domesticVRPConsentRepository.findById(domesticVRPId);
        if (!frDomesticVRPConsent.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("VRP Payment consent behind payment submission '" + domesticVRPId + "' can't be found");
        }
        return ResponseEntity.ok(responseEntity(optionalVrpPayment.get(), frDomesticVRPConsent.get()));
    }

    @Override
    public ResponseEntity domesticVrpPaymentDetailsGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        Optional<FRDomesticVrpPaymentSubmission> optionalVrpPayment = paymentSubmissionRepository.findById(domesticVRPId);
        if (!optionalVrpPayment.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Domestic VRP payment '" + domesticVRPId + "' " +
                    "can't be found to retrieve the details");
        }
        log.debug("Found VRP payment '{}'", domesticVRPId);
        // Build the response object with data just to meet the expected data defined by the spec
        FRDomesticVrpPaymentSubmission paymentSubmission = optionalVrpPayment.get();
        OBDomesticVRPDetailsDataPaymentStatus.StatusEnum status = OBDomesticVRPDetailsDataPaymentStatus.StatusEnum.fromValue(
                paymentSubmission.getStatus().getValue()
        );

        OBDomesticVRPDetailsDataStatusDetail.StatusReasonEnum statusReasonEnum = OBDomesticVRPDetailsDataStatusDetail.StatusReasonEnum.PENDINGSETTLEMENT;
        String localInstrument = paymentSubmission.getDomesticVrpPayment().getData().getInstruction().getLocalInstrument();
        OBDomesticVRPDetails vrpDetails = new OBDomesticVRPDetails()
                .data(
                        new OBDomesticVRPDetailsData()
                                .addPaymentStatusItem(
                                        new OBDomesticVRPDetailsDataPaymentStatus()
                                                .status(status)
                                                .paymentTransactionId(UUID.randomUUID().toString())
                                                .statusUpdateDateTime(new DateTime(paymentSubmission.getUpdated()))
                                                .statusDetail(
                                                        new OBDomesticVRPDetailsDataStatusDetail()
                                                                .localInstrument(localInstrument)
                                                                .status(status.getValue())
                                                                .statusReason(statusReasonEnum)
                                                                .statusReasonDescription(statusReasonEnum.getValue())
                                                )
                                )

                );
        return ResponseEntity.ok(vrpDetails);
    }

    @Override
    /**
     *         @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
     *             @RequestHeader(value = "Authorization", required = true) String authorization,
     *
     *             @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
     *             @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,
     *
     *             @ApiParam(value = "Default", required = true)
     *             @Valid
     *             @RequestBody OBDomesticVRPRequest obDomesticVRPRequest,
     *
     *             @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
     *             @RequestHeader(value = "x-fapi-auth-date", required = false) String xFapiAuthDate,
     *
     *             @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
     *             @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,
     *
     *             @ApiParam(value = "An RFC4122 UID used as a correlation id.")
     *             @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,
     *
     *             @ApiParam(value = "Indicates the user-agent that the PSU is using.")
     *             @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,
     */
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpPost(
            String authorization, String xJwsSignature, OBDomesticVRPRequest obDomesticVRPRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received VRP payment submission: '{}'", obDomesticVRPRequest);
        String consentId = obDomesticVRPRequest.getData().getConsentId();
        FRDomesticVRPConsent frDomesticVRPConsent = domesticVRPConsentRepository.findById(consentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(consentId))
                );
        log.debug("Found VRP consent '{}' to match this consent id: {} ", frDomesticVRPConsent, consentId);
        FRDomesticVRPRequest frDomesticVRPRequest = toFRDomesticVRPRequest(obDomesticVRPRequest);
        FRDomesticVrpPaymentSubmission vrpPaymentSubmission = FRDomesticVrpPaymentSubmission.builder()
                .id(consentId)
                .domesticVrpPayment(frDomesticVRPRequest)
                .status(OBDomesticVRPResponseData.StatusEnum.PENDING)
                .created(new Date())
                .updated(new Date())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        vrpPaymentSubmission = new IdempotentRepositoryAdapter<>(paymentSubmissionRepository)
                .idempotentSave(vrpPaymentSubmission);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(obDomesticVRPRequest, vrpPaymentSubmission, frDomesticVRPConsent));

    }

    private OBDomesticVRPResponse responseEntity(
            FRDomesticVrpPaymentSubmission paymentSubmission,
            FRDomesticVRPConsent frDomesticVRPConsent) {
        OBDomesticVRPRequest obDomesticVRPRequest = toOBDomesticVRPRequest(paymentSubmission.getDomesticVrpPayment());
        return responseEntity(obDomesticVRPRequest, paymentSubmission, frDomesticVRPConsent);
    }

    private OBDomesticVRPResponse responseEntity(
            OBDomesticVRPRequest obDomesticVRPRequest,
            FRDomesticVrpPaymentSubmission paymentSubmission,
            FRDomesticVRPConsent frDomesticVRPConsent) {
        OBDomesticVRPResponse response = new OBDomesticVRPResponse()
                .data(
                        new OBDomesticVRPResponseData()
                                .consentId(paymentSubmission.getId())
                                .domesticVRPId(paymentSubmission.getId())
                                .status(paymentSubmission.getStatus())
                                .creationDateTime(new DateTime(paymentSubmission.getCreated()))
                                .debtorAccount(obDomesticVRPRequest.getData().getInitiation().getDebtorAccount())
                                .initiation(obDomesticVRPRequest.getData().getInitiation())
                                .instruction(obDomesticVRPRequest.getData().getInstruction())
                ).links(
                        resourceLinkService.toVrpSelfLink(
                                paymentSubmission, discovery -> getVersion(discovery).getGetDomesticVrpPayment()
                        )
                ).meta(new Meta())
                .risk(toOBRisk1(frDomesticVRPConsent.getRisk()));

        if (frDomesticVRPConsent.getVrpDetails().getData().getReadRefundAccount().equals(FRReadRefundAccount.YES)) {
            response.getData().refund(obDomesticVRPRequest.getData().getInitiation().getDebtorAccount());
        }
        // just to meet the expected data defined by the spec
        response.getData().expectedExecutionDateTime(DateTime.now())
                .expectedSettlementDateTime(DateTime.now())
                .charges(List.of(
                        new OBDomesticVRPResponseDataCharges()
                                .type(OBExternalPaymentChargeType1Code.BALANCETRANSFEROUT)
                                .chargeBearer(OBChargeBearerType1Code.BORNEBYCREDITOR)
                                .amount(
                                        new OBActiveOrHistoricCurrencyAndAmount()
                                                .amount("1.00")
                                                .currency("GBP")
                                )
                ));

        return response;
    }

    protected OBDiscoveryAPILinksVrpPayment getVersion(DiscoveryConfigurationProperties.VrpPaymentApis discovery) {
        return discovery.getV_3_1_8();
    }
}
