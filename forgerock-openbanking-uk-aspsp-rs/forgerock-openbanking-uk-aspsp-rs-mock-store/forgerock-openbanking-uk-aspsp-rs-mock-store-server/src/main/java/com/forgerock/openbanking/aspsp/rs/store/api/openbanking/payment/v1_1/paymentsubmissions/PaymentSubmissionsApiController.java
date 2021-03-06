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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v1_1.paymentsubmissions;

import com.forgerock.openbanking.aspsp.rs.store.repository.IdempotentRepositoryAdapter;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FRPaymentSetupRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FRPaymentSubmissionRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSubmission;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
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
import uk.org.openbanking.datamodel.payment.OBPaymentDataSubmissionResponse1;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmission1;
import uk.org.openbanking.datamodel.payment.paymentsubmission.OBPaymentSubmissionResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("PaymentSubmissionsApiV1.1")
@Slf4j
public class PaymentSubmissionsApiController implements PaymentSubmissionsApi {
    @Autowired
    private FRPaymentSubmissionRepository frPaymentSubmissionRepository;
    @Autowired
    private FRPaymentSetupRepository frPaymentSetupRepository;
    @Autowired
    private ResourceLinkService resourceLinkService;

    @Override
    public ResponseEntity createPaymentSubmission(
            @ApiParam(value = "Every request will be processed only once per x-idempotency-key. " +
                    "The Idempotency Key will be valid for 24 hours.", required = true)
            @RequestHeader(value = "x-idempotency-key", required = true) String xIdempotencyKey,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be " +
                    "issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The payment ID")
            @RequestHeader(value = "x-ob-payment-id", required = false) String paymentId,

            @ApiParam(value = "Setup a single immediate payment", required = true)
            @Valid
            @RequestBody OBPaymentSubmission1 paymentSubmission,

            HttpServletRequest httpServletRequest
    ) throws OBErrorResponseException {
        log.debug("Received payment submission: '{}'", paymentSubmission);

        Optional<FRPaymentSetup> isPaymentSetup = frPaymentSetupRepository.findById(paymentId);
        if (isPaymentSetup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + paymentId + "' can't be found");
        }

        //Trace the payment submission request
        FRPaymentSubmission frPaymentSubmission = new FRPaymentSubmission();
        frPaymentSubmission.setId(paymentId);
        frPaymentSubmission.setPaymentSubmission(paymentSubmission);
        frPaymentSubmission.setIdempotencyKey(xIdempotencyKey);
        frPaymentSubmission.setObVersion(VersionPathExtractor.getVersionFromPath(httpServletRequest));
        frPaymentSubmission = new IdempotentRepositoryAdapter<>(frPaymentSubmissionRepository)
                .idempotentSave(frPaymentSubmission);

        FRPaymentSetup frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.status(HttpStatus.CREATED).body(packageToPaymentSubmission(frPaymentSubmission, frPaymentSetup));
    }

    @Override
    public ResponseEntity getPaymentSubmission(
            @ApiParam(value = "Unique identification as assigned by the ASPSP to uniquely identify " +
                    "the payment submission resource.", required = true)
            @PathVariable("PaymentSubmissionId") String paymentSubmissionId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be " +
                    "issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId
    ) throws OBErrorResponseException {
        Optional<FRPaymentSubmission> isPaymentSubmission = frPaymentSubmissionRepository.findById(paymentSubmissionId);
        if (isPaymentSubmission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment submission '" + paymentSubmissionId + "' can't be found");
        }
        FRPaymentSubmission frPaymentSubmission = isPaymentSubmission.get();

        Optional<FRPaymentSetup> isPaymentSetup = frPaymentSetupRepository.findById(paymentSubmissionId);
        if (isPaymentSetup.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment setup behind payment submission '" + paymentSubmissionId + "' can't be found");
        }
        FRPaymentSetup frPaymentSetup = isPaymentSetup.get();
        return ResponseEntity.ok(packageToPaymentSubmission(frPaymentSubmission, frPaymentSetup));
    }

    private OBPaymentSubmissionResponse1 packageToPaymentSubmission(FRPaymentSubmission frPaymentSubmission, FRPaymentSetup frPaymentSetup) {
        //Create Payment submission response
        OBPaymentDataSubmissionResponse1 paymentDataSubmissionResponse = new OBPaymentDataSubmissionResponse1()
                .paymentSubmissionId(frPaymentSubmission.getId())
                .paymentId(frPaymentSubmission.getId())
                .status(frPaymentSetup.getStatus().toOBTransactionIndividualStatus1Code())
                .creationDateTime(new DateTime(frPaymentSubmission.getCreated()));
        return new OBPaymentSubmissionResponse1()
                .data(paymentDataSubmissionResponse)
                .links(resourceLinkService.toSelfLink(frPaymentSubmission, discovery -> discovery.getV_1_1().getGetPaymentSubmission()))
                .meta(new Meta());
    }
}
