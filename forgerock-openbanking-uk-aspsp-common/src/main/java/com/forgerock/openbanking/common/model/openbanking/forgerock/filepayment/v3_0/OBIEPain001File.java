/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0;

import com.forgerock.openbanking.common.model.openbanking.obie.pain001.*;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is an internal model of the XML-based "UK.OBIE.pain.001.001.08" file type
 */
@Slf4j
public class OBIEPain001File implements PaymentFile {
    private final List<FRFilePayment> payments;

    public OBIEPain001File(String fileContent) throws OBErrorException {
        log.debug("Parsing file content: {}", fileContent);
        try {
            payments = parseXml(fileContent);
            log.debug("Parsed {} payments from XML", payments.size());
        } catch (FileParseException e) {
            log.warn("Unable to parse XML content for '{}' file", PaymentFileType.UK_OBIE_PAIN_001, e);
            throw new OBErrorException(OBRIErrorType.REQUEST_FILE_XML_INVALID, e.getMessage());
        }
    }

    private List<FRFilePayment> parseXml(String fileContent) {
        try {
            log.debug("Attempt to unmarshal XML '{}'", fileContent);
            Document document = JAXB.unmarshal(new StringReader(fileContent), Document.class);
            log.debug("Unmarshalled to document");
            final List<PaymentInstruction22> pmtInf = document.getCstmrCdtTrfInitn().getPmtInf();
            return pmtInf.stream()
                    .flatMap(e -> e.getCdtTrfTxInf().stream())
                    .map(this::toFRFilePayment)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("JAXB exception while attempting to unmarshal: {}", fileContent, e);
            throw new FileParseException("Invalid XML", e);
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
        return PaymentFileType.UK_OBIE_PAIN_001.getContentType();
    }

    @Override
    public List<FRFilePayment> getPayments() {
        return payments;
    }

    private FRFilePayment toFRFilePayment(CreditTransferTransaction26 payment) {
        String remittanceReference = payment
                .getRmtInf()
                .getStrd()
                .stream()
                .findAny()
                .map(StructuredRemittanceInformation13::getCdtrRefInf)
                .map(CreditorReferenceInformation2::getRef)
                .orElse("");
        String remittanceUnstructured = payment.getRmtInf().getUstrd().stream().findFirst().orElse("");

        String creditorAccountId = payment.getCdtrAcct().getId().getIBAN();
        if (StringUtils.isEmpty(creditorAccountId)) {
            creditorAccountId = payment.getCdtrAcct().getId().getOthr().getId();
        }

        OBActiveOrHistoricCurrencyAndAmount amt = new OBActiveOrHistoricCurrencyAndAmount()
                .amount(payment.getAmt().getInstdAmt().getValue().toPlainString())
                .currency( payment.getAmt().getInstdAmt().getCcy());

        return FRFilePayment.builder()
                .instructionIdentification(payment.getPmtId().getInstrId())
                .endToEndIdentification(payment.getPmtId().getEndToEndId())
                .instructedAmount(amt)
                .created(DateTime.now())
                .creditorAccountIdentification(creditorAccountId)
                .remittanceReference(remittanceReference)
                .remittanceUnstructured(remittanceUnstructured)
                .status(FRFilePayment.PaymentStatus.PENDING)
                .build();
    }

}
