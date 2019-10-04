/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.discovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forgerock.openbanking.commons.configuration.discovery.GenericOBDiscoveryAPILinks;
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

