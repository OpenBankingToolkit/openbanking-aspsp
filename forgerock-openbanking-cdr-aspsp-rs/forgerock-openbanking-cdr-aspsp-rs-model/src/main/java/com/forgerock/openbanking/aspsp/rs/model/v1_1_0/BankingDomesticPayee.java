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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingDomesticPayee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class BankingDomesticPayee {
    /**
     * Type of account object included. Valid values are: { payeeAccountUType - - account A standard Australian account defined by BSB/Account Number payId A PayID recognised by NPP
     */
    public enum PayeeAccountUTypeEnum {
        ACCOUNT("account"),

        CARD("card"),

        PAYID("payId");

        private String value;

        PayeeAccountUTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static PayeeAccountUTypeEnum fromValue(String text) {
            for (PayeeAccountUTypeEnum b : PayeeAccountUTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("payeeAccountUType")
    private PayeeAccountUTypeEnum payeeAccountUType = null;

    @JsonProperty("account")
    private BankingDomesticPayeeAccount account = null;

    @JsonProperty("card")
    private BankingDomesticPayeeCard card = null;

    @JsonProperty("payId")
    private BankingDomesticPayeePayId payId = null;

    public BankingDomesticPayee payeeAccountUType(PayeeAccountUTypeEnum payeeAccountUType) {
        this.payeeAccountUType = payeeAccountUType;
        return this;
    }

    /**
     * Type of account object included. Valid values are: { payeeAccountUType - - account A standard Australian account defined by BSB/Account Number payId A PayID recognised by NPP
     * @return payeeAccountUType
     **/
    @ApiModelProperty(required = true, value = "Type of account object included. Valid values are: { payeeAccountUType - - account A standard Australian account defined by BSB/Account Number payId A PayID recognised by NPP")
    @NotNull

    public PayeeAccountUTypeEnum getPayeeAccountUType() {
        return payeeAccountUType;
    }

    public void setPayeeAccountUType(PayeeAccountUTypeEnum payeeAccountUType) {
        this.payeeAccountUType = payeeAccountUType;
    }

    public BankingDomesticPayee account(BankingDomesticPayeeAccount account) {
        this.account = account;
        return this;
    }

    /**
     * Get account
     * @return account
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingDomesticPayeeAccount getAccount() {
        return account;
    }

    public void setAccount(BankingDomesticPayeeAccount account) {
        this.account = account;
    }

    public BankingDomesticPayee card(BankingDomesticPayeeCard card) {
        this.card = card;
        return this;
    }

    /**
     * Get card
     * @return card
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingDomesticPayeeCard getCard() {
        return card;
    }

    public void setCard(BankingDomesticPayeeCard card) {
        this.card = card;
    }

    public BankingDomesticPayee payId(BankingDomesticPayeePayId payId) {
        this.payId = payId;
        return this;
    }

    /**
     * Get payId
     * @return payId
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingDomesticPayeePayId getPayId() {
        return payId;
    }

    public void setPayId(BankingDomesticPayeePayId payId) {
        this.payId = payId;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingDomesticPayee bankingDomesticPayee = (BankingDomesticPayee) o;
        return Objects.equals(this.payeeAccountUType, bankingDomesticPayee.payeeAccountUType) &&
                Objects.equals(this.account, bankingDomesticPayee.account) &&
                Objects.equals(this.card, bankingDomesticPayee.card) &&
                Objects.equals(this.payId, bankingDomesticPayee.payId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payeeAccountUType, account, card, payId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingDomesticPayee {\n");

        sb.append("    payeeAccountUType: ").append(toIndentedString(payeeAccountUType)).append("\n");
        sb.append("    account: ").append(toIndentedString(account)).append("\n");
        sb.append("    card: ").append(toIndentedString(card)).append("\n");
        sb.append("    payId: ").append(toIndentedString(payId)).append("\n");
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
