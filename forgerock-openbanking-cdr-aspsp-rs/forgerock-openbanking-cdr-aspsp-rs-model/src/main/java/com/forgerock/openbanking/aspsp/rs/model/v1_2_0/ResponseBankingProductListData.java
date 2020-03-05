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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ResponseBankingProductListData
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class ResponseBankingProductListData {
    @JsonProperty("products")
    @Valid
    private List<BankingProductV2> products = new ArrayList<BankingProductV2>();

    public ResponseBankingProductListData products(List<BankingProductV2> products) {
        this.products = products;
        return this;
    }

    public ResponseBankingProductListData addProductsItem(BankingProductV2 productsItem) {
        this.products.add(productsItem);
        return this;
    }

    /**
     * The list of products returned.  If the filter results in an empty set then this array may have no records
     * @return products
     **/
    @ApiModelProperty(required = true, value = "The list of products returned.  If the filter results in an empty set then this array may have no records")
    @NotNull
    @Valid
    public List<BankingProductV2> getProducts() {
        return products;
    }

    public void setProducts(List<BankingProductV2> products) {
        this.products = products;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseBankingProductListData responseBankingProductListData = (ResponseBankingProductListData) o;
        return Objects.equals(this.products, responseBankingProductListData.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseBankingProductListData {\n");

        sb.append("    products: ").append(toIndentedString(products)).append("\n");
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
