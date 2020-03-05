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
 * BankingProductLendingRate
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingProductLendingRate {
    /**
     * The type of rate (fixed, variable, etc). See the next section for an overview of valid values and their meaning
     */
    public enum LendingRateTypeEnum {
        FIXED("FIXED"),

        VARIABLE("VARIABLE"),

        INTRODUCTORY("INTRODUCTORY"),

        DISCOUNT("DISCOUNT"),

        PENALTY("PENALTY"),

        FLOATING("FLOATING"),

        MARKET_LINKED("MARKET_LINKED"),

        CASH_ADVANCE("CASH_ADVANCE"),

        PURCHASE("PURCHASE"),

        BUNDLE_DISCOUNT_FIXED("BUNDLE_DISCOUNT_FIXED"),

        BUNDLE_DISCOUNT_VARIABLE("BUNDLE_DISCOUNT_VARIABLE");

        private String value;

        LendingRateTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static LendingRateTypeEnum fromValue(String text) {
            for (LendingRateTypeEnum b : LendingRateTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("lendingRateType")
    private LendingRateTypeEnum lendingRateType = null;

    @JsonProperty("rate")
    private String rate = null;

    @JsonProperty("comparisonRate")
    private String comparisonRate = null;

    @JsonProperty("calculationFrequency")
    private String calculationFrequency = null;

    @JsonProperty("applicationFrequency")
    private String applicationFrequency = null;

    /**
     * When loan payments are due to be paid within each period. The investment benefit of earlier payments affect the rate that can be offered
     */
    public enum InterestPaymentDueEnum {
        ARREARS("IN_ARREARS"),

        ADVANCE("IN_ADVANCE");

        private String value;

        InterestPaymentDueEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static InterestPaymentDueEnum fromValue(String text) {
            for (InterestPaymentDueEnum b : InterestPaymentDueEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("interestPaymentDue")
    private InterestPaymentDueEnum interestPaymentDue = null;

    @JsonProperty("tiers")
    @Valid
    private List<BankingProductRateTier> tiers = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    public BankingProductLendingRate lendingRateType(LendingRateTypeEnum lendingRateType) {
        this.lendingRateType = lendingRateType;
        return this;
    }

    /**
     * The type of rate (fixed, variable, etc). See the next section for an overview of valid values and their meaning
     * @return lendingRateType
     **/
    @ApiModelProperty(required = true, value = "The type of rate (fixed, variable, etc). See the next section for an overview of valid values and their meaning")
    @NotNull

    public LendingRateTypeEnum getLendingRateType() {
        return lendingRateType;
    }

    public void setLendingRateType(LendingRateTypeEnum lendingRateType) {
        this.lendingRateType = lendingRateType;
    }

    public BankingProductLendingRate rate(String rate) {
        this.rate = rate;
        return this;
    }

    /**
     * The rate to be applied
     * @return rate
     **/
    @ApiModelProperty(required = true, value = "The rate to be applied")
    @NotNull

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public BankingProductLendingRate comparisonRate(String comparisonRate) {
        this.comparisonRate = comparisonRate;
        return this;
    }

    /**
     * A comparison rate equivalent for this rate
     * @return comparisonRate
     **/
    @ApiModelProperty(value = "A comparison rate equivalent for this rate")

    public String getComparisonRate() {
        return comparisonRate;
    }

    public void setComparisonRate(String comparisonRate) {
        this.comparisonRate = comparisonRate;
    }

    public BankingProductLendingRate calculationFrequency(String calculationFrequency) {
        this.calculationFrequency = calculationFrequency;
        return this;
    }

    /**
     * The period after which the rate is applied to the balance to calculate the amount due for the period. Calculation of the amount is often daily (as balances may change) but accumulated until the total amount is 'applied' to the account (see applicationFrequency). Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)
     * @return calculationFrequency
     **/
    @ApiModelProperty(value = "The period after which the rate is applied to the balance to calculate the amount due for the period. Calculation of the amount is often daily (as balances may change) but accumulated until the total amount is 'applied' to the account (see applicationFrequency). Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)")

    public String getCalculationFrequency() {
        return calculationFrequency;
    }

    public void setCalculationFrequency(String calculationFrequency) {
        this.calculationFrequency = calculationFrequency;
    }

    public BankingProductLendingRate applicationFrequency(String applicationFrequency) {
        this.applicationFrequency = applicationFrequency;
        return this;
    }

    /**
     * The period after which the calculated amount(s) (see calculationFrequency) are 'applied' (i.e. debited or credited) to the account. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)
     * @return applicationFrequency
     **/
    @ApiModelProperty(value = "The period after which the calculated amount(s) (see calculationFrequency) are 'applied' (i.e. debited or credited) to the account. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)")

    public String getApplicationFrequency() {
        return applicationFrequency;
    }

    public void setApplicationFrequency(String applicationFrequency) {
        this.applicationFrequency = applicationFrequency;
    }

    public BankingProductLendingRate interestPaymentDue(InterestPaymentDueEnum interestPaymentDue) {
        this.interestPaymentDue = interestPaymentDue;
        return this;
    }

    /**
     * When loan payments are due to be paid within each period. The investment benefit of earlier payments affect the rate that can be offered
     * @return interestPaymentDue
     **/
    @ApiModelProperty(value = "When loan payments are due to be paid within each period. The investment benefit of earlier payments affect the rate that can be offered")

    public InterestPaymentDueEnum getInterestPaymentDue() {
        return interestPaymentDue;
    }

    public void setInterestPaymentDue(InterestPaymentDueEnum interestPaymentDue) {
        this.interestPaymentDue = interestPaymentDue;
    }

    public BankingProductLendingRate tiers(List<BankingProductRateTier> tiers) {
        this.tiers = tiers;
        return this;
    }

    public BankingProductLendingRate addTiersItem(BankingProductRateTier tiersItem) {
        if (this.tiers == null) {
            this.tiers = new ArrayList<BankingProductRateTier>();
        }
        this.tiers.add(tiersItem);
        return this;
    }

    /**
     * Rate tiers applicable for this rate
     * @return tiers
     **/
    @ApiModelProperty(value = "Rate tiers applicable for this rate")
    @Valid
    public List<BankingProductRateTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<BankingProductRateTier> tiers) {
        this.tiers = tiers;
    }

    public BankingProductLendingRate additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [lendingRateType](#tocSproductlendingratetypedoc) specified. Whether mandatory or not is dependent on the value of [lendingRateType](#tocSproductlendingratetypedoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [lendingRateType](#tocSproductlendingratetypedoc) specified. Whether mandatory or not is dependent on the value of [lendingRateType](#tocSproductlendingratetypedoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductLendingRate additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on the rate.
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on the rate.")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductLendingRate additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this rate
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this rate")

    public String getAdditionalInfoUri() {
        return additionalInfoUri;
    }

    public void setAdditionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingProductLendingRate bankingProductLendingRate = (BankingProductLendingRate) o;
        return Objects.equals(this.lendingRateType, bankingProductLendingRate.lendingRateType) &&
                Objects.equals(this.rate, bankingProductLendingRate.rate) &&
                Objects.equals(this.comparisonRate, bankingProductLendingRate.comparisonRate) &&
                Objects.equals(this.calculationFrequency, bankingProductLendingRate.calculationFrequency) &&
                Objects.equals(this.applicationFrequency, bankingProductLendingRate.applicationFrequency) &&
                Objects.equals(this.interestPaymentDue, bankingProductLendingRate.interestPaymentDue) &&
                Objects.equals(this.tiers, bankingProductLendingRate.tiers) &&
                Objects.equals(this.additionalValue, bankingProductLendingRate.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductLendingRate.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductLendingRate.additionalInfoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lendingRateType, rate, comparisonRate, calculationFrequency, applicationFrequency, interestPaymentDue, tiers, additionalValue, additionalInfo, additionalInfoUri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductLendingRate {\n");

        sb.append("    lendingRateType: ").append(toIndentedString(lendingRateType)).append("\n");
        sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
        sb.append("    comparisonRate: ").append(toIndentedString(comparisonRate)).append("\n");
        sb.append("    calculationFrequency: ").append(toIndentedString(calculationFrequency)).append("\n");
        sb.append("    applicationFrequency: ").append(toIndentedString(applicationFrequency)).append("\n");
        sb.append("    interestPaymentDue: ").append(toIndentedString(interestPaymentDue)).append("\n");
        sb.append("    tiers: ").append(toIndentedString(tiers)).append("\n");
        sb.append("    additionalValue: ").append(toIndentedString(additionalValue)).append("\n");
        sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
        sb.append("    additionalInfoUri: ").append(toIndentedString(additionalInfoUri)).append("\n");
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
