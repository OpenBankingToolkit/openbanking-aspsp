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
package com.forgerock.openbanking.aspsp.rs.model.v1_2_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * CommonEmailAddress
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class CommonEmailAddress {
    @JsonProperty("isPreferred")
    private Boolean isPreferred = null;

    /**
     * The purpose for the email, as specified by the customer (Enumeration)
     */
    public enum PurposeEnum {
        WORK("WORK"),

        HOME("HOME"),

        OTHER("OTHER"),

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

    @JsonProperty("address")
    private String address = null;

    public CommonEmailAddress isPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
        return this;
    }

    /**
     * May be true for one and only one email record in the collection. Denotes the default email address
     * @return isPreferred
     **/
    @ApiModelProperty(value = "May be true for one and only one email record in the collection. Denotes the default email address")

    public Boolean isIsPreferred() {
        return isPreferred;
    }

    public void setIsPreferred(Boolean isPreferred) {
        this.isPreferred = isPreferred;
    }

    public CommonEmailAddress purpose(PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * The purpose for the email, as specified by the customer (Enumeration)
     * @return purpose
     **/
    @ApiModelProperty(required = true, value = "The purpose for the email, as specified by the customer (Enumeration)")
    @NotNull

    public PurposeEnum getPurpose() {
        return purpose;
    }

    public void setPurpose(PurposeEnum purpose) {
        this.purpose = purpose;
    }

    public CommonEmailAddress address(String address) {
        this.address = address;
        return this;
    }

    /**
     * A correctly formatted email address, as defined by the addr_spec format in [RFC 5322](https://www.ietf.org/rfc/rfc5322.txt)
     * @return address
     **/
    @ApiModelProperty(required = true, value = "A correctly formatted email address, as defined by the addr_spec format in [RFC 5322](https://www.ietf.org/rfc/rfc5322.txt)")
    @NotNull

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonEmailAddress commonEmailAddress = (CommonEmailAddress) o;
        return Objects.equals(this.isPreferred, commonEmailAddress.isPreferred) &&
                Objects.equals(this.purpose, commonEmailAddress.purpose) &&
                Objects.equals(this.address, commonEmailAddress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPreferred, purpose, address);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonEmailAddress {\n");

        sb.append("    isPreferred: ").append(toIndentedString(isPreferred)).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
