/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter.payment;


import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticScheduledConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticScheduledConsent2;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.service.converter.payment.OBDomesticScheduledConverter;

@Service
public class FRDomesticScheduledConsentConverter {

    public FRDomesticScheduledConsent2 toFRDomesticConsent2(FRDomesticScheduledConsent1 frDomesticScheduledConsent1) {
        FRDomesticScheduledConsent2 frDomesticScheduledConsent2 = new FRDomesticScheduledConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticScheduledConsent1.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticScheduledConsent1.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticScheduledConsent1.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticScheduledConsent1.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticScheduledConsent1.getCreated());
        frDomesticScheduledConsent2.setDomesticScheduledConsent(OBDomesticScheduledConverter.toOBWriteDomesticScheduledConsent2(frDomesticScheduledConsent1.getDomesticScheduledConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticScheduledConsent1.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticScheduledConsent1.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticScheduledConsent1.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticScheduledConsent1.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticScheduledConsent1 toFRDomesticConsent1(FRDomesticScheduledConsent2 frDomesticScheduledConsent2) {
        FRDomesticScheduledConsent1 frDomesticScheduledConsent1 = new FRDomesticScheduledConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticScheduledConsent2.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticScheduledConsent2.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticScheduledConsent2.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticScheduledConsent2.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticScheduledConsent2.getCreated());
        frDomesticScheduledConsent1.setDomesticScheduledConsent(OBDomesticScheduledConverter.toOBWriteDomesticScheduledConsent1(frDomesticScheduledConsent2.getDomesticScheduledConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticScheduledConsent2.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticScheduledConsent2.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticScheduledConsent2.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticScheduledConsent2.getUpdated());
        return frDomesticScheduledConsent1;
    }
}
