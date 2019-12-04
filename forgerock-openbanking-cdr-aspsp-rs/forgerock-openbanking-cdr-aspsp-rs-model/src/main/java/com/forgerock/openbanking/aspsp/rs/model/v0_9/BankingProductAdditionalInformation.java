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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Object that contains links to additional information on specific topics
 */
@ApiModel(description = "Object that contains links to additional information on specific topics")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingProductAdditionalInformation   {
  @JsonProperty("overviewUri")
  private String overviewUri = null;

  @JsonProperty("termsUri")
  private String termsUri = null;

  @JsonProperty("eligibilityUri")
  private String eligibilityUri = null;

  @JsonProperty("feesAndPricingUri")
  private String feesAndPricingUri = null;

  @JsonProperty("bundleUri")
  private String bundleUri = null;

  public BankingProductAdditionalInformation overviewUri(String overviewUri) {
    this.overviewUri = overviewUri;
    return this;
  }

  /**
   * General overview of the product
   * @return overviewUri
  **/
  @ApiModelProperty(value = "General overview of the product")

  public String getOverviewUri() {
    return overviewUri;
  }

  public void setOverviewUri(String overviewUri) {
    this.overviewUri = overviewUri;
  }

  public BankingProductAdditionalInformation termsUri(String termsUri) {
    this.termsUri = termsUri;
    return this;
  }

  /**
   * Terms and conditions for the product
   * @return termsUri
  **/
  @ApiModelProperty(value = "Terms and conditions for the product")

  public String getTermsUri() {
    return termsUri;
  }

  public void setTermsUri(String termsUri) {
    this.termsUri = termsUri;
  }

  public BankingProductAdditionalInformation eligibilityUri(String eligibilityUri) {
    this.eligibilityUri = eligibilityUri;
    return this;
  }

  /**
   * Eligibility rules and criteria for the product
   * @return eligibilityUri
  **/
  @ApiModelProperty(value = "Eligibility rules and criteria for the product")

  public String getEligibilityUri() {
    return eligibilityUri;
  }

  public void setEligibilityUri(String eligibilityUri) {
    this.eligibilityUri = eligibilityUri;
  }

  public BankingProductAdditionalInformation feesAndPricingUri(String feesAndPricingUri) {
    this.feesAndPricingUri = feesAndPricingUri;
    return this;
  }

  /**
   * Description of fees, pricing, discounts, exemptions and bonuses for the product
   * @return feesAndPricingUri
  **/
  @ApiModelProperty(value = "Description of fees, pricing, discounts, exemptions and bonuses for the product")

  public String getFeesAndPricingUri() {
    return feesAndPricingUri;
  }

  public void setFeesAndPricingUri(String feesAndPricingUri) {
    this.feesAndPricingUri = feesAndPricingUri;
  }

  public BankingProductAdditionalInformation bundleUri(String bundleUri) {
    this.bundleUri = bundleUri;
    return this;
  }

  /**
   * Description of a bundle that this product can be part of
   * @return bundleUri
  **/
  @ApiModelProperty(value = "Description of a bundle that this product can be part of")

  public String getBundleUri() {
    return bundleUri;
  }

  public void setBundleUri(String bundleUri) {
    this.bundleUri = bundleUri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingProductAdditionalInformation bankingProductAdditionalInformation = (BankingProductAdditionalInformation) o;
    return Objects.equals(this.overviewUri, bankingProductAdditionalInformation.overviewUri) &&
        Objects.equals(this.termsUri, bankingProductAdditionalInformation.termsUri) &&
        Objects.equals(this.eligibilityUri, bankingProductAdditionalInformation.eligibilityUri) &&
        Objects.equals(this.feesAndPricingUri, bankingProductAdditionalInformation.feesAndPricingUri) &&
        Objects.equals(this.bundleUri, bankingProductAdditionalInformation.bundleUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(overviewUri, termsUri, eligibilityUri, feesAndPricingUri, bundleUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingProductAdditionalInformation {\n");

    sb.append("    overviewUri: ").append(toIndentedString(overviewUri)).append("\n");
    sb.append("    termsUri: ").append(toIndentedString(termsUri)).append("\n");
    sb.append("    eligibilityUri: ").append(toIndentedString(eligibilityUri)).append("\n");
    sb.append("    feesAndPricingUri: ").append(toIndentedString(feesAndPricingUri)).append("\n");
    sb.append("    bundleUri: ").append(toIndentedString(bundleUri)).append("\n");
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
