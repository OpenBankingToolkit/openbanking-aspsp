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
 * ResponseDiscoveryOutagesListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseDiscoveryOutagesListData   {
  @JsonProperty("outages")
  @Valid
  private List<DiscoveryOutage> outages = new ArrayList<DiscoveryOutage>();

  public ResponseDiscoveryOutagesListData outages(List<DiscoveryOutage> outages) {
    this.outages = outages;
    return this;
  }

  public ResponseDiscoveryOutagesListData addOutagesItem(DiscoveryOutage outagesItem) {
    this.outages.add(outagesItem);
    return this;
  }

  /**
   * List of scheduled outages. Property is mandatory but may contain and empty list if no outages are scheduled
   * @return outages
  **/
  @ApiModelProperty(required = true, value = "List of scheduled outages. Property is mandatory but may contain and empty list if no outages are scheduled")
  @NotNull
  @Valid
  public List<DiscoveryOutage> getOutages() {
    return outages;
  }

  public void setOutages(List<DiscoveryOutage> outages) {
    this.outages = outages;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseDiscoveryOutagesListData responseDiscoveryOutagesListData = (ResponseDiscoveryOutagesListData) o;
    return Objects.equals(this.outages, responseDiscoveryOutagesListData.outages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseDiscoveryOutagesListData {\n");

    sb.append("    outages: ").append(toIndentedString(outages)).append("\n");
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
