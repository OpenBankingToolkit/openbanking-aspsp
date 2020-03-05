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
package com.forgerock.openbanking.aspsp.rs.model.v0_9_6;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * The set of payment amounts and destination accounts for this payment accommodating multi-part payments. A single entry indicates a simple payment with one destination account. Must have at least one entry
 */
@ApiModel(description = "The set of payment amounts and destination accounts for this payment accommodating multi-part payments. A single entry indicates a simple payment with one destination account. Must have at least one entry")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingScheduledPaymentSet {
    @JsonProperty("to")
    private BankingScheduledPaymentTo to = null;

    @JsonProperty("isAmountCalculated")
    private Boolean isAmountCalculated = null;

    @JsonProperty("amount")
    private String amount = null;

    @JsonProperty("currency")
    private String currency = null;

    public BankingScheduledPaymentSet to(BankingScheduledPaymentTo to) {
        this.to = to;
        return this;
    }

    /**
     * Get to
     * @return to
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public BankingScheduledPaymentTo getTo() {
        return to;
    }

    public void setTo(BankingScheduledPaymentTo to) {
        this.to = to;
    }

    public BankingScheduledPaymentSet isAmountCalculated(Boolean isAmountCalculated) {
        this.isAmountCalculated = isAmountCalculated;
        return this;
    }

    /**
     * Flag indicating whether the amount of the payment is calculated based on the context of the event. For instance a payment to reduce the balance of a credit card to zero. If absent then false is assumed
     * @return isAmountCalculated
     **/
    @ApiModelProperty(value = "Flag indicating whether the amount of the payment is calculated based on the context of the event. For instance a payment to reduce the balance of a credit card to zero. If absent then false is assumed")

    public Boolean isIsAmountCalculated() {
        return isAmountCalculated;
    }

    public void setIsAmountCalculated(Boolean isAmountCalculated) {
        this.isAmountCalculated = isAmountCalculated;
    }

    public BankingScheduledPaymentSet amount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Flag indicating whether the amount of the payment is calculated based on the context of the event. For instance a payment to reduce the balance of a credit card to zero. If absent then false is assumed
     * @return amount
     **/
    @ApiModelProperty(value = "Flag indicating whether the amount of the payment is calculated based on the context of the event. For instance a payment to reduce the balance of a credit card to zero. If absent then false is assumed")

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BankingScheduledPaymentSet currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * The currency for the payment. AUD assumed if not present
     * @return currency
     **/
    @ApiModelProperty(value = "The currency for the payment. AUD assumed if not present")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingScheduledPaymentSet bankingScheduledPaymentSet = (BankingScheduledPaymentSet) o;
        return Objects.equals(this.to, bankingScheduledPaymentSet.to) &&
                Objects.equals(this.isAmountCalculated, bankingScheduledPaymentSet.isAmountCalculated) &&
                Objects.equals(this.amount, bankingScheduledPaymentSet.amount) &&
                Objects.equals(this.currency, bankingScheduledPaymentSet.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, isAmountCalculated, amount, currency);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingScheduledPaymentSet {\n");

        sb.append("    to: ").append(toIndentedString(to)).append("\n");
        sb.append("    isAmountCalculated: ").append(toIndentedString(isAmountCalculated)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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
