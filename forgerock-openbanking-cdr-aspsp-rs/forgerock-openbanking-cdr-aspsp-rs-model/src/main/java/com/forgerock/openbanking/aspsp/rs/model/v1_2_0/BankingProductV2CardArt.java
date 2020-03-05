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

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * BankingProductV2CardArt
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class BankingProductV2CardArt {
    @JsonProperty("title")
    private String title = null;

    @JsonProperty("imageUri")
    private String imageUri = null;

    public BankingProductV2CardArt title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Display label for the specific image
     * @return title
     **/
    @ApiModelProperty(value = "Display label for the specific image")

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BankingProductV2CardArt imageUri(String imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    /**
     * Link to a PNG, JPG or GIF image with proportions defined by ISO 7810 ID-1 and width no greater than 512 pixels
     * @return imageUri
     **/
    @ApiModelProperty(required = true, value = "Link to a PNG, JPG or GIF image with proportions defined by ISO 7810 ID-1 and width no greater than 512 pixels")
    @NotNull

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingProductV2CardArt bankingProductV2CardArt = (BankingProductV2CardArt) o;
        return Objects.equals(this.title, bankingProductV2CardArt.title) &&
                Objects.equals(this.imageUri, bankingProductV2CardArt.imageUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, imageUri);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingProductV2CardArt {\n");

        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    imageUri: ").append(toIndentedString(imageUri)).append("\n");
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
