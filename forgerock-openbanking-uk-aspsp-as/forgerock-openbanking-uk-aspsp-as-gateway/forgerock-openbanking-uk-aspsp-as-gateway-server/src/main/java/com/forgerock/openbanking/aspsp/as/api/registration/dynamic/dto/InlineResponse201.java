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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * InlineResponse201
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-01-06T10:29:41.982Z")




public class InlineResponse201   {
  @JsonProperty("OBClientRegistration1")
  private OBClientRegistration1 obClientRegistration1 = null;

  public InlineResponse201 obClientRegistration1(OBClientRegistration1 obClientRegistration1) {
    this.obClientRegistration1 = obClientRegistration1;
    return this;
  }

  /**
   * Get obClientRegistration1
   * @return obClientRegistration1
  **/
  @ApiModelProperty(value = "")

  @Valid

  public OBClientRegistration1 getObClientRegistration1() {
    return obClientRegistration1;
  }

  public void setObClientRegistration1(OBClientRegistration1 obClientRegistration1) {
    this.obClientRegistration1 = obClientRegistration1;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InlineResponse201 inlineResponse201 = (InlineResponse201) o;
    return Objects.equals(this.obClientRegistration1, inlineResponse201.obClientRegistration1);
  }

  @Override
  public int hashCode() {
    return Objects.hash(obClientRegistration1);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InlineResponse201 {\n");
    
    sb.append("    obClientRegistration1: ").append(toIndentedString(obClientRegistration1)).append("\n");
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

