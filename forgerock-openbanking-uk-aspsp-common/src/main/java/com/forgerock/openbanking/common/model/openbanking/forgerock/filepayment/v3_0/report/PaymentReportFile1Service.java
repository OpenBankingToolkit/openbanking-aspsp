/**
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
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.report;


import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRFileConsent1;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Generate a file payment report from an authorised payment consent.
 */
@Service
@Slf4j
public class PaymentReportFile1Service {
    /**
     * Consent must be in one of these statuses before we allow generation of a report file.
     */
    private static final Set<ConsentStatusCode> VALID_CONSENT_STATUSES = ImmutableSet.of(ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS, ConsentStatusCode.ACCEPTEDSETTLEMENTCOMPLETED);

    private final OBIEPaymentInitiationReport1Builder obiePaymentInitiationReportBuilder;
    private final OBIEPainXmlReport1Builder obiePainXmlReportBuilder;

    @Autowired
    public PaymentReportFile1Service(OBIEPaymentInitiationReport1Builder obiePaymentInitiationReportBuilder, OBIEPainXmlReport1Builder obiePainXmlReportBuilder) {
        this.obiePaymentInitiationReportBuilder = obiePaymentInitiationReportBuilder;
        this.obiePainXmlReportBuilder = obiePainXmlReportBuilder;
    }

    public String createPaymentReport(final FRFileConsent1 consent) throws OBErrorResponseException {
        log.debug("Create file payment report for consent: {}", consent);
        Preconditions.checkNotNull(consent, "Consent cannot be null");
        Preconditions.checkArgument(!StringUtils.isEmpty(consent.getFileContent()), "File consent cannot have empty file content when generating report file"); // Should not reach this stage so illegal argument exception due to coding error

        if (!VALID_CONSENT_STATUSES.contains(consent.getStatus())) {
            log.debug("User request report file for payment: {} but status was {} so no report file is available. Status must be in: {}", consent.getId(), consent.getStatus(), VALID_CONSENT_STATUSES);
            throw new OBErrorResponseException(HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.FILE_PAYMENT_REPORT_NOT_READY.toOBError1(consent.getId(), consent.getStatus().toString(), VALID_CONSENT_STATUSES.toString()));
        }

        switch (consent.getFileType()) {
                case UK_OBIE_PAYMENT_INITIATION_V3_0:
                    return obiePaymentInitiationReportBuilder.toPaymentReport(consent);
                case UK_OBIE_PAIN_001:
                    return obiePainXmlReportBuilder.toPaymentReport(consent);
                default:
                    log.error("Consent submitted with file type {} should not have passed validation. No report file is supported for this type.", consent.getFileType());
                    throw new IllegalArgumentException("Unknown payment file type: "+consent.getFileType());
        }
    }
}
