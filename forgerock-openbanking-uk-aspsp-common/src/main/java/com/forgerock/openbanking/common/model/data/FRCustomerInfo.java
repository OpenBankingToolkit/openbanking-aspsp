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
package com.forgerock.openbanking.common.model.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import uk.org.openbanking.datamodel.customerinfo.CustomerInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class FRCustomerInfo {
    public FRCustomerInfo(CustomerInfo customerInfo){
        this.partyId = customerInfo.getPartyId();
        this.title = customerInfo.getPartyId();
        this.initials = customerInfo.getInitials();
        this.familyName = customerInfo.getFamilyName();
        this.givenName = customerInfo.getGivenName();
        this.email = customerInfo.getEmail();
        this.phoneNumber = customerInfo.getPhoneNumber();
        this.birthdate = customerInfo.getBirthdate().toDateTimeAtCurrentTime();
        this.address = new FRCustomerInfoAddress(customerInfo.getAddress());
    }

    @Id
    @Indexed
    public String id;

    @Indexed
    public  String userID;
    private String partyId;
    private String title;
    private String initials;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
    )
    private DateTime birthdate;
    private FRCustomerInfoAddress address;
}
