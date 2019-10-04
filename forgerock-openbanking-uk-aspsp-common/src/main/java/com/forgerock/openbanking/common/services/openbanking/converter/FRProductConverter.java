/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRProduct1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRProduct2;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBExternalProductType1Code;
import uk.org.openbanking.datamodel.account.OBProduct1;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;

@Service
public class FRProductConverter {

    public FRProduct1 toProduct1(FRProduct2 product2) {
        FRProduct1 product1 =  new FRProduct1();
        product1.setAccountId(product2.getAccountId());
        product1.setId(product2.getId());
        product1.setCreated(product2.getCreated());
        product1.setUpdated(product2.getUpdated());
        product1.setProduct(toProduct1(product2.getProduct()));
        return product1;
    }

    private OBProduct1 toProduct1(OBReadProduct2DataProduct product) {
        OBProduct1 product1 = new OBProduct1()
                .accountId(product.getAccountId())
                .productName(product.getProductName())
                .productIdentifier(product.getProductId())
                .secondaryProductIdentifier(product.getSecondaryProductId());

        switch(product.getProductType()) {
            case BUSINESSCURRENTACCOUNT:
                product1.setProductType(OBExternalProductType1Code.BCA);
                break;
            case PERSONALCURRENTACCOUNT:
                product1.setProductType(OBExternalProductType1Code.PCA);
        }
        return product1;
    }

}
