/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticStandingOrderConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticStandingOrderConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticStandingOrderConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent3;
import uk.org.openbanking.datamodel.service.converter.payment.OBDomesticStandingOrderConverter;

@Service
public class FRDomesticStandingOrderConsentConverter {

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent1 frDomesticStandingOrderConsent1) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent1.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent1.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent1.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent1.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent1.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(OBDomesticStandingOrderConverter.toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent1.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent1.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent1.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent1.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent1.getUpdated());
        return frDomesticScheduledConsent2;
    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent2 frDomesticStandingOrderConsent2) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent1 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent1.setStatus(frDomesticStandingOrderConsent2.getStatus());
        frDomesticScheduledConsent1.setId(frDomesticStandingOrderConsent2.getId());
        frDomesticScheduledConsent1.setUserId(frDomesticStandingOrderConsent2.getUserId());
        frDomesticScheduledConsent1.setAccountId(frDomesticStandingOrderConsent2.getAccountId());
        frDomesticScheduledConsent1.setCreated(frDomesticStandingOrderConsent2.getCreated());
        frDomesticScheduledConsent1.setDomesticStandingOrderConsent(OBDomesticStandingOrderConverter.toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent2.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent1.setPispId(frDomesticStandingOrderConsent2.getPispId());
        frDomesticScheduledConsent1.setPispName(frDomesticStandingOrderConsent2.getPispName());
        frDomesticScheduledConsent1.setStatusUpdate(frDomesticStandingOrderConsent2.getStatusUpdate());
        frDomesticScheduledConsent1.setUpdated(frDomesticStandingOrderConsent2.getUpdated());
        return frDomesticScheduledConsent1;
    }

    public FRDomesticStandingOrderConsent2 toFRDomesticConsent2(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent2 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent2();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent2(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }

    private OBWriteDomesticStandingOrderConsent2 toOBWriteDomesticStandingOrderConsent2(OBWriteDomesticStandingOrderConsent3 domesticStandingOrderConsent) {
        return (new OBWriteDomesticStandingOrderConsent2())
                .data(new OBWriteDataDomesticStandingOrderConsent2()
                        .authorisation(domesticStandingOrderConsent.getData().getAuthorisation())
                        .initiation(FRStandingOrderPaymentConverter.toOBDomesticStandingOrder2(domesticStandingOrderConsent.getData().getInitiation()))
                        .permission(domesticStandingOrderConsent.getData().getPermission()))
                .risk(domesticStandingOrderConsent.getRisk());

    }

    public FRDomesticStandingOrderConsent1 toFRDomesticConsent1(FRDomesticStandingOrderConsent3 frDomesticStandingOrderConsent3) {
        FRDomesticStandingOrderConsent1 frDomesticScheduledConsent2 = new FRDomesticStandingOrderConsent1();
        frDomesticScheduledConsent2.setStatus(frDomesticStandingOrderConsent3.getStatus());
        frDomesticScheduledConsent2.setId(frDomesticStandingOrderConsent3.getId());
        frDomesticScheduledConsent2.setUserId(frDomesticStandingOrderConsent3.getUserId());
        frDomesticScheduledConsent2.setAccountId(frDomesticStandingOrderConsent3.getAccountId());
        frDomesticScheduledConsent2.setCreated(frDomesticStandingOrderConsent3.getCreated());
        frDomesticScheduledConsent2.setDomesticStandingOrderConsent(toOBWriteDomesticStandingOrderConsent1(frDomesticStandingOrderConsent3.getDomesticStandingOrderConsent()));
        frDomesticScheduledConsent2.setPispId(frDomesticStandingOrderConsent3.getPispId());
        frDomesticScheduledConsent2.setPispName(frDomesticStandingOrderConsent3.getPispName());
        frDomesticScheduledConsent2.setStatusUpdate(frDomesticStandingOrderConsent3.getStatusUpdate());
        frDomesticScheduledConsent2.setUpdated(frDomesticStandingOrderConsent3.getUpdated());
        return frDomesticScheduledConsent2;
    }

    private OBWriteDomesticStandingOrderConsent1 toOBWriteDomesticStandingOrderConsent1(OBWriteDomesticStandingOrderConsent3 domesticStandingOrderConsent) {
        return OBDomesticStandingOrderConverter.toOBWriteDomesticStandingOrderConsent1(toOBWriteDomesticStandingOrderConsent2(domesticStandingOrderConsent));
    }
}
