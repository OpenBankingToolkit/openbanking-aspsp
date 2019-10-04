/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import uk.org.openbanking.datamodel.account.OBBCAData1;
import uk.org.openbanking.datamodel.account.OBPCAData1;
import uk.org.openbanking.datamodel.account.OBProduct2;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CaseSensitiveAccountJsonPropertiesTest {
    private static ObjectMapper om = new ObjectMapper();

    // For issue https://github.com/ForgeCloud/ob-reference-implementation/issues/617
    @Test
    public void doTest() throws Exception {
        // Given
        OBProduct2 obProduct2 = new OBProduct2()
                .pca(new OBPCAData1())
                .bca(new OBBCAData1());

        // When
        String json = om.writeValueAsString(obProduct2);
        if (log.isDebugEnabled()) {
            log.debug(om.writerWithDefaultPrettyPrinter().writeValueAsString(obProduct2));
        }

        // Then
        assertThat(json).isEqualTo("{\"ProductName\":null,\"ProductId\":null,\"AccountId\":null,\"SecondaryProductId\":null,\"ProductType\":null,\"MarketingStateId\":null,\"OtherProductType\":null,\"BCA\":{\"ProductDetails\":null,\"CreditInterest\":null,\"Overdraft\":null,\"OtherFeesCharges\":null},\"PCA\":{\"ProductDetails\":null,\"CreditInterest\":null,\"Overdraft\":null,\"OtherFeesCharges\":null}}");
    }
}