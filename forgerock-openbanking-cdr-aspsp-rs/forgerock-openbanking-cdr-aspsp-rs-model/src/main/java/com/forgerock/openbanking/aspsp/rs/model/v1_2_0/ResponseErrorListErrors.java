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
 * ResponseErrorListErrors
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:02:30.647705Z[Europe/London]")
public class ResponseErrorListErrors {
    @JsonProperty("code")
    private String code = null;

    @JsonProperty("title")
    private String title = null;

    @JsonProperty("detail")
    private String detail = null;

    @JsonProperty("meta")
    private Object meta = null;

    public ResponseErrorListErrors code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Must be one of the following: 0001 – Account not able to be found
     * @return code
     **/
    @ApiModelProperty(required = true, value = "Must be one of the following: 0001 – Account not able to be found")
    @NotNull

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ResponseErrorListErrors title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Must be one of the following: Invalid account
     * @return title
     **/
    @ApiModelProperty(required = true, value = "Must be one of the following: Invalid account")
    @NotNull

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResponseErrorListErrors detail(String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * ID of the account not found
     * @return detail
     **/
    @ApiModelProperty(required = true, value = "ID of the account not found")
    @NotNull

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ResponseErrorListErrors meta(Object meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Optional additional data for specific error types
     * @return meta
     **/
    @ApiModelProperty(value = "Optional additional data for specific error types")

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
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
        ResponseErrorListErrors responseErrorListErrors = (ResponseErrorListErrors) o;
        return Objects.equals(this.code, responseErrorListErrors.code) &&
                Objects.equals(this.title, responseErrorListErrors.title) &&
                Objects.equals(this.detail, responseErrorListErrors.detail) &&
                Objects.equals(this.meta, responseErrorListErrors.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, title, detail, meta);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResponseErrorListErrors {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
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
