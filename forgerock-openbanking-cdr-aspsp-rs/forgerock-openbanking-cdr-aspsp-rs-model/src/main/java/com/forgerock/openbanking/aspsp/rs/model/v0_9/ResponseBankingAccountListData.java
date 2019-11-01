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
 * ResponseBankingAccountListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingAccountListData   {
  @JsonProperty("accounts")
  @Valid
  private List<BankingAccount> accounts = new ArrayList<BankingAccount>();

  public ResponseBankingAccountListData accounts(List<BankingAccount> accounts) {
    this.accounts = accounts;
    return this;
  }

  public ResponseBankingAccountListData addAccountsItem(BankingAccount accountsItem) {
    this.accounts.add(accountsItem);
    return this;
  }

  /**
   * The list of accounts returned. If the filter results in an empty set then this array may have no records
   * @return accounts
  **/
  @ApiModelProperty(required = true, value = "The list of accounts returned. If the filter results in an empty set then this array may have no records")
  @NotNull
  @Valid
  public List<BankingAccount> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<BankingAccount> accounts) {
    this.accounts = accounts;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingAccountListData responseBankingAccountListData = (ResponseBankingAccountListData) o;
    return Objects.equals(this.accounts, responseBankingAccountListData.accounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accounts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingAccountListData {\n");

    sb.append("    accounts: ").append(toIndentedString(accounts)).append("\n");
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
