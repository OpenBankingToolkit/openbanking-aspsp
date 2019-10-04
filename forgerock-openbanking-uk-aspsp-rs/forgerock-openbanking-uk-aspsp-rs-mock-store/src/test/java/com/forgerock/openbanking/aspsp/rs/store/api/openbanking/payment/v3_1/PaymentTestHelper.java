/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1;

import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.model.Tpp;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class PaymentTestHelper {

    public static final String MOCK_CLIENT_ID = "pispId123";
    public static final String MOCK_PISP_NAME = "testPisp";
    public static final String MOCK_PISP_ID = "55555";

    public static void setupMockTpp(TppRepository tppRepository) {
        Tpp tpp = new Tpp();
        tpp.officialName = MOCK_PISP_NAME;
        tpp.id = MOCK_PISP_ID;
        when(tppRepository.findByClientId(eq(MOCK_CLIENT_ID))).thenReturn(tpp);
    }
}
