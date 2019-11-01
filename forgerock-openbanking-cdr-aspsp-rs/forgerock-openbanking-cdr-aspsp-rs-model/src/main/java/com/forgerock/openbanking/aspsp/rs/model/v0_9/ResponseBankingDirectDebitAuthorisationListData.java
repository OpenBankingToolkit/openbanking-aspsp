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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ResponseBankingDirectDebitAuthorisationListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingDirectDebitAuthorisationListData   {
  @JsonProperty("directDebitAuthorisations")
  @Valid
  private List<BankingDirectDebit> directDebitAuthorisations = new ArrayList<BankingDirectDebit>();

  public ResponseBankingDirectDebitAuthorisationListData directDebitAuthorisations(List<BankingDirectDebit> directDebitAuthorisations) {
    this.directDebitAuthorisations = directDebitAuthorisations;
    return this;
  }

  public ResponseBankingDirectDebitAuthorisationListData addDirectDebitAuthorisationsItem(BankingDirectDebit directDebitAuthorisationsItem) {
    this.directDebitAuthorisations.add(directDebitAuthorisationsItem);
    return this;
  }

  /**
   * The list of authorisations returned
   * @return directDebitAuthorisations
  **/
  @ApiModelProperty(required = true, value = "The list of authorisations returned")
  @NotNull
  @Valid
  public List<BankingDirectDebit> getDirectDebitAuthorisations() {
    return directDebitAuthorisations;
  }

  public void setDirectDebitAuthorisations(List<BankingDirectDebit> directDebitAuthorisations) {
    this.directDebitAuthorisations = directDebitAuthorisations;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingDirectDebitAuthorisationListData responseBankingDirectDebitAuthorisationListData = (ResponseBankingDirectDebitAuthorisationListData) o;
    return Objects.equals(this.directDebitAuthorisations, responseBankingDirectDebitAuthorisationListData.directDebitAuthorisations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(directDebitAuthorisations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingDirectDebitAuthorisationListData {\n");

    sb.append("    directDebitAuthorisations: ").append(toIndentedString(directDebitAuthorisations)).append("\n");
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
