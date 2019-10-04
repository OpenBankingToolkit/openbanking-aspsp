/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBExternalProductType1Code;
import uk.org.openbanking.datamodel.account.OBExternalProductType2Code;

@Service
public class OBExternalProductTypeConverter {

    public OBExternalProductType2Code toOBExternalProductType2Code(OBExternalProductType1Code code1) {
        switch (code1) {

            case BCA:
                return OBExternalProductType2Code.BUSINESSCURRENTACCOUNT;
            case PCA:
            default:
                return OBExternalProductType2Code.PERSONALCURRENTACCOUNT;
        }
    }
}
