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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * CommonPhysicalAddressWithPurpose
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:11:31.339883Z[Europe/London]")
public class CommonPhysicalAddressWithPurpose extends CommonPhysicalAddress {
    /**
     * Enumeration of values indicating the purpose of the physical address
     */
    public enum PurposeEnum {
        REGISTERED("REGISTERED"),

        MAIL("MAIL"),

        PHYSICAL("PHYSICAL"),

        WORK("WORK"),

        OTHER("OTHER");

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

    public CommonPhysicalAddressWithPurpose purpose(PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Enumeration of values indicating the purpose of the physical address
     * @return purpose
     **/
    @ApiModelProperty(required = true, value = "Enumeration of values indicating the purpose of the physical address")
    @NotNull

    public PurposeEnum getPurpose() {
        return purpose;
    }

    public void setPurpose(PurposeEnum purpose) {
        this.purpose = purpose;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonPhysicalAddressWithPurpose commonPhysicalAddressWithPurpose = (CommonPhysicalAddressWithPurpose) o;
        return Objects.equals(this.purpose, commonPhysicalAddressWithPurpose.purpose) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(purpose, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonPhysicalAddressWithPurpose {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
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
