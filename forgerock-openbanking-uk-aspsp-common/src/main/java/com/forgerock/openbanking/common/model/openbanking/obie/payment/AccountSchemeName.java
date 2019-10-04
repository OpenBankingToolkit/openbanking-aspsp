/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.obie.payment;

import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.account.OBCashAccount3;

/**
 * Data Type for Account/SchemeName and used to identify the type of Identification used to identify an account.
 *
 */
public enum AccountSchemeName {
    /** Primary Account Number - identifier scheme used to identify a card account e.g. credit and debit cards. */
    UK_OBIE_PAN("PAN"),
    /** Identifier scheme used in the UK by financial institutions to identify the account of a customer. e.g. current accounts */
    UK_OBIE_SORTCODEACCOUNTNUMBER("SortCodeAccountNumber"),
    /** Paym Scheme to make payments via mobile */
    UK_OBIE_PAYM("Paym")
    ;

    // Will add more as they are used in code. See https://openbanking.atlassian.net/wiki/spaces/DZ/pages/1000571919/Namespaced+Enumerations+-+v3.1.2#NamespacedEnumerations-v3.1.2-OBExternalAccountIdentification4Code

    private static final String OBIE_NAMESPACE = "UK.OBIE.";
    private String value;
    private String nameSpacedValue;

    AccountSchemeName(String value) {
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

    // Convenience method
    public static boolean isAccountOfType(OBCashAccount3 account, AccountSchemeName type) {
        if (account==null || StringUtils.isEmpty(account.getSchemeName()))  {
            return false;
        }
        return type.isEqual(account.getSchemeName());
    }
}
