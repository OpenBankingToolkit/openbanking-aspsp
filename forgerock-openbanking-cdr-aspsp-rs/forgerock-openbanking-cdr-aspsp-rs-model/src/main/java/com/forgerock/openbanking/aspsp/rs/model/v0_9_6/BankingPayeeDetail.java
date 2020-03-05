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
 * BankingPayeeDetail
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingPayeeDetail extends BankingPayee {
    /**
     * Type of object included that describes the payee in detail
     */
    public enum PayeeUTypeEnum {
        DOMESTIC("domestic"),

        BILLER("biller"),

        INTERNATIONAL("international");

        private String value;

        PayeeUTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static PayeeUTypeEnum fromValue(String text) {
            for (PayeeUTypeEnum b : PayeeUTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("payeeUType")
    private PayeeUTypeEnum payeeUType = null;

    @JsonProperty("domestic")
    private BankingDomesticPayee domestic = null;

    @JsonProperty("biller")
    private BankingBillerPayee biller = null;

    @JsonProperty("international")
    private BankingInternationalPayee international = null;

    public BankingPayeeDetail payeeUType(PayeeUTypeEnum payeeUType) {
        this.payeeUType = payeeUType;
        return this;
    }

    /**
     * Type of object included that describes the payee in detail
     * @return payeeUType
     **/
    @ApiModelProperty(required = true, value = "Type of object included that describes the payee in detail")
    @NotNull

    public PayeeUTypeEnum getPayeeUType() {
        return payeeUType;
    }

    public void setPayeeUType(PayeeUTypeEnum payeeUType) {
        this.payeeUType = payeeUType;
    }

    public BankingPayeeDetail domestic(BankingDomesticPayee domestic) {
        this.domestic = domestic;
        return this;
    }

    /**
     * Get domestic
     * @return domestic
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingDomesticPayee getDomestic() {
        return domestic;
    }

    public void setDomestic(BankingDomesticPayee domestic) {
        this.domestic = domestic;
    }

    public BankingPayeeDetail biller(BankingBillerPayee biller) {
        this.biller = biller;
        return this;
    }

    /**
     * Get biller
     * @return biller
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingBillerPayee getBiller() {
        return biller;
    }

    public void setBiller(BankingBillerPayee biller) {
        this.biller = biller;
    }

    public BankingPayeeDetail international(BankingInternationalPayee international) {
        this.international = international;
        return this;
    }

    /**
     * Get international
     * @return international
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingInternationalPayee getInternational() {
        return international;
    }

    public void setInternational(BankingInternationalPayee international) {
        this.international = international;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingPayeeDetail bankingPayeeDetail = (BankingPayeeDetail) o;
        return Objects.equals(this.payeeUType, bankingPayeeDetail.payeeUType) &&
                Objects.equals(this.domestic, bankingPayeeDetail.domestic) &&
                Objects.equals(this.biller, bankingPayeeDetail.biller) &&
                Objects.equals(this.international, bankingPayeeDetail.international) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payeeUType, domestic, biller, international, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingPayeeDetail {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    payeeUType: ").append(toIndentedString(payeeUType)).append("\n");
        sb.append("    domestic: ").append(toIndentedString(domestic)).append("\n");
        sb.append("    biller: ").append(toIndentedString(biller)).append("\n");
        sb.append("    international: ").append(toIndentedString(international)).append("\n");
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
