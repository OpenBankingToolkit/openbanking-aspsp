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

import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter.toOBWriteFileConsent1;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter.toOBWriteFileConsent2;
import static uk.org.openbanking.datamodel.service.converter.payment.OBWriteFileConsentConverter.toOBWriteFileConsent3;

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
        frFileConsent1.setWriteFileConsent(toOBWriteFileConsent1(frFileConsent2.getWriteFileConsent()));
        frFileConsent1.setPispId(frFileConsent2.getPispId());
        frFileConsent1.setPispName(frFileConsent2.getPispName());
        frFileConsent1.setStatusUpdate(frFileConsent2.getStatusUpdate());
        frFileConsent1.setUpdated(frFileConsent2.getUpdated());
        frFileConsent2.setPayments(frFileConsent1.getPayments());

        return frFileConsent1;
    }

    public static FRFileConsent2 toFRFileConsent2(FRFileConsent5 frFileConsent5) {
        return FRFileConsent2.builder()
                .id(frFileConsent5.getId())
                .status(frFileConsent5.getStatus())
                .writeFileConsent(toOBWriteFileConsent2(frFileConsent5.getWriteFileConsent()))
                .accountId(frFileConsent5.getAccountId())
                .userId(frFileConsent5.getUserId())
                .pispId(frFileConsent5.getPispId())
                .pispName(frFileConsent5.getPispName())
                .idempotencyKey(frFileConsent5.getIdempotencyKey())
                .created(frFileConsent5.getCreated())
                .statusUpdate(frFileConsent5.getStatusUpdate())
                .updated(frFileConsent5.getUpdated())
                .payments(frFileConsent5.getPayments())
                .fileContent(frFileConsent5.getFileContent())
                .obVersion(frFileConsent5.getObVersion())
                .build();
    }

    public FRFileConsent1 toFRFileConsent1(FRFileConsent5 frFileConsent5) {
        return FRFileConsent1.builder()
                .id(frFileConsent5.getId())
                .status(frFileConsent5.getStatus())
                .writeFileConsent(toOBWriteFileConsent1(frFileConsent5.getWriteFileConsent()))
                .accountId(frFileConsent5.getAccountId())
                .userId(frFileConsent5.getUserId())
                .pispId(frFileConsent5.getPispId())
                .pispName(frFileConsent5.getPispName())
                .created(frFileConsent5.getCreated())
                .statusUpdate(frFileConsent5.getStatusUpdate())
                .updated(frFileConsent5.getUpdated())
                .payments(frFileConsent5.getPayments())
                .fileContent(frFileConsent5.getFileContent())
                .version(frFileConsent5.getObVersion())
                .build();
    }

    public static FRFileConsent5 toFRFileConsent5(FRFileConsent2 frFileConsent2) {
        return FRFileConsent5.builder()
                .id(frFileConsent2.getId())
                .status(frFileConsent2.getStatus())
                .writeFileConsent(toOBWriteFileConsent3(frFileConsent2.getWriteFileConsent()))
                .accountId(frFileConsent2.getAccountId())
                .userId(frFileConsent2.getUserId())
                .pispId(frFileConsent2.getPispId())
                .pispName(frFileConsent2.getPispName())
                .idempotencyKey(frFileConsent2.getIdempotencyKey())
                .created(frFileConsent2.getCreated())
                .statusUpdate(frFileConsent2.getStatusUpdate())
                .updated(frFileConsent2.getUpdated())
                .payments(frFileConsent2.getPayments())
                .fileContent(frFileConsent2.getFileContent())
                .obVersion(frFileConsent2.getObVersion())
                .build();
    }
}
