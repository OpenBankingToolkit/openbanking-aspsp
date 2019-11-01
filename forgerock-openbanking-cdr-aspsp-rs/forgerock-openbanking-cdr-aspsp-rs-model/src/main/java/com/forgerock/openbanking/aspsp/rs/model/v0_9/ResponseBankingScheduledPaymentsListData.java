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
 * ResponseBankingScheduledPaymentsListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingScheduledPaymentsListData   {
  @JsonProperty("scheduledPayments")
  @Valid
  private List<BankingScheduledPayment> scheduledPayments = new ArrayList<BankingScheduledPayment>();

  public ResponseBankingScheduledPaymentsListData scheduledPayments(List<BankingScheduledPayment> scheduledPayments) {
    this.scheduledPayments = scheduledPayments;
    return this;
  }

  public ResponseBankingScheduledPaymentsListData addScheduledPaymentsItem(BankingScheduledPayment scheduledPaymentsItem) {
    this.scheduledPayments.add(scheduledPaymentsItem);
    return this;
  }

  /**
   * The list of scheduled payments to return
   * @return scheduledPayments
  **/
  @ApiModelProperty(required = true, value = "The list of scheduled payments to return")
  @NotNull
  @Valid
  public List<BankingScheduledPayment> getScheduledPayments() {
    return scheduledPayments;
  }

  public void setScheduledPayments(List<BankingScheduledPayment> scheduledPayments) {
    this.scheduledPayments = scheduledPayments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingScheduledPaymentsListData responseBankingScheduledPaymentsListData = (ResponseBankingScheduledPaymentsListData) o;
    return Objects.equals(this.scheduledPayments, responseBankingScheduledPaymentsListData.scheduledPayments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduledPayments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingScheduledPaymentsListData {\n");

    sb.append("    scheduledPayments: ").append(toIndentedString(scheduledPayments)).append("\n");
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
