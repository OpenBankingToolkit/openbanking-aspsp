/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ResponseBankingTransactionListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingTransactionListData   {
  @JsonProperty("transactions")
  @Valid
  private List<BankingTransaction> transactions = new ArrayList<BankingTransaction>();

  public ResponseBankingTransactionListData transactions(List<BankingTransaction> transactions) {
    this.transactions = transactions;
    return this;
  }

  public ResponseBankingTransactionListData addTransactionsItem(BankingTransaction transactionsItem) {
    this.transactions.add(transactionsItem);
    return this;
  }

  /**
   * Get transactions
   * @return transactions
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public List<BankingTransaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<BankingTransaction> transactions) {
    this.transactions = transactions;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingTransactionListData responseBankingTransactionListData = (ResponseBankingTransactionListData) o;
    return Objects.equals(this.transactions, responseBankingTransactionListData.transactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingTransactionListData {\n");

    sb.append("    transactions: ").append(toIndentedString(transactions)).append("\n");
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
