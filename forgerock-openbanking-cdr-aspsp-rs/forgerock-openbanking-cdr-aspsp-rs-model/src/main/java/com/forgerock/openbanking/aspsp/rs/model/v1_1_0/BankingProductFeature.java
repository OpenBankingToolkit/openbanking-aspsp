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
 * BankingProductFeature
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class BankingProductFeature {
    /**
     * The type of feature described
     */
    public enum FeatureTypeEnum {
        CARD_ACCESS("CARD_ACCESS"),

        ADDITIONAL_CARDS("ADDITIONAL_CARDS"),

        UNLIMITED_TXNS("UNLIMITED_TXNS"),

        FREE_TXNS("FREE_TXNS"),

        FREE_TXNS_ALLOWANCE("FREE_TXNS_ALLOWANCE"),

        LOYALTY_PROGRAM("LOYALTY_PROGRAM"),

        OFFSET("OFFSET"),

        OVERDRAFT("OVERDRAFT"),

        REDRAW("REDRAW"),

        INSURANCE("INSURANCE"),

        BALANCE_TRANSFERS("BALANCE_TRANSFERS"),

        INTEREST_FREE("INTEREST_FREE"),

        INTEREST_FREE_TRANSFERS("INTEREST_FREE_TRANSFERS"),

        DIGITAL_WALLET("DIGITAL_WALLET"),

        DIGITAL_BANKING("DIGITAL_BANKING"),

        NPP_PAYID("NPP_PAYID"),

        NPP_ENABLED("NPP_ENABLED"),

        DONATE_INTEREST("DONATE_INTEREST"),

        BILL_PAYMENT("BILL_PAYMENT"),

        COMPLEMENTARY_PRODUCT_DISCOUNTS("COMPLEMENTARY_PRODUCT_DISCOUNTS"),

        BONUS_REWARDS("BONUS_REWARDS"),

        NOTIFICATIONS("NOTIFICATIONS"),

        OTHER("OTHER");

        private String value;

        FeatureTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static FeatureTypeEnum fromValue(String text) {
            for (FeatureTypeEnum b : FeatureTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("featureType")
    private FeatureTypeEnum featureType = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    public BankingProductFeature featureType(FeatureTypeEnum featureType) {
        this.featureType = featureType;
        return this;
    }

    /**
     * The type of feature described
     * @return featureType
     **/
    @ApiModelProperty(required = true, value = "The type of feature described")
    @NotNull

    public FeatureTypeEnum getFeatureType() {
        return featureType;
    }

    public void setFeatureType(FeatureTypeEnum featureType) {
        this.featureType = featureType;
    }

    public BankingProductFeature additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [featureType](#tocSproductfeaturetypedoc) specified. Whether mandatory or not is dependent on the value of the [featureType.](#tocSproductfeaturetypedoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [featureType](#tocSproductfeaturetypedoc) specified. Whether mandatory or not is dependent on the value of the [featureType.](#tocSproductfeaturetypedoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductFeature additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on the feature. Mandatory if the [feature type](#tocSproductfeaturetypedoc) is set to OTHER
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on the feature. Mandatory if the [feature type](#tocSproductfeaturetypedoc) is set to OTHER")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductFeature additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this feature
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this feature")

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
        BankingProductFeature bankingProductFeature = (BankingProductFeature) o;
        return Objects.equals(this.featureType, bankingProductFeature.featureType) &&
                Objects.equals(this.additionalValue, bankingProductFeature.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductFeature.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductFeature.additionalInfoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureType, additionalValue, additionalInfo, additionalInfoUri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductFeature {\n");

        sb.append("    featureType: ").append(toIndentedString(featureType)).append("\n");
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
