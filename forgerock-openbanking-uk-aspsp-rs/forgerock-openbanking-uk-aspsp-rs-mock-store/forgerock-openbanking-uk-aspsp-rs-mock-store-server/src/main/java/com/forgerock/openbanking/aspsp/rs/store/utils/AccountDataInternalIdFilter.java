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

import java.util.function.Consumer;

/**
 * Responsible for determining if an object's internal ID (e.g. a Direct Debit's internal ID) should be displayed.
 * Depending on the application's Spring configuration, this class either sets the ID of each object to null (so it is
 * not displayed), or leaves it populated.
 *
 * <p>
 * By default, the config property <code>rs.data.internal_ids.show</code> is set to <code>true</code>, meaning all IDs
 * remain populated. If some of the internal IDs should be hidden, then <code>rs.data.internal_ids.show</code> should
 * be set to <code>false</code> and the preference for each relevant object should be specified (e.g.
 * <code>rs.data.internal_ids.direct_debit.show</code> for a Direct Debit).
 */
@Service
@Slf4j
public class AccountDataInternalIdFilter {

    private final boolean showAllAccountDataInternalIds;
    private final boolean showDirectDebitIds;
    private final boolean showOfferIds;
    private final boolean showProductIds;
    private final boolean showStatementIds;
    private final boolean showDomesticTransactionIds;
    private final boolean showInternationalTransactionIds;
    private final boolean showDomesticBeneficiaryIds;
    private final boolean showInternationalBeneficiaryIds;
    private final boolean showDomesticScheduledPaymentIds;
    private final boolean showInternationalScheduledPaymentIds;
    private final boolean showDomesticStandingOrderIds;
    private final boolean showInternationalStandingOrderIds;

    public AccountDataInternalIdFilter(
            @Value("${rs.data.internal_ids.show:true}") boolean showAllAccountDataInternalIds,
            @Value("${rs.data.internal_ids.direct_debit.show:false}") boolean showDirectDebitIds,
            @Value("${rs.data.internal_ids.offer.show:false}") boolean showOfferIds,
            @Value("${rs.data.internal_ids.product.show:false}") boolean showProductIds,
            @Value("${rs.data.internal_ids.statement.show:false}") boolean showStatementIds,
            @Value("${rs.data.internal_ids.domestic.transaction.show:false}") boolean showDomesticTransactionIds,
            @Value("${rs.data.internal_ids.international.transaction.show:false}") boolean showInternationalTransactionIds,
            @Value("${rs.data.internal_ids.domestic.beneficiary.show:false}") boolean showDomesticBeneficiaryIds,
            @Value("${rs.data.internal_ids.international.beneficiary.show:false}") boolean showInternationalBeneficiaryIds,
            @Value("${rs.data.internal_ids.domestic.scheduled_payment.show:false}") boolean showDomesticScheduledPaymentIds,
            @Value("${rs.data.internal_ids.international.scheduled_payment.show:false}") boolean showInternationalScheduledPaymentIds,
            @Value("${rs.data.internal_ids.domestic.standing_order.show:false}") boolean showDomesticStandingOrderIds,
            @Value("${rs.data.internal_ids.international.standing_order.show:false}") boolean showInternationalStandingOrderIds) {
        this.showAllAccountDataInternalIds = showAllAccountDataInternalIds;
        this.showDirectDebitIds = showDirectDebitIds;
        this.showOfferIds = showOfferIds;
        this.showProductIds = showProductIds;
        this.showStatementIds = showStatementIds;
        this.showDomesticTransactionIds = showDomesticTransactionIds;
        this.showInternationalTransactionIds = showInternationalTransactionIds;
        this.showDomesticBeneficiaryIds = showDomesticBeneficiaryIds;
        this.showInternationalBeneficiaryIds = showInternationalBeneficiaryIds;
        this.showDomesticScheduledPaymentIds = showDomesticScheduledPaymentIds;
        this.showInternationalScheduledPaymentIds = showInternationalScheduledPaymentIds;
        this.showDomesticStandingOrderIds = showDomesticStandingOrderIds;
        this.showInternationalStandingOrderIds = showInternationalStandingOrderIds;
    }

    public OBDirectDebit1 apply(OBDirectDebit1 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showDirectDebitIds, data::setDirectDebitId);
    }

    public OBReadDirectDebit2DataDirectDebit apply(OBReadDirectDebit2DataDirectDebit data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showDirectDebitIds, data::setDirectDebitId);
    }

    public OBOffer1 apply(OBOffer1 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showOfferIds, data::setOfferId);
    }

    public OBReadProduct2DataProduct apply(OBReadProduct2DataProduct data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showProductIds, data::setProductId);
    }

    public OBStatement1 apply(OBStatement1 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showStatementIds, data::setStatementId);
    }

    public OBStatement2 apply(OBStatement2 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        return apply(data, showStatementIds, data::setStatementId);
    }

    public OBTransaction3 apply(final OBTransaction3 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticTransactionIds : showInternationalTransactionIds;
        return apply(data, showId, data::setTransactionId);
    }

    public OBTransaction4 apply(final OBTransaction4 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticTransactionIds : showInternationalTransactionIds;
        return apply(data, showId, data::setTransactionId);
    }

    public OBTransaction5 apply(final OBTransaction5 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticTransactionIds : showInternationalTransactionIds;
        return apply(data, showId, data::setTransactionId);
    }

    public OBTransaction6 apply(final OBTransaction6 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticTransactionIds : showInternationalTransactionIds;
        return apply(data, showId, data::setTransactionId);
    }

    public OBBeneficiary2 apply(final OBBeneficiary2 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticBeneficiaryIds : showInternationalBeneficiaryIds;
        return apply(data, showId, data::setBeneficiaryId);
    }

    public OBBeneficiary3 apply(final OBBeneficiary3 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticBeneficiaryIds : showInternationalBeneficiaryIds;
        return apply(data, showId, data::setBeneficiaryId);
    }

    public OBBeneficiary4 apply(final OBBeneficiary4 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticBeneficiaryIds : showInternationalBeneficiaryIds;
        return apply(data, showId, data::setBeneficiaryId);
    }

    public OBBeneficiary5 apply(final OBBeneficiary5 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticBeneficiaryIds : showInternationalBeneficiaryIds;
        return apply(data, showId, data::setBeneficiaryId);
    }

    public OBScheduledPayment1 apply(OBScheduledPayment1 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticScheduledPaymentIds : showInternationalScheduledPaymentIds;
        return apply(data, showId, data::setScheduledPaymentId);
    }

    public OBScheduledPayment2 apply(OBScheduledPayment2 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticScheduledPaymentIds : showInternationalScheduledPaymentIds;
        return apply(data, showId, data::setScheduledPaymentId);
    }

    public OBScheduledPayment3 apply(OBScheduledPayment3 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticScheduledPaymentIds : showInternationalScheduledPaymentIds;
        return apply(data, showId, data::setScheduledPaymentId);
    }

    public OBStandingOrder3 apply(OBStandingOrder3 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticStandingOrderIds : showInternationalStandingOrderIds;
        return apply(data, showId, data::setStandingOrderId);
    }

    public OBStandingOrder4 apply(OBStandingOrder4 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticStandingOrderIds : showInternationalStandingOrderIds;
        return apply(data, showId, data::setStandingOrderId);
    }

    public OBStandingOrder5 apply(OBStandingOrder5 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }
        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticStandingOrderIds : showInternationalStandingOrderIds;
        return apply(data, showId, data::setStandingOrderId);
    }

    public OBStandingOrder6 apply(OBStandingOrder6 data) {
        if (showAllAccountDataInternalIds) {
            return displayAllInternalIds(data);
        }

        String schemeName = data.getCreditorAccount() != null ? data.getCreditorAccount().getSchemeName() : "";
        boolean showId = isDomestic(schemeName) ? showDomesticStandingOrderIds : showInternationalStandingOrderIds;
        return apply(data, showId, data::setStandingOrderId);
    }

    private <T> T displayAllInternalIds(final T data) {
        log.debug("Show All Account Data Internal Ids is 'ON'. Data response will contain internal ids");
        return data;
    }

    private <T> T apply(final T data, boolean showIds, Consumer<String> setIdFunction) {
        String objectName = data.getClass().getSimpleName();
        if (showIds) {
            log.debug("Show Internal Ids is 'ON' for {}. Data response will contain internal ids", objectName);
            return data;
        }

        log.debug("Show Internal Ids is 'OFF' for {}. Data response will NOT contain internal ids.", objectName);
        setIdFunction.accept(null);
        log.debug("Removed id");
        return data;
    }

    private boolean isDomestic(String schemeName) {
        return !(OBExternalAccountIdentification3Code.IBAN.toString().equals(schemeName) ||
                OBExternalAccountIdentification4Code.IBAN.toString().equals(schemeName) ||
                OBExternalAccountIdentification4Code.BBAN.toString().equals(schemeName));
    }
}
