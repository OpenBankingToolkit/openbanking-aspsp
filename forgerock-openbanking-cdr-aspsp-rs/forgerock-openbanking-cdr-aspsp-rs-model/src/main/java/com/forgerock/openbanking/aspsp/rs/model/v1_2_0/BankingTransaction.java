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
 * BankingTransaction
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class BankingTransaction {
    @JsonProperty("accountId")
    private String accountId = null;

    @JsonProperty("transactionId")
    private String transactionId = null;

    @JsonProperty("isDetailAvailable")
    private Boolean isDetailAvailable = null;

    /**
     * The type of the transaction
     */
    public enum TypeEnum {
        FEE("FEE"),

        INTEREST_CHARGED("INTEREST_CHARGED"),

        INTEREST_PAID("INTEREST_PAID"),

        TRANSFER_OUTGOING("TRANSFER_OUTGOING"),

        TRANSFER_INCOMING("TRANSFER_INCOMING"),

        PAYMENT("PAYMENT"),

        DIRECT_DEBIT("DIRECT_DEBIT"),

        OTHER("OTHER");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TypeEnum fromValue(String text) {
            for (TypeEnum b : TypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("type")
    private TypeEnum type = null;

    /**
     * Status of the transaction whether pending or posted. Note that there is currently no provision in the standards to guarantee the ability to correlate a pending transaction with an associated posted transaction
     */
    public enum StatusEnum {
        PENDING("PENDING"),

        POSTED("POSTED");

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

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("postingDateTime")
    private String postingDateTime = null;

    @JsonProperty("valueDateTime")
    private String valueDateTime = null;

    @JsonProperty("executionDateTime")
    private String executionDateTime = null;

    @JsonProperty("amount")
    private String amount = null;

    @JsonProperty("currency")
    private String currency = null;

    @JsonProperty("reference")
    private String reference = null;

    @JsonProperty("merchantName")
    private String merchantName = null;

    @JsonProperty("merchantCategoryCode")
    private String merchantCategoryCode = null;

    @JsonProperty("billerCode")
    private String billerCode = null;

    @JsonProperty("billerName")
    private String billerName = null;

    @JsonProperty("crn")
    private String crn = null;

    @JsonProperty("apcaNumber")
    private String apcaNumber = null;

    public BankingTransaction accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * ID of the account for which transactions are provided
     * @return accountId
     **/
    @ApiModelProperty(required = true, value = "ID of the account for which transactions are provided")
    @NotNull

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BankingTransaction transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    /**
     * A unique ID of the transaction adhering to the standards for ID permanence.  This is mandatory (through hashing if necessary) unless there are specific and justifiable technical reasons why a transaction cannot be uniquely identified for a particular account type
     * @return transactionId
     **/
    @ApiModelProperty(value = "A unique ID of the transaction adhering to the standards for ID permanence.  This is mandatory (through hashing if necessary) unless there are specific and justifiable technical reasons why a transaction cannot be uniquely identified for a particular account type")

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BankingTransaction isDetailAvailable(Boolean isDetailAvailable) {
        this.isDetailAvailable = isDetailAvailable;
        return this;
    }

    /**
     * True if extended information is available using the transaction detail end point. False if extended data is not available
     * @return isDetailAvailable
     **/
    @ApiModelProperty(required = true, value = "True if extended information is available using the transaction detail end point. False if extended data is not available")
    @NotNull

    public Boolean isIsDetailAvailable() {
        return isDetailAvailable;
    }

    public void setIsDetailAvailable(Boolean isDetailAvailable) {
        this.isDetailAvailable = isDetailAvailable;
    }

    public BankingTransaction type(TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * The type of the transaction
     * @return type
     **/
    @ApiModelProperty(required = true, value = "The type of the transaction")
    @NotNull

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public BankingTransaction status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Status of the transaction whether pending or posted. Note that there is currently no provision in the standards to guarantee the ability to correlate a pending transaction with an associated posted transaction
     * @return status
     **/
    @ApiModelProperty(required = true, value = "Status of the transaction whether pending or posted. Note that there is currently no provision in the standards to guarantee the ability to correlate a pending transaction with an associated posted transaction")
    @NotNull

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public BankingTransaction description(String description) {
        this.description = description;
        return this;
    }

    /**
     * The transaction description as applied by the financial institution
     * @return description
     **/
    @ApiModelProperty(required = true, value = "The transaction description as applied by the financial institution")
    @NotNull

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankingTransaction postingDateTime(String postingDateTime) {
        this.postingDateTime = postingDateTime;
        return this;
    }

    /**
     * The time the transaction was posted. This field is Mandatory if the transaction has status POSTED.  This is the time that appears on a standard statement
     * @return postingDateTime
     **/
    @ApiModelProperty(value = "The time the transaction was posted. This field is Mandatory if the transaction has status POSTED.  This is the time that appears on a standard statement")

    public String getPostingDateTime() {
        return postingDateTime;
    }

    public void setPostingDateTime(String postingDateTime) {
        this.postingDateTime = postingDateTime;
    }

    public BankingTransaction valueDateTime(String valueDateTime) {
        this.valueDateTime = valueDateTime;
        return this;
    }

    /**
     * Date and time at which assets become available to the account owner in case of a credit entry, or cease to be available to the account owner in case of a debit transaction entry
     * @return valueDateTime
     **/
    @ApiModelProperty(value = "Date and time at which assets become available to the account owner in case of a credit entry, or cease to be available to the account owner in case of a debit transaction entry")

    public String getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(String valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public BankingTransaction executionDateTime(String executionDateTime) {
        this.executionDateTime = executionDateTime;
        return this;
    }

    /**
     * The time the transaction was executed by the originating customer, if available
     * @return executionDateTime
     **/
    @ApiModelProperty(value = "The time the transaction was executed by the originating customer, if available")

    public String getExecutionDateTime() {
        return executionDateTime;
    }

    public void setExecutionDateTime(String executionDateTime) {
        this.executionDateTime = executionDateTime;
    }

    public BankingTransaction amount(String amount) {
        this.amount = amount;
        return this;
    }

    /**
     * The value of the transaction. Negative values mean money was outgoing from the account
     * @return amount
     **/
    @ApiModelProperty(required = true, value = "The value of the transaction. Negative values mean money was outgoing from the account")
    @NotNull

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BankingTransaction currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * The currency for the transaction amount. AUD assumed if not present
     * @return currency
     **/
    @ApiModelProperty(value = "The currency for the transaction amount. AUD assumed if not present")

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BankingTransaction reference(String reference) {
        this.reference = reference;
        return this;
    }

    /**
     * The reference for the transaction provided by the originating institution. Empty string if no data provided
     * @return reference
     **/
    @ApiModelProperty(required = true, value = "The reference for the transaction provided by the originating institution. Empty string if no data provided")
    @NotNull

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BankingTransaction merchantName(String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    /**
     * Name of the merchant for an outgoing payment to a merchant
     * @return merchantName
     **/
    @ApiModelProperty(value = "Name of the merchant for an outgoing payment to a merchant")

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public BankingTransaction merchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
        return this;
    }

    /**
     * The merchant category code (or MCC) for an outgoing payment to a merchant
     * @return merchantCategoryCode
     **/
    @ApiModelProperty(value = "The merchant category code (or MCC) for an outgoing payment to a merchant")

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public BankingTransaction billerCode(String billerCode) {
        this.billerCode = billerCode;
        return this;
    }

    /**
     * BPAY Biller Code for the transaction (if available)
     * @return billerCode
     **/
    @ApiModelProperty(value = "BPAY Biller Code for the transaction (if available)")

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public BankingTransaction billerName(String billerName) {
        this.billerName = billerName;
        return this;
    }

    /**
     * Name of the BPAY biller for the transaction (if available)
     * @return billerName
     **/
    @ApiModelProperty(value = "Name of the BPAY biller for the transaction (if available)")

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public BankingTransaction crn(String crn) {
        this.crn = crn;
        return this;
    }

    /**
     * BPAY CRN for the transaction (if available)
     * @return crn
     **/
    @ApiModelProperty(value = "BPAY CRN for the transaction (if available)")

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public BankingTransaction apcaNumber(String apcaNumber) {
        this.apcaNumber = apcaNumber;
        return this;
    }

    /**
     * 6 Digit APCA number for the initiating institution
     * @return apcaNumber
     **/
    @ApiModelProperty(value = "6 Digit APCA number for the initiating institution")

    public String getApcaNumber() {
        return apcaNumber;
    }

    public void setApcaNumber(String apcaNumber) {
        this.apcaNumber = apcaNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingTransaction bankingTransaction = (BankingTransaction) o;
        return Objects.equals(this.accountId, bankingTransaction.accountId) &&
                Objects.equals(this.transactionId, bankingTransaction.transactionId) &&
                Objects.equals(this.isDetailAvailable, bankingTransaction.isDetailAvailable) &&
                Objects.equals(this.type, bankingTransaction.type) &&
                Objects.equals(this.status, bankingTransaction.status) &&
                Objects.equals(this.description, bankingTransaction.description) &&
                Objects.equals(this.postingDateTime, bankingTransaction.postingDateTime) &&
                Objects.equals(this.valueDateTime, bankingTransaction.valueDateTime) &&
                Objects.equals(this.executionDateTime, bankingTransaction.executionDateTime) &&
                Objects.equals(this.amount, bankingTransaction.amount) &&
                Objects.equals(this.currency, bankingTransaction.currency) &&
                Objects.equals(this.reference, bankingTransaction.reference) &&
                Objects.equals(this.merchantName, bankingTransaction.merchantName) &&
                Objects.equals(this.merchantCategoryCode, bankingTransaction.merchantCategoryCode) &&
                Objects.equals(this.billerCode, bankingTransaction.billerCode) &&
                Objects.equals(this.billerName, bankingTransaction.billerName) &&
                Objects.equals(this.crn, bankingTransaction.crn) &&
                Objects.equals(this.apcaNumber, bankingTransaction.apcaNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, transactionId, isDetailAvailable, type, status, description, postingDateTime, valueDateTime, executionDateTime, amount, currency, reference, merchantName, merchantCategoryCode, billerCode, billerName, crn, apcaNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingTransaction {\n");

        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
        sb.append("    isDetailAvailable: ").append(toIndentedString(isDetailAvailable)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    postingDateTime: ").append(toIndentedString(postingDateTime)).append("\n");
        sb.append("    valueDateTime: ").append(toIndentedString(valueDateTime)).append("\n");
        sb.append("    executionDateTime: ").append(toIndentedString(executionDateTime)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
        sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
        sb.append("    merchantName: ").append(toIndentedString(merchantName)).append("\n");
        sb.append("    merchantCategoryCode: ").append(toIndentedString(merchantCategoryCode)).append("\n");
        sb.append("    billerCode: ").append(toIndentedString(billerCode)).append("\n");
        sb.append("    billerName: ").append(toIndentedString(billerName)).append("\n");
        sb.append("    crn: ").append(toIndentedString(crn)).append("\n");
        sb.append("    apcaNumber: ").append(toIndentedString(apcaNumber)).append("\n");
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
