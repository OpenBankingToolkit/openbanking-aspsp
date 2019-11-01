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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingDomesticPayeeAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingDomesticPayeeAccount   {
  @JsonProperty("accountName")
  private String accountName = null;

  @JsonProperty("bsb")
  private String bsb = null;

  @JsonProperty("accountNumber")
  private String accountNumber = null;

  public BankingDomesticPayeeAccount accountName(String accountName) {
    this.accountName = accountName;
    return this;
  }

  /**
   * Name of the account to pay to
   * @return accountName
  **/
  @ApiModelProperty(required = true, value = "Name of the account to pay to")
  @NotNull

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public BankingDomesticPayeeAccount bsb(String bsb) {
    this.bsb = bsb;
    return this;
  }

  /**
   * BSB of the account to pay to
   * @return bsb
  **/
  @ApiModelProperty(required = true, value = "BSB of the account to pay to")
  @NotNull

  public String getBsb() {
    return bsb;
  }

  public void setBsb(String bsb) {
    this.bsb = bsb;
  }

  public BankingDomesticPayeeAccount accountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * Number of the account to pay to
   * @return accountNumber
  **/
  @ApiModelProperty(required = true, value = "Number of the account to pay to")
  @NotNull

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingDomesticPayeeAccount bankingDomesticPayeeAccount = (BankingDomesticPayeeAccount) o;
    return Objects.equals(this.accountName, bankingDomesticPayeeAccount.accountName) &&
        Objects.equals(this.bsb, bankingDomesticPayeeAccount.bsb) &&
        Objects.equals(this.accountNumber, bankingDomesticPayeeAccount.accountNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountName, bsb, accountNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingDomesticPayeeAccount {\n");

    sb.append("    accountName: ").append(toIndentedString(accountName)).append("\n");
    sb.append("    bsb: ").append(toIndentedString(bsb)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
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
