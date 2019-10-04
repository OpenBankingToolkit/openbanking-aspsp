/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.openbanking;

public enum OBReference {

    //Apis group
    ACCOUNT_REQUEST("AccountRequest"),
    ACCOUNT_ACCESS_CONSENT("AccountAccessConsent"),
    ACCOUNTS("Accounts"),
    BALANCES("Balances"),
    BENEFICIARIES("Beneficiaries"),
    DIRECT_DEBITS("DirectDebits"),
    PRODUCTS("Products"),
    STANDING_ORDERS("StandingOrders"),
    TRANSACTIONS("Transaction"),
    OFFERS("Offers"),
    PARTY("Party"),
    SCHEDULED_PAYMENTS("ScheduledPayments"),
    STATEMENTS("Statements"),

    FUNDS("Funds"),

    EVENTS("Events"),

    SINGLE_PAYMENTS("SinglePayments"),
    SINGLE_PAYMENTS_SUBMISSION("SinglePaymentsSubmission"),

    DOMESTIC_PAYMENTS("DomesticPayments"),
    DOMESTIC_SCHEDULED_PAYMENTS("DomesticScheduledPayments"),
    DOMESTIC_STANDING_ORDERS_PAYMENTS("DomesticStandingOrdersPayments"),

    INTERNATIONAL_PAYMENTS("InternationalPayments"),
    INTERNATIONAL_SCHEDULED_PAYMENTS("InternationalScheduledPayments"),
    INTERNATIONAL_STANDING_ORDERS_PAYMENTS("InternationalStandingOrdersPayments"),

    FILE_PAYMENTS("FilePayments"),

    //Apis
    CREATE_ACCOUNT_REQUEST("CreateAccountRequest"),
    DELETE_ACCOUNT_REQUEST("DeleteAccountRequest"),
    GET_ACCOUNT_REQUEST("GetAccountRequest"),

    CREATE_ACCOUNT_ACCESS_CONSENT("CreateAccountAccessConsent"),
    DELETE_ACCOUNT_ACCESS_CONSENT("DeleteAccountAccessConsent"),
    GET_ACCOUNT_ACCESS_CONSENT("GetAccountAccessConsent"),

    GET_ACCOUNTS("GetAccounts"),
    GET_ACCOUNT("GetAccount"),
    GET_ACCOUNT_TRANSACTIONS("GetAccountTransactions"),
    GET_ACCOUNT_BENEFICIARIES("GetAccountBeneficiaries"),
    GET_ACCOUNT_BALANCES("GetAccountBalances"),
    GET_ACCOUNT_DIRECT_DEBITS("GetAccountDirectDebits"),
    GET_ACCOUNT_STANDING_ORDERS("GetAccountStandingOrders"),
    GET_ACCOUNT_PRODUCT("GetAccountProduct"),
    GET_STANDING_ORDERS("GetStandingOrders"),
    GET_DIRECT_DEBITS("GetDirectDebits"),
    GET_BENEFICIARIES("GetBeneficiaries"),
    GET_TRANSACTIONS("GetTransactions"),
    GET_BALANCES("GetBalances"),
    GET_PRODUCTS("GetProducts"),
    GET_ACCOUNT_OFFERS("GetAccountOffers"),
    GET_ACCOUNT_PARTY("GetAccountParty"),
    GET_ACCOUNT_PARTIES("GetAccountParties"),
    GET_ACCOUNT_SCHEDULED_PAYMENTS("GetAccountScheduledPayments"),
    GET_ACCOUNT_STATEMENTS("GetAccountStatements"),
    GET_ACCOUNT_STATEMENT("GetAccountStatement"),
    GET_ACCOUNT_STATEMENT_FILE("GetAccountStatementFile"),
    GET_ACCOUNT_STATEMENT_TRANSACTIONS("GetAccountStatementTransactions"),
    GET_OFFERS("GetOffers"),
    GET_PARTY("GetParty"),
    GET_SCHEDULED_PAYMENTS("GetScheduledPayments"),
    GET_STATEMENTS("GetStatements"),

    CREATE_FUNDS_CONFIRMATION_CONSENT("CreateFundsConfirmationConsent"),
    GET_FUNDS_CONFIRMATION_CONSENT("GetFundsConfirmationConsent"),
    CREATE_FUNDS_CONFIRMATION("CreateFundsConfirmation"),
    GET_FUNDS_CONFIRMATION("GetFundsConfirmation"),
    DELETE_FUNDS_CONFIRMATION_CONSENT("DeleteFundsConfirmationConsent"),

    CREATE_CALLBACK_URL("CreateCallbackUrl"),
    GET_CALLBACK_URLS("GetCallbackUrls"),
    AMEND_CALLBACK_URL("AmendCallbackUrl"),
    DELETE_CALLBACK_URL("DeleteCallbackUrl"),

    CREATE_EVENT_SUBSCRIPTION("CreateEventSubscription"),
    GET_EVENT_SUBSCRIPTION("GetEventSubscription"),
    AMEND_EVENT_SUBSCRIPTION("AmendEventSubscription"),
    DELETE_EVENT_SUBSCRIPTION("DeleteEventSubscription"),

    EVENT_AGGREGATED_POLLING("EventAggregatedPolling"),

    CREATE_SINGLE_IMMEDIATE_PAYMENT("CreateSingleImmediatePayment"),
    GET_SINGLE_IMMEDIATE_PAYMENT("GetSingleImmediatePayment"),
    CREATE_PAYMENT_SUBMISSION("CreatePaymentSubmission"),
    GET_PAYMENT_SUBMISSION("GetPaymentSubmission"),

    CREATE_DOMESTIC_PAYMENT_CONSENT("CreateDomesticPaymentConsent"),
    GET_DOMESTIC_PAYMENT_CONSENT("GetDomesticPaymentConsent"),
    CREATE_DOMESTIC_PAYMENT("CreateDomesticPayment"),
    GET_DOMESTIC_PAYMENT("GetDomesticPayment"),
    GET_DOMESTIC_PAYMENT_CONSENTS_CONSENT_ID_FUNDS_CONFIRMATION("GetDomesticPaymentConsentsConsentIdFundsConfirmation"),

    CREATE_DOMESTIC_SCHEDULED_PAYMENT_CONSENT("CreateDomesticScheduledPaymentConsent"),
    GET_DOMESTIC_SCHEDULED_PAYMENT_CONSENT("GetDomesticScheduledPaymentConsent"),
    CREATE_DOMESTIC_SCHEDULED_PAYMENT("CreateDomesticScheduledPayment"),
    GET_DOMESTIC_SCHEDULED_PAYMENT("GetDomesticScheduledPayment"),

    CREATE_DOMESTIC_STANDING_ORDER_CONSENT("CreateDomesticStandingOrderConsent"),
    GET_DOMESTIC_STANDING_ORDER_CONSENT("GetDomesticStandingOrderConsent"),
    CREATE_DOMESTIC_STANDING_ORDER("CreateDomesticStandingOrder"),
    GET_DOMESTIC_STANDING_ORDER("GetDomesticStandingOrder"),

    CREATE_INTERNATIONAL_PAYMENT_CONSENT("CreateInternationalPaymentConsent"),
    GET_INTERNATIONAL_PAYMENT_CONSENT("GetInternationalPaymentConsent"),
    CREATE_INTERNATIONAL_PAYMENT("CreateInternationalPayment"),
    GET_INTERNATIONAL_PAYMENT("GetInternationalPayment"),
    GET_INTERNATIONAL_PAYMENT_CONSENTS_CONSENT_ID_FUNDS_CONFIRMATION("GetInternationalPaymentConsentsConsentIdFundsConfirmation"),

    CREATE_INTERNATIONAL_SCHEDULED_PAYMENT_CONSENT("CreateInternationalScheduledPaymentConsent"),
    GET_INTERNATIONAL_SCHEDULED_PAYMENT_CONSENT("GetInternationalScheduledPaymentConsent"),
    CREATE_INTERNATIONAL_SCHEDULED_PAYMENT("CreateInternationalScheduledPayment"),
    GET_INTERNATIONAL_SCHEDULED_PAYMENT("GetInternationalScheduledPayment"),
    GET_INTERNATIONAL_SCHEDULED_PAYMENT_CONSENTS_CONSENT_ID_FUNDS_CONFIRMATION("GetInternationalScheduledPaymentConsentsConsentIdFundsConfirmation"),

    CREATE_INTERNATIONAL_STANDING_ORDER_CONSENT("CreateInternationalStandingOrderConsent"),
    GET_INTERNATIONAL_STANDING_ORDER_CONSENT("GetInternationalStandingOrderConsent"),
    CREATE_INTERNATIONAL_STANDING_ORDER("CreateInternationalStandingOrder"),
    GET_INTERNATIONAL_STANDING_ORDER("GetInternationalStandingOrder"),

    CREATE_FILE_PAYMENT_CONSENT("CreateFilePaymentConsent"),
    GET_FILE_PAYMENT_CONSENT("GetFilePaymentConsent"),

    CREATE_FILE_PAYMENT_FILE("CreateFilePaymentFile"),
    GET_FILE_PAYMENT_FILE("GetFilePaymentFile"),

    CREATE_FILE_PAYMENT("CreateFilePayment"),
    GET_FILE_PAYMENT("GetFilePayment"),

    GET_FILE_PAYMENT_REPORT("GetFilePaymentReport"),

    NONE("");

    private String reference;

    OBReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
