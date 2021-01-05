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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteInternationalStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderPaymentSubmission;
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
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrderResponse7Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_5.ResponseConverter.StatusCodeConverter.toOBWriteInternationalStandingOrderResponse7DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBDebtorIdentification1;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConsentConverter.toOBWriteInternationalStandingOrder4DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteInternationalStandingOrderConverter.toFRWriteInternationalStandingOrder;
import static com.forgerock.openbanking.common.services.openbanking.payment.PaymentUtil.getOBWriteInternationalResponse5DataRefundInstance;

@Controller("InternationalStandingOrdersApiV3.1.5")
@Slf4j
public class InternationalStandingOrdersApiController implements InternationalStandingOrdersApi {

    private final InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository;
    private final InternationalStandingOrderPaymentSubmissionRepository internationalStandingOrderPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    public InternationalStandingOrdersApiController(InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository, InternationalStandingOrderPaymentSubmissionRepository internationalStandingOrderPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.internationalStandingOrderConsentRepository = internationalStandingOrderConsentRepository;
        this.internationalStandingOrderPaymentSubmissionRepository = internationalStandingOrderPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteInternationalStandingOrderResponse7> createInternationalStandingOrders(
            OBWriteInternationalStandingOrder4 obWriteInternationalStandingOrder4,
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
        log.debug("Received payment submission: '{}'", obWriteInternationalStandingOrder4);
        FRWriteInternationalStandingOrder frInternationalStandingOrder = toFRWriteInternationalStandingOrder(obWriteInternationalStandingOrder4);
        log.trace("Converted to: '{}'", frInternationalStandingOrder);

        String paymentId = obWriteInternationalStandingOrder4.getData().getConsentId();
        FRInternationalStandingOrderConsent paymentConsent = internationalStandingOrderConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRInternationalStandingOrderPaymentSubmission frPaymentSubmission = FRInternationalStandingOrderPaymentSubmission.builder()
                .id(paymentId)
                .internationalStandingOrder(frInternationalStandingOrder)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .version(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(internationalStandingOrderPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityInstance(frPaymentSubmission, paymentConsent));
    }

    public ResponseEntity getInternationalStandingOrdersInternationalStandingOrderPaymentId(
            String internationalStandingOrderPaymentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {

        Optional<FRInternationalStandingOrderPaymentSubmission> isPaymentSubmission = internationalStandingOrderPaymentSubmissionRepository.findById(internationalStandingOrderPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + internationalStandingOrderPaymentId + "' can't be found");
        }
        FRInternationalStandingOrderPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRInternationalStandingOrderConsent> isPaymentSetup = internationalStandingOrderConsentRepository.findById(internationalStandingOrderPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + internationalStandingOrderPaymentId + "' can't be found");
        }
        FRInternationalStandingOrderConsent frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(entityInstance(frPaymentSubmission, frPaymentSetup));
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getInternationalStandingOrdersInternationalStandingOrderPaymentIdPaymentDetails(
            String internationalStandingOrderPaymentId,
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

    private OBWriteInternationalStandingOrderResponse7 entityInstance(FRInternationalStandingOrderPaymentSubmission frPaymentSubmission, FRInternationalStandingOrderConsent frInternationalStandingOrderConsent) {

        OBWriteInternationalStandingOrderResponse7 obWriteInternationalStandingOrderResponse7 = new OBWriteInternationalStandingOrderResponse7()
                .data(new OBWriteInternationalStandingOrderResponse7Data()
                        .internationalStandingOrderId(frPaymentSubmission.getId())
                        .initiation(toOBWriteInternationalStandingOrder4DataInitiation(frPaymentSubmission.getInternationalStandingOrder().getData().getInitiation()))
                        .creationDateTime(frInternationalStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(frInternationalStandingOrderConsent.getStatusUpdate())
                        .status(toOBWriteInternationalStandingOrderResponse7DataStatus(frInternationalStandingOrderConsent.getStatus()))
                        .debtor(toOBDebtorIdentification1(frInternationalStandingOrderConsent.getInitiation().getDebtorAccount()))
                        .consentId(frInternationalStandingOrderConsent.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetInternationalStandingOrder()))
                .meta(new Meta());

        // ZD: 55834 - https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/14
        getOBWriteInternationalResponse5DataRefundInstance(
                frInternationalStandingOrderConsent.getInternationalStandingOrderConsent().getData().getReadRefundAccount(),
                toOBWriteInternationalStandingOrder4DataInitiation(frInternationalStandingOrderConsent.getInitiation())
        ).ifPresent(dataRefund -> obWriteInternationalStandingOrderResponse7.getData().setRefund(dataRefund));

        return obWriteInternationalStandingOrderResponse7;
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}
