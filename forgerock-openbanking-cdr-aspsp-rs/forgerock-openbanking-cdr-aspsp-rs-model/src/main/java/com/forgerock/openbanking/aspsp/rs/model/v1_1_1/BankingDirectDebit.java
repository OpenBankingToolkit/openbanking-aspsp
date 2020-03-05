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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingDirectDebit
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:51.111520Z[Europe/London]")
public class BankingDirectDebit {
    @JsonProperty("accountId")
    private String accountId = null;

    @JsonProperty("authorisedEntity")
    private BankingAuthorisedEntity authorisedEntity = null;

    @JsonProperty("lastDebitDateTime")
    private String lastDebitDateTime = null;

    @JsonProperty("lastDebitAmount")
    private String lastDebitAmount = null;

    public BankingDirectDebit accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    /**
     * A unique ID of the account adhering to the standards for ID permanence.
     * @return accountId
     **/
    @ApiModelProperty(required = true, value = "A unique ID of the account adhering to the standards for ID permanence.")
    @NotNull

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BankingDirectDebit authorisedEntity(BankingAuthorisedEntity authorisedEntity) {
        this.authorisedEntity = authorisedEntity;
        return this;
    }

    /**
     * Get authorisedEntity
     * @return authorisedEntity
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public BankingAuthorisedEntity getAuthorisedEntity() {
        return authorisedEntity;
    }

    public void setAuthorisedEntity(BankingAuthorisedEntity authorisedEntity) {
        this.authorisedEntity = authorisedEntity;
    }

    public BankingDirectDebit lastDebitDateTime(String lastDebitDateTime) {
        this.lastDebitDateTime = lastDebitDateTime;
        return this;
    }

    /**
     * The date and time of the last debit executed under this authorisation
     * @return lastDebitDateTime
     **/
    @ApiModelProperty(value = "The date and time of the last debit executed under this authorisation")

    public String getLastDebitDateTime() {
        return lastDebitDateTime;
    }

    public void setLastDebitDateTime(String lastDebitDateTime) {
        this.lastDebitDateTime = lastDebitDateTime;
    }

    public BankingDirectDebit lastDebitAmount(String lastDebitAmount) {
        this.lastDebitAmount = lastDebitAmount;
        return this;
    }

    /**
     * The amount of the last debit executed under this authorisation
     * @return lastDebitAmount
     **/
    @ApiModelProperty(value = "The amount of the last debit executed under this authorisation")

    public String getLastDebitAmount() {
        return lastDebitAmount;
    }

    public void setLastDebitAmount(String lastDebitAmount) {
        this.lastDebitAmount = lastDebitAmount;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingDirectDebit bankingDirectDebit = (BankingDirectDebit) o;
        return Objects.equals(this.accountId, bankingDirectDebit.accountId) &&
                Objects.equals(this.authorisedEntity, bankingDirectDebit.authorisedEntity) &&
                Objects.equals(this.lastDebitDateTime, bankingDirectDebit.lastDebitDateTime) &&
                Objects.equals(this.lastDebitAmount, bankingDirectDebit.lastDebitAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, authorisedEntity, lastDebitDateTime, lastDebitAmount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingDirectDebit {\n");

        sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
        sb.append("    authorisedEntity: ").append(toIndentedString(authorisedEntity)).append("\n");
        sb.append("    lastDebitDateTime: ").append(toIndentedString(lastDebitDateTime)).append("\n");
        sb.append("    lastDebitAmount: ").append(toIndentedString(lastDebitAmount)).append("\n");
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
