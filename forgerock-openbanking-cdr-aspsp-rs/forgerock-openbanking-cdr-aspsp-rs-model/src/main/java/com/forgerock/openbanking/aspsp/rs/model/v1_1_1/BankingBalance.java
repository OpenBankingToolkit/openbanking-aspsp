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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingBalance
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:51.111520Z[Europe/London]")
public class BankingBalance {
    @JsonProperty("accountId")
    private String accountId = null;

    @JsonProperty("currentBalance")
    private String currentBalance = null;

    @JsonProperty("availableBalance")
    private String availableBalance = null;

    @JsonProperty("creditLimit")
    private String creditLimit = null;

    @JsonProperty("amortisedLimit")
    private String amortisedLimit = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("purses")
    @Valid
    private List<BankingBalancePurse> purses = null;

    public BankingBalance accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * A unique ID of the account adhering to the standards for ID permanence
     * @return accountId
     **/
    @ApiModelProperty(required = true, value = "A unique ID of the account adhering to the standards for ID permanence")
    @NotNull

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BankingBalance currentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    /**
     * The balance of the account at this time. Should align to the balance available via other channels such as Internet Banking. Assumed to be negative if the customer has money owing
     * @return currentBalance
     **/
    @ApiModelProperty(required = true, value = "The balance of the account at this time. Should align to the balance available via other channels such as Internet Banking. Assumed to be negative if the customer has money owing")
    @NotNull

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BankingBalance availableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
        return this;
    }

    /**
     * Balance representing the amount of funds available for transfer. Assumed to be zero or positive
     * @return availableBalance
     **/
    @ApiModelProperty(required = true, value = "Balance representing the amount of funds available for transfer. Assumed to be zero or positive")
    @NotNull

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BankingBalance creditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
        return this;
    }

    /**
     * Object representing the maximum amount of credit that is available for this account. Assumed to be zero if absent
     * @return creditLimit
     **/
    @ApiModelProperty(value = "Object representing the maximum amount of credit that is available for this account. Assumed to be zero if absent")

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BankingBalance amortisedLimit(String amortisedLimit) {
        this.amortisedLimit = amortisedLimit;
        return this;
    }

    /**
     * Object representing the available limit amortised according to payment schedule. Assumed to be zero if absent
     * @return amortisedLimit
     **/
    @ApiModelProperty(value = "Object representing the available limit amortised according to payment schedule. Assumed to be zero if absent")

    public String getAmortisedLimit() {
        return amortisedLimit;
    }

    public void setAmortisedLimit(String amortisedLimit) {
        this.amortisedLimit = amortisedLimit;
    }

    public BankingBalance currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * The currency for the balance amounts. If absent assumed to be AUD
     * @return currency
     **/
    @ApiModelProperty(value = "The currency for the balance amounts. If absent assumed to be AUD")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BankingBalance purses(List<BankingBalancePurse> purses) {
        this.purses = purses;
        return this;
    }

    public BankingBalance addPursesItem(BankingBalancePurse pursesItem) {
        if (this.purses == null) {
            this.purses = new ArrayList<BankingBalancePurse>();
        }
        this.purses.add(pursesItem);
        return this;
    }

    /**
     * Optional array of balances for the account in other currencies. Included to support accounts that support multi-currency purses such as Travel Cards
     * @return purses
     **/
    @ApiModelProperty(value = "Optional array of balances for the account in other currencies. Included to support accounts that support multi-currency purses such as Travel Cards")
    @Valid
    public List<BankingBalancePurse> getPurses() {
        return purses;
    }

    public void setPurses(List<BankingBalancePurse> purses) {
        this.purses = purses;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingBalance bankingBalance = (BankingBalance) o;
        return Objects.equals(this.accountId, bankingBalance.accountId) &&
                Objects.equals(this.currentBalance, bankingBalance.currentBalance) &&
                Objects.equals(this.availableBalance, bankingBalance.availableBalance) &&
                Objects.equals(this.creditLimit, bankingBalance.creditLimit) &&
                Objects.equals(this.amortisedLimit, bankingBalance.amortisedLimit) &&
                Objects.equals(this.currency, bankingBalance.currency) &&
                Objects.equals(this.purses, bankingBalance.purses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, currentBalance, availableBalance, creditLimit, amortisedLimit, currency, purses);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingBalance {\n");

        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    currentBalance: ").append(toIndentedString(currentBalance)).append("\n");
        sb.append("    availableBalance: ").append(toIndentedString(availableBalance)).append("\n");
        sb.append("    creditLimit: ").append(toIndentedString(creditLimit)).append("\n");
        sb.append("    amortisedLimit: ").append(toIndentedString(amortisedLimit)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    purses: ").append(toIndentedString(purses)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
