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
 * http://www.apache.org/licenses/LICENSE-2.0
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

public class PaymentUtil {

    // flag set on the consent by the PISP if PSU requests a refund for the transaction.
    // ZD: 55834 - https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/14

    /**
     * Get the instance of response data refund for domestic payments valuating the values read refund account and the object initiation
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         object, to valuated the instance of the specified domestic payment initiation type
     * @return optional refund data instance {@link OBWriteDomesticResponse4DataRefund}
     */
    public static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            Object initiation) {
        switch (InitiationClassTypes.fromClass(initiation.getClass())) {
            case OBWriteDomestic2Data_Initiation:
                return getOBWriteDomesticResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteDomestic2DataInitiation) initiation).getDebtorAccount());
            case OBWriteDomesticScheduled2Data_Initiation:
                return getOBWriteDomesticResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteDomesticScheduled2DataInitiation) initiation).getDebtorAccount());
            case OBWriteDomesticStandingOrder3Data_Initiation:
                return getOBWriteDomesticResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteDomesticStandingOrder3DataInitiation) initiation).getDebtorAccount());
            default:
                return Optional.empty();
        }
    }

    /**
     * Get the instance of response data refund for domestic payments valuating the values read refund account and the object initiation
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         object, to valuated the instance of the specified domestic payment initiation type
     * @return optional refund data instance {@link OBWriteDomesticResponse5DataRefund}
     */
    public static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            Object initiation) {
        switch (InitiationClassTypes.fromClass(initiation.getClass())) {
            case OBWriteDomestic2Data_Initiation:
                return getOBWriteDomesticResponse5DataRefund(frReadRefundAccount, ((OBWriteDomestic2DataInitiation)initiation).getDebtorAccount());
            case OBWriteDomesticScheduled2Data_Initiation:
                return getOBWriteDomesticResponse5DataRefund(
                        frReadRefundAccount,
                        ((OBWriteDomesticScheduled2DataInitiation) initiation).getDebtorAccount());
            case OBWriteDomesticStandingOrder3Data_Initiation:
                return getOBWriteDomesticResponse5DataRefund(
                        frReadRefundAccount,
                        ((OBWriteDomesticStandingOrder3DataInitiation) initiation).getDebtorAccount());
            default:
                return Optional.empty();
        }
    }

    /**
     * Get the instance of response data refund valuating the values read refund account and the object initiation
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         object, to valuated the instance of the specified international payment initiation type
     * @return optional refund data instance {@link OBWriteInternationalResponse4DataRefund}
     */
    public static Optional<OBWriteInternationalResponse4DataRefund> getOBWriteInternationalResponse4DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            Object initiation) {
        switch (InitiationClassTypes.fromClass(initiation.getClass())) {
            case OBWriteInternational3Data_Initiation:
                return getObWriteInternationalResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternational3DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternational3DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternational3DataInitiation) initiation).getCreditorAgent()
                );
            case OBWriteInternationalScheduled3Data_Initiation:
                return getObWriteInternationalResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getCreditorAgent()
                );
            case OBWriteInternationalStandingOrder4Data_Initiation:
                return getObWriteInternationalResponse4DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getCreditorAgent()
                );
            default:
                return Optional.empty();
        }
    }

    /**
     * Get the instance of response data refund for domestic payments valuating the values read refund account and the object initiation
     * @param frReadRefundAccount
     *         {@link FRReadRefundAccount}
     * @param initiation
     *         object, to valuated the instance of the specified domestic payment initiation type
     * @return optional refund data instance {@link OBWriteInternationalResponse5DataRefund}
     */
    public static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefundInstance(
            FRReadRefundAccount frReadRefundAccount,
            Object initiation) {
        switch (InitiationClassTypes.fromClass(initiation.getClass())) {
            case OBWriteInternational3Data_Initiation:
                return getOBWriteInternationalResponse5DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternational3DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternational3DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternational3DataInitiation) initiation).getCreditorAgent()
                );
            case OBWriteInternationalScheduled3Data_Initiation:
                return getOBWriteInternationalResponse5DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternationalScheduled3DataInitiation) initiation).getCreditorAgent()
                );
            case OBWriteInternationalStandingOrder4Data_Initiation:
                return getOBWriteInternationalResponse5DataRefund(
                        frReadRefundAccount,
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getDebtorAccount(),
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getCreditor(),
                        ((OBWriteInternationalStandingOrder4DataInitiation) initiation).getCreditorAgent()
                );
            default:
                return Optional.empty();
        }
    }

    /**
     * Private definitions
     */

    private enum InitiationClassTypes {
        // Domestic payments initiation types
        // v3.1.4, v3.1.5, v3.1.6
        OBWriteDomestic2Data_Initiation(OBWriteDomestic2DataInitiation.class),
        OBWriteDomesticScheduled2Data_Initiation(OBWriteDomesticScheduled2DataInitiation.class),
        OBWriteDomesticStandingOrder3Data_Initiation(OBWriteDomesticStandingOrder3DataInitiation.class),
        // international payments initiation types
        // v3.1.4, v3.1.5, v3.1.6
        OBWriteInternational3Data_Initiation(OBWriteInternational3DataInitiation.class),
        OBWriteInternationalScheduled3Data_Initiation(OBWriteInternationalScheduled3DataInitiation.class),
        OBWriteInternationalStandingOrder4Data_Initiation(OBWriteInternationalStandingOrder4DataInitiation.class),
        // unknown type
        UNKNOWN(null);

        private final Class<?> typeClass;

        InitiationClassTypes(Class<?> typeClass) {
            this.typeClass = typeClass;
        }

        public static InitiationClassTypes fromClass(Class<?> cls) {
            for (InitiationClassTypes c : values()) {
                if (c.typeClass == cls)
                    return c;
            }
            return UNKNOWN;
        }
    }

    private static boolean mustHaveRefund(FRReadRefundAccount frReadRefundAccount) {
        return frReadRefundAccount != null && frReadRefundAccount.equals(FRReadRefundAccount.YES);
    }

    private static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return getOBWriteDomesticResponse4DataRefund(
                frReadRefundAccount,
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification());
    }

    private static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount) {
        return getOBWriteDomesticResponse4DataRefund(
                frReadRefundAccount,
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification());
    }

    private static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return getOBWriteDomesticResponse5DataRefund(
                frReadRefundAccount,
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification());
    }

    private static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount) {
        return getOBWriteDomesticResponse5DataRefund(
                frReadRefundAccount,
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification());
    }

    private static Optional<OBWriteDomesticResponse4DataRefund> getOBWriteDomesticResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (mustHaveRefund(frReadRefundAccount)) {
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

    private static Optional<OBWriteDomesticResponse5DataRefund> getOBWriteDomesticResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification) {
        if (mustHaveRefund(frReadRefundAccount)) {
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
        return getObWriteInternationalResponse4DataRefund(
                frReadRefundAccount,
                creditor,
                creditor.getName(),
                creditor.getPostalAddress(),
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification(),
                creditorAgent,
                creditorAgent.getSchemeName(),
                creditorAgent.getIdentification(),
                creditorAgent.getName(),
                creditorAgent.getPostalAddress());
    }

    private static Optional<OBWriteInternationalResponse4DataRefund> getObWriteInternationalResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return getObWriteInternationalResponse4DataRefund(
                frReadRefundAccount,
                creditor,
                creditor.getName(),
                creditor.getPostalAddress(),
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification(),
                creditorAgent,
                creditorAgent.getSchemeName(),
                creditorAgent.getIdentification(),
                creditorAgent.getName(),
                creditorAgent.getPostalAddress());
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomestic2DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternational3DataInitiationCreditorAgent creditorAgent) {
        return getOBWriteInternationalResponse5DataRefund(
                frReadRefundAccount,
                creditor,
                creditor.getName(),
                creditor.getPostalAddress(),
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification(),
                creditorAgent,
                creditorAgent.getSchemeName(),
                creditorAgent.getIdentification(),
                creditorAgent.getName(),
                creditorAgent.getPostalAddress());
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount,
            OBWriteInternational3DataInitiationCreditor creditor,
            OBWriteInternationalStandingOrder4DataInitiationCreditorAgent creditorAgent) {
        return getOBWriteInternationalResponse5DataRefund(
                frReadRefundAccount,
                creditor,
                creditor.getName(),
                creditor.getPostalAddress(),
                debtorAccount.getSchemeName(),
                debtorAccount.getIdentification(),
                debtorAccount.getName(),
                debtorAccount.getSecondaryIdentification(),
                creditorAgent,
                creditorAgent.getSchemeName(),
                creditorAgent.getIdentification(),
                creditorAgent.getName(),
                creditorAgent.getPostalAddress());
    }

    private static Optional<OBWriteInternationalResponse4DataRefund> getObWriteInternationalResponse4DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            Object creditor,
            String creditorName,
            OBPostalAddress6 creditorPostalAddress,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification,
            Object creditorAgent,
            String creditorAgentSchemeName,
            String creditorAgentIdentification,
            String creditorAgentName,
            OBPostalAddress6 creditorAgentPostalAddress) {
        if (mustHaveRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteInternationalResponse4DataRefund obWriteInternationalResponse4DataRefund = new OBWriteInternationalResponse4DataRefund()
                    .account(
                            new OBWriteDomesticResponse4DataRefundAccount()
                                    .schemeName(debtorAccountSchemeName)
                                    .identification(debtorAccountIdentification)
                                    .name(debtorAccountName)
                                    .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            // refund creditor optional
            if (creditor != null) {
                obWriteInternationalResponse4DataRefund
                        .creditor(new OBWriteInternationalResponse4DataRefundCreditor()
                                .name(creditorName)
                                .postalAddress(creditorPostalAddress)
                        );
            }
            // refund agent optional
            if (creditorAgent != null) {
                obWriteInternationalResponse4DataRefund
                        .agent(
                                new OBWriteInternationalResponse4DataRefundAgent()
                                        .schemeName(creditorAgentSchemeName)
                                        .identification(creditorAgentIdentification)
                                        .name(creditorAgentName)
                                        .postalAddress(creditorAgentPostalAddress)
                        );
            }
            return Optional.of(obWriteInternationalResponse4DataRefund);
        }
        return Optional.empty();
    }

    private static Optional<OBWriteInternationalResponse5DataRefund> getOBWriteInternationalResponse5DataRefund(
            FRReadRefundAccount frReadRefundAccount,
            Object creditor,
            String creditorName,
            OBPostalAddress6 creditorPostalAddress,
            String debtorAccountSchemeName,
            String debtorAccountIdentification,
            String debtorAccountName,
            String debtorAccountSecondaryIdentification,
            Object creditorAgent,
            String creditorAgentSchemeName,
            String creditorAgentIdentification,
            String creditorAgentName,
            OBPostalAddress6 creditorAgentPostalAddress) {
        if (mustHaveRefund(frReadRefundAccount)) {
            // account mandatory
            OBWriteInternationalResponse5DataRefund obWriteInternationalResponse5DataRefund = new OBWriteInternationalResponse5DataRefund()
                    .account(
                            new OBWriteDomesticResponse5DataRefundAccount()
                                    .schemeName(debtorAccountSchemeName)
                                    .identification(debtorAccountIdentification)
                                    .name(debtorAccountName)
                                    .secondaryIdentification(debtorAccountSecondaryIdentification)
                    );
            // refund creditor optional
            if (creditor != null) {
                obWriteInternationalResponse5DataRefund
                        .creditor(new OBWriteInternationalResponse5DataRefundCreditor()
                                .name(creditorName)
                                .postalAddress(creditorPostalAddress)
                        );
            }
            // refund agent optional
            if (creditorAgent != null) {
                obWriteInternationalResponse5DataRefund
                        .agent(
                                new OBWriteInternationalResponse5DataRefundAgent()
                                        .schemeName(creditorAgentSchemeName)
                                        .identification(creditorAgentIdentification)
                                        .name(creditorAgentName)
                                        .postalAddress(creditorAgentPostalAddress)
                        );
            }
            return Optional.of(obWriteInternationalResponse5DataRefund);
        }
        return Optional.empty();
    }

}
