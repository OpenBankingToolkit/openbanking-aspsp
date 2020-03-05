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
 * CommonPhoneNumber
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class CommonPhoneNumber {
    @JsonProperty("isPreferred")
    private Boolean isPreferred = null;

    /**
     * The purpose of the number as specified by the customer
     */
    public enum PurposeEnum {
        MOBILE("MOBILE"),

        HOME("HOME"),

        WORK("WORK"),

        OTHER("OTHER"),

        INTERNATIONAL("INTERNATIONAL"),

        UNSPECIFIED("UNSPECIFIED");

        private String value;

        PurposeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static PurposeEnum fromValue(String text) {
            for (PurposeEnum b : PurposeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("purpose")
    private PurposeEnum purpose = null;

    @JsonProperty("countryCode")
    private String countryCode = null;

    @JsonProperty("areaCode")
    private String areaCode = null;

    @JsonProperty("number")
    private String number = null;

    @JsonProperty("extension")
    private String extension = null;

    @JsonProperty("fullNumber")
    private String fullNumber = null;

    public CommonPhoneNumber isPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
        return this;
    }

    /**
     * May be true for one and only one entry to indicate the preferred phone number. Assumed to be 'false' if not present
     * @return isPreferred
     **/
    @ApiModelProperty(value = "May be true for one and only one entry to indicate the preferred phone number. Assumed to be 'false' if not present")

    public Boolean isIsPreferred() {
        return isPreferred;
    }

    public void setIsPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
    }

    public CommonPhoneNumber purpose(PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * The purpose of the number as specified by the customer
     * @return purpose
     **/
    @ApiModelProperty(required = true, value = "The purpose of the number as specified by the customer")
    @NotNull

    public PurposeEnum getPurpose() {
        return purpose;
    }

    public void setPurpose(PurposeEnum purpose) {
        this.purpose = purpose;
    }

    public CommonPhoneNumber countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    /**
     * If absent, assumed to be Australia (+61). The + should be included
     * @return countryCode
     **/
    @ApiModelProperty(value = "If absent, assumed to be Australia (+61). The + should be included")

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public CommonPhoneNumber areaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    /**
     * Required for non Mobile Phones, if field is present and refers to Australian code - the leading 0 should be omitted.
     * @return areaCode
     **/
    @ApiModelProperty(value = "Required for non Mobile Phones, if field is present and refers to Australian code - the leading 0 should be omitted.")

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public CommonPhoneNumber number(String number) {
        this.number = number;
        return this;
    }

    /**
     * The actual phone number, with leading zeros as appropriate
     * @return number
     **/
    @ApiModelProperty(required = true, value = "The actual phone number, with leading zeros as appropriate")
    @NotNull

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CommonPhoneNumber extension(String extension) {
        this.extension = extension;
        return this;
    }

    /**
     * An extension number (if applicable)
     * @return extension
     **/
    @ApiModelProperty(value = "An extension number (if applicable)")

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public CommonPhoneNumber fullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
        return this;
    }

    /**
     * Fully formatted phone number with country code, area code, number and extension incorporated. Formatted according to section 5.1.4. of [RFC 3966](https://www.ietf.org/rfc/rfc3966.txt)
     * @return fullNumber
     **/
    @ApiModelProperty(required = true, value = "Fully formatted phone number with country code, area code, number and extension incorporated. Formatted according to section 5.1.4. of [RFC 3966](https://www.ietf.org/rfc/rfc3966.txt)")
    @NotNull

    public String getFullNumber() {
        return fullNumber;
    }

    public void setFullNumber(String fullNumber) {
        this.fullNumber = fullNumber;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonPhoneNumber commonPhoneNumber = (CommonPhoneNumber) o;
        return Objects.equals(this.isPreferred, commonPhoneNumber.isPreferred) &&
                Objects.equals(this.purpose, commonPhoneNumber.purpose) &&
                Objects.equals(this.countryCode, commonPhoneNumber.countryCode) &&
                Objects.equals(this.areaCode, commonPhoneNumber.areaCode) &&
                Objects.equals(this.number, commonPhoneNumber.number) &&
                Objects.equals(this.extension, commonPhoneNumber.extension) &&
                Objects.equals(this.fullNumber, commonPhoneNumber.fullNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPreferred, purpose, countryCode, areaCode, number, extension, fullNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonPhoneNumber {\n");

        sb.append("    isPreferred: ").append(toIndentedString(isPreferred)).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
        sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
        sb.append("    areaCode: ").append(toIndentedString(areaCode)).append("\n");
        sb.append("    number: ").append(toIndentedString(number)).append("\n");
        sb.append("    extension: ").append(toIndentedString(extension)).append("\n");
        sb.append("    fullNumber: ").append(toIndentedString(fullNumber)).append("\n");
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
