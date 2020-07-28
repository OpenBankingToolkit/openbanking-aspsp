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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_0.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderPaymentSubmission3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.DomesticStandingOrderConsent5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderPaymentSubmission3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticStandingOrderConsent5;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrderResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticStandingOrderConverter.toOBDomesticStandingOrder1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBDomesticStandingOrderConverter.toOBDomesticStandingOrder3;

@Controller("DomesticStandingOrdersApiV3.0")
@Slf4j
public class DomesticStandingOrdersApiController implements DomesticStandingOrdersApi {
    private DomesticStandingOrderConsent5Repository domesticStandingOrderConsentRepository;
    private DomesticStandingOrderPaymentSubmission3Repository domesticStandingOrderPaymentSubmissionRepository;
    private ResourceLinkService resourceLinkService;

    @Autowired
    public DomesticStandingOrdersApiController(DomesticStandingOrderConsent5Repository domesticStandingOrderConsentRepository, DomesticStandingOrderPaymentSubmission3Repository domesticStandingOrderPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        this.domesticStandingOrderConsentRepository = domesticStandingOrderConsentRepository;
        this.domesticStandingOrderPaymentSubmissionRepository = domesticStandingOrderPaymentSubmissionRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createDomesticStandingOrders(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBWriteDomesticStandingOrder1 obWriteDomesticStandingOrder1,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Every request will be processed only once per x-idempotency-key.  The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "A detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = true) String xJwsSignature,

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
    ) throws OBErrorResponseException {
        log.debug("Received payment submission: {}", obWriteDomesticStandingOrder1);
        OBWriteDomesticStandingOrder3 payment = toOBWriteDomesticStandingOrder3(obWriteDomesticStandingOrder1);
        log.trace("Converted to OBWriteDomesticStandingOrder2: {}", payment);

        String paymentId = payment.getData().getConsentId();
        FRDomesticStandingOrderConsent5 paymentConsent = domesticStandingOrderConsentRepository.findById(paymentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_CONSENT_BEHIND_SUBMISSION_NOT_FOUND.toOBError1(paymentId))
                );
        log.debug("Found consent '{}' to match this payment id: {} ", paymentConsent, paymentId);

        FRDomesticStandingOrderPaymentSubmission3 frPaymentSubmission = FRDomesticStandingOrderPaymentSubmission3.builder()
                .id(paymentId)
                .domesticStandingOrder(payment)
                .created(new Date())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(domesticStandingOrderPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(packagePayment(frPaymentSubmission, paymentConsent));
    }

    @Override
    public ResponseEntity getDomesticStandingOrdersDomesticStandingOrderId(
            @ApiParam(value = "DomesticStandingOrderId", required = true)
            @PathVariable("DomesticStandingOrderId") String domesticStandingOrderId,

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
    ) throws OBErrorResponseException {
        Optional<FRDomesticStandingOrderPaymentSubmission3> isPaymentSubmission = domesticStandingOrderPaymentSubmissionRepository.findById(domesticStandingOrderId);
        if (!isPaymentSubmission.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + domesticStandingOrderId + "' can't be found");
        }
        FRDomesticStandingOrderPaymentSubmission3 frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRDomesticStandingOrderConsent5> isPaymentSetup = domesticStandingOrderConsentRepository.findById(domesticStandingOrderId);
        if (!isPaymentSetup.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + domesticStandingOrderId + "' can't be found");
        }
        FRDomesticStandingOrderConsent5 frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(packagePayment(frPaymentSubmission, frPaymentSetup));
    }

    private OBWriteDomesticStandingOrderResponse1 packagePayment(FRDomesticStandingOrderPaymentSubmission3 frPaymentSubmission,
                                                                 FRDomesticStandingOrderConsent5 frDomesticStandingOrderConsent1) {
        return new OBWriteDomesticStandingOrderResponse1().data(new OBWriteDataDomesticStandingOrderResponse1()
                .domesticStandingOrderId(frPaymentSubmission.getId())
                .initiation(toOBDomesticStandingOrder1(frPaymentSubmission.getDomesticStandingOrder().getData().getInitiation()))
                .creationDateTime(frDomesticStandingOrderConsent1.getCreated())
                .statusUpdateDateTime(frDomesticStandingOrderConsent1.getStatusUpdate())
                .status(frDomesticStandingOrderConsent1.getStatus().toOBExternalStatusCode1())
                .consentId(frDomesticStandingOrderConsent1.getId()))
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_3_0().getGetDomesticStandingOrder()))
                .meta(new Meta());
    }

    // TODO #272 - move to uk-datamodel
    public static OBWriteDomesticStandingOrder3 toOBWriteDomesticStandingOrder3(OBWriteDomesticStandingOrder1 obWriteDomesticStandingOrder1) {
        return (new OBWriteDomesticStandingOrder3())
                .data(toOBWriteDataDomesticStandingOrder3(obWriteDomesticStandingOrder1.getData()))
                .risk(obWriteDomesticStandingOrder1.getRisk());
    }

    public static OBWriteDataDomesticStandingOrder3 toOBWriteDataDomesticStandingOrder3(OBWriteDataDomesticStandingOrder1 data) {
        return data == null ? null : (new OBWriteDataDomesticStandingOrder3())
                .consentId(data.getConsentId())
                .initiation(toOBDomesticStandingOrder3(data.getInitiation()));
    }

}
