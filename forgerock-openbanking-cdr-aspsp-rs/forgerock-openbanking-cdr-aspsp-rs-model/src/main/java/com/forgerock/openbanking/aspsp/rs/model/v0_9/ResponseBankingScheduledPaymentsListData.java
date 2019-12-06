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
