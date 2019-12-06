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
package com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_1.report;

import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FileParseException;
import com.forgerock.openbanking.common.model.openbanking.obie.pain001.GroupHeader48;
import com.forgerock.openbanking.common.model.openbanking.obie.pain00200109.*;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OBIEPainXmlReport2Builder {
    private static final String REPORT_MESSAGE_TYPE = "pain.002.001.09";

    String toPaymentReport(FRFileConsent2 consent) {
        log.debug("Create {} report file for consent id: {}", consent.getFileType(), consent.getId());
        final Document reportDocument = mapConsentToReportDocument(consent);
        log.debug("Created JAXB object for report file: {}", reportDocument);
        return marshalXML(consent.getId(), reportDocument);
    }

    private String marshalXML(String consentId, Document reportDocument) {
        try {
            StringWriter writer = new StringWriter();
            JAXB.marshal(reportDocument, writer);
            log.debug("Serializing JAXB document into XML");
            return writer.toString();
        } catch (DataBindingException e) {
            log.error("Trying to marshal object: '{}' from consentId: {} into XML failed", reportDocument, consentId, e);
            throw new FileParseException("Unable to generate report file for consent: '"+consentId+"' with XML marshalling exception",e);
        }
    }

    private Document mapConsentToReportDocument(FRFileConsent2 consent) {
        com.forgerock.openbanking.common.model.openbanking.obie.pain001.Document paymentInitiationDocument;
        try {
          paymentInitiationDocument = JAXB.unmarshal(new StringReader(consent.getFileContent()), com.forgerock.openbanking.common.model.openbanking.obie.pain001.Document.class);
          log.debug("Unmarshalled file content from consent: {} into Document", consent.getId());
        } catch (Exception e) {
            log.warn("JAXB exception while attempting to parse existing file content on consent: {}, xml: {}", consent, consent.getFileContent(), e);
            throw new IllegalStateException("Invalid XML in file consent: "+consent.getId(), e); // IllegalState because this should have been parsed on creation so should never fail here
        }

        final List<PaymentTransaction92> paymentTxns =
                consent.getPayments().stream()
                        .map(this::toPaymentTx)
                        .collect(Collectors.toList());
        log.debug("Mapped {} consent file payments into Payment Transactions", paymentTxns.size());

        final CustomerPaymentStatusReportV09 cstmrPmtStsRpt = new CustomerPaymentStatusReportV09();
        cstmrPmtStsRpt.setGrpHdr(toReportGrpHeader(paymentInitiationDocument));
        cstmrPmtStsRpt.setOrgnlGrpInfAndSts(toOrgnlGrpInfAndSts(paymentInitiationDocument));
        cstmrPmtStsRpt.getOrgnlPmtInfAndSts().add(toPmtInst(paymentInitiationDocument, paymentTxns));

        Document document = new Document();
        document.setCstmrPmtStsRpt(cstmrPmtStsRpt);
        return document;
    }

    private static OriginalPaymentInstruction27 toPmtInst(com.forgerock.openbanking.common.model.openbanking.obie.pain001.Document paymentInitiationDocument, List<PaymentTransaction92> paymentTxns) {
        OriginalPaymentInstruction27 pmtInst = new OriginalPaymentInstruction27();
        pmtInst.setOrgnlPmtInfId(paymentInitiationDocument.getCstmrCdtTrfInitn().getPmtInf().get(0).getPmtInfId());
        pmtInst.getTxInfAndSts().addAll(paymentTxns);
        return pmtInst;
    }

    private static OriginalGroupHeader13 toOrgnlGrpInfAndSts(com.forgerock.openbanking.common.model.openbanking.obie.pain001.Document paymentInitiationDocument) {
        OriginalGroupHeader13 orgnlGrpInfAndSts  = new OriginalGroupHeader13();
        orgnlGrpInfAndSts.setOrgnlMsgId(paymentInitiationDocument.getCstmrCdtTrfInitn().getGrpHdr().getMsgId());
        orgnlGrpInfAndSts.setOrgnlMsgNmId(REPORT_MESSAGE_TYPE);
        log.debug("Mapped payment file group header {} into report file OrgnlGrpInfAndSts: {}", paymentInitiationDocument.getCstmrCdtTrfInitn().getGrpHdr(), orgnlGrpInfAndSts);
        return orgnlGrpInfAndSts;
    }

    private static GroupHeader74 toReportGrpHeader(com.forgerock.openbanking.common.model.openbanking.obie.pain001.Document paymentInitiationDocument) {
        GroupHeader48 consentGrpHdr = paymentInitiationDocument.getCstmrCdtTrfInitn().getGrpHdr();
        GroupHeader74 reportGrpHdr = new GroupHeader74();
        reportGrpHdr.setMsgId(UUID.randomUUID().toString());
        reportGrpHdr.setCreDtTm(consentGrpHdr.getCreDtTm());
        log.debug("Mapped payment file group header {} into report file group header: {}", consentGrpHdr, reportGrpHdr);
        return reportGrpHdr;
    }

    private PaymentTransaction92 toPaymentTx(FRFilePayment frPayment) {
        final PaymentTransaction92 txInf = new PaymentTransaction92();
        txInf.setOrgnlInstrId(frPayment.getInstructionIdentification());
        txInf.setOrgnlEndToEndId(frPayment.getEndToEndIdentification());
        txInf.setTxSts(toExternalPainStatus(frPayment.getStatus()));
        log.debug("Mapped file payment: {} into payment transaction: {}", frPayment, txInf);
        return txInf;
    }

    private String toExternalPainStatus(FRFilePayment.PaymentStatus status) {
        switch (status) {
            case PENDING:
                return PainExternalPaymentStatus.ACCEPTED_SETTLEMENT_IN_PROGRESS.getStatusString();
            case COMPLETED:
                return  PainExternalPaymentStatus.ACCEPTED_SETTLEMENT_COMPLETED.getStatusString();
            default:
                return PainExternalPaymentStatus.REJECTED.getStatusString();
        }
    }

    public enum PainExternalPaymentStatus {
        /** Accepted settlement in progress */
        ACCEPTED_SETTLEMENT_IN_PROGRESS("ACSP"),
        ACCEPTED_SETTLEMENT_COMPLETED("ASCS"),
        REJECTED("RJCT");

        private String statusString;
        PainExternalPaymentStatus(String statusString) {
            this.statusString = statusString;
        }

        public String getStatusString() {
            return statusString;
        }
    }
}
