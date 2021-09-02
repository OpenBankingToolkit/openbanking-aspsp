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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FileConsent;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.HashUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
public class FilePaymentsApiEndpointWrapper extends RSEndpointWrapper<FilePaymentsApiEndpointWrapper, FilePaymentsApiEndpointWrapper.FilePaymentRestEndpointContent> {
    private FileConsent consent;

    public FilePaymentsApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                          TppStoreService tppStoreService) {
        super(RSEndpointWrapperService, tppStoreService);
    }

    public FilePaymentsApiEndpointWrapper payment(FileConsent consent) {
        this.consent = consent;
        return this;
    }

    @Override
    protected ResponseEntity run(FilePaymentRestEndpointContent main) throws OBErrorException {
        return main.run(oAuth2ClientId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Collections.singletonList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();
    }

    public void verifyFileHash(final String fileContent) throws OBErrorException {
        if (StringUtils.isEmpty(fileContent.isEmpty())) {
            log.warn("File content of request is empty");
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_EMPTY);
        }
        String expectedFileHash = consent.getFileHash();
        String actualFileHash = HashUtils.computeSHA256FullHash(fileContent);

        log.debug("Consent metadata contains file hash of: '{}'. Actual file hash is: '{}'", expectedFileHash, actualFileHash);
        if (!expectedFileHash.equals(actualFileHash)) {
            log.warn("Expected file hash from consent metadata: '{}' does not match actual hash of payment file contents: '{}'", expectedFileHash, actualFileHash);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_INCORRECT_FILE_HASH, actualFileHash, expectedFileHash);
        }
        log.debug("File hash is correct for consent id: ", consent.getId());
    }

    public void verifyContentTypeHeader(String contentTypeHeader) throws OBErrorException {
        // Check the file content-type header is compatible with the consent type
        MediaType consentContentType = consent.getFileType().getContentType();
        log.debug("Consent indicates file content-type of: '{}'. Actual content-type header of submitted file: '{}'", consent.getFileType().getContentType(), contentTypeHeader);

        if (!consentContentType.isCompatibleWith(MediaType.parseMediaType(contentTypeHeader))) {
            log.warn("Content type header '{}' for payment file consent does not match the specified file type: '{}'. Expected content-type: {}",
                    consentContentType, consent.getFileType(), consentContentType);
            throw new OBErrorException(OBRIErrorType.REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE, consentContentType);
        }
        log.debug("File content type is correct for consent id: ", consent.getId());

    }

    public interface FilePaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
