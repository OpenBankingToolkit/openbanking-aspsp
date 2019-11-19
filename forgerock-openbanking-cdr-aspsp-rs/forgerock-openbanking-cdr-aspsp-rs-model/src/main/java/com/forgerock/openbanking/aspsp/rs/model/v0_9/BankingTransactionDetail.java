/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingTransactionDetail
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingTransactionDetail extends BankingTransaction  {
  @JsonProperty("extendedData")
  private BankingTransactionDetailExtendedData extendedData = null;

  public BankingTransactionDetail extendedData(BankingTransactionDetailExtendedData extendedData) {
    this.extendedData = extendedData;
    return this;
  }

  /**
   * Get extendedData
   * @return extendedData
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public BankingTransactionDetailExtendedData getExtendedData() {
    return extendedData;
  }

  public void setExtendedData(BankingTransactionDetailExtendedData extendedData) {
    this.extendedData = extendedData;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingTransactionDetail bankingTransactionDetail = (BankingTransactionDetail) o;
    return Objects.equals(this.extendedData, bankingTransactionDetail.extendedData) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(extendedData, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingTransactionDetail {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    extendedData: ").append(toIndentedString(extendedData)).append("\n");
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