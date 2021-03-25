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
package com.forgerock.openbanking.aspsp.rs.api.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forgerock.openbanking.common.conf.discovery.GenericOBDiscoveryAPILinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class DiscoveryResponse {
    @JsonProperty("Data")
    private DiscoveryData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static final class DiscoveryData {
        @JsonProperty("FinancialId")
        private String financialId;
        @JsonProperty("PaymentInitiationAPI")
        private List<OBDiscoveryAPI<GenericOBDiscoveryAPILinks>> paymentInitiationAPIs;
        @JsonProperty("AccountAndTransactionAPI")
        private List<OBDiscoveryAPI<GenericOBDiscoveryAPILinks>> accountAndTransactionAPIs;
        @JsonProperty("FundsConfirmationAPI")
        private List<OBDiscoveryAPI<GenericOBDiscoveryAPILinks>> fundsConfirmationAPIs;
        @JsonProperty("EventNotificationAPI")
        private List<OBDiscoveryAPI<GenericOBDiscoveryAPILinks>> eventNotificationAPIs;

        OBDiscoveryAPI<GenericOBDiscoveryAPILinks> getPaymentInitiationByVersion(String version) {
            return paymentInitiationAPIs.stream().filter(api -> api.getVersion().equals(version)).findFirst().orElseThrow(IllegalArgumentException::new);
        }

        OBDiscoveryAPI<GenericOBDiscoveryAPILinks> getAccountAndTransactionByVersion(String version) {
            return accountAndTransactionAPIs.stream().filter(api -> api.getVersion().equals(version)).findFirst().orElseThrow(IllegalArgumentException::new);
        }

        OBDiscoveryAPI<GenericOBDiscoveryAPILinks> getFundsConfirmationByVersion(String version) {
            return fundsConfirmationAPIs.stream().filter(api -> api.getVersion().equals(version)).findFirst().orElseThrow(IllegalArgumentException::new);
        }
    }
}

