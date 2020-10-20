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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBCashBalance1;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Objects;

/**
 * This class exists purely for migration purposes and should be removed once all clusters have been upgraded to a release of openbanking-reference-implementation
 * containing v3.1.6.
 *
 * <p>
 * Note that Prior to extensive refactoring, there were a series of these "FR" mongo document classes that were named in sequence (e.g. FRAccount2,
 * FRAccount3 etc.). The sequence number was incremented each time there was a new version of the OB model objects they contained. Instead of this, there
 * is now one FR document class (e.g. FRAccount) for each corresponding area of the payments API. Each one contains our own "domain" model object, rather
 * than the OB model ones. This means that if a new OB release adds new fields to an OB object, the fields only need to be added to the our domain objects
 * (and mapped accordingly). There should be no need to create a new version of the "FR" mongo documents and corresponding repositories.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
@Deprecated
public class FRBalance1 {
    private final static NumberFormat FORMAT_AMOUNT = new DecimalFormat("#0.00");

    @Id
    @Indexed
    public String id;
    @Indexed
    public String accountId;
    public OBCashBalance1 balance;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;


    public OBActiveOrHistoricCurrencyAndAmount getCurrencyAndAmount() {
        return getBalance().getAmount();
    }

    public BigDecimal getAmount() {
        return new BigDecimal(getBalance().getAmount().getAmount());
    }

    public String getCurrency() {
        return getBalance().getAmount().getCurrency();
    }

    public OBCreditDebitCode getCreditDebitIndicator() {
        return getBalance().getCreditDebitIndicator();
    }

    public void setAmount(BigDecimal amount) {
        getBalance().getAmount().setAmount(FORMAT_AMOUNT.format(amount));
    }

    public void setCreditDebitIndicator(OBCreditDebitCode code) {
        getBalance().setCreditDebitIndicator(code);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FRBalance1 that = (FRBalance1) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(accountId, that.accountId) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(created, that.created) &&
                Objects.equals(updated, that.updated);
    }

    public int hashCode() {
        return Objects.hash(id, accountId, balance, created, updated);
    }
}
