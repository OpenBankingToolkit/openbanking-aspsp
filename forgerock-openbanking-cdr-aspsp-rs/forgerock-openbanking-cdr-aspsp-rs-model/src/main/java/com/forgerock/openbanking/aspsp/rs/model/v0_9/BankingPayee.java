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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingPayee
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingPayee   {
  @JsonProperty("payeeId")
  private String payeeId = null;

  @JsonProperty("nickname")
  private String nickname = null;

  @JsonProperty("description")
  private String description = null;

  /**
   * The type of payee. DOMESTIC means a registered payee for domestic payments including NPP. INTERNATIONAL means a registered payee for international payments. BILLER means a registered payee for BPAY
   */
  public enum TypeEnum {
    DOMESTIC("DOMESTIC"),
    
    INTERNATIONAL("INTERNATIONAL"),
    
    BILLER("BILLER");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("creationDate")
  private String creationDate = null;

  public BankingPayee payeeId(String payeeId) {
    this.payeeId = payeeId;
    return this;
  }

  /**
   * ID of the payee adhering to the rules of ID permanence
   * @return payeeId
  **/
  @ApiModelProperty(required = true, value = "ID of the payee adhering to the rules of ID permanence")
  @NotNull

  public String getPayeeId() {
    return payeeId;
  }

  public void setPayeeId(String payeeId) {
    this.payeeId = payeeId;
  }

  public BankingPayee nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * The short display name of the payee as provided by the customer
   * @return nickname
  **/
  @ApiModelProperty(required = true, value = "The short display name of the payee as provided by the customer")
  @NotNull

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public BankingPayee description(String description) {
    this.description = description;
    return this;
  }

  /**
   * A description of the payee provided by the customer
   * @return description
  **/
  @ApiModelProperty(value = "A description of the payee provided by the customer")

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BankingPayee type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * The type of payee. DOMESTIC means a registered payee for domestic payments including NPP. INTERNATIONAL means a registered payee for international payments. BILLER means a registered payee for BPAY
   * @return type
  **/
  @ApiModelProperty(required = true, value = "The type of payee. DOMESTIC means a registered payee for domestic payments including NPP. INTERNATIONAL means a registered payee for international payments. BILLER means a registered payee for BPAY")
  @NotNull

  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public BankingPayee creationDate(String creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * The date the payee was created by the customer
   * @return creationDate
  **/
  @ApiModelProperty(value = "The date the payee was created by the customer")

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingPayee bankingPayee = (BankingPayee) o;
    return Objects.equals(this.payeeId, bankingPayee.payeeId) &&
        Objects.equals(this.nickname, bankingPayee.nickname) &&
        Objects.equals(this.description, bankingPayee.description) &&
        Objects.equals(this.type, bankingPayee.type) &&
        Objects.equals(this.creationDate, bankingPayee.creationDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(payeeId, nickname, description, type, creationDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingPayee {\n");

    sb.append("    payeeId: ").append(toIndentedString(payeeId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
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
