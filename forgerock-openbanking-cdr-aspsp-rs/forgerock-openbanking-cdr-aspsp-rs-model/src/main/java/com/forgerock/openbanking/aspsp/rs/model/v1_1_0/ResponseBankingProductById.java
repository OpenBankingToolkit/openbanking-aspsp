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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_0;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * ResponseBankingProductById
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class ResponseBankingProductById {
    @JsonProperty("data")
    private BankingProductDetail data = null;

    @JsonProperty("links")
    private Links links = null;

    @JsonProperty("meta")
    private Meta meta = null;

    public ResponseBankingProductById data(BankingProductDetail data) {
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
    public BankingProductDetail getData() {
        return data;
    }

    public void setData(BankingProductDetail data) {
        this.data = data;
    }

    public ResponseBankingProductById links(Links links) {
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
    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public ResponseBankingProductById meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Get meta
     * @return meta
     **/
    @ApiModelProperty(value = "")

    @Valid
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseBankingProductById responseBankingProductById = (ResponseBankingProductById) o;
        return Objects.equals(this.data, responseBankingProductById.data) &&
                Objects.equals(this.links, responseBankingProductById.links) &&
                Objects.equals(this.meta, responseBankingProductById.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, links, meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseBankingProductById {\n");

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
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
