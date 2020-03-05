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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_0;

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
 * BankingProductDiscount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class BankingProductDiscount {
    @JsonProperty("description")
    private String description = null;

    /**
     * The type of discount. See the next section for an overview of valid values and their meaning
     */
    public enum DiscountTypeEnum {
        BALANCE("BALANCE"),

        DEPOSITS("DEPOSITS"),

        PAYMENTS("PAYMENTS"),

        FEE_CAP("FEE_CAP"),

        ELIGIBILITY_ONLY("ELIGIBILITY_ONLY");

        private String value;

        DiscountTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DiscountTypeEnum fromValue(String text) {
            for (DiscountTypeEnum b : DiscountTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("discountType")
    private DiscountTypeEnum discountType = null;

    @JsonProperty("amount")
    private String amount = null;

    @JsonProperty("balanceRate")
    private String balanceRate = null;

    @JsonProperty("transactionRate")
    private String transactionRate = null;

    @JsonProperty("accruedRate")
    private String accruedRate = null;

    @JsonProperty("feeRate")
    private String feeRate = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    @JsonProperty("eligibility")
    @Valid
    private List<BankingProductDiscountEligibility> eligibility = null;

    public BankingProductDiscount description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the discount
     * @return description
     **/
    @ApiModelProperty(required = true, value = "Description of the discount")
    @NotNull

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankingProductDiscount discountType(DiscountTypeEnum discountType) {
        this.discountType = discountType;
        return this;
    }

    /**
     * The type of discount. See the next section for an overview of valid values and their meaning
     * @return discountType
     **/
    @ApiModelProperty(required = true, value = "The type of discount. See the next section for an overview of valid values and their meaning")
    @NotNull

    public DiscountTypeEnum getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountTypeEnum discountType) {
        this.discountType = discountType;
    }

    public BankingProductDiscount amount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Value of the discount. When following properties include one of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory
     * @return amount
     **/
    @ApiModelProperty(value = "Value of the discount. When following properties include one of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory")

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BankingProductDiscount balanceRate(String balanceRate) {
        this.balanceRate = balanceRate;
        return this;
    }

    /**
     * A discount rate calculated based on a proportion of the balance. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee
     * @return balanceRate
     **/
    @ApiModelProperty(value = "A discount rate calculated based on a proportion of the balance. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee")

    public String getBalanceRate() {
        return balanceRate;
    }

    public void setBalanceRate(String balanceRate) {
        this.balanceRate = balanceRate;
    }

    public BankingProductDiscount transactionRate(String transactionRate) {
        this.transactionRate = transactionRate;
        return this;
    }

    /**
     * A discount rate calculated based on a proportion of a transaction. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory
     * @return transactionRate
     **/
    @ApiModelProperty(value = "A discount rate calculated based on a proportion of a transaction. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory")

    public String getTransactionRate() {
        return transactionRate;
    }

    public void setTransactionRate(String transactionRate) {
        this.transactionRate = transactionRate;
    }

    public BankingProductDiscount accruedRate(String accruedRate) {
        this.accruedRate = accruedRate;
        return this;
    }

    /**
     * A discount rate calculated based on a proportion of the calculated interest accrued on the account. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee
     * @return accruedRate
     **/
    @ApiModelProperty(value = "A discount rate calculated based on a proportion of the calculated interest accrued on the account. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee")

    public String getAccruedRate() {
        return accruedRate;
    }

    public void setAccruedRate(String accruedRate) {
        this.accruedRate = accruedRate;
    }

    public BankingProductDiscount feeRate(String feeRate) {
        this.feeRate = feeRate;
        return this;
    }

    /**
     * A discount rate calculated based on a proportion of the fee to which this discount is attached. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee
     * @return feeRate
     **/
    @ApiModelProperty(value = "A discount rate calculated based on a proportion of the fee to which this discount is attached. Note that the currency of the fee discount is expected to be the same as the currency of the fee itself. One of amount, balanceRate, transactionRate, accruedRate and feeRate is mandatory. Unless noted in additionalInfo, assumes the application and calculation frequency are the same as the corresponding fee")

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public BankingProductDiscount additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [discountType](#tocSproductdiscounttypedoc) specified. Whether mandatory or not is dependent on the value of [discountType](#tocSproductdiscounttypedoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [discountType](#tocSproductdiscounttypedoc) specified. Whether mandatory or not is dependent on the value of [discountType](#tocSproductdiscounttypedoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductDiscount additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on the discount
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on the discount")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductDiscount additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this discount
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this discount")

    public String getAdditionalInfoUri() {
        return additionalInfoUri;
    }

    public void setAdditionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
    }

    public BankingProductDiscount eligibility(List<BankingProductDiscountEligibility> eligibility) {
        this.eligibility = eligibility;
        return this;
    }

    public BankingProductDiscount addEligibilityItem(BankingProductDiscountEligibility eligibilityItem) {
        if (this.eligibility == null) {
            this.eligibility = new ArrayList<BankingProductDiscountEligibility>();
        }
        this.eligibility.add(eligibilityItem);
        return this;
    }

    /**
     * Eligibility constraints that apply to this discount
     * @return eligibility
     **/
    @ApiModelProperty(value = "Eligibility constraints that apply to this discount")
    @Valid
    public List<BankingProductDiscountEligibility> getEligibility() {
        return eligibility;
    }

    public void setEligibility(List<BankingProductDiscountEligibility> eligibility) {
        this.eligibility = eligibility;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingProductDiscount bankingProductDiscount = (BankingProductDiscount) o;
        return Objects.equals(this.description, bankingProductDiscount.description) &&
                Objects.equals(this.discountType, bankingProductDiscount.discountType) &&
                Objects.equals(this.amount, bankingProductDiscount.amount) &&
                Objects.equals(this.balanceRate, bankingProductDiscount.balanceRate) &&
                Objects.equals(this.transactionRate, bankingProductDiscount.transactionRate) &&
                Objects.equals(this.accruedRate, bankingProductDiscount.accruedRate) &&
                Objects.equals(this.feeRate, bankingProductDiscount.feeRate) &&
                Objects.equals(this.additionalValue, bankingProductDiscount.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductDiscount.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductDiscount.additionalInfoUri) &&
                Objects.equals(this.eligibility, bankingProductDiscount.eligibility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, discountType, amount, balanceRate, transactionRate, accruedRate, feeRate, additionalValue, additionalInfo, additionalInfoUri, eligibility);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductDiscount {\n");

        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    discountType: ").append(toIndentedString(discountType)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    balanceRate: ").append(toIndentedString(balanceRate)).append("\n");
        sb.append("    transactionRate: ").append(toIndentedString(transactionRate)).append("\n");
        sb.append("    accruedRate: ").append(toIndentedString(accruedRate)).append("\n");
        sb.append("    feeRate: ").append(toIndentedString(feeRate)).append("\n");
        sb.append("    additionalValue: ").append(toIndentedString(additionalValue)).append("\n");
        sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
        sb.append("    additionalInfoUri: ").append(toIndentedString(additionalInfoUri)).append("\n");
        sb.append("    eligibility: ").append(toIndentedString(eligibility)).append("\n");
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
