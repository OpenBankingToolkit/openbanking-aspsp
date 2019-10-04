/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf.discovery;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.discovery.*;

@Service
@ConfigurationProperties(prefix = "rs-discovery")
public class DiscoveryConfigurationProperties {

    @Data
    @NoArgsConstructor
    public static class Apis {
        public PaymentApis payments;
        public AccountsApis accounts;
        public FundsConfirmationApis fundsConfirmations;
        public EventNotificationApis eventNotifications;
    }

    @Data
    @NoArgsConstructor
    public static class FundsConfirmationApis {
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_0;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_1;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_2;
    }

    @Data
    @NoArgsConstructor
    public static class EventNotificationApis {
        public OBDiscoveryAPILinksEventNotification3 v_3_0;
        public OBDiscoveryAPILinksEventNotification3 v_3_1;
        public OBDiscoveryAPILinksEventNotification3 v_3_1_1;
        public OBDiscoveryAPILinksEventNotification3 v_3_1_2;
    }

    @Data
    @NoArgsConstructor
    public static class PaymentApis {
        public OBDiscoveryAPILinksPayment1 v_1_1;
        public OBDiscoveryAPILinksPayment1 v_2_0;
        public OBDiscoveryAPILinksPayment3 v_3_0;
        public OBDiscoveryAPILinksPayment4 v_3_1;
        public OBDiscoveryAPILinksPayment4 v_3_1_1;
        public OBDiscoveryAPILinksPayment4 v_3_1_2;
    }

    @Data
    @NoArgsConstructor
    public static class AccountsApis {
        public OBDiscoveryAPILinksAccount1 v_1_1;
        public OBDiscoveryAPILinksAccount2 v_2_0;
        public OBDiscoveryAPILinksAccount3 v_3_0;
        public OBDiscoveryAPILinksAccount3 v_3_1;
        public OBDiscoveryAPILinksAccount3 v_3_1_1;
        public OBDiscoveryAPILinksAccount3 v_3_1_2;
    }

    @Data
    @NoArgsConstructor
    public static class DiscoveryPaymentApi {
        public String version;
        public String createSingleImmediatePayment;
        public String getSingleImmediatePayment;
        public String createPaymentSubmission;
        public String getPaymentSubmission;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreateSingleImmediatePayment() {
            return createSingleImmediatePayment;
        }

        public void setCreateSingleImmediatePayment(String createSingleImmediatePayment) {
            this.createSingleImmediatePayment = createSingleImmediatePayment;
        }

        public String getGetSingleImmediatePayment() {
            return getSingleImmediatePayment;
        }

        public void setGetSingleImmediatePayment(String getSingleImmediatePayment) {
            this.getSingleImmediatePayment = getSingleImmediatePayment;
        }

        public String getCreatePaymentSubmission() {
            return createPaymentSubmission;
        }

        public void setCreatePaymentSubmission(String createPaymentSubmission) {
            this.createPaymentSubmission = createPaymentSubmission;
        }

        public String getGetPaymentSubmission() {
            return getPaymentSubmission;
        }

        public void setGetPaymentSubmission(String getPaymentSubmission) {
            this.getPaymentSubmission = getPaymentSubmission;
        }
    }

    public static class DiscoveryAccountApi {
        public String version;
        public String createAccountRequest;
        public String getAccountRequest;
        public String deleteAccountRequest;
        public String getAccounts;
        public String getAccount;
        public String getAccountTransactions;
        public String getAccountBeneficiaries;
        public String getAccountBalances;
        public String getAccountDirectDebits;
        public String getAccountStandingOrders;
        public String getAccountProduct;
        public String getStandingOrders;
        public String getDirectDebits;
        public String getBeneficiaries;
        public String getTransactions;
        public String getBalances;
        public String getProducts;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCreateAccountRequest() {
            return createAccountRequest;
        }

        public void setCreateAccountRequest(String createAccountRequest) {
            this.createAccountRequest = createAccountRequest;
        }

        public String getGetAccountRequest() {
            return getAccountRequest;
        }

        public void setGetAccountRequest(String getAccountRequest) {
            this.getAccountRequest = getAccountRequest;
        }

        public String getDeleteAccountRequest() {
            return deleteAccountRequest;
        }

        public void setDeleteAccountRequest(String deleteAccountRequest) {
            this.deleteAccountRequest = deleteAccountRequest;
        }

        public String getGetAccounts() {
            return getAccounts;
        }

        public void setGetAccounts(String getAccounts) {
            this.getAccounts = getAccounts;
        }

        public String getGetAccount() {
            return getAccount;
        }

        public void setGetAccount(String getAccount) {
            this.getAccount = getAccount;
        }

        public String getGetAccountTransactions() {
            return getAccountTransactions;
        }

        public void setGetAccountTransactions(String getAccountTransactions) {
            this.getAccountTransactions = getAccountTransactions;
        }

        public String getGetAccountBeneficiaries() {
            return getAccountBeneficiaries;
        }

        public void setGetAccountBeneficiaries(String getAccountBeneficiaries) {
            this.getAccountBeneficiaries = getAccountBeneficiaries;
        }

        public String getGetAccountBalances() {
            return getAccountBalances;
        }

        public void setGetAccountBalances(String getAccountBalances) {
            this.getAccountBalances = getAccountBalances;
        }

        public String getGetAccountDirectDebits() {
            return getAccountDirectDebits;
        }

        public void setGetAccountDirectDebits(String getAccountDirectDebits) {
            this.getAccountDirectDebits = getAccountDirectDebits;
        }

        public String getGetAccountStandingOrders() {
            return getAccountStandingOrders;
        }

        public void setGetAccountStandingOrders(String getAccountStandingOrders) {
            this.getAccountStandingOrders = getAccountStandingOrders;
        }

        public String getGetAccountProduct() {
            return getAccountProduct;
        }

        public void setGetAccountProduct(String getAccountProduct) {
            this.getAccountProduct = getAccountProduct;
        }

        public String getGetStandingOrders() {
            return getStandingOrders;
        }

        public void setGetStandingOrders(String getStandingOrders) {
            this.getStandingOrders = getStandingOrders;
        }

        public String getGetDirectDebits() {
            return getDirectDebits;
        }

        public void setGetDirectDebits(String getDirectDebits) {
            this.getDirectDebits = getDirectDebits;
        }

        public String getGetBeneficiaries() {
            return getBeneficiaries;
        }

        public void setGetBeneficiaries(String getBeneficiaries) {
            this.getBeneficiaries = getBeneficiaries;
        }

        public String getGetTransactions() {
            return getTransactions;
        }

        public void setGetTransactions(String getTransactions) {
            this.getTransactions = getTransactions;
        }

        public String getGetBalances() {
            return getBalances;
        }

        public void setGetBalances(String getBalances) {
            this.getBalances = getBalances;
        }

        public String getGetProducts() {
            return getProducts;
        }

        public void setGetProducts(String getProducts) {
            this.getProducts = getProducts;
        }
    }

    public Apis apis;

    public Apis getApis() {
        return apis;
    }

    public void setApis(Apis apis) {
        this.apis = apis;
    }
}
