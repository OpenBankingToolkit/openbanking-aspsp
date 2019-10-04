/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRInternationalConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalConsent2;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalConverter;

@Service
public class FRInternationalConsentConverter {

    public FRInternationalConsent2 toFRInternationalConsent2(FRInternationalConsent1 frInternationalConsent1) {
        FRInternationalConsent2 frInternationalConsent2 = new FRInternationalConsent2();

        frInternationalConsent2.setId(frInternationalConsent1.getId());
        frInternationalConsent2.setStatus(frInternationalConsent1.getStatus());
        frInternationalConsent2.setUserId(frInternationalConsent1.getUserId());
        frInternationalConsent2.setAccountId(frInternationalConsent1.getAccountId());
        frInternationalConsent2.setCreated(frInternationalConsent1.getCreated());
        frInternationalConsent2.setInternationalConsent(OBInternationalConverter.toOBWriteInternationalConsent2(frInternationalConsent1.getInternationalConsent()));
        frInternationalConsent2.setPispId(frInternationalConsent1.getPispId());
        frInternationalConsent2.setPispName(frInternationalConsent1.getPispName());
        frInternationalConsent2.setStatusUpdate(frInternationalConsent1.getStatusUpdate());

        return frInternationalConsent2;
    }

    public FRInternationalConsent1 toFRInternationalConsent1(FRInternationalConsent2 frInternationalConsent2) {
        FRInternationalConsent1 frInternationalConsent1 = new FRInternationalConsent1();

        frInternationalConsent1.setId(frInternationalConsent2.getId());
        frInternationalConsent1.setStatus(frInternationalConsent2.getStatus());
        frInternationalConsent1.setUserId(frInternationalConsent2.getUserId());
        frInternationalConsent1.setAccountId(frInternationalConsent2.getAccountId());
        frInternationalConsent1.setCreated(frInternationalConsent2.getCreated());
        frInternationalConsent1.setInternationalConsent(OBInternationalConverter.toOBWriteInternationalConsent1(frInternationalConsent2.getInternationalConsent()));
        frInternationalConsent1.setPispId(frInternationalConsent2.getPispId());
        frInternationalConsent1.setPispName(frInternationalConsent2.getPispName());
        frInternationalConsent1.setStatusUpdate(frInternationalConsent2.getStatusUpdate());

        return frInternationalConsent1;
    }
}
