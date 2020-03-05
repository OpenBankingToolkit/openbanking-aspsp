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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingProductEligibility
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingProductEligibility {
    /**
     * The type of eligibility criteria described.  See the next section for an overview of valid values and their meaning
     */
    public enum EligibilityTypeEnum {
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

        OTHER("OTHER");

        private String value;

        EligibilityTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static EligibilityTypeEnum fromValue(String text) {
            for (EligibilityTypeEnum b : EligibilityTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("eligibilityType")
    private EligibilityTypeEnum eligibilityType = null;

    @JsonProperty("additionalValue")
    private String additionalValue = null;

    @JsonProperty("additionalInfo")
    private String additionalInfo = null;

    @JsonProperty("additionalInfoUri")
    private String additionalInfoUri = null;

    public BankingProductEligibility eligibilityType(EligibilityTypeEnum eligibilityType) {
        this.eligibilityType = eligibilityType;
        return this;
    }

    /**
     * The type of eligibility criteria described.  See the next section for an overview of valid values and their meaning
     * @return eligibilityType
     **/
    @ApiModelProperty(required = true, value = "The type of eligibility criteria described.  See the next section for an overview of valid values and their meaning")
    @NotNull

    public EligibilityTypeEnum getEligibilityType() {
        return eligibilityType;
    }

    public void setEligibilityType(EligibilityTypeEnum eligibilityType) {
        this.eligibilityType = eligibilityType;
    }

    public BankingProductEligibility additionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
        return this;
    }

    /**
     * Generic field containing additional information relevant to the [eligibilityType](#tocSproducteligibilitytypedoc) specified. Whether mandatory or not is dependent on the value of [eligibilityType](#tocSproducteligibilitytypedoc)
     * @return additionalValue
     **/
    @ApiModelProperty(value = "Generic field containing additional information relevant to the [eligibilityType](#tocSproducteligibilitytypedoc) specified. Whether mandatory or not is dependent on the value of [eligibilityType](#tocSproducteligibilitytypedoc)")

    public String getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(String additionalValue) {
        this.additionalValue = additionalValue;
    }

    public BankingProductEligibility additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    /**
     * Display text providing more information on the [eligibility](#tocSproducteligibilitytypedoc) criteria. Mandatory if the field is set to OTHER
     * @return additionalInfo
     **/
    @ApiModelProperty(value = "Display text providing more information on the [eligibility](#tocSproducteligibilitytypedoc) criteria. Mandatory if the field is set to OTHER")

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public BankingProductEligibility additionalInfoUri(String additionalInfoUri) {
        this.additionalInfoUri = additionalInfoUri;
        return this;
    }

    /**
     * Link to a web page with more information on this eligibility criteria
     * @return additionalInfoUri
     **/
    @ApiModelProperty(value = "Link to a web page with more information on this eligibility criteria")

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
        BankingProductEligibility bankingProductEligibility = (BankingProductEligibility) o;
        return Objects.equals(this.eligibilityType, bankingProductEligibility.eligibilityType) &&
                Objects.equals(this.additionalValue, bankingProductEligibility.additionalValue) &&
                Objects.equals(this.additionalInfo, bankingProductEligibility.additionalInfo) &&
                Objects.equals(this.additionalInfoUri, bankingProductEligibility.additionalInfoUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eligibilityType, additionalValue, additionalInfo, additionalInfoUri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductEligibility {\n");

        sb.append("    eligibilityType: ").append(toIndentedString(eligibilityType)).append("\n");
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
