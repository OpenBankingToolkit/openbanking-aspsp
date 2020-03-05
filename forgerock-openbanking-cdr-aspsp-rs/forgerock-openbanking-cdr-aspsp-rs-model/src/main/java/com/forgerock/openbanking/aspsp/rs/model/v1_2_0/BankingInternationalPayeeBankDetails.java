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
package com.forgerock.openbanking.aspsp.rs.model.v1_2_0;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingInternationalPayeeBankDetails
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class BankingInternationalPayeeBankDetails {
    @JsonProperty("country")
    private String country = null;

    @JsonProperty("accountNumber")
    private String accountNumber = null;

    @JsonProperty("bankAddress")
    private BankingInternationalPayeeBankDetailsBankAddress bankAddress = null;

    @JsonProperty("beneficiaryBankBIC")
    private String beneficiaryBankBIC = null;

    @JsonProperty("fedWireNumber")
    private String fedWireNumber = null;

    @JsonProperty("sortCode")
    private String sortCode = null;

    @JsonProperty("chipNumber")
    private String chipNumber = null;

    @JsonProperty("routingNumber")
    private String routingNumber = null;

    @JsonProperty("legalEntityIdentifier")
    private String legalEntityIdentifier = null;

    public BankingInternationalPayeeBankDetails country(String country) {
        this.country = country;
        return this;
    }

    /**
     * Country of the recipient institution. A valid [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country code
     * @return country
     **/
    @ApiModelProperty(required = true, value = "Country of the recipient institution. A valid [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country code")
    @NotNull

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BankingInternationalPayeeBankDetails accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    /**
     * Account Targeted for payment
     * @return accountNumber
     **/
    @ApiModelProperty(required = true, value = "Account Targeted for payment")
    @NotNull

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankingInternationalPayeeBankDetails bankAddress(BankingInternationalPayeeBankDetailsBankAddress bankAddress) {
        this.bankAddress = bankAddress;
        return this;
    }

    /**
     * Get bankAddress
     * @return bankAddress
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BankingInternationalPayeeBankDetailsBankAddress getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(BankingInternationalPayeeBankDetailsBankAddress bankAddress) {
        this.bankAddress = bankAddress;
    }

    public BankingInternationalPayeeBankDetails beneficiaryBankBIC(String beneficiaryBankBIC) {
        this.beneficiaryBankBIC = beneficiaryBankBIC;
        return this;
    }

    /**
     * Swift bank code.  Aligns with standard [ISO 9362](https://www.iso.org/standard/60390.html)
     * @return beneficiaryBankBIC
     **/
    @ApiModelProperty(value = "Swift bank code.  Aligns with standard [ISO 9362](https://www.iso.org/standard/60390.html)")

    public String getBeneficiaryBankBIC() {
        return beneficiaryBankBIC;
    }

    public void setBeneficiaryBankBIC(String beneficiaryBankBIC) {
        this.beneficiaryBankBIC = beneficiaryBankBIC;
    }

    public BankingInternationalPayeeBankDetails fedWireNumber(String fedWireNumber) {
        this.fedWireNumber = fedWireNumber;
        return this;
    }

    /**
     * Number for Fedwire payment (Federal Reserve Wire Network)
     * @return fedWireNumber
     **/
    @ApiModelProperty(value = "Number for Fedwire payment (Federal Reserve Wire Network)")

    public String getFedWireNumber() {
        return fedWireNumber;
    }

    public void setFedWireNumber(String fedWireNumber) {
        this.fedWireNumber = fedWireNumber;
    }

    public BankingInternationalPayeeBankDetails sortCode(String sortCode) {
        this.sortCode = sortCode;
        return this;
    }

    /**
     * Sort code used for account identification in some jurisdictions
     * @return sortCode
     **/
    @ApiModelProperty(value = "Sort code used for account identification in some jurisdictions")

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public BankingInternationalPayeeBankDetails chipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
        return this;
    }

    /**
     * Number for the Clearing House Interbank Payments System
     * @return chipNumber
     **/
    @ApiModelProperty(value = "Number for the Clearing House Interbank Payments System")

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }

    public BankingInternationalPayeeBankDetails routingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
        return this;
    }

    /**
     * International bank routing number
     * @return routingNumber
     **/
    @ApiModelProperty(value = "International bank routing number")

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public BankingInternationalPayeeBankDetails legalEntityIdentifier(String legalEntityIdentifier) {
        this.legalEntityIdentifier = legalEntityIdentifier;
        return this;
    }

    /**
     * The legal entity identifier (LEI) for the beneficiary.  Aligns with [ISO 17442](https://www.iso.org/standard/59771.html)
     * @return legalEntityIdentifier
     **/
    @ApiModelProperty(value = "The legal entity identifier (LEI) for the beneficiary.  Aligns with [ISO 17442](https://www.iso.org/standard/59771.html)")

    public String getLegalEntityIdentifier() {
        return legalEntityIdentifier;
    }

    public void setLegalEntityIdentifier(String legalEntityIdentifier) {
        this.legalEntityIdentifier = legalEntityIdentifier;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingInternationalPayeeBankDetails bankingInternationalPayeeBankDetails = (BankingInternationalPayeeBankDetails) o;
        return Objects.equals(this.country, bankingInternationalPayeeBankDetails.country) &&
                Objects.equals(this.accountNumber, bankingInternationalPayeeBankDetails.accountNumber) &&
                Objects.equals(this.bankAddress, bankingInternationalPayeeBankDetails.bankAddress) &&
                Objects.equals(this.beneficiaryBankBIC, bankingInternationalPayeeBankDetails.beneficiaryBankBIC) &&
                Objects.equals(this.fedWireNumber, bankingInternationalPayeeBankDetails.fedWireNumber) &&
                Objects.equals(this.sortCode, bankingInternationalPayeeBankDetails.sortCode) &&
                Objects.equals(this.chipNumber, bankingInternationalPayeeBankDetails.chipNumber) &&
                Objects.equals(this.routingNumber, bankingInternationalPayeeBankDetails.routingNumber) &&
                Objects.equals(this.legalEntityIdentifier, bankingInternationalPayeeBankDetails.legalEntityIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, accountNumber, bankAddress, beneficiaryBankBIC, fedWireNumber, sortCode, chipNumber, routingNumber, legalEntityIdentifier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingInternationalPayeeBankDetails {\n");

        sb.append("    country: ").append(toIndentedString(country)).append("\n");
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    bankAddress: ").append(toIndentedString(bankAddress)).append("\n");
        sb.append("    beneficiaryBankBIC: ").append(toIndentedString(beneficiaryBankBIC)).append("\n");
        sb.append("    fedWireNumber: ").append(toIndentedString(fedWireNumber)).append("\n");
        sb.append("    sortCode: ").append(toIndentedString(sortCode)).append("\n");
        sb.append("    chipNumber: ").append(toIndentedString(chipNumber)).append("\n");
        sb.append("    routingNumber: ").append(toIndentedString(routingNumber)).append("\n");
        sb.append("    legalEntityIdentifier: ").append(toIndentedString(legalEntityIdentifier)).append("\n");
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
