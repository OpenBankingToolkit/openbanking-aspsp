/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Indicates that the payment is a once off payment on a specific future date. Mandatory if recurrenceUType is set to onceOff
 */
@ApiModel(description = "Indicates that the payment is a once off payment on a specific future date. Mandatory if recurrenceUType is set to onceOff")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingScheduledPaymentRecurrenceOnceOff   {
  @JsonProperty("paymentDate")
  private String paymentDate = null;

  public BankingScheduledPaymentRecurrenceOnceOff paymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
    return this;
  }

  /**
   * The scheduled date for the once off payment
   * @return paymentDate
  **/
  @ApiModelProperty(required = true, value = "The scheduled date for the once off payment")
  @NotNull

  public String getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(String paymentDate) {
    this.paymentDate = paymentDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingScheduledPaymentRecurrenceOnceOff bankingScheduledPaymentRecurrenceOnceOff = (BankingScheduledPaymentRecurrenceOnceOff) o;
    return Objects.equals(this.paymentDate, bankingScheduledPaymentRecurrenceOnceOff.paymentDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(paymentDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingScheduledPaymentRecurrenceOnceOff {\n");

    sb.append("    paymentDate: ").append(toIndentedString(paymentDate)).append("\n");
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
