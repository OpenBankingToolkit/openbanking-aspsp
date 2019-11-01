/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
 * BankingProductConstraint
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingProductConstraint   {
  /**
   * The type of constraint described.  See the next section for an overview of valid values and their meaning
   */
  public enum ConstraintTypeEnum {
    MIN_BALANCE("MIN_BALANCE"),
    
    MAX_BALANCE("MAX_BALANCE"),
    
    OPENING_BALANCE("OPENING_BALANCE"),
    
    MAX_LIMIT("MAX_LIMIT"),
    
    MIN_LIMIT("MIN_LIMIT");

    private String value;

    ConstraintTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ConstraintTypeEnum fromValue(String text) {
      for (ConstraintTypeEnum b : ConstraintTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("constraintType")
  private ConstraintTypeEnum constraintType = null;

  @JsonProperty("additionalValue")
  private String additionalValue = null;

  @JsonProperty("additionalInfo")
  private String additionalInfo = null;

  @JsonProperty("additionalInfoUri")
  private String additionalInfoUri = null;

  public BankingProductConstraint constraintType(ConstraintTypeEnum constraintType) {
    this.constraintType = constraintType;
    return this;
  }

  /**
   * The type of constraint described.  See the next section for an overview of valid values and their meaning
   * @return constraintType
  **/
  @ApiModelProperty(required = true, value = "The type of constraint described.  See the next section for an overview of valid values and their meaning")
  @NotNull

  public ConstraintTypeEnum getConstraintType() {
    return constraintType;
  }

  public void setConstraintType(ConstraintTypeEnum constraintType) {
    this.constraintType = constraintType;
  }

  public BankingProductConstraint additionalValue(String additionalValue) {
    this.additionalValue = additionalValue;
    return this;
  }

  /**
   * Generic field containing additional information relevant to the [constraintType](#tocSproductconstrainttypedoc) specified.  Whether mandatory or not is dependent on the value of [constraintType](#tocSproductconstrainttypedoc)
   * @return additionalValue
  **/
  @ApiModelProperty(value = "Generic field containing additional information relevant to the [constraintType](#tocSproductconstrainttypedoc) specified.  Whether mandatory or not is dependent on the value of [constraintType](#tocSproductconstrainttypedoc)")

  public String getAdditionalValue() {
    return additionalValue;
  }

  public void setAdditionalValue(String additionalValue) {
    this.additionalValue = additionalValue;
  }

  public BankingProductConstraint additionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
    return this;
  }

  /**
   * Display text providing more information the constraint
   * @return additionalInfo
  **/
  @ApiModelProperty(value = "Display text providing more information the constraint")

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public BankingProductConstraint additionalInfoUri(String additionalInfoUri) {
    this.additionalInfoUri = additionalInfoUri;
    return this;
  }

  /**
   * Link to a web page with more information on the constraint
   * @return additionalInfoUri
  **/
  @ApiModelProperty(value = "Link to a web page with more information on the constraint")

  public String getAdditionalInfoUri() {
    return additionalInfoUri;
  }

  public void setAdditionalInfoUri(String additionalInfoUri) {
    this.additionalInfoUri = additionalInfoUri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingProductConstraint bankingProductConstraint = (BankingProductConstraint) o;
    return Objects.equals(this.constraintType, bankingProductConstraint.constraintType) &&
        Objects.equals(this.additionalValue, bankingProductConstraint.additionalValue) &&
        Objects.equals(this.additionalInfo, bankingProductConstraint.additionalInfo) &&
        Objects.equals(this.additionalInfoUri, bankingProductConstraint.additionalInfoUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(constraintType, additionalValue, additionalInfo, additionalInfoUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingProductConstraint {\n");

    sb.append("    constraintType: ").append(toIndentedString(constraintType)).append("\n");
    sb.append("    additionalValue: ").append(toIndentedString(additionalValue)).append("\n");
    sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
    sb.append("    additionalInfoUri: ").append(toIndentedString(additionalInfoUri)).append("\n");
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
