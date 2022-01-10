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
package com.forgerock.openbanking.aspsp.rs.api.customerinfo.v1_0;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.LocalDate;
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;

import java.io.IOException;

public class InfoApiTestHelper {

    public static ReadCustomerInfo getValidReadCustomerInfo() throws IOException {
        String data = "{ \"Data\" : { \"birthdate\" : \"2000-01-23\", \"address\" : { \"street_address\" : [ \"street_address\", \"street_address\", \"street_address\", \"street_address\", \"street_address\" ], \"country\" : \"country\", \"postal_code\" : \"postal_code\" }, \"initials\" : \"initials\", \"party_id\" : \"party_id\", \"phone_number\" : \"phone_number\", \"title\" : \"title\", \"given_name\" : \"given_name\", \"family_name\" : \"family_name\", \"email\" : \"email\" } }";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        ReadCustomerInfo response = mapper.readValue(data, ReadCustomerInfo.class);
        return response;
    }

    public static ReadCustomerInfo getReadCustomerInfo_Under18() throws IOException {
        ReadCustomerInfo response = getValidReadCustomerInfo();
        LocalDate fourteenYearsAgo = new LocalDate();
        fourteenYearsAgo.minusYears(14);
        response.getData().setBirthdate(fourteenYearsAgo);
        return response;
    }
}
