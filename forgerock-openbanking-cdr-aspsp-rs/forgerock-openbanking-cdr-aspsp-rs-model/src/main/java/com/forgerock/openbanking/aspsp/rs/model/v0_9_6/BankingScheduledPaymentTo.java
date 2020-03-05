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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Object containing details of the destination of the payment. Used to specify a variety of payment destination types
 */
@ApiModel(description = "Object containing details of the destination of the payment. Used to specify a variety of payment destination types")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingScheduledPaymentTo {
    /**
     * The type of object provided that specifies the destination of the funds for the payment.
     */
    public enum ToUTypeEnum {
        ACCOUNTID("accountId"),

        PAYEEID("payeeId"),

        DOMESTIC("domestic"),

        BILLER("biller"),

        INTERNATIONAL("international");

        private String value;

        ToUTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ToUTypeEnum fromValue(String text) {
            for (ToUTypeEnum b : ToUTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("toUType")
    private ToUTypeEnum toUType = null;

    @JsonProperty("accountId")
    private String accountId = null;

    @JsonProperty("payeeId")
    private String payeeId = null;

    @JsonProperty("domestic")
    private BankingDomesticPayee domestic = null;

    @JsonProperty("biller")
    private BankingBillerPayee biller = null;

    @JsonProperty("international")
    private BankingInternationalPayee international = null;

    public BankingScheduledPaymentTo toUType(ToUTypeEnum toUType) {
        this.toUType = toUType;
        return this;
    }

    /**
     * The type of object provided that specifies the destination of the funds for the payment.
     * @return toUType
     **/
    @ApiModelProperty(required = true, value = "The type of object provided that specifies the destination of the funds for the payment.")
    @NotNull

    public ToUTypeEnum getToUType() {
        return toUType;
    }

    public void setToUType(ToUTypeEnum toUType) {
        this.toUType = toUType;
    }

    public BankingScheduledPaymentTo accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * Present if toUType is set to accountId. Indicates that the payment is to another account that is accessible under the current consent
     * @return accountId
     **/
    @ApiModelProperty(value = "Present if toUType is set to accountId. Indicates that the payment is to another account that is accessible under the current consent")

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BankingScheduledPaymentTo payeeId(String payeeId) {
        this.payeeId = payeeId;
        return this;
    }

    /**
     * Present if toUType is set to payeeId. Indicates that the payment is to registered payee that can be accessed using the payee end point. If the Bank Payees scope has not been consented to then a payeeId should not be provided and the full payee details should be provided instead
     * @return payeeId
     **/
    @ApiModelProperty(value = "Present if toUType is set to payeeId. Indicates that the payment is to registered payee that can be accessed using the payee end point. If the Bank Payees scope has not been consented to then a payeeId should not be provided and the full payee details should be provided instead")

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public BankingScheduledPaymentTo domestic(BankingDomesticPayee domestic) {
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

    public BankingScheduledPaymentTo biller(BankingBillerPayee biller) {
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

    public BankingScheduledPaymentTo international(BankingInternationalPayee international) {
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
        BankingScheduledPaymentTo bankingScheduledPaymentTo = (BankingScheduledPaymentTo) o;
        return Objects.equals(this.toUType, bankingScheduledPaymentTo.toUType) &&
                Objects.equals(this.accountId, bankingScheduledPaymentTo.accountId) &&
                Objects.equals(this.payeeId, bankingScheduledPaymentTo.payeeId) &&
                Objects.equals(this.domestic, bankingScheduledPaymentTo.domestic) &&
                Objects.equals(this.biller, bankingScheduledPaymentTo.biller) &&
                Objects.equals(this.international, bankingScheduledPaymentTo.international);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toUType, accountId, payeeId, domestic, biller, international);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingScheduledPaymentTo {\n");

        sb.append("    toUType: ").append(toIndentedString(toUType)).append("\n");
        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    payeeId: ").append(toIndentedString(payeeId)).append("\n");
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
