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
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FileParseException;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.OBDomestic1;
import uk.org.openbanking.datamodel.payment.OBRemittanceInformation1;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticResponse1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticResponse1;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OBIEPaymentInitiationReport1Builder {

    private final ObjectMapper objectMapper;

    @Autowired
    public OBIEPaymentInitiationReport1Builder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    String toPaymentReport(final FRFileConsent5 consent) {
        log.debug("Create {} report file for consent id: {}", consent.getFileType(), consent.getId());
        final List<OBWriteDomesticResponse1> payments =
                consent.getPayments().stream()
                .map(payment -> mapDomesticPayment(consent, payment))
                .collect(Collectors.toList());
        log.debug("Mapped {} domestic payments into OBWriteDomesticResponse1 objects", payments.size());
        final JsonReportFile jsonReportFile = JsonReportFile.builder()
                .data(JsonReportFile.Data.builder()
                        .domesticPayments(payments)
                        .build())
                .build();
        log.debug("Created Json Report File: {}", jsonReportFile);
        try {
            log.debug("Serializing Json Report File");
            return objectMapper.writeValueAsString(jsonReportFile);
        } catch (JsonProcessingException e) {
            log.error("Trying to parse consent: {} into report file failed", consent, e);
            throw new FileParseException("Unable to parse payments into report file.", e);
        }
    }

    private static OBWriteDomesticResponse1 mapDomesticPayment(FRFileConsent5 consent, FRFilePayment filePayment) {
            OBWriteDomesticResponse1 response = new OBWriteDomesticResponse1()
            .data(new OBWriteDataDomesticResponse1()
                    .initiation(
                            // Not all information about payment - just enough to identify it and match status to what TPP submitted
                            // If all payment info required we could reparse file content on consent but should not be necessary for a status report and this is simpler and faster.
                            new OBDomestic1()
                            .instructionIdentification(filePayment.getInstructionIdentification())
                            .endToEndIdentification(filePayment.getEndToEndIdentification())
                            .instructedAmount(filePayment.getInstructedAmount())
                            .remittanceInformation(new OBRemittanceInformation1()
                                    .reference(filePayment.getRemittanceReference())
                                    .unstructured(filePayment.getRemittanceUnstructured()))
                            .creditorAccount(new OBCashAccount3().identification(filePayment.getCreditorAccountIdentification()))

                    )
                    .consentId(consent.getId())
                    .creationDateTime(consent.getCreated())
                    .domesticPaymentId(consent.getId())
                    .statusUpdateDateTime(consent.getStatusUpdate())
                    .status(toOBStatus(filePayment.getStatus()))
            );
            log.debug("Mapped file payment: {} into OB response object: {}", filePayment, response);
            return response;
    }

    private static OBTransactionIndividualStatus1Code toOBStatus(FRFilePayment.PaymentStatus paymentStatus) {
        switch (paymentStatus) {
            case PENDING:
                return OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTINPROCESS;
            case COMPLETED:
                return OBTransactionIndividualStatus1Code.ACCEPTEDSETTLEMENTCOMPLETED;
            default:
                return OBTransactionIndividualStatus1Code.REJECTED;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JsonReportFile {
        @JsonProperty("Data")
        private Data data;
        @lombok.Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Data {
            @JsonProperty("DomesticPayments")
            private List<OBWriteDomesticResponse1> domesticPayments;
        }
    }

}
