/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBExternalProductType1Code;
import uk.org.openbanking.datamodel.account.OBProduct1;
import uk.org.openbanking.datamodel.account.OBReadProduct2DataProduct;

@Service
public class FRProductConverter {

    // TODO #296 - add required methods once FRAccount is using FR domain classes
//    public FRProduct1 toProduct1(FRProduct product2) {
//        FRProduct1 product1 =  new FRProduct1();
//        product1.setAccountId(product2.getAccountId());
//        product1.setId(product2.getId());
//        product1.setCreated(product2.getCreated());
//        product1.setUpdated(product2.getUpdated());
//        product1.setProduct(toProduct1(product2.getProduct()));
//        return product1;
//    }

    public static OBProduct1 toOBProduct1(OBReadProduct2DataProduct product) {
        OBProduct1 product1 = new OBProduct1()
                .accountId(product.getAccountId())
                .productName(product.getProductName())
                .productIdentifier(product.getProductId())
                .secondaryProductIdentifier(product.getSecondaryProductId());

        switch (product.getProductType()) {
            case BUSINESSCURRENTACCOUNT:
                product1.setProductType(OBExternalProductType1Code.BCA);
                break;
            case PERSONALCURRENTACCOUNT:
                product1.setProductType(OBExternalProductType1Code.PCA);
        }
        return product1;
    }

}
