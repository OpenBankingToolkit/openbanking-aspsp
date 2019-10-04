/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRFileConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.service.converter.payment.OBFileConverter;

@Service
public class FRFileConsentConverter {

    public FRFileConsent2 toFRFileConsent2(FRFileConsent1 frFileConsent1) {
        FRFileConsent2 frFileConsent2 = new FRFileConsent2();
        frFileConsent2.setStatus(frFileConsent1.getStatus());
        frFileConsent2.setId(frFileConsent1.getId());
        frFileConsent2.setUserId(frFileConsent1.getUserId());
        frFileConsent2.setAccountId(frFileConsent1.getAccountId());
        frFileConsent2.setCreated(frFileConsent1.getCreated());
        frFileConsent2.setWriteFileConsent(OBFileConverter.toOBWriteFileConsent2(frFileConsent1.getWriteFileConsent()));
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
        frFileConsent1.setWriteFileConsent(OBFileConverter.toOBWriteFileConsent1(frFileConsent2.getWriteFileConsent()));
        frFileConsent1.setPispId(frFileConsent2.getPispId());
        frFileConsent1.setPispName(frFileConsent2.getPispName());
        frFileConsent1.setStatusUpdate(frFileConsent2.getStatusUpdate());
        frFileConsent1.setUpdated(frFileConsent2.getUpdated());
        frFileConsent2.setPayments(frFileConsent1.getPayments());

        return frFileConsent1;
    }
}
