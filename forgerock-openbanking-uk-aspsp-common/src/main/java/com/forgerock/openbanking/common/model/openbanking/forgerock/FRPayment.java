/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import uk.org.openbanking.datamodel.payment.OBInitiation1;
import uk.org.openbanking.datamodel.payment.OBRisk1;


/**
 * Representation of a payment.
 */

public interface FRPayment {

    OBInitiation1 getInitiation();

    OBRisk1 getRisk();
}
