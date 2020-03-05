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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Indicates that the payment is a once off payment on a specific future date. Mandatory if recurrenceUType is set to onceOff
 */
@ApiModel(description = "Indicates that the payment is a once off payment on a specific future date. Mandatory if recurrenceUType is set to onceOff")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:16:26.310659Z[Europe/London]")
public class BankingScheduledPaymentRecurrenceOnceOff {
    @JsonProperty("paymentDate")
    private String paymentDate = null;

    public BankingScheduledPaymentRecurrenceOnceOff paymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    /**
     * The scheduled date for the once off payment
     * @return paymentDate
     **/
    @ApiModelProperty(required = true, value = "The scheduled date for the once off payment")
    @NotNull

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingScheduledPaymentRecurrenceOnceOff bankingScheduledPaymentRecurrenceOnceOff = (BankingScheduledPaymentRecurrenceOnceOff) o;
        return Objects.equals(this.paymentDate, bankingScheduledPaymentRecurrenceOnceOff.paymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingScheduledPaymentRecurrenceOnceOff {\n");

        sb.append("    paymentDate: ").append(toIndentedString(paymentDate)).append("\n");
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
