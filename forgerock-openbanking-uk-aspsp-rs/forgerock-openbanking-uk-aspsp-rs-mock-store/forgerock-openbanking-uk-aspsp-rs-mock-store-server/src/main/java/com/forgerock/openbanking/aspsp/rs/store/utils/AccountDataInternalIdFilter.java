/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.*;

import java.util.Objects;
import java.util.function.Consumer;

@Service
@Slf4j
public class AccountDataInternalIdFilter {
    private final boolean showAccountDataInternalIds;

    public AccountDataInternalIdFilter(@Value("${rs.data.internal_ids.show:true}") boolean showAccountDataInternalIds) {
        this.showAccountDataInternalIds = showAccountDataInternalIds;
    }

    public OBTransaction1 apply(final OBTransaction1 data) { return apply(data, data::setTransactionId); }

    public OBTransaction2 apply(final OBTransaction2 data) { return apply(data, data::setTransactionId); }

    public OBTransaction3 apply(final OBTransaction3 data) { return apply(data, data::setTransactionId); }

    public OBTransaction4 apply(final OBTransaction4 data) { return apply(data, data::setTransactionId); }

    public OBTransaction5 apply(final OBTransaction5 data) { return apply(data, data::setTransactionId); }

    public OBBeneficiary2 apply(final OBBeneficiary2 data) { return apply(data, data::setBeneficiaryId); }

    public OBBeneficiary3 apply(final OBBeneficiary3 data) { return apply(data, data::setBeneficiaryId); }

    public OBDirectDebit1 apply(OBDirectDebit1 data) {
        return apply(data, data::setDirectDebitId);
    }

    public OBOffer1 apply(OBOffer1 data) {
        return apply(data, data::setOfferId);
    }

    public OBScheduledPayment1 apply(OBScheduledPayment1 data) { return apply(data, data::setScheduledPaymentId); }

    public OBScheduledPayment2 apply(OBScheduledPayment2 data) { return apply(data, data::setScheduledPaymentId); }

    public OBStandingOrder1 apply(OBStandingOrder1 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStandingOrder2 apply(OBStandingOrder2 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStandingOrder3 apply(OBStandingOrder3 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStandingOrder4 apply(OBStandingOrder4 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStandingOrder5 apply(OBStandingOrder5 data) { return apply(data, data::setStandingOrderId); }

    public OBStatement1 apply(OBStatement1 data) {
        return apply(data, data::setStatementId);
    }

    private  <T> T apply(final T data, Consumer<String> setIdFunction) {
        if (showAccountDataInternalIds) {
            log.debug("Show Account Data Internal Ids is 'ON'. Data response will contain internal ids");
            return data;
        }
        log.debug("Show Data API Internal Ids is 'OFF'. Data response will NOT contain internal ids. Data: {}", data);
        if (Objects.nonNull(data)) {
            setIdFunction.accept(null);
            log.debug("Removed id");
        }
        return data;
    }

}
