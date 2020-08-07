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

    public OBTransaction1 apply(final OBTransaction1 data) {
        return apply(data, data::setTransactionId);
    }

    public OBTransaction2 apply(final OBTransaction2 data) {
        return apply(data, data::setTransactionId);
    }

    public OBTransaction3 apply(final OBTransaction3 data) {
        return apply(data, data::setTransactionId);
    }

    public OBTransaction4 apply(final OBTransaction4 data) {
        return apply(data, data::setTransactionId);
    }

    public OBTransaction5 apply(final OBTransaction5 data) {
        return apply(data, data::setTransactionId);
    }

    public OBTransaction6 apply(final OBTransaction6 data) {
        return apply(data, data::setTransactionId);
    }

    public OBBeneficiary2 apply(final OBBeneficiary2 data) {
        return apply(data, data::setBeneficiaryId);
    }

    public OBBeneficiary3 apply(final OBBeneficiary3 data) {
        return apply(data, data::setBeneficiaryId);
    }

    public OBBeneficiary4 apply(final OBBeneficiary4 data) {
        return apply(data, data::setBeneficiaryId);
    }

    public OBBeneficiary5 apply(final OBBeneficiary5 data) {
        return apply(data, data::setBeneficiaryId);
    }

    public OBDirectDebit1 apply(OBDirectDebit1 data) {
        return apply(data, data::setDirectDebitId);
    }

    public OBReadDirectDebit2DataDirectDebit apply(OBReadDirectDebit2DataDirectDebit data) {
        return apply(data, data::setDirectDebitId);
    }

    public OBOffer1 apply(OBOffer1 data) {
        return apply(data, data::setOfferId);
    }

    public OBScheduledPayment1 apply(OBScheduledPayment1 data) {
        return apply(data, data::setScheduledPaymentId);
    }

    public OBScheduledPayment2 apply(OBScheduledPayment2 data) {
        return apply(data, data::setScheduledPaymentId);
    }

    public OBScheduledPayment3 apply(OBScheduledPayment3 data) {
        return apply(data, data::setScheduledPaymentId);
    }

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

    public OBStandingOrder5 apply(OBStandingOrder5 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStandingOrder6 apply(OBStandingOrder6 data) {
        return apply(data, data::setStandingOrderId);
    }

    public OBStatement1 apply(OBStatement1 data) {
        return apply(data, data::setStatementId);
    }

    public OBStatement2 apply(OBStatement2 data) {
        return apply(data, data::setStatementId);
    }

    private <T> T apply(final T data, Consumer<String> setIdFunction) {
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
