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
package com.forgerock.openbanking.aspsp.rs.model.v1_0_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingProductFee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:11:31.339883Z[Europe/London]")
public class BankingProductFee {
    @JsonProperty("name")
    private String name = null;

    /**
     * The type of fee
     */
    public enum FeeTypeEnum {
        PERIODIC("PERIODIC"),

        TRANSACTION("TRANSACTION"),

        WITHDRAWAL("WITHDRAWAL"),

        DEPOSIT("DEPOSIT"),

        PAYMENT("PAYMENT"),

        PURCHASE("PURCHASE"),

        EVENT("EVENT"),

        UPFRONT("UPFRONT"),

        EXIT("EXIT");

        private String value;

        FeeTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static FeeTypeEnum fromValue(String text) {
            for (FeeTypeEnum b : FeeTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("feeType")
    private FeeTypeEnum feeType = null;

    @JsonProperty("amount")
    private String amount = null;

    @JsonProperty("balanceRate")
    private String balanceRate = null;

    @JsonProperty("transactionRate")
    private String transactionRate = null;

    @JsonProperty("accruedRate")
    private String accruedRate = null;

    @JsonProperty("accrualFrequency")
    private String accrualFrequency = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    @JsonProperty("discounts")
    @Valid
    private List<BankingProductDiscount> discounts = null;

    public BankingProductFee name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the fee
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the fee")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankingProductFee feeType(FeeTypeEnum feeType) {
        this.feeType = feeType;
        return this;
    }

    /**
     * The type of fee
     * @return feeType
     **/
    @ApiModelProperty(required = true, value = "The type of fee")
    @NotNull

    public FeeTypeEnum getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeTypeEnum feeType) {
        this.feeType = feeType;
    }

    public BankingProductFee amount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * The amount charged for the fee. One of amount, balanceRate, transactionRate and accruedRate is mandatory
     * @return amount
     **/
    @ApiModelProperty(value = "The amount charged for the fee. One of amount, balanceRate, transactionRate and accruedRate is mandatory")

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BankingProductFee balanceRate(String balanceRate) {
        this.balanceRate = balanceRate;
        return this;
    }

    /**
     * A fee rate calculated based on a proportion of the balance. One of amount, balanceRate, transactionRate and accruedRate is mandatory
     * @return balanceRate
     **/
    @ApiModelProperty(value = "A fee rate calculated based on a proportion of the balance. One of amount, balanceRate, transactionRate and accruedRate is mandatory")

    public String getBalanceRate() {
        return balanceRate;
    }

    public void setBalanceRate(String balanceRate) {
        this.balanceRate = balanceRate;
    }

    public BankingProductFee transactionRate(String transactionRate) {
        this.transactionRate = transactionRate;
        return this;
    }

    /**
     * A fee rate calculated based on a proportion of a transaction. One of amount, balanceRate, transactionRate and accruedRate is mandatory
     * @return transactionRate
     **/
    @ApiModelProperty(value = "A fee rate calculated based on a proportion of a transaction. One of amount, balanceRate, transactionRate and accruedRate is mandatory")

    public String getTransactionRate() {
        return transactionRate;
    }

    public void setTransactionRate(String transactionRate) {
        this.transactionRate = transactionRate;
    }

    public BankingProductFee accruedRate(String accruedRate) {
        this.accruedRate = accruedRate;
        return this;
    }

    /**
     * A fee rate calculated based on a proportion of the calculated interest accrued on the account. One of amount, balanceRate, transactionRate and accruedRate is mandatory
     * @return accruedRate
     **/
    @ApiModelProperty(value = "A fee rate calculated based on a proportion of the calculated interest accrued on the account. One of amount, balanceRate, transactionRate and accruedRate is mandatory")

    public String getAccruedRate() {
        return accruedRate;
    }

    public void setAccruedRate(String accruedRate) {
        this.accruedRate = accruedRate;
    }

    public BankingProductFee accrualFrequency(String accrualFrequency) {
        this.accrualFrequency = accrualFrequency;
        return this;
    }

    /**
     * The indicative frequency with which the fee is calculated on the account. Only applies if balanceRate or accruedRate is also present. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)
     * @return accrualFrequency
     **/
    @ApiModelProperty(value = "The indicative frequency with which the fee is calculated on the account. Only applies if balanceRate or accruedRate is also present. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)")

    public String getAccrualFrequency() {
        return accrualFrequency;
    }

    public void setAccrualFrequency(String accrualFrequency) {
        this.accrualFrequency = accrualFrequency;
    }

    public BankingProductFee currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * The currency the fee will be charged in. Assumes AUD if absent
     * @return currency
     **/
    @ApiModelProperty(value = "The currency the fee will be charged in. Assumes AUD if absent")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BankingProductFee additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [feeType](#tocSproductfeetypedoc) specified. Whether mandatory or not is dependent on the value of [feeType](#tocSproductfeetypedoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [feeType](#tocSproductfeetypedoc) specified. Whether mandatory or not is dependent on the value of [feeType](#tocSproductfeetypedoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductFee additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on the fee
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on the fee")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductFee additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this fee
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this fee")

    public String getAdditionalInfoUri() {
        return additionalInfoUri;
    }

    public void setAdditionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
    }

    public BankingProductFee discounts(List<BankingProductDiscount> discounts) {
        this.discounts = discounts;
        return this;
    }

    public BankingProductFee addDiscountsItem(BankingProductDiscount discountsItem) {
        if (this.discounts == null) {
            this.discounts = new ArrayList<BankingProductDiscount>();
        }
        this.discounts.add(discountsItem);
        return this;
    }

    /**
     * An optional list of discounts to this fee that may be available
     * @return discounts
     **/
    @ApiModelProperty(value = "An optional list of discounts to this fee that may be available")
    @Valid
    public List<BankingProductDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<BankingProductDiscount> discounts) {
        this.discounts = discounts;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingProductFee bankingProductFee = (BankingProductFee) o;
        return Objects.equals(this.name, bankingProductFee.name) &&
                Objects.equals(this.feeType, bankingProductFee.feeType) &&
                Objects.equals(this.amount, bankingProductFee.amount) &&
                Objects.equals(this.balanceRate, bankingProductFee.balanceRate) &&
                Objects.equals(this.transactionRate, bankingProductFee.transactionRate) &&
                Objects.equals(this.accruedRate, bankingProductFee.accruedRate) &&
                Objects.equals(this.accrualFrequency, bankingProductFee.accrualFrequency) &&
                Objects.equals(this.currency, bankingProductFee.currency) &&
                Objects.equals(this.additionalValue, bankingProductFee.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductFee.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductFee.additionalInfoUri) &&
                Objects.equals(this.discounts, bankingProductFee.discounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, feeType, amount, balanceRate, transactionRate, accruedRate, accrualFrequency, currency, additionalValue, additionalInfo, additionalInfoUri, discounts);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductFee {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    feeType: ").append(toIndentedString(feeType)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    balanceRate: ").append(toIndentedString(balanceRate)).append("\n");
        sb.append("    transactionRate: ").append(toIndentedString(transactionRate)).append("\n");
        sb.append("    accruedRate: ").append(toIndentedString(accruedRate)).append("\n");
        sb.append("    accrualFrequency: ").append(toIndentedString(accrualFrequency)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    additionalValue: ").append(toIndentedString(additionalValue)).append("\n");
        sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
        sb.append("    additionalInfoUri: ").append(toIndentedString(additionalInfoUri)).append("\n");
        sb.append("    discounts: ").append(toIndentedString(discounts)).append("\n");
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
