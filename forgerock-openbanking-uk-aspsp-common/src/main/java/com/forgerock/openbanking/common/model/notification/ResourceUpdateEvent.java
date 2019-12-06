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
package com.forgerock.openbanking.common.model.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Represents an event where a resource has updated in RS as a set of claims.
 * See https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-TPPDataModel
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResourceUpdateEvent {

    @JsonProperty(OpenBankingConstants.EventNotificationClaims.SUBJECT)
    private Subject subject;

    // Helper method
    @JsonIgnore
    public Optional<String> getFirstResourceLink() {
        return getSubject().getResourceLinks()
                .stream()
                .findFirst()
                .map(ResourceLink::getLink);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Subject {
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.SUBJECT_TYPE)
        private String subjectType;
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.RESOURCE_ID)
        private String resourceId;
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.RESOURCE_TYPE)
        private String resourceType;
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.RESOURCE_LINKS)
        private List<ResourceLink> resourceLinks;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class ResourceLink {
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.RESOURCE_VERSION)
        public String version;
        @JsonProperty(OpenBankingConstants.EventNotificationClaims.RESOURCE_LINK)
        public String link;
    }

}
