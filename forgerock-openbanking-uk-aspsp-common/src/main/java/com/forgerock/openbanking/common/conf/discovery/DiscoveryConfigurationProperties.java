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
package com.forgerock.openbanking.common.conf.discovery;

import com.forgerock.openbanking.common.model.version.OBVersion;
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
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_3;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_4;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_5;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_6;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_7;
        public OBDiscoveryAPILinksFundsConfirmation3 v_3_1_8;

        public OBDiscoveryAPILinksFundsConfirmation3 getVersion(OBVersion version){
            switch (version){
                case v3_0: return v_3_0;
                case v3_1: return v_3_1;
                case v3_1_1: return v_3_1_1;
                case v3_1_2: return v_3_1_2;
                case v3_1_3: return v_3_1_3;
                case v3_1_4: return v_3_1_4;
                case v3_1_5: return v_3_1_5;
                case v3_1_6: return v_3_1_6;
                case v3_1_7: return v_3_1_7;
                case v3_1_8: return v_3_1_8;
                default: return null;
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class EventNotificationApis {
        public OBDiscoveryAPILinksEventNotification3 v_3_0;
        public OBDiscoveryAPILinksEventNotification3 v_3_1;
        public OBDiscoveryAPILinksEventNotification3 v_3_1_1;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_2;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_3;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_4;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_5;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_6;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_7;
        public OBDiscoveryAPILinksEventNotification4 v_3_1_8;

        public OBDiscoveryAPILinks getVersion(OBVersion version){
            switch (version){
                case v3_0: return v_3_0;
                case v3_1: return v_3_1;
                case v3_1_1: return v_3_1_1;
                case v3_1_2: return v_3_1_2;
                case v3_1_3: return v_3_1_3;
                case v3_1_4: return v_3_1_4;
                case v3_1_5: return v_3_1_5;
                case v3_1_6: return v_3_1_6;
                case v3_1_7: return v_3_1_7;
                case v3_1_8: return v_3_1_8;
                default: return null;
            }
        }
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
        public OBDiscoveryAPILinksPayment4 v_3_1_3;
        public OBDiscoveryAPILinksPayment4 v_3_1_4;
        public OBDiscoveryAPILinksPayment4 v_3_1_5;
        public OBDiscoveryAPILinksPayment4 v_3_1_6;
        public OBDiscoveryAPILinksPayment4 v_3_1_7;
        public OBDiscoveryAPILinksPayment4 v_3_1_8;
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
        public OBDiscoveryAPILinksAccount3 v_3_1_3;
        public OBDiscoveryAPILinksAccount3 v_3_1_4;
        public OBDiscoveryAPILinksAccount3 v_3_1_5;
        public OBDiscoveryAPILinksAccount3 v_3_1_6;
        public OBDiscoveryAPILinksAccount3 v_3_1_7;
        public OBDiscoveryAPILinksAccount3 v_3_1_8;
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
