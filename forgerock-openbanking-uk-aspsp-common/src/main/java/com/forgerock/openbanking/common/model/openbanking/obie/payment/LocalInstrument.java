/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.obie.payment;

/**
 * ASPSP's payment service to be used for making a payment.
 */
public enum LocalInstrument {
    /** A balance transfer allows you to move debt between payment cards */
    UK_OBIE_BalanceTransfer("BalanceTransfer"),
    /** A money transfer allows to allows you to borrow money from a credit card and transfer it to a current account */
    UK_OBIE_MoneyTransfer("MoneyTransfer"),
    /** Paym is a UK mobile payment system that lets you transfer money using a mobile number as an account identifier*/
    UK_OBIE_Paym("Paym")
    ;

    // Will add more as they are used in code. See https://openbanking.atlassian.net/wiki/spaces/DZ/pages/1000571919/Namespaced+Enumerations+-+v3.1.1#NamespacedEnumerations-v3.1.1-OBExternalLocalInstrument1Code

    private static final String OBIE_NAMESPACE = "UK.OBIE.";
    private String value;
    private String nameSpacedValue;

    LocalInstrument(String value) {
        this.value = value;
        this.nameSpacedValue = OBIE_NAMESPACE + value;

    }

    public String getNamespacedValue() {
        return nameSpacedValue;
    }

    // We need to support name-spaced and non-namespaced values as an ASPSP can submit either in a consent
    public boolean isEqual(String str) {
        return nameSpacedValue.equalsIgnoreCase(str) || value.equalsIgnoreCase(str);
    }
}
