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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class BankingScheduledPaymentRecurrenceLastWeekday {
    @JsonProperty("finalPaymentDate")
    private String finalPaymentDate = null;

    @JsonProperty("paymentsRemaining")
    private Integer paymentsRemaining = null;

    @JsonProperty("interval")
    private String interval = null;

    /**
     * The weekDay specified. The payment will occur on the last occurrence of this weekday in the interval.
     */
    public enum LastWeekDayEnum {
        MON("MON"),

        TUE("TUE"),

        WED("WED"),

        THU("THU"),

        FRI("FRI"),

        SAT("SAT"),

        SUN("SUN");

        private String value;

        LastWeekDayEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static LastWeekDayEnum fromValue(String text) {
            for (LastWeekDayEnum b : LastWeekDayEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("lastWeekDay")
    private LastWeekDayEnum lastWeekDay = null;

    /**
     * Enumerated field giving the treatment where a scheduled payment date is not a business day.  If absent assumed to be ON
     */
    public enum NonBusinessDayTreatmentEnum {
        AFTER("AFTER"),

        BEFORE("BEFORE"),

        ON("ON"),

        ONLY("ONLY");

        private String value;

        NonBusinessDayTreatmentEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static NonBusinessDayTreatmentEnum fromValue(String text) {
            for (NonBusinessDayTreatmentEnum b : NonBusinessDayTreatmentEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("nonBusinessDayTreatment")
    private NonBusinessDayTreatmentEnum nonBusinessDayTreatment = NonBusinessDayTreatmentEnum.ON;

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
     * The interval for the payment. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations) (excludes recurrence syntax) with components less than a day in length ignored. This duration defines the period between payments starting with nextPaymentDate
     * @return interval
     **/
    @ApiModelProperty(required = true, value = "The interval for the payment. Formatted according to [ISO 8601 Durations](https://en.wikipedia.org/wiki/ISO_8601#Durations) (excludes recurrence syntax) with components less than a day in length ignored. This duration defines the period between payments starting with nextPaymentDate")
    @NotNull

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public BankingScheduledPaymentRecurrenceLastWeekday lastWeekDay(LastWeekDayEnum lastWeekDay) {
        this.lastWeekDay = lastWeekDay;
        return this;
    }

    /**
     * The weekDay specified. The payment will occur on the last occurrence of this weekday in the interval.
     * @return lastWeekDay
     **/
    @ApiModelProperty(required = true, value = "The weekDay specified. The payment will occur on the last occurrence of this weekday in the interval.")
    @NotNull

    public LastWeekDayEnum getLastWeekDay() {
        return lastWeekDay;
    }

    public void setLastWeekDay(LastWeekDayEnum lastWeekDay) {
        this.lastWeekDay = lastWeekDay;
    }

    public BankingScheduledPaymentRecurrenceLastWeekday nonBusinessDayTreatment(NonBusinessDayTreatmentEnum nonBusinessDayTreatment) {
        this.nonBusinessDayTreatment = nonBusinessDayTreatment;
        return this;
    }

    /**
     * Enumerated field giving the treatment where a scheduled payment date is not a business day.  If absent assumed to be ON
     * @return nonBusinessDayTreatment
     **/
    @ApiModelProperty(value = "Enumerated field giving the treatment where a scheduled payment date is not a business day.  If absent assumed to be ON")

    public NonBusinessDayTreatmentEnum getNonBusinessDayTreatment() {
        return nonBusinessDayTreatment;
    }

    public void setNonBusinessDayTreatment(NonBusinessDayTreatmentEnum nonBusinessDayTreatment) {
        this.nonBusinessDayTreatment = nonBusinessDayTreatment;
    }


    @Override
    public boolean equals(java.lang.Object o) {
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
                Objects.equals(this.lastWeekDay, bankingScheduledPaymentRecurrenceLastWeekday.lastWeekDay) &&
                Objects.equals(this.nonBusinessDayTreatment, bankingScheduledPaymentRecurrenceLastWeekday.nonBusinessDayTreatment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalPaymentDate, paymentsRemaining, interval, lastWeekDay, nonBusinessDayTreatment);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingScheduledPaymentRecurrenceLastWeekday {\n");

        sb.append("    finalPaymentDate: ").append(toIndentedString(finalPaymentDate)).append("\n");
        sb.append("    paymentsRemaining: ").append(toIndentedString(paymentsRemaining)).append("\n");
        sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
        sb.append("    lastWeekDay: ").append(toIndentedString(lastWeekDay)).append("\n");
        sb.append("    nonBusinessDayTreatment: ").append(toIndentedString(nonBusinessDayTreatment)).append("\n");
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
