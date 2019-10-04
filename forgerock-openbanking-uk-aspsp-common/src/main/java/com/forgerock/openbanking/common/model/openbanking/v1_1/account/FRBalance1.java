/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
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
 * Representation of an account. This model is only useful for the demo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FRBalance1 implements FRBalance {
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


    @Override
    public OBActiveOrHistoricCurrencyAndAmount getCurrencyAndAmount() {
        return getBalance().getAmount();
    }

    @Override
    public BigDecimal getAmount() {
        return new BigDecimal(getBalance().getAmount().getAmount());
    }

    @Override
    public String getCurrency() {
        return getBalance().getAmount().getCurrency();
    }

    @Override
    public OBCreditDebitCode getCreditDebitIndicator() {
        return getBalance().getCreditDebitIndicator();
    }

    @Override
    public void setAmount(BigDecimal amount) {
        getBalance().getAmount().setAmount(FORMAT_AMOUNT.format(amount));
    }

    @Override
    public void setCreditDebitIndicator(OBCreditDebitCode code) {
        getBalance().setCreditDebitIndicator(code);
    }

    @Override
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

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, balance, created, updated);
    }
}
