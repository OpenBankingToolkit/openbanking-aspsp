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
 * ResponseBankingTransactionList
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class ResponseBankingTransactionList   {
  @JsonProperty("data")
  private ResponseBankingTransactionListData data = null;

  @JsonProperty("links")
  private LinksPaginated links = null;

  @JsonProperty("meta")
  private MetaPaginated meta = null;

  public ResponseBankingTransactionList data(ResponseBankingTransactionListData data) {
    this.data = data;
    return this;
  }

  /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public ResponseBankingTransactionListData getData() {
    return data;
  }

  public void setData(ResponseBankingTransactionListData data) {
    this.data = data;
  }

  public ResponseBankingTransactionList links(LinksPaginated links) {
    this.links = links;
    return this;
  }

  /**
   * Get links
   * @return links
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public LinksPaginated getLinks() {
    return links;
  }

  public void setLinks(LinksPaginated links) {
    this.links = links;
  }

  public ResponseBankingTransactionList meta(MetaPaginated meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get meta
   * @return meta
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public MetaPaginated getMeta() {
    return meta;
  }

  public void setMeta(MetaPaginated meta) {
    this.meta = meta;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseBankingTransactionList responseBankingTransactionList = (ResponseBankingTransactionList) o;
    return Objects.equals(this.data, responseBankingTransactionList.data) &&
        Objects.equals(this.links, responseBankingTransactionList.links) &&
        Objects.equals(this.meta, responseBankingTransactionList.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, links, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseBankingTransactionList {\n");

    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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
