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
package com.forgerock.openbanking.aspsp.rs.model.v0_9_6;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * LinksPaginated
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class LinksPaginated {
    @JsonProperty("self")
    private String self = null;

    @JsonProperty("first")
    private String first = null;

    @JsonProperty("prev")
    private String prev = null;

    @JsonProperty("next")
    private String next = null;

    @JsonProperty("last")
    private String last = null;

    public LinksPaginated self(String self) {
        this.self = self;
        return this;
    }

    /**
     * Fully qualified link that generated the current response document
     * @return self
     **/
    @ApiModelProperty(required = true, value = "Fully qualified link that generated the current response document")
    @NotNull

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public LinksPaginated first(String first) {
        this.first = first;
        return this;
    }

    /**
     * URI to the first page of this set. Mandatory if this response is not the first page
     * @return first
     **/
    @ApiModelProperty(value = "URI to the first page of this set. Mandatory if this response is not the first page")

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public LinksPaginated prev(String prev) {
        this.prev = prev;
        return this;
    }

    /**
     * URI to the previous page of this set. Mandatory if this response is not the first page
     * @return prev
     **/
    @ApiModelProperty(value = "URI to the previous page of this set. Mandatory if this response is not the first page")

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public LinksPaginated next(String next) {
        this.next = next;
        return this;
    }

    /**
     * URI to the next page of this set. Mandatory if this response is not the last page
     * @return next
     **/
    @ApiModelProperty(value = "URI to the next page of this set. Mandatory if this response is not the last page")

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public LinksPaginated last(String last) {
        this.last = last;
        return this;
    }

    /**
     * URI to the last page of this set. Mandatory if this response is not the last page
     * @return last
     **/
    @ApiModelProperty(value = "URI to the last page of this set. Mandatory if this response is not the last page")

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinksPaginated linksPaginated = (LinksPaginated) o;
        return Objects.equals(this.self, linksPaginated.self) &&
                Objects.equals(this.first, linksPaginated.first) &&
                Objects.equals(this.prev, linksPaginated.prev) &&
                Objects.equals(this.next, linksPaginated.next) &&
                Objects.equals(this.last, linksPaginated.last);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, first, prev, next, last);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinksPaginated {\n");

        sb.append("    self: ").append(toIndentedString(self)).append("\n");
        sb.append("    first: ").append(toIndentedString(first)).append("\n");
        sb.append("    prev: ").append(toIndentedString(prev)).append("\n");
        sb.append("    next: ").append(toIndentedString(next)).append("\n");
        sb.append("    last: ").append(toIndentedString(last)).append("\n");
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
