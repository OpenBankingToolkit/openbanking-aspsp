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
package com.forgerock.openbanking.aspsp.rs.api.payment.v1_1.old;

import com.forgerock.openbanking.aspsp.rs.api.OBHeaders;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBPaymentDataSetupResponse1;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetupResponse1;

@Slf4j
@Service
public class PaymentSetup {

    private TppStoreService tppStoreService;
    private PaymentId paymentId;

    public PaymentSetup(TppStoreService tppStoreService, PaymentId paymentId) {
        this.tppStoreService = tppStoreService;
        this.paymentId = paymentId;
    }

    public OBPaymentSetupResponse1 create(OBHeaders headers, String tppId, OBPaymentSetup1 paymentSetupPOSTRequest) {
        String paymentID = paymentId.create();
        log.debug("FRPaymentConsent ID generated '{}'.", paymentID);

        FRPaymentSetup1 payment = registerPendingPayment(tppId, paymentSetupPOSTRequest, paymentID);

        FRPaymentSetup1 paymentSetupEntity = tracePaymentSetup(headers, paymentSetupPOSTRequest, paymentID);

        updatePaymentToAcceptedTechnicalValidation(payment);

        return updatePaymentSetup(paymentSetupPOSTRequest, payment, paymentSetupEntity);


    }

    private FRPaymentSetup1 registerPendingPayment(String tppId, OBPaymentSetup1 paymentSetupPOSTRequest, String paymentID) {
        FRPaymentSetup1 payment = new FRPaymentSetup1();
        payment.setStatus(ConsentStatusCode.PENDING);
        payment.setPisp(tppStoreService.findByClientId(tppId).get());
        //return paymentsService.createPayment(payment);
        return null;
    }

    private FRPaymentSetup1 tracePaymentSetup(OBHeaders headers, OBPaymentSetup1 paymentSetupPOSTRequest, String paymentID) {
        FRPaymentSetup1 paymentSetupEntity = new FRPaymentSetup1();
        paymentSetupEntity.setId(paymentID);
        paymentSetupEntity.setPaymentSetupRequest(paymentSetupPOSTRequest);
        //paymentSetupEntity = paymentSetupsService.createPaymentSetup(paymentSetupEntity);
        return paymentSetupEntity;
    }

    private void updatePaymentToAcceptedTechnicalValidation(FRPaymentConsent payment) {
        payment.setStatus(ConsentStatusCode.ACCEPTEDTECHNICALVALIDATION);
    }

    private OBPaymentSetupResponse1 updatePaymentSetup(OBPaymentSetup1 paymentSetupPOSTRequest,
                                                       FRPaymentConsent payment,
                                                       FRPaymentSetup1 paymentSetupEntity) {

        //Create Payment Setup response
        OBPaymentDataSetupResponse1 data = new OBPaymentDataSetupResponse1()
                .paymentId(payment.getId())
                .status(OBTransactionIndividualStatus1Code.ACCEPTEDTECHNICALVALIDATION)
                .creationDateTime(payment.getCreated())
                .initiation(paymentSetupPOSTRequest.getData().getInitiation());
        OBPaymentSetupResponse1 paymentSetupPOSTResponse = new OBPaymentSetupResponse1()
                .data(data)
                .risk(paymentSetupPOSTRequest.getRisk());

        //Trace the payment setup request
        //paymentSetupsService.updatePaymentSetup(paymentSetupEntity);
        return paymentSetupPOSTResponse;
    }
}
