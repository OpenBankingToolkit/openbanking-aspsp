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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingLoanAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingLoanAccount   {
  @JsonProperty("originalStartDate")
  private String originalStartDate = null;

  @JsonProperty("originalLoanAmount")
  private String originalLoanAmount = null;

  @JsonProperty("originalLoanCurrency")
  private String originalLoanCurrency = null;

  @JsonProperty("loanEndDate")
  private String loanEndDate = null;

  @JsonProperty("nextInstalmentDate")
  private String nextInstalmentDate = null;

  @JsonProperty("minInstalmentAmount")
  private String minInstalmentAmount = null;

  @JsonProperty("minInstalmentCurrency")
  private String minInstalmentCurrency = null;

  @JsonProperty("maxRedraw")
  private String maxRedraw = null;

  @JsonProperty("maxRedrawCurrency")
  private String maxRedrawCurrency = null;

  @JsonProperty("minRedraw")
  private String minRedraw = null;

  @JsonProperty("minRedrawCurrency")
  private String minRedrawCurrency = null;

  @JsonProperty("offsetAccountEnabled")
  private Boolean offsetAccountEnabled = null;

  @JsonProperty("offsetAccountIds")
  @Valid
  private List<String> offsetAccountIds = null;

  /**
   * Options in place for repayments. If absent defaults to PRINCIPAL_AND_INTEREST
   */
  public enum RepaymentTypeEnum {
    INTEREST_ONLY("INTEREST_ONLY"),
    
    PRINCIPAL_AND_INTEREST("PRINCIPAL_AND_INTEREST");

    private String value;

    RepaymentTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RepaymentTypeEnum fromValue(String text) {
      for (RepaymentTypeEnum b : RepaymentTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("repaymentType")
  private RepaymentTypeEnum repaymentType = RepaymentTypeEnum.PRINCIPAL_AND_INTEREST;

  @JsonProperty("repaymentFrequency")
  private String repaymentFrequency = null;

  public BankingLoanAccount originalStartDate(String originalStartDate) {
    this.originalStartDate = originalStartDate;
    return this;
  }

  /**
   * Optional original start date for the loan
   * @return originalStartDate
  **/
  @ApiModelProperty(value = "Optional original start date for the loan")

  public String getOriginalStartDate() {
    return originalStartDate;
  }

  public void setOriginalStartDate(String originalStartDate) {
    this.originalStartDate = originalStartDate;
  }

  public BankingLoanAccount originalLoanAmount(String originalLoanAmount) {
    this.originalLoanAmount = originalLoanAmount;
    return this;
  }

  /**
   * Optional original loan value
   * @return originalLoanAmount
  **/
  @ApiModelProperty(value = "Optional original loan value")

  public String getOriginalLoanAmount() {
    return originalLoanAmount;
  }

  public void setOriginalLoanAmount(String originalLoanAmount) {
    this.originalLoanAmount = originalLoanAmount;
  }

  public BankingLoanAccount originalLoanCurrency(String originalLoanCurrency) {
    this.originalLoanCurrency = originalLoanCurrency;
    return this;
  }

  /**
   * If absent assumes AUD
   * @return originalLoanCurrency
  **/
  @ApiModelProperty(value = "If absent assumes AUD")

  public String getOriginalLoanCurrency() {
    return originalLoanCurrency;
  }

  public void setOriginalLoanCurrency(String originalLoanCurrency) {
    this.originalLoanCurrency = originalLoanCurrency;
  }

  public BankingLoanAccount loanEndDate(String loanEndDate) {
    this.loanEndDate = loanEndDate;
    return this;
  }

  /**
   * Date that the loan is due to be repaid in full
   * @return loanEndDate
  **/
  @ApiModelProperty(required = true, value = "Date that the loan is due to be repaid in full")
  @NotNull

  public String getLoanEndDate() {
    return loanEndDate;
  }

  public void setLoanEndDate(String loanEndDate) {
    this.loanEndDate = loanEndDate;
  }

  public BankingLoanAccount nextInstalmentDate(String nextInstalmentDate) {
    this.nextInstalmentDate = nextInstalmentDate;
    return this;
  }

  /**
   * Next date that an instalment is required
   * @return nextInstalmentDate
  **/
  @ApiModelProperty(required = true, value = "Next date that an instalment is required")
  @NotNull

  public String getNextInstalmentDate() {
    return nextInstalmentDate;
  }

  public void setNextInstalmentDate(String nextInstalmentDate) {
    this.nextInstalmentDate = nextInstalmentDate;
  }

  public BankingLoanAccount minInstalmentAmount(String minInstalmentAmount) {
    this.minInstalmentAmount = minInstalmentAmount;
    return this;
  }

  /**
   * Minimum amount of next instalment
   * @return minInstalmentAmount
  **/
  @ApiModelProperty(value = "Minimum amount of next instalment")

  public String getMinInstalmentAmount() {
    return minInstalmentAmount;
  }

  public void setMinInstalmentAmount(String minInstalmentAmount) {
    this.minInstalmentAmount = minInstalmentAmount;
  }

  public BankingLoanAccount minInstalmentCurrency(String minInstalmentCurrency) {
    this.minInstalmentCurrency = minInstalmentCurrency;
    return this;
  }

  /**
   * If absent assumes AUD
   * @return minInstalmentCurrency
  **/
  @ApiModelProperty(value = "If absent assumes AUD")

  public String getMinInstalmentCurrency() {
    return minInstalmentCurrency;
  }

  public void setMinInstalmentCurrency(String minInstalmentCurrency) {
    this.minInstalmentCurrency = minInstalmentCurrency;
  }

  public BankingLoanAccount maxRedraw(String maxRedraw) {
    this.maxRedraw = maxRedraw;
    return this;
  }

  /**
   * Maximum amount of funds that can be redrawn. If not present redraw is not available even if the feature exists for the account
   * @return maxRedraw
  **/
  @ApiModelProperty(value = "Maximum amount of funds that can be redrawn. If not present redraw is not available even if the feature exists for the account")

  public String getMaxRedraw() {
    return maxRedraw;
  }

  public void setMaxRedraw(String maxRedraw) {
    this.maxRedraw = maxRedraw;
  }

  public BankingLoanAccount maxRedrawCurrency(String maxRedrawCurrency) {
    this.maxRedrawCurrency = maxRedrawCurrency;
    return this;
  }

  /**
   * If absent assumes AUD
   * @return maxRedrawCurrency
  **/
  @ApiModelProperty(value = "If absent assumes AUD")

  public String getMaxRedrawCurrency() {
    return maxRedrawCurrency;
  }

  public void setMaxRedrawCurrency(String maxRedrawCurrency) {
    this.maxRedrawCurrency = maxRedrawCurrency;
  }

  public BankingLoanAccount minRedraw(String minRedraw) {
    this.minRedraw = minRedraw;
    return this;
  }

  /**
   * Minimum redraw amount
   * @return minRedraw
  **/
  @ApiModelProperty(value = "Minimum redraw amount")

  public String getMinRedraw() {
    return minRedraw;
  }

  public void setMinRedraw(String minRedraw) {
    this.minRedraw = minRedraw;
  }

  public BankingLoanAccount minRedrawCurrency(String minRedrawCurrency) {
    this.minRedrawCurrency = minRedrawCurrency;
    return this;
  }

  /**
   * If absent assumes AUD
   * @return minRedrawCurrency
  **/
  @ApiModelProperty(value = "If absent assumes AUD")

  public String getMinRedrawCurrency() {
    return minRedrawCurrency;
  }

  public void setMinRedrawCurrency(String minRedrawCurrency) {
    this.minRedrawCurrency = minRedrawCurrency;
  }

  public BankingLoanAccount offsetAccountEnabled(Boolean offsetAccountEnabled) {
    this.offsetAccountEnabled = offsetAccountEnabled;
    return this;
  }

  /**
   * Set to true if one or more offset accounts are configured for this loan account
   * @return offsetAccountEnabled
  **/
  @ApiModelProperty(value = "Set to true if one or more offset accounts are configured for this loan account")

  public Boolean isOffsetAccountEnabled() {
    return offsetAccountEnabled;
  }

  public void setOffsetAccountEnabled(Boolean offsetAccountEnabled) {
    this.offsetAccountEnabled = offsetAccountEnabled;
  }

  public BankingLoanAccount offsetAccountIds(List<String> offsetAccountIds) {
    this.offsetAccountIds = offsetAccountIds;
    return this;
  }

  public BankingLoanAccount addOffsetAccountIdsItem(String offsetAccountIdsItem) {
    if (this.offsetAccountIds == null) {
      this.offsetAccountIds = new ArrayList<String>();
    }
    this.offsetAccountIds.add(offsetAccountIdsItem);
    return this;
  }

  /**
   * The accountIDs of the configured offset accounts attached to this loan. Only offset accounts that can be accessed under the current authorisation should be included. It is expected behaviour that offsetAccountEnabled is set to true but the offsetAccountIds field is absent or empty. This represents a situation where an offset account exists but details can not be accessed under the current authorisation
   * @return offsetAccountIds
  **/
  @ApiModelProperty(value = "The accountIDs of the configured offset accounts attached to this loan. Only offset accounts that can be accessed under the current authorisation should be included. It is expected behaviour that offsetAccountEnabled is set to true but the offsetAccountIds field is absent or empty. This represents a situation where an offset account exists but details can not be accessed under the current authorisation")

  public List<String> getOffsetAccountIds() {
    return offsetAccountIds;
  }

  public void setOffsetAccountIds(List<String> offsetAccountIds) {
    this.offsetAccountIds = offsetAccountIds;
  }

  public BankingLoanAccount repaymentType(RepaymentTypeEnum repaymentType) {
    this.repaymentType = repaymentType;
    return this;
  }

  /**
   * Options in place for repayments. If absent defaults to PRINCIPAL_AND_INTEREST
   * @return repaymentType
  **/
  @ApiModelProperty(value = "Options in place for repayments. If absent defaults to PRINCIPAL_AND_INTEREST")

  public RepaymentTypeEnum getRepaymentType() {
    return repaymentType;
  }

  public void setRepaymentType(RepaymentTypeEnum repaymentType) {
    this.repaymentType = repaymentType;
  }

  public BankingLoanAccount repaymentFrequency(String repaymentFrequency) {
    this.repaymentFrequency = repaymentFrequency;
    return this;
  }

  /**
   * The expected or required repayment frequency. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)
   * @return repaymentFrequency
  **/
  @ApiModelProperty(required = true, value = "The expected or required repayment frequency. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)")
  @NotNull

  public String getRepaymentFrequency() {
    return repaymentFrequency;
  }

  public void setRepaymentFrequency(String repaymentFrequency) {
    this.repaymentFrequency = repaymentFrequency;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingLoanAccount bankingLoanAccount = (BankingLoanAccount) o;
    return Objects.equals(this.originalStartDate, bankingLoanAccount.originalStartDate) &&
        Objects.equals(this.originalLoanAmount, bankingLoanAccount.originalLoanAmount) &&
        Objects.equals(this.originalLoanCurrency, bankingLoanAccount.originalLoanCurrency) &&
        Objects.equals(this.loanEndDate, bankingLoanAccount.loanEndDate) &&
        Objects.equals(this.nextInstalmentDate, bankingLoanAccount.nextInstalmentDate) &&
        Objects.equals(this.minInstalmentAmount, bankingLoanAccount.minInstalmentAmount) &&
        Objects.equals(this.minInstalmentCurrency, bankingLoanAccount.minInstalmentCurrency) &&
        Objects.equals(this.maxRedraw, bankingLoanAccount.maxRedraw) &&
        Objects.equals(this.maxRedrawCurrency, bankingLoanAccount.maxRedrawCurrency) &&
        Objects.equals(this.minRedraw, bankingLoanAccount.minRedraw) &&
        Objects.equals(this.minRedrawCurrency, bankingLoanAccount.minRedrawCurrency) &&
        Objects.equals(this.offsetAccountEnabled, bankingLoanAccount.offsetAccountEnabled) &&
        Objects.equals(this.offsetAccountIds, bankingLoanAccount.offsetAccountIds) &&
        Objects.equals(this.repaymentType, bankingLoanAccount.repaymentType) &&
        Objects.equals(this.repaymentFrequency, bankingLoanAccount.repaymentFrequency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalStartDate, originalLoanAmount, originalLoanCurrency, loanEndDate, nextInstalmentDate, minInstalmentAmount, minInstalmentCurrency, maxRedraw, maxRedrawCurrency, minRedraw, minRedrawCurrency, offsetAccountEnabled, offsetAccountIds, repaymentType, repaymentFrequency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingLoanAccount {\n");

    sb.append("    originalStartDate: ").append(toIndentedString(originalStartDate)).append("\n");
    sb.append("    originalLoanAmount: ").append(toIndentedString(originalLoanAmount)).append("\n");
    sb.append("    originalLoanCurrency: ").append(toIndentedString(originalLoanCurrency)).append("\n");
    sb.append("    loanEndDate: ").append(toIndentedString(loanEndDate)).append("\n");
    sb.append("    nextInstalmentDate: ").append(toIndentedString(nextInstalmentDate)).append("\n");
    sb.append("    minInstalmentAmount: ").append(toIndentedString(minInstalmentAmount)).append("\n");
    sb.append("    minInstalmentCurrency: ").append(toIndentedString(minInstalmentCurrency)).append("\n");
    sb.append("    maxRedraw: ").append(toIndentedString(maxRedraw)).append("\n");
    sb.append("    maxRedrawCurrency: ").append(toIndentedString(maxRedrawCurrency)).append("\n");
    sb.append("    minRedraw: ").append(toIndentedString(minRedraw)).append("\n");
    sb.append("    minRedrawCurrency: ").append(toIndentedString(minRedrawCurrency)).append("\n");
    sb.append("    offsetAccountEnabled: ").append(toIndentedString(offsetAccountEnabled)).append("\n");
    sb.append("    offsetAccountIds: ").append(toIndentedString(offsetAccountIds)).append("\n");
    sb.append("    repaymentType: ").append(toIndentedString(repaymentType)).append("\n");
    sb.append("    repaymentFrequency: ").append(toIndentedString(repaymentFrequency)).append("\n");
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
