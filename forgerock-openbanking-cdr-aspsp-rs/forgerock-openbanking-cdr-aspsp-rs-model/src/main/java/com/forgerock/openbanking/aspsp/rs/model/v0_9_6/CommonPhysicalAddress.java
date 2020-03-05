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
import java.util.Objects;

/**
 * CommonPhysicalAddress
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class CommonPhysicalAddress {
    /**
     * The type of address object present
     */
    public enum AddressUTypeEnum {
        SIMPLE("simple"),

        PAF("paf");

        private String value;

        AddressUTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static AddressUTypeEnum fromValue(String text) {
            for (AddressUTypeEnum b : AddressUTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("addressUType")
    private AddressUTypeEnum addressUType = null;

    @JsonProperty("simple")
    private CommonSimpleAddress simple = null;

    @JsonProperty("paf")
    private CommonPAFAddress paf = null;

    public CommonPhysicalAddress addressUType(AddressUTypeEnum addressUType) {
        this.addressUType = addressUType;
        return this;
    }

    /**
     * The type of address object present
     * @return addressUType
     **/
    @ApiModelProperty(required = true, value = "The type of address object present")
    @NotNull

    public AddressUTypeEnum getAddressUType() {
        return addressUType;
    }

    public void setAddressUType(AddressUTypeEnum addressUType) {
        this.addressUType = addressUType;
    }

    public CommonPhysicalAddress simple(CommonSimpleAddress simple) {
        this.simple = simple;
        return this;
    }

    /**
     * Get simple
     * @return simple
     **/
    @ApiModelProperty(value = "")

    @Valid
    public CommonSimpleAddress getSimple() {
        return simple;
    }

    public void setSimple(CommonSimpleAddress simple) {
        this.simple = simple;
    }

    public CommonPhysicalAddress paf(CommonPAFAddress paf) {
        this.paf = paf;
        return this;
    }

    /**
     * Get paf
     * @return paf
     **/
    @ApiModelProperty(value = "")

    @Valid
    public CommonPAFAddress getPaf() {
        return paf;
    }

    public void setPaf(CommonPAFAddress paf) {
        this.paf = paf;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonPhysicalAddress commonPhysicalAddress = (CommonPhysicalAddress) o;
        return Objects.equals(this.addressUType, commonPhysicalAddress.addressUType) &&
                Objects.equals(this.simple, commonPhysicalAddress.simple) &&
                Objects.equals(this.paf, commonPhysicalAddress.paf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressUType, simple, paf);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonPhysicalAddress {\n");

        sb.append("    addressUType: ").append(toIndentedString(addressUType)).append("\n");
        sb.append("    simple: ").append(toIndentedString(simple)).append("\n");
        sb.append("    paf: ").append(toIndentedString(paf)).append("\n");
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
