/**
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
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * DiscoveryOutage
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class DiscoveryOutage   {
  @JsonProperty("outageTime")
  private String outageTime = null;

  @JsonProperty("duration")
  private String duration = null;

  @JsonProperty("isPartial")
  private Boolean isPartial = null;

  @JsonProperty("explanation")
  private String explanation = null;

  public DiscoveryOutage outageTime(String outageTime) {
    this.outageTime = outageTime;
    return this;
  }

  /**
   * Date and time that the outage is scheduled to begin
   * @return outageTime
  **/
  @ApiModelProperty(required = true, value = "Date and time that the outage is scheduled to begin")
  @NotNull

  public String getOutageTime() {
    return outageTime;
  }

  public void setOutageTime(String outageTime) {
    this.outageTime = outageTime;
  }

  public DiscoveryOutage duration(String duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Planned duration of the outage. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)
   * @return duration
  **/
  @ApiModelProperty(required = true, value = "Planned duration of the outage. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations)")
  @NotNull

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public DiscoveryOutage isPartial(Boolean isPartial) {
    this.isPartial = isPartial;
    return this;
  }

  /**
   * Flag that indicates, if present and set to true, that the outage is only partial meaning that only a subset of normally available end points will be affected by the outage
   * @return isPartial
  **/
  @ApiModelProperty(value = "Flag that indicates, if present and set to true, that the outage is only partial meaning that only a subset of normally available end points will be affected by the outage")

  public Boolean isIsPartial() {
    return isPartial;
  }

  public void setIsPartial(Boolean isPartial) {
    this.isPartial = isPartial;
  }

  public DiscoveryOutage explanation(String explanation) {
    this.explanation = explanation;
    return this;
  }

  /**
   * Provides an explanation of the current outage that can be displayed to an end customer
   * @return explanation
  **/
  @ApiModelProperty(required = true, value = "Provides an explanation of the current outage that can be displayed to an end customer")
  @NotNull

  public String getExplanation() {
    return explanation;
  }

  public void setExplanation(String explanation) {
    this.explanation = explanation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiscoveryOutage discoveryOutage = (DiscoveryOutage) o;
    return Objects.equals(this.outageTime, discoveryOutage.outageTime) &&
        Objects.equals(this.duration, discoveryOutage.duration) &&
        Objects.equals(this.isPartial, discoveryOutage.isPartial) &&
        Objects.equals(this.explanation, discoveryOutage.explanation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outageTime, duration, isPartial, explanation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiscoveryOutage {\n");

    sb.append("    outageTime: ").append(toIndentedString(outageTime)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    isPartial: ").append(toIndentedString(isPartial)).append("\n");
    sb.append("    explanation: ").append(toIndentedString(explanation)).append("\n");
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
