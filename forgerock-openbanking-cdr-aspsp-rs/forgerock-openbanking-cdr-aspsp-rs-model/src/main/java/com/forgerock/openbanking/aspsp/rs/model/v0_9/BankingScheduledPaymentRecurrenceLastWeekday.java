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
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Indicates that the schedule of payments is defined according to the last occurrence of a specific weekday in an interval. Mandatory if recurrenceUType is set to lastWeekDay
 */
@ApiModel(description = "Indicates that the schedule of payments is defined according to the last occurrence of a specific weekday in an interval. Mandatory if recurrenceUType is set to lastWeekDay")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class BankingScheduledPaymentRecurrenceLastWeekday   {
  @JsonProperty("finalPaymentDate")
  private String finalPaymentDate = null;

  @JsonProperty("paymentsRemaining")
  private Integer paymentsRemaining = null;

  @JsonProperty("interval")
  private String interval = null;

  @JsonProperty("lastWeekDay")
  private Integer lastWeekDay = null;

  public BankingScheduledPaymentRecurrenceLastWeekday finalPaymentDate(String finalPaymentDate) {
    this.finalPaymentDate = finalPaymentDate;
    return this;
  }

  /**
   * The limit date after which no more payments should be made using this schedule. If both finalPaymentDate and paymentsRemaining are present then payments will stop according to the most constraining value. If neither field is present the payments will continue indefinitely
   * @return finalPaymentDate
  **/
  @ApiModelProperty(value = "The limit date after which no more payments should be made using this schedule. If both finalPaymentDate and paymentsRemaining are present then payments will stop according to the most constraining value. If neither field is present the payments will continue indefinitely")

  public String getFinalPaymentDate() {
    return finalPaymentDate;
  }

  public void setFinalPaymentDate(String finalPaymentDate) {
    this.finalPaymentDate = finalPaymentDate;
  }

  public BankingScheduledPaymentRecurrenceLastWeekday paymentsRemaining(Integer paymentsRemaining) {
    this.paymentsRemaining = paymentsRemaining;
    return this;
  }

  /**
   * Indicates the number of payments remaining in the schedule. If both finalPaymentDate and paymentsRemaining are present then payments will stop according to the most constraining value. If neither field is present the payments will continue indefinitely
   * @return paymentsRemaining
  **/
  @ApiModelProperty(value = "Indicates the number of payments remaining in the schedule. If both finalPaymentDate and paymentsRemaining are present then payments will stop according to the most constraining value. If neither field is present the payments will continue indefinitely")

  public Integer getPaymentsRemaining() {
    return paymentsRemaining;
  }

  public void setPaymentsRemaining(Integer paymentsRemaining) {
    this.paymentsRemaining = paymentsRemaining;
  }

  public BankingScheduledPaymentRecurrenceLastWeekday interval(String interval) {
    this.interval = interval;
    return this;
  }

  /**
   * The interval for the payment. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations) with components less than a day in length ignored. This duration defines the period between payments starting with nextPaymentDate
   * @return interval
  **/
  @ApiModelProperty(required = true, value = "The interval for the payment. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations) with components less than a day in length ignored. This duration defines the period between payments starting with nextPaymentDate")
  @NotNull

  public String getInterval() {
    return interval;
  }

  public void setInterval(String interval) {
    this.interval = interval;
  }

  public BankingScheduledPaymentRecurrenceLastWeekday lastWeekDay(Integer lastWeekDay) {
    this.lastWeekDay = lastWeekDay;
    return this;
  }

  /**
   * The weekDay specified. The payment will occur on the last occurrence of this weekday in the interval. Value is constrained to 1 to 7 with 1 indicating Monday.
   * @return lastWeekDay
  **/
  @ApiModelProperty(required = true, value = "The weekDay specified. The payment will occur on the last occurrence of this weekday in the interval. Value is constrained to 1 to 7 with 1 indicating Monday.")
  @NotNull

  public Integer getLastWeekDay() {
    return lastWeekDay;
  }

  public void setLastWeekDay(Integer lastWeekDay) {
    this.lastWeekDay = lastWeekDay;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankingScheduledPaymentRecurrenceLastWeekday bankingScheduledPaymentRecurrenceLastWeekday = (BankingScheduledPaymentRecurrenceLastWeekday) o;
    return Objects.equals(this.finalPaymentDate, bankingScheduledPaymentRecurrenceLastWeekday.finalPaymentDate) &&
        Objects.equals(this.paymentsRemaining, bankingScheduledPaymentRecurrenceLastWeekday.paymentsRemaining) &&
        Objects.equals(this.interval, bankingScheduledPaymentRecurrenceLastWeekday.interval) &&
        Objects.equals(this.lastWeekDay, bankingScheduledPaymentRecurrenceLastWeekday.lastWeekDay);
  }

  @Override
  public int hashCode() {
    return Objects.hash(finalPaymentDate, paymentsRemaining, interval, lastWeekDay);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankingScheduledPaymentRecurrenceLastWeekday {\n");

    sb.append("    finalPaymentDate: ").append(toIndentedString(finalPaymentDate)).append("\n");
    sb.append("    paymentsRemaining: ").append(toIndentedString(paymentsRemaining)).append("\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    lastWeekDay: ").append(toIndentedString(lastWeekDay)).append("\n");
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
