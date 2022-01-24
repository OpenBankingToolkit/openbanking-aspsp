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
package com.forgerock.openbanking.aspsp.rs.store.utils;

import com.forgerock.openbanking.common.model.data.FRAddressTypeCode;
import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.model.data.FRCustomerInfoAddress;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class FRCustomerInfoTestHelper {
    public static FRCustomerInfoAddress aValidFRCustomerInfoAddress() {
        return FRCustomerInfoAddress.builder()
                .streetAddress(List.of("999", "Letsbe Avenue", "Chelmsford", "Essex"))
                .addressType(FRAddressTypeCode.RESIDENTIAL)
                .country("UK")
                .postalCode("ES12 3RR").build();
    }

    public static FRCustomerInfo aValidFRCustomerInfo() {
        return FRCustomerInfo.builder()
                .id("3242-2343-23432-4323")
                .userID("fredtitmus")
                .address(FRCustomerInfoTestHelper.aValidFRCustomerInfoAddress())
                .birthdate(new DateTime().minusYears(19))
                .email("fred.titmus@forgerock.com")
                .familyName("Titmus")
                .givenName("Fred")
                .initials("F R")
                .title("Mr")
                .partyId("partyId")
                .phoneNumber("+44 3456 789789").build();
    }
}
