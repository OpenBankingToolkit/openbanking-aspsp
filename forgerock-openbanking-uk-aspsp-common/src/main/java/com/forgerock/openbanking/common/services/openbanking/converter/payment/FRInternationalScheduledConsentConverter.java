/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRInternationalScheduledConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.service.converter.payment.OBInternationalScheduledConverter;

@Service
public class FRInternationalScheduledConsentConverter {

    public FRInternationalScheduledConsent2 toFRInternationalConsent2(FRInternationalScheduledConsent1 frInternationalScheduledConsent1) {
        FRInternationalScheduledConsent2 frInternationalScheduledConsent2 = new FRInternationalScheduledConsent2();
        frInternationalScheduledConsent2.setStatus(frInternationalScheduledConsent1.getStatus());
        frInternationalScheduledConsent2.setId(frInternationalScheduledConsent1.getId());
        frInternationalScheduledConsent2.setUserId(frInternationalScheduledConsent1.getUserId());
        frInternationalScheduledConsent2.setAccountId(frInternationalScheduledConsent1.getAccountId());
        frInternationalScheduledConsent2.setCreated(frInternationalScheduledConsent1.getCreated());
        frInternationalScheduledConsent2.setInternationalScheduledConsent(OBInternationalScheduledConverter.toOBWriteInternationalScheduledConsent2(frInternationalScheduledConsent1.getInternationalScheduledConsent()));
        frInternationalScheduledConsent2.setPispId(frInternationalScheduledConsent1.getPispId());
        frInternationalScheduledConsent2.setPispName(frInternationalScheduledConsent1.getPispName());
        frInternationalScheduledConsent2.setStatusUpdate(frInternationalScheduledConsent1.getStatusUpdate());
        frInternationalScheduledConsent2.setUpdated(frInternationalScheduledConsent1.getUpdated());
        return frInternationalScheduledConsent2;
    }

    public FRInternationalScheduledConsent1 toFRInternationalConsent1(FRInternationalScheduledConsent2 frInternationalScheduledConsent2) {
        FRInternationalScheduledConsent1 frInternationalScheduledConsent1 = new FRInternationalScheduledConsent1();
        frInternationalScheduledConsent1.setStatus(frInternationalScheduledConsent2.getStatus());
        frInternationalScheduledConsent1.setId(frInternationalScheduledConsent2.getId());
        frInternationalScheduledConsent1.setUserId(frInternationalScheduledConsent2.getUserId());
        frInternationalScheduledConsent1.setAccountId(frInternationalScheduledConsent2.getAccountId());
        frInternationalScheduledConsent1.setCreated(frInternationalScheduledConsent2.getCreated());
        frInternationalScheduledConsent1.setInternationalScheduledConsent(OBInternationalScheduledConverter.toOBWriteInternationalScheduledConsent1(frInternationalScheduledConsent2.getInternationalScheduledConsent()));
        frInternationalScheduledConsent1.setPispId(frInternationalScheduledConsent2.getPispId());
        frInternationalScheduledConsent1.setPispName(frInternationalScheduledConsent2.getPispName());
        frInternationalScheduledConsent1.setStatusUpdate(frInternationalScheduledConsent2.getStatusUpdate());
        frInternationalScheduledConsent1.setUpdated(frInternationalScheduledConsent2.getUpdated());
        return frInternationalScheduledConsent1;
    }
}
