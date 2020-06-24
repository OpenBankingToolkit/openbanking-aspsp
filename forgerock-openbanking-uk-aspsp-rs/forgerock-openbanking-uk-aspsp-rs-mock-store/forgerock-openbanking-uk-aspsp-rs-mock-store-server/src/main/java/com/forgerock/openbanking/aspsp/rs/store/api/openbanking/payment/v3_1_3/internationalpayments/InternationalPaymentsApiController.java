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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.internationalpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalConsent4Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.payments.InternationalPaymentSubmission4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalConsent4;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.payment.FRInternationalPaymentSubmission4;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalResponse4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalResponse4Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteInternationalResponse4DataStatus;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("InternationalPaymentsApiV3.1.3")
@Slf4j
public class InternationalPaymentsApiController implements InternationalPaymentsApi {

    private final InternationalConsent4Repository internationalConsentRepository;
    private final InternationalPaymentSubmission4Repository internationalPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    public InternationalPaymentsApiController(InternationalConsent4Repository internationalConsentRepository, InternationalPaymentSubmission4Repository internationalPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.internationalConsentRepository = internationalConsentRepository;
        this.internationalPaymentSubmissionRepository = internationalPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

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
        log.debug("Received payment submission: {}", obWriteInternational3);

        String paymentId = obWriteInternational3.getData().getConsentId();
        FRInternationalConsent4 paymentConsent = internationalConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRInternationalPaymentSubmission4 frPaymentSubmission = FRInternationalPaymentSubmission4.builder()
                .id(paymentId)
                .internationalPayment(obWriteInternational3)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(internationalPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

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
        Optional<FRInternationalPaymentSubmission4> isPaymentSubmission = internationalPaymentSubmissionRepository.findById(internationalPaymentId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + internationalPaymentId + "' can't be found");
        }
        FRInternationalPaymentSubmission4 frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRInternationalConsent4> isPaymentSetup = internationalConsentRepository.findById(internationalPaymentId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + internationalPaymentId + "' can't be found");
        }
        FRInternationalConsent4 frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getInternationalPaymentsInternationalPaymentIdPaymentDetails(
            String internationalPaymentId,
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

    private OBWriteInternationalResponse4 packagePayment(FRInternationalPaymentSubmission4 frPaymentSubmission, FRInternationalConsent4 frInternationalConsent) {
        return new OBWriteInternationalResponse4()
                .data(new OBWriteInternationalResponse4Data()
                        .internationalPaymentId(frPaymentSubmission.getId())
                        .initiation(frPaymentSubmission.getInternationalPayment().getData().getInitiation())
                        .creationDateTime(frInternationalConsent.getCreated())
                        .statusUpdateDateTime(frInternationalConsent.getStatusUpdate())
                        .status(toOBWriteInternationalResponse4DataStatus(frInternationalConsent.getStatus()))
                        .consentId(frInternationalConsent.getId())
                        .exchangeRateInformation(frInternationalConsent.getCalculatedExchangeRate()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_1_3().getGetInternationalPayment()))
                .meta(new Meta());
    }

}
