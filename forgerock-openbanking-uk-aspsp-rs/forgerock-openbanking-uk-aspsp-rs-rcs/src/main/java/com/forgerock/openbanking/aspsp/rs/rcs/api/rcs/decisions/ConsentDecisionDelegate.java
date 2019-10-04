/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.exceptions.OBErrorException;

import java.io.IOException;
import java.util.List;

public interface ConsentDecisionDelegate {
    String getTppIdBehindConsent();

    String getUserIDBehindConsent();

    void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException;

    void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException;
}
