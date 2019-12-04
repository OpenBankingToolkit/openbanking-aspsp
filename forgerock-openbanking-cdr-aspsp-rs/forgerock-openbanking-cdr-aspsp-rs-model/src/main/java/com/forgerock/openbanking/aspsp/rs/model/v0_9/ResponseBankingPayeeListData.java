/**
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
 * ResponseBankingPayeeListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingPayeeListData   {
  @JsonProperty("payees")
  @Valid
  private List<BankingPayee> payees = new ArrayList<BankingPayee>();

  public ResponseBankingPayeeListData payees(List<BankingPayee> payees) {
    this.payees = payees;
    return this;
  }

  public ResponseBankingPayeeListData addPayeesItem(BankingPayee payeesItem) {
    this.payees.add(payeesItem);
    return this;
  }

  /**
   * The list of payees returned
   * @return payees
  **/
  @ApiModelProperty(required = true, value = "The list of payees returned")
  @NotNull
  @Valid
  public List<BankingPayee> getPayees() {
    return payees;
  }

  public void setPayees(List<BankingPayee> payees) {
    this.payees = payees;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingPayeeListData responseBankingPayeeListData = (ResponseBankingPayeeListData) o;
    return Objects.equals(this.payees, responseBankingPayeeListData.payees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingPayeeListData {\n");

    sb.append("    payees: ").append(toIndentedString(payees)).append("\n");
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
