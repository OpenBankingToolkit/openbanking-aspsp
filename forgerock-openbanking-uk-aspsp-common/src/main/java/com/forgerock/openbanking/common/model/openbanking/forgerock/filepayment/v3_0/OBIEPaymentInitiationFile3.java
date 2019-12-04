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
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.utils.JsonUtils;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBDomestic1;
import uk.org.openbanking.datamodel.payment.OBRemittanceInformation1;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is an internal model of the JSON-based "UK.OBIE.PaymentInitiation.3.0" file type
 */
@Slf4j
public class OBIEPaymentInitiationFile3 implements PaymentFile {
    private static final String DATA_NODE = "Data";
    private static final String DOMESTIC_PAYMENTS_NODE = "DomesticPayments";

    private final ObjectMapper objectMapper;
    private final List<FRFilePayment> payments;

    public OBIEPaymentInitiationFile3(final String fileContent) throws OBErrorException {
        log.debug("Parsing file content: {}", fileContent);
        objectMapper = new ObjectMapper();
        try {
            payments = parseJson(fileContent);
        } catch (FileParseException e) {
            log.warn("Unable to parse JSON content for '{}' file", PaymentFileType.UK_OBIE_PAYMENT_INITIATION_V3_0, e);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_JSON_INVALID, e.getMessage());
        }
    }

    @Override
    public int getNumberOfTransactions() {
        return payments.size();
    }

    @Override
    public BigDecimal getControlSum() {
        return payments.stream()
                .map(FRFilePayment::getInstructedAmount)
                .map(OBActiveOrHistoricCurrencyAndAmount::getAmount)
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public MediaType getContentType() {
        return PaymentFileType.UK_OBIE_PAYMENT_INITIATION_V3_0.getContentType();
    }

    @Override
    public List<FRFilePayment> getPayments() {
       return payments;
    }

    private List<FRFilePayment> parseJson(String fileContent)  {
        if (StringUtils.isEmpty(fileContent)) {
            throw new FileParseException("Unable to parse empty file content");
        }
        try {
            final JsonNode dataNode = objectMapper.readTree(fileContent)
                    .path(DATA_NODE);

            // TODO Do we need to support other payment type nodes? Need more information on UK.OBIE.PaymentInitiation.3.0 file format spec
            return JsonUtils.streamArray(dataNode.path(DOMESTIC_PAYMENTS_NODE))
                    .map(this::toOBDomestic)
                    .map(this::toFRFilePayment)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error parsing JSON file. File content: '{}'", fileContent, e);
            throw new FileParseException("Failed to read file content", e);
        }
    }

    private OBDomestic1 toOBDomestic(JsonNode paymentNode) {
        try {
            return objectMapper.treeToValue(paymentNode, OBDomestic1.class);
        } catch (JsonProcessingException e) { // Needs to be a unchecked exception
            throw new FileParseException("Unable to parse a node: " + paymentNode, e);
        }
    }

    private FRFilePayment toFRFilePayment(OBDomestic1 payment) {
        Optional<OBRemittanceInformation1> remittanceInformation = Optional.ofNullable(payment.getRemittanceInformation());
        return FRFilePayment.builder()
                .instructionIdentification(payment.getInstructionIdentification())
                .endToEndIdentification(payment.getEndToEndIdentification())
                .instructedAmount(payment.getInstructedAmount())
                .created(DateTime.now())
                .creditorAccountIdentification(payment.getCreditorAccount().getIdentification())
                .remittanceReference(remittanceInformation.map(OBRemittanceInformation1::getReference).orElse(""))
                .remittanceUnstructured(remittanceInformation.map(OBRemittanceInformation1::getUnstructured).orElse(""))
                .status(FRFilePayment.PaymentStatus.PENDING)
                .build();
    }
}
