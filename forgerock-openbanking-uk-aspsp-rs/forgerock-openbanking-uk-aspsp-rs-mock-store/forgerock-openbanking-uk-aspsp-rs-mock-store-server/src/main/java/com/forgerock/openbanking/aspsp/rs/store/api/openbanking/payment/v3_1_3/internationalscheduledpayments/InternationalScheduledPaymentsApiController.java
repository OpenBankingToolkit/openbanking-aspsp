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
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.internationalscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledPaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalScheduledConsent;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledResponse4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduledResponse4Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_3.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteInternationalScheduledResponse4DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConsentConverter.toOBWriteInternationalScheduled3DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalScheduledConverter.toFRWriteInternationalScheduled;
import static uk.org.openbanking.datamodel.service.converter.payment.OBExchangeRateConverter.toOBWriteInternationalConsentResponse4DataExchangeRateInformation;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalScheduledPaymentsApiV3.1.3")
@Slf4j
public class InternationalScheduledPaymentsApiController implements InternationalScheduledPaymentsApi {

    private final InternationalScheduledConsentRepository internationalScheduledConsentRepository;
    private final InternationalScheduledPaymentSubmissionRepository internationalScheduledPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    public InternationalScheduledPaymentsApiController(InternationalScheduledConsentRepository internationalScheduledConsentRepository, InternationalScheduledPaymentSubmissionRepository internationalScheduledPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.internationalScheduledConsentRepository = internationalScheduledConsentRepository;
        this.internationalScheduledPaymentSubmissionRepository = internationalScheduledPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteInternationalScheduledResponse4> createInternationalScheduledPayments(
            OBWriteInternationalScheduled3 obWriteInternationalScheduled3,
            String authorization,
            String xIdempotencyKey,
            String xJwsSignature,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received payment submission: {}", obWriteInternationalScheduled3);

        String paymentId = obWriteInternationalScheduled3.getData().getConsentId();
        FRInternationalScheduledConsent paymentConsent = internationalScheduledConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRInternationalScheduledPaymentSubmission frPaymentSubmission = FRInternationalScheduledPaymentSubmission.builder()
                .id(paymentId)
                .internationalScheduledPayment(toFRWriteInternationalScheduled(obWriteInternationalScheduled3))
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(internationalScheduledPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

    public ResponseEntity getInternationalScheduledPaymentsInternationalScheduledPaymentId(
            String internationalScheduledPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalScheduledPaymentSubmission> isPaymentSubmission = internationalScheduledPaymentSubmissionRepository.findById(internationalScheduledPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + internationalScheduledPaymentId + "' can't be found");
        }
        FRInternationalScheduledPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRInternationalScheduledConsent> isPaymentSetup = internationalScheduledConsentRepository.findById(internationalScheduledPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + internationalScheduledPaymentId + "' can't be found");
        }
        FRInternationalScheduledConsent frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getInternationalScheduledPaymentsInternationalScheduledPaymentIdPaymentDetails(
            String internationalScheduledPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        // Optional endpoint - not implemented
        return new ResponseEntity<OBWritePaymentDetailsResponse1>(HttpStatus.NOT_IMPLEMENTED);
    }

    private OBWriteInternationalScheduledResponse4 packagePayment(FRInternationalScheduledPaymentSubmission frPaymentSubmission, FRInternationalScheduledConsent frInternationalScheduledConsent) {
        return new OBWriteInternationalScheduledResponse4()
                .data(
                        new OBWriteInternationalScheduledResponse4Data()
                                .internationalScheduledPaymentId(frPaymentSubmission.getId())
                                .initiation(toOBWriteInternationalScheduled3DataInitiation(frPaymentSubmission.getInternationalScheduledPayment().getData().getInitiation()))
                                .creationDateTime(frInternationalScheduledConsent.getCreated())
                                .statusUpdateDateTime(frInternationalScheduledConsent.getStatusUpdate())
                                .consentId(frInternationalScheduledConsent.getId())
                                .status(toOBWriteInternationalScheduledResponse4DataStatus(frInternationalScheduledConsent.getStatus()))
                                .exchangeRateInformation(toOBWriteInternationalConsentResponse4DataExchangeRateInformation(frInternationalScheduledConsent.getCalculatedExchangeRate()))
                                .expectedExecutionDateTime(frInternationalScheduledConsent.getInitiation().getRequestedExecutionDateTime()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetInternationalScheduledPayment()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_3();
    }
}
