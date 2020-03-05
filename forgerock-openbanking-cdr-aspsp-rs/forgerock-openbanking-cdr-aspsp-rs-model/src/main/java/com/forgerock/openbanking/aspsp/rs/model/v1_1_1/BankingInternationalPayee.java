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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingInternationalPayee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:51.111520Z[Europe/London]")
public class BankingInternationalPayee {
    @JsonProperty("beneficiaryDetails")
    private Object beneficiaryDetails = null;

    @JsonProperty("bankDetails")
    private Object bankDetails = null;

    public BankingInternationalPayee beneficiaryDetails(Object beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
        return this;
    }

    /**
     * Get beneficiaryDetails
     * @return beneficiaryDetails
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Object getBeneficiaryDetails() {
        return beneficiaryDetails;
    }

    public void setBeneficiaryDetails(Object beneficiaryDetails) {
        this.beneficiaryDetails = beneficiaryDetails;
    }

    public BankingInternationalPayee bankDetails(Object bankDetails) {
        this.bankDetails = bankDetails;
        return this;
    }

    /**
     * Get bankDetails
     * @return bankDetails
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    public Object getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(Object bankDetails) {
        this.bankDetails = bankDetails;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingInternationalPayee bankingInternationalPayee = (BankingInternationalPayee) o;
        return Objects.equals(this.beneficiaryDetails, bankingInternationalPayee.beneficiaryDetails) &&
                Objects.equals(this.bankDetails, bankingInternationalPayee.bankDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beneficiaryDetails, bankDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingInternationalPayee {\n");

        sb.append("    beneficiaryDetails: ").append(toIndentedString(beneficiaryDetails)).append("\n");
        sb.append("    bankDetails: ").append(toIndentedString(bankDetails)).append("\n");
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
