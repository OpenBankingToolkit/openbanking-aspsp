/**
 * Copyright 2021 ForgeRock AS.
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.common.model.data;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.openbanking.datamodel.customerinfo.CustomerInfo;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FRCustomerInfo {
    public FRCustomerInfo(CustomerInfo customerInfo){
        this.partyId = customerInfo.getPartyId();
        this.title = customerInfo.getPartyId();
        this.initials = customerInfo.getInitials();
        this.familyName = customerInfo.getFamilyName();
        this.givenName = customerInfo.getGivenName();
        this.email = customerInfo.getEmail();
        this.phoneNumber = customerInfo.getPhoneNumber();
        this.birthdate = customerInfo.getBirthdate();
        this.address = new FRCustomerInfoAddress(customerInfo.getAddress());
    }


    @JsonProperty("party_id")
    private String partyId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("initials")
    private String initials;
    @JsonProperty("family_name")
    private String familyName;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("birthdate")
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
    )
    private LocalDate birthdate;
    @JsonProperty("address")
    private FRCustomerInfoAddress address;

}
