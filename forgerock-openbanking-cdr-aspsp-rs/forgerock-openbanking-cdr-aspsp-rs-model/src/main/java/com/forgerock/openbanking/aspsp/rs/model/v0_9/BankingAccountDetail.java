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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingAccountDetail
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingAccountDetail extends BankingAccount  {
  @JsonProperty("bsb")
  private String bsb = null;

  @JsonProperty("accountNumber")
  private String accountNumber = null;

  @JsonProperty("bundleName")
  private String bundleName = null;

  /**
   * The type of structure to present account specific fields.
   */
  public enum SpecificAccountUTypeEnum {
    TERMDEPOSIT("termDeposit"),
    
    CREDITCARD("creditCard"),
    
    LOAN("loan");

    private String value;

    SpecificAccountUTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SpecificAccountUTypeEnum fromValue(String text) {
      for (SpecificAccountUTypeEnum b : SpecificAccountUTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("specificAccountUType")
  private SpecificAccountUTypeEnum specificAccountUType = null;

  @JsonProperty("termDeposit")
  private BankingTermDepositAccount termDeposit = null;

  @JsonProperty("creditCard")
  private BankingCreditCardAccount creditCard = null;

  @JsonProperty("loan")
  private BankingLoanAccount loan = null;

  @JsonProperty("depositRate")
  private String depositRate = null;

  @JsonProperty("lendingRate")
  private String lendingRate = null;

  @JsonProperty("depositRates")
  @Valid
  private List<BankingProductDepositRate> depositRates = null;

  @JsonProperty("lendingRates")
  @Valid
  private List<BankingProductLendingRate> lendingRates = null;

  @JsonProperty("features")
  @Valid
  private List<Object> features = null;

  @JsonProperty("fees")
  @Valid
  private List<BankingProductFee> fees = null;

  @JsonProperty("addresses")
  @Valid
  private List<CommonPhysicalAddress> addresses = null;

  public BankingAccountDetail bsb(String bsb) {
    this.bsb = bsb;
    return this;
  }

  /**
   * The unmasked BSB for the account. Is expected to be formatted as digits only with leading zeros included and no punctuation or spaces
   * @return bsb
  **/
  @ApiModelProperty(value = "The unmasked BSB for the account. Is expected to be formatted as digits only with leading zeros included and no punctuation or spaces")

  public String getBsb() {
    return bsb;
  }

  public void setBsb(String bsb) {
    this.bsb = bsb;
  }

  public BankingAccountDetail accountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }

  /**
   * The unmasked account number for the account. Should not be supplied if the account number is a PAN requiring PCI compliance. Is expected to be formatted as digits only with leading zeros included and no punctuation or spaces
   * @return accountNumber
  **/
  @ApiModelProperty(value = "The unmasked account number for the account. Should not be supplied if the account number is a PAN requiring PCI compliance. Is expected to be formatted as digits only with leading zeros included and no punctuation or spaces")

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BankingAccountDetail bundleName(String bundleName) {
    this.bundleName = bundleName;
    return this;
  }

  /**
   * Optional field to indicate if this account is part of a bundle that is providing additional benefit for to the customer
   * @return bundleName
  **/
  @ApiModelProperty(value = "Optional field to indicate if this account is part of a bundle that is providing additional benefit for to the customer")

  public String getBundleName() {
    return bundleName;
  }

  public void setBundleName(String bundleName) {
    this.bundleName = bundleName;
  }

  public BankingAccountDetail specificAccountUType(SpecificAccountUTypeEnum specificAccountUType) {
    this.specificAccountUType = specificAccountUType;
    return this;
  }

  /**
   * The type of structure to present account specific fields.
   * @return specificAccountUType
  **/
  @ApiModelProperty(value = "The type of structure to present account specific fields.")

  public SpecificAccountUTypeEnum getSpecificAccountUType() {
    return specificAccountUType;
  }

  public void setSpecificAccountUType(SpecificAccountUTypeEnum specificAccountUType) {
    this.specificAccountUType = specificAccountUType;
  }

  public BankingAccountDetail termDeposit(BankingTermDepositAccount termDeposit) {
    this.termDeposit = termDeposit;
    return this;
  }

  /**
   * Get termDeposit
   * @return termDeposit
  **/
  @ApiModelProperty(value = "")

  @Valid
  public BankingTermDepositAccount getTermDeposit() {
    return termDeposit;
  }

  public void setTermDeposit(BankingTermDepositAccount termDeposit) {
    this.termDeposit = termDeposit;
  }

  public BankingAccountDetail creditCard(BankingCreditCardAccount creditCard) {
    this.creditCard = creditCard;
    return this;
  }

  /**
   * Get creditCard
   * @return creditCard
  **/
  @ApiModelProperty(value = "")

  @Valid
  public BankingCreditCardAccount getCreditCard() {
    return creditCard;
  }

  public void setCreditCard(BankingCreditCardAccount creditCard) {
    this.creditCard = creditCard;
  }

  public BankingAccountDetail loan(BankingLoanAccount loan) {
    this.loan = loan;
    return this;
  }

  /**
   * Get loan
   * @return loan
  **/
  @ApiModelProperty(value = "")

  @Valid
  public BankingLoanAccount getLoan() {
    return loan;
  }

  public void setLoan(BankingLoanAccount loan) {
    this.loan = loan;
  }

  public BankingAccountDetail depositRate(String depositRate) {
    this.depositRate = depositRate;
    return this;
  }

  /**
   * current rate to calculate interest earned being applied to deposit balances as it stands at the time of the API call
   * @return depositRate
  **/
  @ApiModelProperty(value = "current rate to calculate interest earned being applied to deposit balances as it stands at the time of the API call")

  public String getDepositRate() {
    return depositRate;
  }

  public void setDepositRate(String depositRate) {
    this.depositRate = depositRate;
  }

  public BankingAccountDetail lendingRate(String lendingRate) {
    this.lendingRate = lendingRate;
    return this;
  }

  /**
   * The current rate to calculate interest payable being applied to lending balances as it stands at the time of the API call
   * @return lendingRate
  **/
  @ApiModelProperty(value = "The current rate to calculate interest payable being applied to lending balances as it stands at the time of the API call")

  public String getLendingRate() {
    return lendingRate;
  }

  public void setLendingRate(String lendingRate) {
    this.lendingRate = lendingRate;
  }

  public BankingAccountDetail depositRates(List<BankingProductDepositRate> depositRates) {
    this.depositRates = depositRates;
    return this;
  }

  public BankingAccountDetail addDepositRatesItem(BankingProductDepositRate depositRatesItem) {
    if (this.depositRates == null) {
      this.depositRates = new ArrayList<BankingProductDepositRate>();
    }
    this.depositRates.add(depositRatesItem);
    return this;
  }

  /**
   * Fully described deposit rates for this account based on the equivalent structure in Product Reference
   * @return depositRates
  **/
  @ApiModelProperty(value = "Fully described deposit rates for this account based on the equivalent structure in Product Reference")
  @Valid
  public List<BankingProductDepositRate> getDepositRates() {
    return depositRates;
  }

  public void setDepositRates(List<BankingProductDepositRate> depositRates) {
    this.depositRates = depositRates;
  }

  public BankingAccountDetail lendingRates(List<BankingProductLendingRate> lendingRates) {
    this.lendingRates = lendingRates;
    return this;
  }

  public BankingAccountDetail addLendingRatesItem(BankingProductLendingRate lendingRatesItem) {
    if (this.lendingRates == null) {
      this.lendingRates = new ArrayList<BankingProductLendingRate>();
    }
    this.lendingRates.add(lendingRatesItem);
    return this;
  }

  /**
   * Fully described deposit rates for this account based on the equivalent structure in Product Reference
   * @return lendingRates
  **/
  @ApiModelProperty(value = "Fully described deposit rates for this account based on the equivalent structure in Product Reference")
  @Valid
  public List<BankingProductLendingRate> getLendingRates() {
    return lendingRates;
  }

  public void setLendingRates(List<BankingProductLendingRate> lendingRates) {
    this.lendingRates = lendingRates;
  }

  public BankingAccountDetail features(List<Object> features) {
    this.features = features;
    return this;
  }

  public BankingAccountDetail addFeaturesItem(Object featuresItem) {
    if (this.features == null) {
      this.features = new ArrayList<Object>();
    }
    this.features.add(featuresItem);
    return this;
  }

  /**
   * Array of features of the account based on the equivalent structure in Product Reference with the following additional field
   * @return features
  **/
  @ApiModelProperty(value = "Array of features of the account based on the equivalent structure in Product Reference with the following additional field")

  public List<Object> getFeatures() {
    return features;
  }

  public void setFeatures(List<Object> features) {
    this.features = features;
  }

  public BankingAccountDetail fees(List<BankingProductFee> fees) {
    this.fees = fees;
    return this;
  }

  public BankingAccountDetail addFeesItem(BankingProductFee feesItem) {
    if (this.fees == null) {
      this.fees = new ArrayList<BankingProductFee>();
    }
    this.fees.add(feesItem);
    return this;
  }

  /**
   * Fees and charges applicable to the account based on the equivalent structure in Product Reference
   * @return fees
  **/
  @ApiModelProperty(value = "Fees and charges applicable to the account based on the equivalent structure in Product Reference")
  @Valid
  public List<BankingProductFee> getFees() {
    return fees;
  }

  public void setFees(List<BankingProductFee> fees) {
    this.fees = fees;
  }

  public BankingAccountDetail addresses(List<CommonPhysicalAddress> addresses) {
    this.addresses = addresses;
    return this;
  }

  public BankingAccountDetail addAddressesItem(CommonPhysicalAddress addressesItem) {
    if (this.addresses == null) {
      this.addresses = new ArrayList<CommonPhysicalAddress>();
    }
    this.addresses.add(addressesItem);
    return this;
  }

  /**
   * The addresses for the account to be used for correspondence
   * @return addresses
  **/
  @ApiModelProperty(value = "The addresses for the account to be used for correspondence")
  @Valid
  public List<CommonPhysicalAddress> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<CommonPhysicalAddress> addresses) {
    this.addresses = addresses;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingAccountDetail bankingAccountDetail = (BankingAccountDetail) o;
    return Objects.equals(this.bsb, bankingAccountDetail.bsb) &&
        Objects.equals(this.accountNumber, bankingAccountDetail.accountNumber) &&
        Objects.equals(this.bundleName, bankingAccountDetail.bundleName) &&
        Objects.equals(this.specificAccountUType, bankingAccountDetail.specificAccountUType) &&
        Objects.equals(this.termDeposit, bankingAccountDetail.termDeposit) &&
        Objects.equals(this.creditCard, bankingAccountDetail.creditCard) &&
        Objects.equals(this.loan, bankingAccountDetail.loan) &&
        Objects.equals(this.depositRate, bankingAccountDetail.depositRate) &&
        Objects.equals(this.lendingRate, bankingAccountDetail.lendingRate) &&
        Objects.equals(this.depositRates, bankingAccountDetail.depositRates) &&
        Objects.equals(this.lendingRates, bankingAccountDetail.lendingRates) &&
        Objects.equals(this.features, bankingAccountDetail.features) &&
        Objects.equals(this.fees, bankingAccountDetail.fees) &&
        Objects.equals(this.addresses, bankingAccountDetail.addresses) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bsb, accountNumber, bundleName, specificAccountUType, termDeposit, creditCard, loan, depositRate, lendingRate, depositRates, lendingRates, features, fees, addresses, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingAccountDetail {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    bsb: ").append(toIndentedString(bsb)).append("\n");
    sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
    sb.append("    bundleName: ").append(toIndentedString(bundleName)).append("\n");
    sb.append("    specificAccountUType: ").append(toIndentedString(specificAccountUType)).append("\n");
    sb.append("    termDeposit: ").append(toIndentedString(termDeposit)).append("\n");
    sb.append("    creditCard: ").append(toIndentedString(creditCard)).append("\n");
    sb.append("    loan: ").append(toIndentedString(loan)).append("\n");
    sb.append("    depositRate: ").append(toIndentedString(depositRate)).append("\n");
    sb.append("    lendingRate: ").append(toIndentedString(lendingRate)).append("\n");
    sb.append("    depositRates: ").append(toIndentedString(depositRates)).append("\n");
    sb.append("    lendingRates: ").append(toIndentedString(lendingRates)).append("\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
    sb.append("    fees: ").append(toIndentedString(fees)).append("\n");
    sb.append("    addresses: ").append(toIndentedString(addresses)).append("\n");
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
