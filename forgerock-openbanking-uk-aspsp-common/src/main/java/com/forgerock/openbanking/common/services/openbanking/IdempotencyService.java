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
package com.forgerock.openbanking.common.services.openbanking;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.PaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.PaymentSubmission;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;

/**
 * Performs validation of idempotent requests.
 */
@Service
@Slf4j
public class IdempotencyService {
    private static final int X_IDEMPOTENCY_MAX_KEY_LENGTH = 40;
    private static final int X_IDEMPOTENCY_KEY_EXPIRY_HOURS = 24;

    public static boolean isIdempotencyKeyHeaderValid(String xIdempotencyKey) {
        return !StringUtils.isEmpty(xIdempotencyKey)
                && xIdempotencyKey.length() <= X_IDEMPOTENCY_MAX_KEY_LENGTH;
    }

    /**
     * For payment consents.
     *
     * Consent body must be the same on new and existing requests. Idempotency key must be less than expiry time. (X_IDEMPOTENCY_KEY_EXPIRY_HOURS)
     */
    public static <T> void validateIdempotencyRequest(String xIdempotencyKey, T submittedRequestBody, PaymentConsent existingConsent, Supplier<T> existingConsentRequestBody) throws OBErrorResponseException {
        log.debug("Found an existing consent '{}' with the same x-idempotency-key '{}'.", existingConsent.getId(), xIdempotencyKey);
        checkIdempotencyKeyExpiry(xIdempotencyKey, existingConsent.getId(), existingConsent.getCreated());
        checkIdempotencyRequestBodyUnchanged(xIdempotencyKey, submittedRequestBody, existingConsentRequestBody.get(), existingConsent.getId());
    }

    /**
     * For payment submissions.
     *
     * Idempotency key must be the same for existing and new requests. Idempotency key must be less than expiry time. (X_IDEMPOTENCY_KEY_EXPIRY_HOURS)
     */
    public static <T> void validateIdempotencyRequest(PaymentSubmission submittedPayment, PaymentSubmission existingPayment)
            throws OBErrorResponseException {
        checkMatchingIdempotencyKey(submittedPayment.getIdempotencyKey(), existingPayment);
        checkIdempotencyKeyExpiry(submittedPayment.getIdempotencyKey(), existingPayment.getId(), new DateTime(existingPayment.getCreated()));
        // We don't need to check if body changed since previous request as that is not possible because submission data/risk cannot be changed from the consent anyway.
    }

    /**
     * For file upload.
     *
     * Consent body must be the same on new and existing requests. Idempotency key must be less than expiry time. (X_IDEMPOTENCY_KEY_EXPIRY_HOURS)
     */
    public static <T> void validateIdempotencyRequest(String xIdempotencyKey, FRFileConsent existingConsent) throws OBErrorResponseException {
        log.debug("Found an existing consent '{}' with the same x-idempotency-key '{}'.", existingConsent.getId(), xIdempotencyKey);
        checkMatchingIdempotencyKey(xIdempotencyKey, existingConsent);
        checkIdempotencyKeyExpiry(xIdempotencyKey, existingConsent.getId(), existingConsent.getCreated());
        // We don't need to check if file content body changed since previous request with same consent as that would fail the file hash check in RS-API already.
    }

    private static void checkMatchingIdempotencyKey(String xIdempotencyKey, PaymentSubmission existingPayment) throws OBErrorResponseException {
        if (!xIdempotencyKey.equals(existingPayment.getIdempotencyKey())) {
            log.warn("An existing payment submission with the same id but a different idempotency key was found. Cannot create this payment." +
                    "Payment id: {}, idempotency key of request: {}, idempotency key of existing payment: {}", existingPayment.getId(), xIdempotencyKey, existingPayment.getIdempotencyKey());
            throw new OBErrorResponseException(
                    HttpStatus.FORBIDDEN,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_SUBMISSION_ALREADY_EXISTS
                            .toOBError1(existingPayment.getId())
            );
        }
        log.info("Existing payment '{}' has the same x-idempotency-key '{}'.", existingPayment.getId(), xIdempotencyKey);
    }

    private static void checkMatchingIdempotencyKey(String xIdempotencyKey, FRFileConsent existingFileConsent) throws OBErrorResponseException {
        checkMatchingIdempotencyKey(xIdempotencyKey, existingFileConsent.getIdempotencyKey(), existingFileConsent.getId(), existingFileConsent.getStatus());
    }

    private static void checkMatchingIdempotencyKey(String xIdempotencyKey, String fileConsentIdempotencyKey, String fileConsentId, ConsentStatusCode consentStatusCode)
            throws OBErrorResponseException {
        if (!xIdempotencyKey.equals(fileConsentIdempotencyKey)) {
            log.warn("An existing file consent with the same id as the upload but a different idempotency key was found. Cannot upload this file." +
                    "Consent id: {}, idempotency header of file upload: {}, idempotency key of existing consent: {}", fileConsentId, xIdempotencyKey, fileConsentIdempotencyKey);
            throw new OBErrorResponseException(
                    HttpStatus.FORBIDDEN,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.PAYMENT_ALREADY_SUBMITTED.toOBError1(consentStatusCode.toOBExternalConsentStatus2Code())
            );
        }
        log.info("Existing payment '{}' has the same x-idempotency-key '{}'.", fileConsentId, xIdempotencyKey);
    }

    // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/937656404/Read+Write+Data+API+Specification+-+v3.1#Read/WriteDataAPISpecification-v3.1-Idempotency.1
    private static void checkIdempotencyKeyExpiry(String xIdempotencyKey, String paymentId, DateTime paymentCreated) throws OBErrorResponseException {
        if ((DateTime.now().minusHours(X_IDEMPOTENCY_KEY_EXPIRY_HOURS).isAfter(paymentCreated))) {
            log.debug("Matching idempotency key '{}' provided but previous use was more than '{}' hours ago so it has expired so rejecting request. Previous use was on id: '{}'",
                    xIdempotencyKey, X_IDEMPOTENCY_KEY_EXPIRY_HOURS, paymentId);
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.IDEMPOTENCY_KEY_EXPIRED.toOBError1(xIdempotencyKey, paymentId, paymentCreated.toString(BOOKED_TIME_DATE_FORMAT), X_IDEMPOTENCY_KEY_EXPIRY_HOURS)
            );
        }
    }

    // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/937656404/Read+Write+Data+API+Specification+-+v3.1#Read/WriteDataAPISpecification-v3.1-Idempotency.1
    private static <T> void checkIdempotencyRequestBodyUnchanged(String xIdempotencyKey, T submittedRequestBody, T existingRequestBody, String paymentId) throws OBErrorResponseException {
        if (!submittedRequestBody.equals(existingRequestBody)) {
            log.debug("Matching idempotency key provided but request body was not equal to previous request so rejecting. xIdempotency key: {}, request body: {}, existing id: {}, existing request body: {}",
                    xIdempotencyKey, submittedRequestBody, paymentId, existingRequestBody);
            throw new OBErrorResponseException(
                    HttpStatus.UNAUTHORIZED,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.IDEMPOTENCY_KEY_REQUEST_BODY_CHANGED.toOBError1(xIdempotencyKey, paymentId, existingRequestBody, submittedRequestBody)
            );
        }
    }
}
