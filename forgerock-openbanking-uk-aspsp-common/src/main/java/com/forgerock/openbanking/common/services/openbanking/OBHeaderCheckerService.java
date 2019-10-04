/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking;

import com.forgerock.openbanking.common.conf.RSConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OBHeaderCheckerService {
    private final RSConfiguration rsConfiguration;

    @Autowired
    public OBHeaderCheckerService(RSConfiguration rsConfiguration) {
        this.rsConfiguration = rsConfiguration;
    }

    public boolean verifyFinancialIdHeader(String xFapiFinancialId) {
        //Verify financial ID
        return rsConfiguration.financialId.equals(xFapiFinancialId);
    }
}
