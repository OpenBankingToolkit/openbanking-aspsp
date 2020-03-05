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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingBillerPayee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:11:31.339883Z[Europe/London]")
public class BankingBillerPayee {
    @JsonProperty("billerCode")
    private String billerCode = null;

    @JsonProperty("crn")
    private String crn = null;

    @JsonProperty("billerName")
    private String billerName = null;

    public BankingBillerPayee billerCode(String billerCode) {
        this.billerCode = billerCode;
        return this;
    }

    /**
     * BPAY Biller Code of the Biller
     * @return billerCode
     **/
    @ApiModelProperty(required = true, value = "BPAY Biller Code of the Biller")
    @NotNull

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public BankingBillerPayee crn(String crn) {
        this.crn = crn;
        return this;
    }

    /**
     * BPAY CRN of the Biller. If the contents of the CRN match the format of a Credit Card PAN then it should be masked using the rules applicable for the MaskedPANString common type
     * @return crn
     **/
    @ApiModelProperty(value = "BPAY CRN of the Biller. If the contents of the CRN match the format of a Credit Card PAN then it should be masked using the rules applicable for the MaskedPANString common type")

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public BankingBillerPayee billerName(String billerName) {
        this.billerName = billerName;
        return this;
    }

    /**
     * Name of the Biller
     * @return billerName
     **/
    @ApiModelProperty(required = true, value = "Name of the Biller")
    @NotNull

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingBillerPayee bankingBillerPayee = (BankingBillerPayee) o;
        return Objects.equals(this.billerCode, bankingBillerPayee.billerCode) &&
                Objects.equals(this.crn, bankingBillerPayee.crn) &&
                Objects.equals(this.billerName, bankingBillerPayee.billerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billerCode, crn, billerName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingBillerPayee {\n");

        sb.append("    billerCode: ").append(toIndentedString(billerCode)).append("\n");
        sb.append("    crn: ").append(toIndentedString(crn)).append("\n");
        sb.append("    billerName: ").append(toIndentedString(billerName)).append("\n");
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
