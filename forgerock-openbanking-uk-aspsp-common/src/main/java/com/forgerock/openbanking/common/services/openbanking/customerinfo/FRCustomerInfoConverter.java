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
package com.forgerock.openbanking.common.services.openbanking.customerinfo;

import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.model.data.FRCustomerInfoAddress;
import uk.org.openbanking.datamodel.customerinfo.AddressTypeCode;
import uk.org.openbanking.datamodel.customerinfo.CustomerInfo;
import uk.org.openbanking.datamodel.customerinfo.CustomerInfoAddress;

public class FRCustomerInfoConverter {

    public static CustomerInfo toCustomerInfo(FRCustomerInfo frCustomerInfo){
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setInitials(frCustomerInfo.getInitials());
        customerInfo.setBirthdate(frCustomerInfo.getBirthdate().toLocalDate());
        customerInfo.setEmail(frCustomerInfo.getEmail());
        customerInfo.setFamilyName(frCustomerInfo.getFamilyName());
        customerInfo.setGivenName(frCustomerInfo.getGivenName());
        customerInfo.setPartyId(frCustomerInfo.getPartyId());
        customerInfo.setTitle(frCustomerInfo.getTitle());
        customerInfo.setPhoneNumber(frCustomerInfo.getPhoneNumber());
        customerInfo.setAddress(toCustomerInfoAddress(frCustomerInfo.getAddress()));
        return customerInfo;
    }

    private static CustomerInfoAddress toCustomerInfoAddress(FRCustomerInfoAddress address) {
        CustomerInfoAddress customerInfoAddress = new CustomerInfoAddress();
        customerInfoAddress.setStreetAddress(address.getStreetAddress());
        customerInfoAddress.setAddressType(AddressTypeCode.fromValue(address.getAddressType().getValue()));
        customerInfoAddress.setCountry(address.getCountry());
        customerInfoAddress.setPostalCode(address.getPostalCode());
        return customerInfoAddress;
    }
}
