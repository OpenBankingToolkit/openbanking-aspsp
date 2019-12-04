/**
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