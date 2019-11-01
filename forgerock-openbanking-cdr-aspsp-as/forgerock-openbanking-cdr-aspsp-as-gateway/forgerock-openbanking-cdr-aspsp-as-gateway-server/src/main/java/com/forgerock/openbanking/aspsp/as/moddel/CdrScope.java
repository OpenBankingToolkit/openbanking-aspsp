/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.moddel;

public class CdrScope  {
    public static final String BANK_ACCOUNT_BASIC_READ = "bank:accounts.basic:read";
    public static final String BANK_ACCOUNT_DETAILS_READ = "bank:accounts.detail:read";
    public static final String BANK_TRANSACTIONS_READ = "bank:transactions:read";
    public static final String BANK_PAYEE_READ = "bank:payees:read";
    public static final String BANK_REGULAR_PAYMENT_READ = "bank:regular_payments:read";
    public static final String COMMON_CUSTOMER_BASIC_READ = "common:customer.basic:read";
    public static final String COMMON_CUSTOMER_DETAILS_READ = "common:customer.detail:read";
}