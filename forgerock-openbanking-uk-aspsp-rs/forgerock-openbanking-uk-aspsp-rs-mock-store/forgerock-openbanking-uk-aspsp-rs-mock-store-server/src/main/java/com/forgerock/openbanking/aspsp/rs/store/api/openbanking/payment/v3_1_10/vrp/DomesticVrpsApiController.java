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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_10.vrp;

import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.DomesticVRPConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.vrp.FRDomesticVrpPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVrpPaymentSubmission;
import com.forgerock.openbanking.common.services.openbanking.IdempotencyService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.repositories.TppRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksVrpPayment;
import uk.org.openbanking.datamodel.vrp.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.vrp.OBChargeBearerType1Code;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPDetails;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPDetailsData;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPDetailsDataPaymentStatus;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPDetailsDataStatusDetail;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPResponseDataCharges;
import uk.org.openbanking.datamodel.vrp.OBExternalPaymentChargeType1Code;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPResponse;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPResponseData;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRPaymentRiskConverter.toOBRisk1;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toFRDomesticVRPRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters.toOBDomesticVRPRequestv3_1_10;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller("DomesticVrpsApiV3.1.10")
@Slf4j
public class DomesticVrpsApiController implements DomesticVrpsApi {

    private final FRDomesticVrpPaymentSubmissionRepository paymentSubmissionRepository;
    private final DomesticVRPConsentRepository domesticVRPConsentRepository;
    private final ResourceLinkService resourceLinkService;
    private final TppRepository tppRepository;

    public DomesticVrpsApiController(FRDomesticVrpPaymentSubmissionRepository paymentSubmissionRepository, DomesticVRPConsentRepository domesticVRPConsentRepository, ResourceLinkService resourceLinkService, TppRepository tppRepository) {
        this.paymentSubmissionRepository = paymentSubmissionRepository;
        this.domesticVRPConsentRepository = domesticVRPConsentRepository;
        this.resourceLinkService = resourceLinkService;
        this.tppRepository = tppRepository;
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
        final String consentId = optionalVrpPayment.get().getDomesticVrpPayment().getData().getConsentId();
        Optional<FRDomesticVRPConsent> frDomesticVRPConsent = domesticVRPConsentRepository.findById(consentId);
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
        OBDomesticVRPDetailsDataPaymentStatus.StatusEnum status = OBDomesticVRPDetailsDataPaymentStatus.StatusEnum.valueOf(
                paymentSubmission.getStatus()
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
     *             @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
     *             @RequestHeader(value = "Authorization", required = true) String authorization,
     *
     *             @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours. ", required = true)
     *             @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,
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
     *
     *             @ApiParam(value = "The PISP ID" )
     *             @RequestHeader(value="x-ob-client-id") String clientId,
     */
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpPost(
            String authorization, String xIdempotencyKey, String xJwsSignature, OBDomesticVRPRequest obDomesticVRPRequest,
            String xFapiAuthDate, String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent, String clientId,
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

        final Tpp tpp = tppRepository.findByClientId(clientId);
        Optional<FRDomesticVrpPaymentSubmission> vrpPaymentSubmissionByIdempotencyKey = paymentSubmissionRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (vrpPaymentSubmissionByIdempotencyKey.isPresent()) {
            final FRDomesticVrpPaymentSubmission frDomesticVrpPaymentSubmission = vrpPaymentSubmissionByIdempotencyKey.get();
            IdempotencyService.validateIdempotencyRequest(xIdempotencyKey, frDomesticVRPRequest, frDomesticVrpPaymentSubmission);
            log.info("Idempotent request for VRP payment is valid. Returning [201 CREATED] but take no further action.");
            return createOBDomesticVRPResponse(obDomesticVRPRequest, frDomesticVRPConsent, frDomesticVrpPaymentSubmission);
        }

        FRDomesticVrpPaymentSubmission vrpPaymentSubmission = FRDomesticVrpPaymentSubmission.builder()
                .idempotencyKey(xIdempotencyKey)
                .pispId(tpp.getId())
                .domesticVrpPayment(frDomesticVRPRequest)
                .status(OBDomesticVRPResponseData.StatusEnum.PENDING.name())
                .created(new Date())
                .updated(new Date())
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        vrpPaymentSubmission = paymentSubmissionRepository.save(vrpPaymentSubmission);

        return createOBDomesticVRPResponse(obDomesticVRPRequest, frDomesticVRPConsent, vrpPaymentSubmission);
    }

    private ResponseEntity<OBDomesticVRPResponse> createOBDomesticVRPResponse(OBDomesticVRPRequest obDomesticVRPRequest,
                                                                              FRDomesticVRPConsent frDomesticVRPConsent,
                                                                              FRDomesticVrpPaymentSubmission frDomesticVrpPaymentSubmission) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(responseEntity(obDomesticVRPRequest, frDomesticVrpPaymentSubmission, frDomesticVRPConsent));
    }

    private OBDomesticVRPResponse responseEntity(
            FRDomesticVrpPaymentSubmission paymentSubmission,
            FRDomesticVRPConsent frDomesticVRPConsent) {
        OBDomesticVRPRequest obDomesticVRPRequest = toOBDomesticVRPRequestv3_1_10(paymentSubmission.getDomesticVrpPayment());
        return responseEntity(obDomesticVRPRequest, paymentSubmission, frDomesticVRPConsent);
    }

    private OBDomesticVRPResponse responseEntity(
            OBDomesticVRPRequest obDomesticVRPRequest,
            FRDomesticVrpPaymentSubmission paymentSubmission,
            FRDomesticVRPConsent frDomesticVRPConsent) {
        OBDomesticVRPResponse response = new OBDomesticVRPResponse()
                .data(
                        new OBDomesticVRPResponseData()
                                .consentId(frDomesticVRPConsent.getId())
                                .domesticVRPId(paymentSubmission.getId())
                                .status(OBDomesticVRPResponseData.StatusEnum.valueOf(paymentSubmission.getStatus()))
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
        final DateTime creationDateTime = response.getData().getCreationDateTime();
        response.getData().expectedExecutionDateTime(creationDateTime.plusMinutes(1))
                .expectedSettlementDateTime(creationDateTime.plusMinutes(5))
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
        return discovery.getV_3_1_10();
    }
}
