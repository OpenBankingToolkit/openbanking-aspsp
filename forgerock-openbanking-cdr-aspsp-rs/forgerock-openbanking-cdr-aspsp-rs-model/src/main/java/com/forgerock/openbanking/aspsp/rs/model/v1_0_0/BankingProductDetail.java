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
package com.forgerock.openbanking.aspsp.rs.model.v1_0_0;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BankingProductDetail
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:11:31.339883Z[Europe/London]")
public class BankingProductDetail extends BankingProduct {
    @JsonProperty("bundles")
    @Valid
    private List<BankingProductBundle> bundles = null;

    @JsonProperty("features")
    @Valid
    private List<BankingProductFeature> features = null;

    @JsonProperty("constraints")
    @Valid
    private List<BankingProductConstraint> constraints = null;

    @JsonProperty("eligibility")
    @Valid
    private List<BankingProductEligibility> eligibility = null;

    @JsonProperty("fees")
    @Valid
    private List<BankingProductFee> fees = null;

    @JsonProperty("depositRates")
    @Valid
    private List<BankingProductDepositRate> depositRates = null;

    @JsonProperty("lendingRates")
    @Valid
    private List<BankingProductLendingRate> lendingRates = null;

    public BankingProductDetail bundles(List<BankingProductBundle> bundles) {
        this.bundles = bundles;
        return this;
    }

    public BankingProductDetail addBundlesItem(BankingProductBundle bundlesItem) {
        if (this.bundles == null) {
            this.bundles = new ArrayList<BankingProductBundle>();
        }
        this.bundles.add(bundlesItem);
        return this;
    }

    /**
     * An array of bundles that this product participates in.  Each bundle is described by free form information but also by a list of product IDs of the other products that are included in the bundle.  It is assumed that the current product is included in the bundle also
     * @return bundles
     **/
    @ApiModelProperty(value = "An array of bundles that this product participates in.  Each bundle is described by free form information but also by a list of product IDs of the other products that are included in the bundle.  It is assumed that the current product is included in the bundle also")
    @Valid
    public List<BankingProductBundle> getBundles() {
        return bundles;
    }

    public void setBundles(List<BankingProductBundle> bundles) {
        this.bundles = bundles;
    }

    public BankingProductDetail features(List<BankingProductFeature> features) {
        this.features = features;
        return this;
    }

    public BankingProductDetail addFeaturesItem(BankingProductFeature featuresItem) {
        if (this.features == null) {
            this.features = new ArrayList<BankingProductFeature>();
        }
        this.features.add(featuresItem);
        return this;
    }

    /**
     * Array of features available for the product
     * @return features
     **/
    @ApiModelProperty(value = "Array of features available for the product")
    @Valid
    public List<BankingProductFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<BankingProductFeature> features) {
        this.features = features;
    }

    public BankingProductDetail constraints(List<BankingProductConstraint> constraints) {
        this.constraints = constraints;
        return this;
    }

    public BankingProductDetail addConstraintsItem(BankingProductConstraint constraintsItem) {
        if (this.constraints == null) {
            this.constraints = new ArrayList<BankingProductConstraint>();
        }
        this.constraints.add(constraintsItem);
        return this;
    }

    /**
     * Constraints on the application for or operation of the product such as minimum balances or limit thresholds
     * @return constraints
     **/
    @ApiModelProperty(value = "Constraints on the application for or operation of the product such as minimum balances or limit thresholds")
    @Valid
    public List<BankingProductConstraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<BankingProductConstraint> constraints) {
        this.constraints = constraints;
    }

    public BankingProductDetail eligibility(List<BankingProductEligibility> eligibility) {
        this.eligibility = eligibility;
        return this;
    }

    public BankingProductDetail addEligibilityItem(BankingProductEligibility eligibilityItem) {
        if (this.eligibility == null) {
            this.eligibility = new ArrayList<BankingProductEligibility>();
        }
        this.eligibility.add(eligibilityItem);
        return this;
    }

    /**
     * Eligibility criteria for the product
     * @return eligibility
     **/
    @ApiModelProperty(value = "Eligibility criteria for the product")
    @Valid
    public List<BankingProductEligibility> getEligibility() {
        return eligibility;
    }

    public void setEligibility(List<BankingProductEligibility> eligibility) {
        this.eligibility = eligibility;
    }

    public BankingProductDetail fees(List<BankingProductFee> fees) {
        this.fees = fees;
        return this;
    }

    public BankingProductDetail addFeesItem(BankingProductFee feesItem) {
        if (this.fees == null) {
            this.fees = new ArrayList<BankingProductFee>();
        }
        this.fees.add(feesItem);
        return this;
    }

    /**
     * Fees applicable for the product
     * @return fees
     **/
    @ApiModelProperty(value = "Fees applicable for the product")
    @Valid
    public List<BankingProductFee> getFees() {
        return fees;
    }

    public void setFees(List<BankingProductFee> fees) {
        this.fees = fees;
    }

    public BankingProductDetail depositRates(List<BankingProductDepositRate> depositRates) {
        this.depositRates = depositRates;
        return this;
    }

    public BankingProductDetail addDepositRatesItem(BankingProductDepositRate depositRatesItem) {
        if (this.depositRates == null) {
            this.depositRates = new ArrayList<BankingProductDepositRate>();
        }
        this.depositRates.add(depositRatesItem);
        return this;
    }

    /**
     * Interest rates available for deposits
     * @return depositRates
     **/
    @ApiModelProperty(value = "Interest rates available for deposits")
    @Valid
    public List<BankingProductDepositRate> getDepositRates() {
        return depositRates;
    }

    public void setDepositRates(List<BankingProductDepositRate> depositRates) {
        this.depositRates = depositRates;
    }

    public BankingProductDetail lendingRates(List<BankingProductLendingRate> lendingRates) {
        this.lendingRates = lendingRates;
        return this;
    }

    public BankingProductDetail addLendingRatesItem(BankingProductLendingRate lendingRatesItem) {
        if (this.lendingRates == null) {
            this.lendingRates = new ArrayList<BankingProductLendingRate>();
        }
        this.lendingRates.add(lendingRatesItem);
        return this;
    }

    /**
     * Interest rates charged against lending balances
     * @return lendingRates
     **/
    @ApiModelProperty(value = "Interest rates charged against lending balances")
    @Valid
    public List<BankingProductLendingRate> getLendingRates() {
        return lendingRates;
    }

    public void setLendingRates(List<BankingProductLendingRate> lendingRates) {
        this.lendingRates = lendingRates;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingProductDetail bankingProductDetail = (BankingProductDetail) o;
        return Objects.equals(this.bundles, bankingProductDetail.bundles) &&
                Objects.equals(this.features, bankingProductDetail.features) &&
                Objects.equals(this.constraints, bankingProductDetail.constraints) &&
                Objects.equals(this.eligibility, bankingProductDetail.eligibility) &&
                Objects.equals(this.fees, bankingProductDetail.fees) &&
                Objects.equals(this.depositRates, bankingProductDetail.depositRates) &&
                Objects.equals(this.lendingRates, bankingProductDetail.lendingRates) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bundles, features, constraints, eligibility, fees, depositRates, lendingRates, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductDetail {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    bundles: ").append(toIndentedString(bundles)).append("\n");
        sb.append("    features: ").append(toIndentedString(features)).append("\n");
        sb.append("    constraints: ").append(toIndentedString(constraints)).append("\n");
        sb.append("    eligibility: ").append(toIndentedString(eligibility)).append("\n");
        sb.append("    fees: ").append(toIndentedString(fees)).append("\n");
        sb.append("    depositRates: ").append(toIndentedString(depositRates)).append("\n");
        sb.append("    lendingRates: ").append(toIndentedString(lendingRates)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
