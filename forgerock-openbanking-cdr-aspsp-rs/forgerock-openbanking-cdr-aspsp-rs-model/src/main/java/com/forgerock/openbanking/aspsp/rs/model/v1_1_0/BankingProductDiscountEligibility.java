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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingProductDiscountEligibility
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class BankingProductDiscountEligibility {
    /**
     * The type of the specific eligibility constraint for a discount
     */
    public enum DiscountEligibilityTypeEnum {
        BUSINESS("BUSINESS"),

        PENSION_RECIPIENT("PENSION_RECIPIENT"),

        MIN_AGE("MIN_AGE"),

        MAX_AGE("MAX_AGE"),

        MIN_INCOME("MIN_INCOME"),

        MIN_TURNOVER("MIN_TURNOVER"),

        STAFF("STAFF"),

        STUDENT("STUDENT"),

        EMPLOYMENT_STATUS("EMPLOYMENT_STATUS"),

        RESIDENCY_STATUS("RESIDENCY_STATUS"),

        NATURAL_PERSON("NATURAL_PERSON"),

        INTRODUCTORY("INTRODUCTORY"),

        OTHER("OTHER");

        private String value;

        DiscountEligibilityTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static DiscountEligibilityTypeEnum fromValue(String text) {
            for (DiscountEligibilityTypeEnum b : DiscountEligibilityTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("discountEligibilityType")
    private DiscountEligibilityTypeEnum discountEligibilityType = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    public BankingProductDiscountEligibility discountEligibilityType(DiscountEligibilityTypeEnum discountEligibilityType) {
        this.discountEligibilityType = discountEligibilityType;
        return this;
    }

    /**
     * The type of the specific eligibility constraint for a discount
     * @return discountEligibilityType
     **/
    @ApiModelProperty(required = true, value = "The type of the specific eligibility constraint for a discount")
    @NotNull

    public DiscountEligibilityTypeEnum getDiscountEligibilityType() {
        return discountEligibilityType;
    }

    public void setDiscountEligibilityType(DiscountEligibilityTypeEnum discountEligibilityType) {
        this.discountEligibilityType = discountEligibilityType;
    }

    public BankingProductDiscountEligibility additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [discountEligibilityType](#tocSproductdiscounteligibilitydoc) specified. Whether mandatory or not is dependent on the value of [discountEligibilityType](#tocSproductdiscounteligibilitydoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [discountEligibilityType](#tocSproductdiscounteligibilitydoc) specified. Whether mandatory or not is dependent on the value of [discountEligibilityType](#tocSproductdiscounteligibilitydoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductDiscountEligibility additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on this eligibility constraint
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on this eligibility constraint")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductDiscountEligibility additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this eligibility constraint
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this eligibility constraint")

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
        BankingProductDiscountEligibility bankingProductDiscountEligibility = (BankingProductDiscountEligibility) o;
        return Objects.equals(this.discountEligibilityType, bankingProductDiscountEligibility.discountEligibilityType) &&
                Objects.equals(this.additionalValue, bankingProductDiscountEligibility.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductDiscountEligibility.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductDiscountEligibility.additionalInfoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountEligibilityType, additionalValue, additionalInfo, additionalInfoUri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductDiscountEligibility {\n");

        sb.append("    discountEligibilityType: ").append(toIndentedString(discountEligibilityType)).append("\n");
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
