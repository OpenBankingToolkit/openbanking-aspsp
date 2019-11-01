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
 * BankingBalancePurse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingBalancePurse   {
  @JsonProperty("amount")
  private String amount = null;

  @JsonProperty("currency")
  private String currency = null;

  public BankingBalancePurse amount(String amount) {
    this.amount = amount;
    return this;
  }

  /**
   * The balance available for this additional currency purse
   * @return amount
  **/
  @ApiModelProperty(required = true, value = "The balance available for this additional currency purse")
  @NotNull

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public BankingBalancePurse currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * The currency for the purse
   * @return currency
  **/
  @ApiModelProperty(value = "The currency for the purse")

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingBalancePurse bankingBalancePurse = (BankingBalancePurse) o;
    return Objects.equals(this.amount, bankingBalancePurse.amount) &&
        Objects.equals(this.currency, bankingBalancePurse.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingBalancePurse {\n");

    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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
