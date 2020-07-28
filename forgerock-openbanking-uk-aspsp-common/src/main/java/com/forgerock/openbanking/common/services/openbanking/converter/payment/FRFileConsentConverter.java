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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRFileConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRFileConsent5;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBFile1;
import uk.org.openbanking.datamodel.payment.OBWriteDataFileConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteFile2DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent3;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent3Data;
import uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter;

import static uk.org.openbanking.datamodel.service.converter.payment.OBAccountConverter.toOBCashAccount3;
import static uk.org.openbanking.datamodel.service.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBRemittanceInformationConverter.toOBRemittanceInformation1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter.toOBWriteFileConsent2;

@Service
public class FRFileConsentConverter {

    public FRFileConsent2 toFRFileConsent2(FRFileConsent1 frFileConsent1) {
        FRFileConsent2 frFileConsent2 = new FRFileConsent2();
        frFileConsent2.setStatus(frFileConsent1.getStatus());
        frFileConsent2.setId(frFileConsent1.getId());
        frFileConsent2.setUserId(frFileConsent1.getUserId());
        frFileConsent2.setAccountId(frFileConsent1.getAccountId());
        frFileConsent2.setCreated(frFileConsent1.getCreated());
        frFileConsent2.setWriteFileConsent(toOBWriteFileConsent2(frFileConsent1.getWriteFileConsent()));
        frFileConsent2.setPispId(frFileConsent1.getPispId());
        frFileConsent2.setPispName(frFileConsent1.getPispName());
        frFileConsent2.setStatusUpdate(frFileConsent1.getStatusUpdate());
        frFileConsent2.setUpdated(frFileConsent1.getUpdated());

        frFileConsent2.setPayments(frFileConsent1.getPayments());

        return frFileConsent2;
    }

    public FRFileConsent1 toFRFileConsent1(FRFileConsent2 frFileConsent2) {
        FRFileConsent1 frFileConsent1 = new FRFileConsent1();
        frFileConsent1.setStatus(frFileConsent2.getStatus());
        frFileConsent1.setId(frFileConsent2.getId());
        frFileConsent1.setUserId(frFileConsent2.getUserId());
        frFileConsent1.setAccountId(frFileConsent2.getAccountId());
        frFileConsent1.setCreated(frFileConsent2.getCreated());
        frFileConsent1.setWriteFileConsent(OBWriteFileConsentConverter.toOBWriteFileConsent1(frFileConsent2.getWriteFileConsent()));
        frFileConsent1.setPispId(frFileConsent2.getPispId());
        frFileConsent1.setPispName(frFileConsent2.getPispName());
        frFileConsent1.setStatusUpdate(frFileConsent2.getStatusUpdate());
        frFileConsent1.setUpdated(frFileConsent2.getUpdated());
        frFileConsent2.setPayments(frFileConsent1.getPayments());

        return frFileConsent1;
    }

    public static FRFileConsent2 toFRFileConsent2(FRFileConsent5 consent) {
        return FRFileConsent2.builder()
                .id(consent.getId())
                .status(consent.getStatus())
                .writeFileConsent(toOBWriteFileConsent2(consent.getWriteFileConsent()))
                .accountId(consent.getAccountId())
                .userId(consent.getUserId())
                .pispId(consent.getPispId())
                .pispName(consent.getPispName())
                .idempotencyKey(consent.getIdempotencyKey())
                .created(consent.getCreated())
                .statusUpdate(consent.getStatusUpdate())
                .updated(consent.getUpdated())
                .payments(consent.getPayments())
                .fileContent(consent.getFileContent())
                .obVersion(consent.getObVersion())
                .build();
    }

    public FRFileConsent1 toFRFileConsent1(FRFileConsent5 consent) {
        return FRFileConsent1.builder()
                .id(consent.getId())
                .status(consent.getStatus())
                .writeFileConsent(toOBWriteFileConsent1(consent.getWriteFileConsent()))
                .accountId(consent.getAccountId())
                .userId(consent.getUserId())
                .pispId(consent.getPispId())
                .pispName(consent.getPispName())
                .created(consent.getCreated())
                .statusUpdate(consent.getStatusUpdate())
                .updated(consent.getUpdated())
                .payments(consent.getPayments())
                .fileContent(consent.getFileContent())
                .version(consent.getObVersion())
                .build();
    }

    // TODO #272 - move to uk-datamodel
    public static OBWriteFileConsent1 toOBWriteFileConsent1(OBWriteFileConsent3 writeFileConsent) {
        return (new OBWriteFileConsent1())
                .data(toOBWriteDataFileConsent1(writeFileConsent.getData()));
    }

    public static OBWriteDataFileConsent1 toOBWriteDataFileConsent1(OBWriteFileConsent3Data data) {
        return data == null ? null : (new OBWriteDataFileConsent1())
                .initiation(toOBFile1(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    private static OBFile1 toOBFile1(OBWriteFile2DataInitiation initiation) {
        return initiation == null ? null : (new OBFile1())
                .fileType(initiation.getFileType())
                .fileHash(initiation.getFileHash())
                .fileReference(initiation.getFileReference())
                .numberOfTransactions(initiation.getNumberOfTransactions())
                .controlSum(initiation.getControlSum())
                .requestedExecutionDateTime(initiation.getRequestedExecutionDateTime())
                .localInstrument(initiation.getLocalInstrument())
                .debtorAccount(toOBCashAccount3(initiation.getDebtorAccount()))
                .remittanceInformation(toOBRemittanceInformation1(initiation.getRemittanceInformation()));
    }
}
