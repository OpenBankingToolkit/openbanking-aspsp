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
import uk.org.openbanking.datamodel.customerinfo.CustomerInfoAddress;

import javax.validation.Valid;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FRCustomerInfoAddress {

    public FRCustomerInfoAddress(CustomerInfoAddress customerInfoAddress){
        this.addressType = FRAddressTypeCode.fromValue(customerInfoAddress.getAddressType().toString());
        this.streetAddress = customerInfoAddress.getStreetAddress();
        this.postalCode = customerInfoAddress.getPostalCode();
        this.country = customerInfoAddress.getCountry();
    }

    @JsonProperty("address_type")
    private FRAddressTypeCode addressType;
    @JsonProperty("street_address")
    @Valid
    private List<String> streetAddress = null;
    @JsonProperty("postal_code")
    private String postalCode;
    @JsonProperty("country")
    private String country;
}
