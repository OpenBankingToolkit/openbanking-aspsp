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
package com.forgerock.openbanking.common.model.openbanking.domain.payment.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.payment.OBExternalExtendedAccountType1Code;

import java.util.List;

/**
 * Represents an equivalent object in the OB data model. It is stored within mongo (instead of the OB object), in order to make it easier to introduce new
 * versions of the Read/Write API.
 *
 * <p>
 * Note that this object is used across multiple versions of the Read/Write API, meaning that some values won't be populated. For this reason it is
 * a mutable {@link lombok.Data} rather than an immutable {@link lombok.Value} one.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRPaymentRisk {

    private FRExternalPaymentContextCode paymentContextCode;
    private String merchantCategoryCode;
    private String merchantCustomerIdentification;
    private FRRiskDeliveryAddress deliveryAddress;
    private Boolean contractPresentIndicator;
    private Boolean beneficiaryPrepopulatedIndicator;
    private String paymentPurposeCode;
    private FRExternalExtendedAccountType beneficiaryAccountType;

    /**
     * Represents an equivalent object in the OB data model. It is stored within mongo (instead of the OB object), in order to make it easier to introduce new
     * versions of the Read/Write API.
     *
     * <p>
     * Note that this object is used across multiple versions of the Read/Write API, meaning that some values won't be populated. For this reason it is
     * a mutable {@link lombok.Data} rather than an immutable {@link lombok.Value} one.
     * </p>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FRRiskDeliveryAddress {

        private List<String> addressLine;
        private String streetName;
        private String buildingNumber;
        private String postCode;
        private String townName;
        private String countrySubDivision;
        private String country;
    }
}
