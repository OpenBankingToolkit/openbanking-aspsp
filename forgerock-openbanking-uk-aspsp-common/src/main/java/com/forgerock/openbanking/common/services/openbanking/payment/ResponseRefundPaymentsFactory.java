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

package com.forgerock.openbanking.common.services.openbanking.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import uk.org.openbanking.datamodel.payment.*;

import java.util.Optional;

public class ResponseRefundPaymentsFactory {

    // flag set on the consent by the PISP if PSU requests a refund for the transaction.
    // ZD: 55834 - https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/14

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomestic2DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse4DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomesticScheduled2DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticScheduled2DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse4DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomesticStandingOrder3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse4DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomestic2DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse5DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomesticScheduled2DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticScheduled2DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse5DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteDomesticStandingOrder3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteDomesticStandingOrder3DataInitiation initiation) {
        if (initiation.getDebtorAccount() != null) {
            return getOBWriteDomesticResponse5DataRefund(
                    frReadRefundAccount,
                    initiation.getDebtorAccount().getSchemeName(),
                    initiation.getDebtorAccount().getIdentification(),
                    initiation.getDebtorAccount().getName(),
                    initiation.getDebtorAccount().getSecondaryIdentification());
        }
        return Optional.empty();
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternational3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse4DataRefund> getOBWriteInternationalResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternational3DataInitiation initiation) {
        return getObWriteInternationalResponse4DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternationalScheduled3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse4DataRefund> getOBWriteInternationalResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternationalScheduled3DataInitiation initiation) {
        return getObWriteInternationalResponse4DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternationalStandingOrder4DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse4DataRefund> getOBWriteInternationalResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternationalStandingOrder4DataInitiation initiation) {
        return getObWriteInternationalResponse4DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternational3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternational3DataInitiation initiation) {
        return getOBWriteInternationalResponse5DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternationalScheduled3DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternationalScheduled3DataInitiation initiation) {
        return getOBWriteInternationalResponse5DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /**
     * Get the appropriate refund model object response instance
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         {@link OBWriteInternationalStandingOrder4DataInitiation}
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount, OBWriteInternationalStandingOrder4DataInitiation initiation) {
        return getOBWriteInternationalResponse5DataRefund(
                frReadRefundAccount,
                initiation.getDebtorAccount(),
                initiation.getCreditor(),
                initiation.getCreditorAgent()
        );
    }

    /*
    Private methods
     */
    private static boolean hasRefund(FRReadRefundAccount frReadRefundAccount) {
        return frReadRefundAccount != null && frReadRefundAccount.equals(FRReadRefundAccount.YES);
    }

    private static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (hasRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteDomesticResponse4DataRefund obWriteDomesticResponse4DataRefund = new OBWriteDomesticResponse4DataRefund()
                    .account(
                            new OBWriteDomesticResponse4DataRefundAccount()
                                    .schemeName(debtorAccountSchemeName)
                                    .identification(debtorAccountIdentification)
                                    .name(debtorAccountName)
                                    .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            return Optional.of(obWriteDomesticResponse4DataRefund);
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse4DataRefund> getOBWriteInternationalResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (hasRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteInternationalResponse4DataRefund obWriteInternationalResponse4DataRefund = new OBWriteInternationalResponse4DataRefund()
                    .account(new OBWriteDomesticResponse4DataRefundAccount()
                            .schemeName(debtorAccountSchemeName)
                            .identification(debtorAccountIdentification)
                            .name(debtorAccountName)
                            .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            return Optional.of(obWriteInternationalResponse4DataRefund);
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (hasRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteInternationalResponse5DataRefund obWriteInternationalResponse5DataRefund = new OBWriteInternationalResponse5DataRefund()
                    .account(new OBWriteDomesticResponse5DataRefundAccount()
                            .schemeName(debtorAccountSchemeName)
                            .identification(debtorAccountIdentification)
                            .name(debtorAccountName)
                            .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            return Optional.of(obWriteInternationalResponse5DataRefund);
        }
        return Optional.empty();
    }

    private static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (hasRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteDomesticResponse5DataRefund obWriteDomesticResponse5DataRefund = new OBWriteDomesticResponse5DataRefund()
                    .account(
                            new OBWriteDomesticResponse5DataRefundAccount()
                                    .schemeName(debtorAccountSchemeName)
                                    .identification(debtorAccountIdentification)
                                    .name(debtorAccountName)
                                    .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            return Optional.of(obWriteDomesticResponse5DataRefund);
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse4DataRefund> getObWriteInternationalResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {

        if (debtorAccount != null) {
            Optional<OBWriteInternationalResponse4DataRefund> obWriteInternationalResponse4DataRefund = getOBWriteInternationalResponse4DataRefund(frReadRefundAccount,
                    debtorAccount.getSchemeName(),
                    debtorAccount.getIdentification(),
                    debtorAccount.getName(),
                    debtorAccount.getSecondaryIdentification());
            if (creditor != null && obWriteInternationalResponse4DataRefund.isPresent()) {
                obWriteInternationalResponse4DataRefund.get()
                        .creditor(new OBWriteInternationalResponse4DataRefundCreditor()
                                .name(creditor.getName())
                                .postalAddress(creditor.getPostalAddress())
                        );
            }
            if (creditorAgent != null && obWriteInternationalResponse4DataRefund.isPresent()) {
                obWriteInternationalResponse4DataRefund.get()
                        .agent(
                                new OBWriteInternationalResponse4DataRefundAgent()
                                        .schemeName(creditorAgent.getSchemeName())
                                        .identification(creditorAgent.getIdentification())
                                        .name(creditorAgent.getName())
                                        .postalAddress(creditorAgent.getPostalAddress())
                        );
            }
            return obWriteInternationalResponse4DataRefund;
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse4DataRefund> getObWriteInternationalResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        if (debtorAccount != null) {
            Optional<OBWriteInternationalResponse4DataRefund> obWriteInternationalResponse4DataRefund = getOBWriteInternationalResponse4DataRefund(frReadRefundAccount,
                    debtorAccount.getSchemeName(),
                    debtorAccount.getIdentification(),
                    debtorAccount.getName(),
                    debtorAccount.getSecondaryIdentification());
            if (creditor != null && obWriteInternationalResponse4DataRefund.isPresent()) {
                obWriteInternationalResponse4DataRefund.get()
                        .creditor(new OBWriteInternationalResponse4DataRefundCreditor()
                                .name(creditor.getName())
                                .postalAddress(creditor.getPostalAddress())
                        );
            }
            if (creditorAgent != null && obWriteInternationalResponse4DataRefund.isPresent()) {
                obWriteInternationalResponse4DataRefund.get()
                        .agent(
                                new OBWriteInternationalResponse4DataRefundAgent()
                                        .schemeName(creditorAgent.getSchemeName())
                                        .identification(creditorAgent.getIdentification())
                                        .name(creditorAgent.getName())
                                        .postalAddress(creditorAgent.getPostalAddress())
                        );
            }
            return obWriteInternationalResponse4DataRefund;
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        if (debtorAccount != null) {
            Optional<OBWriteInternationalResponse5DataRefund> obWriteInternationalResponse5DataRefund = getOBWriteInternationalResponse5DataRefund(
                    frReadRefundAccount,
                    debtorAccount.getSchemeName(),
                    debtorAccount.getIdentification(),
                    debtorAccount.getName(),
                    debtorAccount.getSecondaryIdentification());
            if (creditor != null && obWriteInternationalResponse5DataRefund.isPresent()) {
                obWriteInternationalResponse5DataRefund.get()
                        .creditor(new OBWriteInternationalResponse5DataRefundCreditor()
                                .name(creditor.getName())
                                .postalAddress(creditor.getPostalAddress())
                        );
            }
            if (creditorAgent != null && obWriteInternationalResponse5DataRefund.isPresent()) {
                obWriteInternationalResponse5DataRefund.get()
                        .agent(
                                new OBWriteInternationalResponse5DataRefundAgent()
                                        .schemeName(creditorAgent.getSchemeName())
                                        .identification(creditorAgent.getIdentification())
                                        .name(creditorAgent.getName())
                                        .postalAddress(creditorAgent.getPostalAddress())
                        );
            }
            return obWriteInternationalResponse5DataRefund;
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        if (debtorAccount != null) {
            Optional<OBWriteInternationalResponse5DataRefund> obWriteInternationalResponse5DataRefund = getOBWriteInternationalResponse5DataRefund(frReadRefundAccount,
                    debtorAccount.getSchemeName(),
                    debtorAccount.getIdentification(),
                    debtorAccount.getName(),
                    debtorAccount.getSecondaryIdentification());
            if (creditor != null && obWriteInternationalResponse5DataRefund.isPresent()) {
                obWriteInternationalResponse5DataRefund.get()
                        .creditor(new OBWriteInternationalResponse5DataRefundCreditor()
                                .name(creditor.getName())
                                .postalAddress(creditor.getPostalAddress())
                        );
            }
            if (creditorAgent != null && obWriteInternationalResponse5DataRefund.isPresent()) {
                obWriteInternationalResponse5DataRefund.get()
                        .agent(
                                new OBWriteInternationalResponse5DataRefundAgent()
                                        .schemeName(creditorAgent.getSchemeName())
                                        .identification(creditorAgent.getIdentification())
                                        .name(creditorAgent.getName())
                                        .postalAddress(creditorAgent.getPostalAddress())
                        );
            }
            return obWriteInternationalResponse5DataRefund;
        }
        return Optional.empty();
    }
}
