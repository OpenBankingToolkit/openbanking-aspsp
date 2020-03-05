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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingScheduledPayment
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:11:31.339883Z[Europe/London]")
public class BankingScheduledPayment {
    @JsonProperty("scheduledPaymentId")
    private String scheduledPaymentId = null;

    @JsonProperty("nickname")
    private String nickname = null;

    @JsonProperty("payerReference")
    private String payerReference = null;

    @JsonProperty("payeeReference")
    private String payeeReference = null;

    /**
     * Indicates whether the schedule is currently active. The value SKIP is equivalent to ACTIVE except that the customer has requested the next normal occurrence to be skipped.
     */
    public enum StatusEnum {
        ACTIVE("ACTIVE"),

        SKIP("SKIP"),

        INACTIVE("INACTIVE");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusEnum fromValue(String text) {
            for (StatusEnum b : StatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("status")
    private StatusEnum status = null;

    @JsonProperty("from")
    private BankingScheduledPaymentFrom from = null;

    @JsonProperty("paymentSet")
    @Valid
    private List<BankingScheduledPaymentSet> paymentSet = new ArrayList<BankingScheduledPaymentSet>();

    @JsonProperty("recurrence")
    private BankingScheduledPaymentRecurrence recurrence = null;

    public BankingScheduledPayment scheduledPaymentId(String scheduledPaymentId) {
        this.scheduledPaymentId = scheduledPaymentId;
        return this;
    }

    /**
     * A unique ID of the scheduled payment adhering to the standards for ID permanence
     * @return scheduledPaymentId
     **/
    @ApiModelProperty(required = true, value = "A unique ID of the scheduled payment adhering to the standards for ID permanence")
    @NotNull

    public String getScheduledPaymentId() {
        return scheduledPaymentId;
    }

    public void setScheduledPaymentId(String scheduledPaymentId) {
        this.scheduledPaymentId = scheduledPaymentId;
    }

    public BankingScheduledPayment nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    /**
     * The short display name of the payee as provided by the customer
     * @return nickname
     **/
    @ApiModelProperty(value = "The short display name of the payee as provided by the customer")

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BankingScheduledPayment payerReference(String payerReference) {
        this.payerReference = payerReference;
        return this;
    }

    /**
     * The reference for the transaction that will be used by the originating institution for the purposes of constructing a statement narrative on the payer’s account. Empty string if no data provided
     * @return payerReference
     **/
    @ApiModelProperty(required = true, value = "The reference for the transaction that will be used by the originating institution for the purposes of constructing a statement narrative on the payer’s account. Empty string if no data provided")
    @NotNull

    public String getPayerReference() {
        return payerReference;
    }

    public void setPayerReference(String payerReference) {
        this.payerReference = payerReference;
    }

    public BankingScheduledPayment payeeReference(String payeeReference) {
        this.payeeReference = payeeReference;
        return this;
    }

    /**
     * The reference for the transaction that will be provided by the originating institution. Empty string if no data provided
     * @return payeeReference
     **/
    @ApiModelProperty(required = true, value = "The reference for the transaction that will be provided by the originating institution. Empty string if no data provided")
    @NotNull

    public String getPayeeReference() {
        return payeeReference;
    }

    public void setPayeeReference(String payeeReference) {
        this.payeeReference = payeeReference;
    }

    public BankingScheduledPayment status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Indicates whether the schedule is currently active. The value SKIP is equivalent to ACTIVE except that the customer has requested the next normal occurrence to be skipped.
     * @return status
     **/
    @ApiModelProperty(required = true, value = "Indicates whether the schedule is currently active. The value SKIP is equivalent to ACTIVE except that the customer has requested the next normal occurrence to be skipped.")
    @NotNull

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public BankingScheduledPayment from(BankingScheduledPaymentFrom from) {
        this.from = from;
        return this;
    }

    /**
     * Get from
     * @return from
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public BankingScheduledPaymentFrom getFrom() {
        return from;
    }

    public void setFrom(BankingScheduledPaymentFrom from) {
        this.from = from;
    }

    public BankingScheduledPayment paymentSet(List<BankingScheduledPaymentSet> paymentSet) {
        this.paymentSet = paymentSet;
        return this;
    }

    public BankingScheduledPayment addPaymentSetItem(BankingScheduledPaymentSet paymentSetItem) {
        this.paymentSet.add(paymentSetItem);
        return this;
    }

    /**
     * Get paymentSet
     * @return paymentSet
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Valid
    public List<BankingScheduledPaymentSet> getPaymentSet() {
        return paymentSet;
    }

    public void setPaymentSet(List<BankingScheduledPaymentSet> paymentSet) {
        this.paymentSet = paymentSet;
    }

    public BankingScheduledPayment recurrence(BankingScheduledPaymentRecurrence recurrence) {
        this.recurrence = recurrence;
        return this;
    }

    /**
     * Get recurrence
     * @return recurrence
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public BankingScheduledPaymentRecurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(BankingScheduledPaymentRecurrence recurrence) {
        this.recurrence = recurrence;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingScheduledPayment bankingScheduledPayment = (BankingScheduledPayment) o;
        return Objects.equals(this.scheduledPaymentId, bankingScheduledPayment.scheduledPaymentId) &&
                Objects.equals(this.nickname, bankingScheduledPayment.nickname) &&
                Objects.equals(this.payerReference, bankingScheduledPayment.payerReference) &&
                Objects.equals(this.payeeReference, bankingScheduledPayment.payeeReference) &&
                Objects.equals(this.status, bankingScheduledPayment.status) &&
                Objects.equals(this.from, bankingScheduledPayment.from) &&
                Objects.equals(this.paymentSet, bankingScheduledPayment.paymentSet) &&
                Objects.equals(this.recurrence, bankingScheduledPayment.recurrence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduledPaymentId, nickname, payerReference, payeeReference, status, from, paymentSet, recurrence);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingScheduledPayment {\n");

        sb.append("    scheduledPaymentId: ").append(toIndentedString(scheduledPaymentId)).append("\n");
        sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
        sb.append("    payerReference: ").append(toIndentedString(payerReference)).append("\n");
        sb.append("    payeeReference: ").append(toIndentedString(payeeReference)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    from: ").append(toIndentedString(from)).append("\n");
        sb.append("    paymentSet: ").append(toIndentedString(paymentSet)).append("\n");
        sb.append("    recurrence: ").append(toIndentedString(recurrence)).append("\n");
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
