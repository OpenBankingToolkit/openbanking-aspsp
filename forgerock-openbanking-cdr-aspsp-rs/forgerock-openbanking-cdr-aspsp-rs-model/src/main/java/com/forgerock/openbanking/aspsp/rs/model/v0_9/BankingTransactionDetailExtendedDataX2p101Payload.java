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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingTransactionDetailExtendedDataX2p101Payload
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingTransactionDetailExtendedDataX2p101Payload   {
  @JsonProperty("extendedDescription")
  private String extendedDescription = null;

  @JsonProperty("endToEndId")
  private String endToEndId = null;

  @JsonProperty("purposeCode")
  private String purposeCode = null;

  public BankingTransactionDetailExtendedDataX2p101Payload extendedDescription(String extendedDescription) {
    this.extendedDescription = extendedDescription;
    return this;
  }

  /**
   * An extended string description. Only present if specified by the extensionUType field
   * @return extendedDescription
  **/
  @ApiModelProperty(required = true, value = "An extended string description. Only present if specified by the extensionUType field")
  @NotNull

  public String getExtendedDescription() {
    return extendedDescription;
  }

  public void setExtendedDescription(String extendedDescription) {
    this.extendedDescription = extendedDescription;
  }

  public BankingTransactionDetailExtendedDataX2p101Payload endToEndId(String endToEndId) {
    this.endToEndId = endToEndId;
    return this;
  }

  /**
   * An end to end ID for the payment created at initiation
   * @return endToEndId
  **/
  @ApiModelProperty(value = "An end to end ID for the payment created at initiation")

  public String getEndToEndId() {
    return endToEndId;
  }

  public void setEndToEndId(String endToEndId) {
    this.endToEndId = endToEndId;
  }

  public BankingTransactionDetailExtendedDataX2p101Payload purposeCode(String purposeCode) {
    this.purposeCode = purposeCode;
    return this;
  }

  /**
   * Purpose of the payment.  Format is defined by NPP standards for the x2p1.01 overlay service
   * @return purposeCode
  **/
  @ApiModelProperty(value = "Purpose of the payment.  Format is defined by NPP standards for the x2p1.01 overlay service")

  public String getPurposeCode() {
    return purposeCode;
  }

  public void setPurposeCode(String purposeCode) {
    this.purposeCode = purposeCode;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingTransactionDetailExtendedDataX2p101Payload bankingTransactionDetailExtendedDataX2p101Payload = (BankingTransactionDetailExtendedDataX2p101Payload) o;
    return Objects.equals(this.extendedDescription, bankingTransactionDetailExtendedDataX2p101Payload.extendedDescription) &&
        Objects.equals(this.endToEndId, bankingTransactionDetailExtendedDataX2p101Payload.endToEndId) &&
        Objects.equals(this.purposeCode, bankingTransactionDetailExtendedDataX2p101Payload.purposeCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extendedDescription, endToEndId, purposeCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingTransactionDetailExtendedDataX2p101Payload {\n");

    sb.append("    extendedDescription: ").append(toIndentedString(extendedDescription)).append("\n");
    sb.append("    endToEndId: ").append(toIndentedString(endToEndId)).append("\n");
    sb.append("    purposeCode: ").append(toIndentedString(purposeCode)).append("\n");
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
