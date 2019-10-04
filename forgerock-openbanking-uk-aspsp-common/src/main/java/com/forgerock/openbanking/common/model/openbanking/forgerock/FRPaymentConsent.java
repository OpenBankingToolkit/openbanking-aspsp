/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import com.forgerock.openbanking.model.Tpp;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.payment.OBRisk1;


/**
 * Representation of a payment.
 */

public interface FRPaymentConsent {

    void setPisp(Tpp tpp);

    String getPispName();

    String getId();

    ConsentStatusCode getStatus();

    void setStatus(ConsentStatusCode status);

    Object getInitiation();

    OBRisk1 getRisk();

    String getAccountId();

    void setAccountId(String accountId);

    String getUserId();

    void setUserId(String userId);

    String getPispId();

    DateTime getCreated();

    default DateTime getStatusUpdate() {
        return null; // Implemented in V3.0 onwards
    }

    default boolean isNew() {
        return getCreated() == null;
    }
}
