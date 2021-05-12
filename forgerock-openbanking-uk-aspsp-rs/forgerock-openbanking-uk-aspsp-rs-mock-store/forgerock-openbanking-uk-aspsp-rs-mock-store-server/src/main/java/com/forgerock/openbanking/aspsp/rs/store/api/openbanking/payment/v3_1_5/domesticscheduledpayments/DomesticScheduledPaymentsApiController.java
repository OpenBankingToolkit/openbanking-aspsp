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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.domesticscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticScheduledConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticScheduledPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduled;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticScheduledDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDomesticResponseDataRefund;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticScheduledConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticScheduledPaymentSubmission;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRResponseDataRefundConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduled2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledResponse5;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledResponse5Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_5.ResponseStatusCodeConverter.toOBWriteDomesticScheduledResponse5DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccountDebtor4;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConsentConverter.toOBWriteDomesticScheduled2DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticScheduledConverter.toFRWriteDomesticScheduled;
import static com.forgerock.openbanking.common.services.openbanking.payment.FRResponseDataRefundFactory.frDomesticResponseDataRefund;

@Controller("DomesticScheduledPaymentsApiV3.1.5")
@Slf4j
public class DomesticScheduledPaymentsApiController implements DomesticScheduledPaymentsApi {

    private final DomesticScheduledConsentRepository domesticScheduledConsentRepository;
    private final DomesticScheduledPaymentSubmissionRepository domesticScheduledPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    @Autowired
    public DomesticScheduledPaymentsApiController(DomesticScheduledConsentRepository domesticScheduledConsentRepository, DomesticScheduledPaymentSubmissionRepository domesticScheduledPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.domesticScheduledConsentRepository = domesticScheduledConsentRepository;
        this.domesticScheduledPaymentSubmissionRepository = domesticScheduledPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteDomesticScheduledResponse5> createDomesticScheduledPayments(
            OBWriteDomesticScheduled2 obWriteDomesticScheduled2,
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
        log.debug("Received payment submission: '{}'", obWriteDomesticScheduled2);
        FRWriteDomesticScheduled frWriteDomesticScheduled = toFRWriteDomesticScheduled(obWriteDomesticScheduled2);
        log.trace("Converted to: '{}'", frWriteDomesticScheduled);

        String paymentId = obWriteDomesticScheduled2.getData().getConsentId();
        FRDomesticScheduledConsent paymentConsent = domesticScheduledConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        // Save Payment
        FRDomesticScheduledPaymentSubmission frPaymentSubmission = FRDomesticScheduledPaymentSubmission.builder()
                .id(paymentId)
                .domesticScheduledPayment(frWriteDomesticScheduled)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(domesticScheduledPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(frPaymentSubmission, paymentConsent));
    }

    public ResponseEntity getDomesticScheduledPaymentsDomesticScheduledPaymentId(
            String domesticScheduledPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        Optional<FRDomesticScheduledPaymentSubmission> isPaymentSubmission = domesticScheduledPaymentSubmissionRepository.findById(domesticScheduledPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + domesticScheduledPaymentId + "' can't be found");
        }
        FRDomesticScheduledPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRDomesticScheduledConsent> isPaymentSetup = domesticScheduledConsentRepository.findById(domesticScheduledPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + domesticScheduledPaymentId + "' can't be found");
        }
        FRDomesticScheduledConsent frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(responseEntity(frPaymentSubmission, frPaymentSetup));
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getDomesticScheduledPaymentsDomesticScheduledPaymentIdPaymentDetails(
            String domesticScheduledPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) {
        // Optional endpoint - not implemented
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    private OBWriteDomesticScheduledResponse5 responseEntity(FRDomesticScheduledPaymentSubmission frPaymentSubmission, FRDomesticScheduledConsent frDomesticScheduledConsent) {
        FRReadRefundAccount readRefundAccount = frDomesticScheduledConsent.getDomesticScheduledConsent().getData().getReadRefundAccount();
        FRWriteDomesticScheduledDataInitiation initiation = frPaymentSubmission.getDomesticScheduledPayment().getData().getInitiation();
        Optional<FRDomesticResponseDataRefund> refund = frDomesticResponseDataRefund(readRefundAccount, initiation);

        return new OBWriteDomesticScheduledResponse5()
                .data(new OBWriteDomesticScheduledResponse5Data()
                        .domesticScheduledPaymentId(frPaymentSubmission.getId())
                        .initiation(toOBWriteDomesticScheduled2DataInitiation(frDomesticScheduledConsent.getInitiation()))
                        .creationDateTime(frDomesticScheduledConsent.getCreated())
                        .statusUpdateDateTime(frDomesticScheduledConsent.getStatusUpdate())
                        .status(toOBWriteDomesticScheduledResponse5DataStatus(frDomesticScheduledConsent.getStatus()))
                        .debtor(toOBCashAccountDebtor4(frDomesticScheduledConsent.getInitiation().getDebtorAccount()))
                        .consentId(frDomesticScheduledConsent.getId())
                        .refund(refund.map(FRResponseDataRefundConverter::toOBWriteDomesticResponse5DataRefund).orElse(null)))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetDomesticScheduledPayment()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}
