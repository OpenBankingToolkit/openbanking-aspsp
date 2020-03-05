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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingInternationalPayeeBankDetailsBankAddress
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class BankingInternationalPayeeBankDetailsBankAddress {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("address")
    private String address = null;

    public BankingInternationalPayeeBankDetailsBankAddress name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the recipient Bank
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the recipient Bank")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankingInternationalPayeeBankDetailsBankAddress address(String address) {
        this.address = address;
        return this;
    }

    /**
     * Address of the recipient Bank
     * @return address
     **/
    @ApiModelProperty(required = true, value = "Address of the recipient Bank")
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
        BankingInternationalPayeeBankDetailsBankAddress bankingInternationalPayeeBankDetailsBankAddress = (BankingInternationalPayeeBankDetailsBankAddress) o;
        return Objects.equals(this.name, bankingInternationalPayeeBankDetailsBankAddress.name) &&
                Objects.equals(this.address, bankingInternationalPayeeBankDetailsBankAddress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingInternationalPayeeBankDetailsBankAddress {\n");

        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
