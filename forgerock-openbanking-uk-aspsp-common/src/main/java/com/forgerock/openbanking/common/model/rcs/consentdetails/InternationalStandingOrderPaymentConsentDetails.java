/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.rcs.consentdetails;

import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;

import java.util.List;

/**
 * Models the consent data that is used for
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternationalStandingOrderPaymentConsentDetails extends ConsentDetails {

    private OBStandingOrder5 standingOrder;

    protected String decisionApiUri;

    protected List<FRAccountWithBalance> accounts;
    protected String username;
    protected String logo;
    protected String clientId;
    protected String merchantName;
    protected DateTime expiredDate;
    protected String currencyOfTransfer;
    protected String paymentReference;


    @Override
    public IntentType getIntentType() {
        return IntentType.PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT;
    }
}
