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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.DomesticStandingOrderPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrder;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticStandingOrderDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDomesticResponseDataRefund;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticStandingOrderPaymentSubmission;
import com.forgerock.openbanking.common.services.openbanking.converter.payment.FRResponseDataRefundConverter;
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
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderResponse6;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderResponse6Data;
import uk.org.openbanking.datamodel.payment.OBWritePaymentDetailsResponse1;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_5.ResponseStatusCodeConverter.toOBWriteDomesticStandingOrderResponse6DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccountDebtor4;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsentResponse6DataInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteDomesticStandingOrderConverter.toFRWriteDomesticStandingOrder;
import static com.forgerock.openbanking.common.services.openbanking.payment.FRResponseDataRefundFactory.frDomesticResponseDataRefund;

@Controller("DomesticStandingOrdersApiV3.1.5")
@Slf4j
public class DomesticStandingOrdersApiController implements DomesticStandingOrdersApi {

    private final DomesticStandingOrderConsentRepository domesticStandingOrderConsentRepository;
    private final DomesticStandingOrderPaymentSubmissionRepository domesticStandingOrderPaymentSubmissionRepository;
    private final ResourceLinkService resourceLinkService;

    public DomesticStandingOrdersApiController(DomesticStandingOrderConsentRepository domesticStandingOrderConsentRepository, DomesticStandingOrderPaymentSubmissionRepository domesticStandingOrderPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.domesticStandingOrderConsentRepository = domesticStandingOrderConsentRepository;
        this.domesticStandingOrderPaymentSubmissionRepository = domesticStandingOrderPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity<OBWriteDomesticStandingOrderResponse6> createDomesticStandingOrders(
            OBWriteDomesticStandingOrder3 obWriteDomesticStandingOrder3,
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
        log.debug("Received payment submission: '{}'", obWriteDomesticStandingOrder3);
        FRWriteDomesticStandingOrder frWriteDomesticStandingOrder = toFRWriteDomesticStandingOrder(obWriteDomesticStandingOrder3);
        log.trace("Converted to: '{}'", frWriteDomesticStandingOrder);

        String paymentId = obWriteDomesticStandingOrder3.getData().getConsentId();
        FRDomesticStandingOrderConsent paymentConsent = domesticStandingOrderConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRDomesticStandingOrderPaymentSubmission frPaymentSubmission = FRDomesticStandingOrderPaymentSubmission.builder()
                .id(paymentId)
                .domesticStandingOrder(frWriteDomesticStandingOrder)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(domesticStandingOrderPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity(frPaymentSubmission, paymentConsent));
    }

    public ResponseEntity getDomesticStandingOrdersDomesticStandingOrderId(
            String domesticStandingOrderId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {

        Optional<FRDomesticStandingOrderPaymentSubmission> isPaymentSubmission = domesticStandingOrderPaymentSubmissionRepository.findById(domesticStandingOrderId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + domesticStandingOrderId + "' can't be found");
        }
        FRDomesticStandingOrderPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRDomesticStandingOrderConsent> isPaymentSetup = domesticStandingOrderConsentRepository.findById(domesticStandingOrderId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + domesticStandingOrderId + "' can't be found");
        }
        FRDomesticStandingOrderConsent frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(responseEntity(frPaymentSubmission, frPaymentSetup));
    }

    public ResponseEntity<OBWritePaymentDetailsResponse1> getDomesticStandingOrdersDomesticStandingOrderIdPaymentDetails(
            String domesticStandingOrderId,
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

    private OBWriteDomesticStandingOrderResponse6 responseEntity(FRDomesticStandingOrderPaymentSubmission frPaymentSubmission, FRDomesticStandingOrderConsent frDomesticStandingOrderConsent) {
        FRReadRefundAccount readRefundAccount = frDomesticStandingOrderConsent.getDomesticStandingOrderConsent().getData().getReadRefundAccount();
        FRWriteDomesticStandingOrderDataInitiation initiation = frPaymentSubmission.getDomesticStandingOrder().getData().getInitiation();
        Optional<FRDomesticResponseDataRefund> refund = frDomesticResponseDataRefund(readRefundAccount, initiation);

        return new OBWriteDomesticStandingOrderResponse6()
                .data(new OBWriteDomesticStandingOrderResponse6Data()
                        .domesticStandingOrderId(frPaymentSubmission.getId())
                        .initiation(toOBWriteDomesticStandingOrderConsentResponse6DataInitiation(initiation))
                        .creationDateTime(frDomesticStandingOrderConsent.getCreated())
                        .statusUpdateDateTime(frDomesticStandingOrderConsent.getStatusUpdate())
                        .status(toOBWriteDomesticStandingOrderResponse6DataStatus(frDomesticStandingOrderConsent.getStatus()))
                        .debtor(toOBCashAccountDebtor4(frDomesticStandingOrderConsent.getInitiation().getDebtorAccount()))
                        .consentId(frDomesticStandingOrderConsent.getId())
                        .refund(refund.map(FRResponseDataRefundConverter::toOBWriteDomesticResponse5DataRefund).orElse(null)))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> getVersion(discovery).getGetDomesticStandingOrder()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_5();
    }

}
