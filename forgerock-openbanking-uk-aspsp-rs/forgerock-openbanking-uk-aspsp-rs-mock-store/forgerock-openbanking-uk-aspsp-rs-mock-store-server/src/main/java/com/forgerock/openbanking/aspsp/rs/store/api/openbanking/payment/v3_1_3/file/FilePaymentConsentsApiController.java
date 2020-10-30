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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_3.file;


import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.FileConsentRepository;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.VersionPathExtractor;
import com.forgerock.openbanking.aspsp.rs.store.validator.ControlSumValidator;
import com.forgerock.openbanking.aspsp.rs.store.validator.FileTransactionCountValidator;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFileConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFile;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFileFactory;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent3;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsentResponse3;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsentResponse3Data;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_3.ConsentStatusCodeToResponseDataStatusConverter.toOBWriteFileConsentResponse3DataStatus;
import static com.forgerock.openbanking.common.services.openbanking.IdempotencyService.validateIdempotencyRequest;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRDataAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toFRWriteFileConsent;
import static com.forgerock.openbanking.common.services.openbanking.converter.payment.FRWriteFileConsentConverter.toOBWriteFile2DataInitiation;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-05-22T14:20:48.770Z")

@Controller("FilePaymentConsentsApiV3.1.3")
@Slf4j
public class FilePaymentConsentsApiController implements FilePaymentConsentsApi {

    private final TppRepository tppRepository;
    private final FileConsentRepository fileConsentRepository;
    private final ResourceLinkService resourceLinkService;
    private final ConsentMetricService consentMetricService;

    public FilePaymentConsentsApiController(ConsentMetricService consentMetricService, TppRepository tppRepository, FileConsentRepository fileConsentRepository, ResourceLinkService resourceLinkService) {
        this.tppRepository = tppRepository;
        this.fileConsentRepository = fileConsentRepository;
        this.resourceLinkService = resourceLinkService;
        this.consentMetricService = consentMetricService;
    }

    public ResponseEntity<OBWriteFileConsentResponse3> createFilePaymentConsents(
            OBWriteFileConsent3 obWriteFileConsent3,
            String authorization,
            String xIdempotencyKey,
            String xJwsSignature,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            String clientId,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Received: '{}'", obWriteFileConsent3);
        FRWriteFileConsent frWriteFileConsent = toFRWriteFileConsent(obWriteFileConsent3);
        log.trace("Converted to: '{}'", frWriteFileConsent);

        final Tpp tpp = Optional.ofNullable(tppRepository.findByClientId(clientId))
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        "TPP not found for client id",
                        Collections.singletonList(OBRIErrorType.REQUEST_INVALID_HEADER.toOBError1("x-ob-client-id"))
                ));
        log.debug("Got TPP '{}' for client Id '{}'", tpp, clientId);
        Optional<FRFileConsent> consentByIdempotencyKey = fileConsentRepository.findByIdempotencyKeyAndPispId(xIdempotencyKey, tpp.getId());
        if (consentByIdempotencyKey.isPresent()) {
            validateIdempotencyRequest(xIdempotencyKey, frWriteFileConsent, consentByIdempotencyKey.get(), () -> consentByIdempotencyKey.get().getWriteFileConsent());
            log.info("Idempotent request is valid. Returning [201 CREATED] but take no further action.");
            return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(consentByIdempotencyKey.get()));
        }
        log.debug("No consent with matching idempotency key has been found. Creating new consent.");

        FRFileConsent fileConsent = FRFileConsent.builder().id(IntentType.PAYMENT_FILE_CONSENT.generateIntentId())
                .status(ConsentStatusCode.AWAITINGUPLOAD)
                .writeFileConsent(frWriteFileConsent)
                .pispId(tpp.getId())
                .pispName(tpp.getOfficialName())
                .statusUpdate(DateTime.now())
                .updated(new Date())
                .idempotencyKey(xIdempotencyKey)
                .obVersion(VersionPathExtractor.getVersionFromPath(request))
                .build();
        log.debug("Saving consent: '{}'", fileConsent);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(fileConsent.getId(), fileConsent.getStatus().name()));
        fileConsent = fileConsentRepository.save(fileConsent);
        log.info("Created consent id: '{}'", fileConsent.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(packageResponse(fileConsent));
    }

    public ResponseEntity<Void> createFilePaymentConsentsConsentIdFile(
            String fileParam,
            String consentId,
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
        log.trace("Received: '{}'", fileParam);

        FRFileConsent fileConsent = fileConsentRepository.findById(consentId)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                .toOBError1()
                ));

        // If file already exists it could be idempotent request
        if (!StringUtils.isEmpty(fileConsent.getFileContent())) {
            if (xIdempotencyKey.equals(fileConsent.getIdempotencyKey())) {
                validateIdempotencyRequest(xIdempotencyKey, fileConsent);
                log.info("File already exists for consent: '{}' and has matching idempotent key: '{}'. No action taken but returning 200/OK");
                return ResponseEntity.ok().build();
            } else {
                log.debug("This consent already has a file uploaded and the idempotency key does not match the previous upload so rejecting.");
                throw new OBErrorResponseException(
                        HttpStatus.FORBIDDEN,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_ALREADY_SUBMITTED
                                .toOBError1(fileConsent.getStatus().toOBExternalConsentStatus2Code())
                );
            }
        }

        // We parse the file and check metadata against the parsed file
        try {
            PaymentFile paymentFile = PaymentFileFactory.createPaymentFile(fileConsent.getFileType(), fileParam);
            log.info("Successfully parsed file of type: '{}' for consent: '{}'", fileConsent.getFileType(), fileConsent.getId());
            FileTransactionCountValidator.validate(fileConsent, paymentFile);
            ControlSumValidator.validate(fileConsent, paymentFile);

            fileConsent.setPayments(paymentFile.getPayments());
            fileConsent.setFileContent(fileParam);
            fileConsent.setUpdated(new Date());
            fileConsent.setStatus(ConsentStatusCode.AWAITINGAUTHORISATION);
            fileConsent.setStatusUpdate(DateTime.now());
            fileConsentRepository.save(fileConsent);
        } catch (OBErrorException e) {
            throw new OBErrorResponseException(e.getObriErrorType().getHttpStatus(), OBRIErrorResponseCategory.REQUEST_INVALID, e.getOBError());
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<OBWriteFileConsentResponse3> getFilePaymentConsentsConsentId(
            String consentId,
            String authorization,
            DateTime xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {

        return fileConsentRepository.findById(consentId)
                .map(consent -> ResponseEntity.ok(packageResponse(consent)))
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                .toOBError1(consentId))
                );
    }

    public ResponseEntity getFilePaymentConsentsConsentIdFile(
            String consentId,
            String authorization,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xFapiInteractionId,
            String xCustomerUserAgent,
            HttpServletRequest request,
            Principal principal
    ) throws OBErrorResponseException {

        return fileConsentRepository.findById(consentId)
                .map(FRFileConsent::getFileContent)
                .map(fileStr -> new ByteArrayResource(fileStr.getBytes()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.PAYMENT_ID_NOT_FOUND
                                .toOBError1())
                );
    }

    private OBWriteFileConsentResponse3 packageResponse(FRFileConsent fileConsent) {
        return new OBWriteFileConsentResponse3()
                .data(new OBWriteFileConsentResponse3Data()
                        .consentId(fileConsent.getId())
                        .status(toOBWriteFileConsentResponse3DataStatus(fileConsent.getStatus()))
                        .creationDateTime(fileConsent.getCreated())
                        .statusUpdateDateTime(fileConsent.getStatusUpdate())
                        .initiation(toOBWriteFile2DataInitiation(fileConsent.getInitiation()))
                        .authorisation(toOBWriteDomesticConsent3DataAuthorisation(fileConsent.getWriteFileConsent().getData().getAuthorisation()))
                )
                .links(resourceLinkService.toSelfLink(fileConsent, discovery -> getVersion(discovery).getGetFilePaymentConsent()))
                .meta(new Meta());
    }

    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_3();
    }

}
