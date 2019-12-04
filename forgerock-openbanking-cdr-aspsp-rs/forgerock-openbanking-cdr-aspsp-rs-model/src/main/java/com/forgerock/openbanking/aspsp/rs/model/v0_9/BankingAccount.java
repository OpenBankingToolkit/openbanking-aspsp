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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingAccount   {
  @JsonProperty("accountId")
  private String accountId = null;

  @JsonProperty("creationDate")
  private String creationDate = null;

  @JsonProperty("displayName")
  private String displayName = null;

  @JsonProperty("nickname")
  private String nickname = null;

  /**
   * Open or closed status for the account. If not present then OPEN is assumed
   */
  public enum OpenStatusEnum {
    OPEN("OPEN"),
    
    CLOSED("CLOSED");

    private String value;

    OpenStatusEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OpenStatusEnum fromValue(String text) {
      for (OpenStatusEnum b : OpenStatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("openStatus")
  private OpenStatusEnum openStatus = OpenStatusEnum.OPEN;

  @JsonProperty("isOwned")
  private Boolean isOwned = true;

  @JsonProperty("maskedNumber")
  private String maskedNumber = null;

  @JsonProperty("productCategory")
  private BankingProductCategory productCategory = null;

  @JsonProperty("productName")
  private String productName = null;

  public BankingAccount accountId(String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * A unique ID of the account adhering to the standards for ID permanence
   * @return accountId
  **/
  @ApiModelProperty(required = true, value = "A unique ID of the account adhering to the standards for ID permanence")
  @NotNull

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public BankingAccount creationDate(String creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * Date that the account was created (if known)
   * @return creationDate
  **/
  @ApiModelProperty(value = "Date that the account was created (if known)")

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public BankingAccount displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * The display name of the account as defined by the bank. This should not incorporate account numbers or PANs. If it does the values should be masked according to the rules of the MaskedAccountString common type.
   * @return displayName
  **/
  @ApiModelProperty(required = true, value = "The display name of the account as defined by the bank. This should not incorporate account numbers or PANs. If it does the values should be masked according to the rules of the MaskedAccountString common type.")
  @NotNull

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public BankingAccount nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * A customer supplied nick name for the account
   * @return nickname
  **/
  @ApiModelProperty(value = "A customer supplied nick name for the account")

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public BankingAccount openStatus(OpenStatusEnum openStatus) {
    this.openStatus = openStatus;
    return this;
  }

  /**
   * Open or closed status for the account. If not present then OPEN is assumed
   * @return openStatus
  **/
  @ApiModelProperty(value = "Open or closed status for the account. If not present then OPEN is assumed")

  public OpenStatusEnum getOpenStatus() {
    return openStatus;
  }

  public void setOpenStatus(OpenStatusEnum openStatus) {
    this.openStatus = openStatus;
  }

  public BankingAccount isOwned(Boolean isOwned) {
    this.isOwned = isOwned;
    return this;
  }

  /**
   * Flag indicating that the customer associated with the authorisation is an owner of the account. Does not indicate sole ownership, however. If not present then 'true' is assumed
   * @return isOwned
  **/
  @ApiModelProperty(value = "Flag indicating that the customer associated with the authorisation is an owner of the account. Does not indicate sole ownership, however. If not present then 'true' is assumed")

  public Boolean isIsOwned() {
    return isOwned;
  }

  public void setIsOwned(Boolean isOwned) {
    this.isOwned = isOwned;
  }

  public BankingAccount maskedNumber(String maskedNumber) {
    this.maskedNumber = maskedNumber;
    return this;
  }

  /**
   * A masked version of the account. Whether BSB/Account Number, Credit Card PAN or another number
   * @return maskedNumber
  **/
  @ApiModelProperty(required = true, value = "A masked version of the account. Whether BSB/Account Number, Credit Card PAN or another number")
  @NotNull

  public String getMaskedNumber() {
    return maskedNumber;
  }

  public void setMaskedNumber(String maskedNumber) {
    this.maskedNumber = maskedNumber;
  }

  public BankingAccount productCategory(BankingProductCategory productCategory) {
    this.productCategory = productCategory;
    return this;
  }

  /**
   * Get productCategory
   * @return productCategory
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public BankingProductCategory getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(BankingProductCategory productCategory) {
    this.productCategory = productCategory;
  }

  public BankingAccount productName(String productName) {
    this.productName = productName;
    return this;
  }

  /**
   * The unique identifier of the account as defined by the account holder (akin to model number for the account)
   * @return productName
  **/
  @ApiModelProperty(required = true, value = "The unique identifier of the account as defined by the account holder (akin to model number for the account)")
  @NotNull

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingAccount bankingAccount = (BankingAccount) o;
    return Objects.equals(this.accountId, bankingAccount.accountId) &&
        Objects.equals(this.creationDate, bankingAccount.creationDate) &&
        Objects.equals(this.displayName, bankingAccount.displayName) &&
        Objects.equals(this.nickname, bankingAccount.nickname) &&
        Objects.equals(this.openStatus, bankingAccount.openStatus) &&
        Objects.equals(this.isOwned, bankingAccount.isOwned) &&
        Objects.equals(this.maskedNumber, bankingAccount.maskedNumber) &&
        Objects.equals(this.productCategory, bankingAccount.productCategory) &&
        Objects.equals(this.productName, bankingAccount.productName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, creationDate, displayName, nickname, openStatus, isOwned, maskedNumber, productCategory, productName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingAccount {\n");

    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    creationDate: ").append(toIndentedString(creationDate)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    openStatus: ").append(toIndentedString(openStatus)).append("\n");
    sb.append("    isOwned: ").append(toIndentedString(isOwned)).append("\n");
    sb.append("    maskedNumber: ").append(toIndentedString(maskedNumber)).append("\n");
    sb.append("    productCategory: ").append(toIndentedString(productCategory)).append("\n");
    sb.append("    productName: ").append(toIndentedString(productName)).append("\n");
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
