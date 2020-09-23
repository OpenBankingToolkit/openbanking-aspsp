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
package com.forgerock.openbanking.common.services.openbanking.converter.common;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPostalAddress;
import uk.org.openbanking.datamodel.payment.OBAddressTypeCode;
import uk.org.openbanking.datamodel.payment.OBPostalAddress6;

public class FRPostalAddressConverter {

    public static FRPostalAddress toFRPostalAddress(OBPostalAddress6 obPostalAddress6) {
        return obPostalAddress6 == null ? null : FRPostalAddress.builder()
                .addressType(toFRAddressTypeCode(obPostalAddress6.getAddressType()))
                .department(obPostalAddress6.getDepartment())
                .subDepartment(obPostalAddress6.getSubDepartment())
                .streetName(obPostalAddress6.getStreetName())
                .buildingNumber(obPostalAddress6.getBuildingNumber())
                .postCode(obPostalAddress6.getPostCode())
                .townName(obPostalAddress6.getTownName())
                .countrySubDivision(obPostalAddress6.getCountrySubDivision())
                .country(obPostalAddress6.getCountry())
                .addressLine(obPostalAddress6.getAddressLine())
                .build();
    }

    public static FRPostalAddress.AddressTypeCode toFRAddressTypeCode(OBAddressTypeCode obAddressTypeCode) {
        return obAddressTypeCode == null ? null : FRPostalAddress.AddressTypeCode.valueOf(obAddressTypeCode.name());
    }

    public static OBPostalAddress6 toOBPostalAddress6(FRPostalAddress frPostalAddress) {
        return frPostalAddress == null ? null : new OBPostalAddress6()
                .addressType(toOBAddressTypeCode(frPostalAddress.getAddressType()))
                .department(frPostalAddress.getDepartment())
                .subDepartment(frPostalAddress.getSubDepartment())
                .streetName(frPostalAddress.getStreetName())
                .buildingNumber(frPostalAddress.getBuildingNumber())
                .postCode(frPostalAddress.getPostCode())
                .townName(frPostalAddress.getTownName())
                .countrySubDivision(frPostalAddress.getCountrySubDivision())
                .country(frPostalAddress.getCountry())
                .addressLine(frPostalAddress.getAddressLine());
    }

    public static OBAddressTypeCode toOBAddressTypeCode(FRPostalAddress.AddressTypeCode frAddressTypeCode) {
        return frAddressTypeCode == null ? null : OBAddressTypeCode.valueOf(frAddressTypeCode.name());
    }
}
