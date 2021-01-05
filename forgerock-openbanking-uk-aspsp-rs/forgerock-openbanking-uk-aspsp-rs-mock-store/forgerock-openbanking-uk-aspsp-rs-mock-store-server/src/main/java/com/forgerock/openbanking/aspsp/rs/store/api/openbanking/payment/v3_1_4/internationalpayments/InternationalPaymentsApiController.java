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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_4.internationalpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternational;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalPaymentSubmission;
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
import uk.org.openbanking.datamodel.payment.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_3.ResponseConverter.StatusCodeConverter.toOBWriteInternationalResponse4DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRExchangeRateConverter.toOBWriteInternationalConsentResponse4DataExchangeRateInformation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConsentConverter.toOBWriteInternational3DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalConverter.toFRWriteInternational;
import static com.forgerock.openbanking.common.services.openbanking.payment.PaymentUtil.getOBWriteInternationalResponse4DataRefundInstance;

@Controller("InternationalPaymentsApiV3.1.4")
@Slf4j
public class InternationalPaymentsApiController implements InternationalPaymentsApi {

    private final InternationalConsentRepository internationalConsentRepository;
    private final InternationalPaymentSubmissionRepository internationalPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    public InternationalPaymentsApiController(InternationalConsentRepository internationalConsentRepository, InternationalPaymentSubmissionRepository internationalPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.internationalConsentRepository = internationalConsentRepository;
        this.internationalPaymentSubmissionRepository = internationalPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity<OBWriteInternationalResponse4> createInternationalPayments(
            OBWriteInternational3 obWriteInternational3,
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
        log.debug("Received payment submission: '{}'", obWriteInternational3);
        FRWriteInternational frWriteInternational = toFRWriteInternational(obWriteInternational3);
        log.trace("Converted to: '{}'", frWriteInternational);

        String paymentId = obWriteInternational3.getData().getConsentId();
        FRInternationalConsent paymentConsent = internationalConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRInternationalPaymentSubmission frPaymentSubmission = FRInternationalPaymentSubmission.builder()
                .id(paymentId)
                .internationalPayment(frWriteInternational)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(internationalPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityInstance(frPaymentSubmission, paymentConsent));
    }

    @Override
    public ResponseEntity getInternationalPaymentsInternationalPaymentId(
            String internationalPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRInternationalPaymentSubmission> isPaymentSubmission = internationalPaymentSubmissionRepository.findById(internationalPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + internationalPaymentId + "' can't be found");
        }
        FRInternationalPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRInternationalConsent> isPaymentConsentSetup = internationalConsentRepository.findById(internationalPaymentId);
        if (!isPaymentConsentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + internationalPaymentId + "' can't be found");
        }
        FRInternationalConsent frPaymentSetup = isPaymentConsentSetup.get();
        return ResponseEntity.ok(entityInstance(frPaymentSubmission, frPaymentSetup));
    }

    @Override
    public ResponseEntity<OBWritePaymentDetailsResponse1> getInternationalPaymentsInternationalPaymentIdPaymentDetails(
            String internationalPaymentId,
            String authorization,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        // Optional endpoint - not implemented
        return new ResponseEntity<OBWritePaymentDetailsResponse1>(HttpStatus.NOT_IMPLEMENTED);
    }

    private OBWriteInternationalResponse4 entityInstance(FRInternationalPaymentSubmission frPaymentSubmission, FRInternationalConsent frInternationalConsent) {
        OBWriteInternationalResponse4 obWriteInternationalResponse4 = new OBWriteInternationalResponse4()
                .data(new OBWriteInternationalResponse4Data()
                        .internationalPaymentId(frPaymentSubmission.getId())
                        .initiation(toOBWriteInternational3DataInitiation(frPaymentSubmission.getInternationalPayment().getData().getInitiation()))
                        .creationDateTime(frInternationalConsent.getCreated())
                        .statusUpdateDateTime(frInternationalConsent.getStatusUpdate())
                        .status(toOBWriteInternationalResponse4DataStatus(frInternationalConsent.getStatus()))
                        .consentId(frInternationalConsent.getId())
                        .exchangeRateInformation(toOBWriteInternationalConsentResponse4DataExchangeRateInformation(frInternationalConsent.getCalculatedExchangeRate())))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetInternationalPayment()))
                .meta(new Meta());

        // ZD: 55834 - https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/14
        getOBWriteInternationalResponse4DataRefundInstance(
                frInternationalConsent.getInternationalConsent().getData().getReadRefundAccount(),
                toOBWriteInternational3DataInitiation(frInternationalConsent.getInitiation())
        ).ifPresent(
                dataRefund -> obWriteInternationalResponse4.getData().setRefund(dataRefund)
        );
        return obWriteInternationalResponse4;
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_4();
    }
}
