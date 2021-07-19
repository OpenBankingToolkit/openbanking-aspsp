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
package com.forgerock.openbanking.common.model.onboarding;

import java.util.List;

public class ManualRegistrationRequest {

    public String applicationName;
    public String organisationId;
    public String directoryId;
    public String applicationDescription;
    public List<String> redirectUris;
    public String softwareStatementAssertion;
    public String qWacPem;
    public String appId;
    public String psd2Roles;

    public ManualRegistrationRequest(String applicationName, String organisationId, String directoryId, String applicationDescription, List<String> redirectUris, String softwareStatementAssertion, String qWacPem, String appId, String psd2Roles) {
        this.applicationName = applicationName;
        this.organisationId = organisationId;
        this.directoryId = directoryId;
        this.applicationDescription = applicationDescription;
        this.redirectUris = redirectUris;
        this.softwareStatementAssertion = softwareStatementAssertion;
        this.qWacPem = qWacPem;
        this.appId = appId;
        this.psd2Roles = psd2Roles;
    }

    public ManualRegistrationRequest() {
    }

    public static ManualRegistrationRequestBuilder builder() {
        return new ManualRegistrationRequestBuilder();
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public String getOrganisationId() {
        return this.organisationId;
    }

    public String getDirectoryId() {
        return this.directoryId;
    }

    public String getApplicationDescription() {
        return this.applicationDescription;
    }

    public List<String> getRedirectUris() {
        return this.redirectUris;
    }

    public String getSoftwareStatementAssertion() {
        return this.softwareStatementAssertion;
    }

    public String getQWacPem() {
        return this.qWacPem;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getPsd2Roles() {
        return this.psd2Roles;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public void setRedirectUris(List<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public void setSoftwareStatementAssertion(String softwareStatementAssertion) {
        this.softwareStatementAssertion = softwareStatementAssertion;
    }

    public void setQWacPem(String qWacPem) {
        this.qWacPem = qWacPem;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setPsd2Roles(String psd2Roles) {
        this.psd2Roles = psd2Roles;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ManualRegistrationRequest)) return false;
        final ManualRegistrationRequest other = (ManualRegistrationRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$applicationName = this.getApplicationName();
        final Object other$applicationName = other.getApplicationName();
        if (this$applicationName == null ? other$applicationName != null : !this$applicationName.equals(other$applicationName))
            return false;
        final Object this$organisationId = this.getOrganisationId();
        final Object other$organisationId = other.getOrganisationId();
        if (this$organisationId == null ? other$organisationId != null : !this$organisationId.equals(other$organisationId))
            return false;
        final Object this$directoryId = this.getDirectoryId();
        final Object other$directoryId = other.getDirectoryId();
        if (this$directoryId == null ? other$directoryId != null : !this$directoryId.equals(other$directoryId))
            return false;
        final Object this$applicationDescription = this.getApplicationDescription();
        final Object other$applicationDescription = other.getApplicationDescription();
        if (this$applicationDescription == null ? other$applicationDescription != null : !this$applicationDescription.equals(other$applicationDescription))
            return false;
        final Object this$redirectUris = this.getRedirectUris();
        final Object other$redirectUris = other.getRedirectUris();
        if (this$redirectUris == null ? other$redirectUris != null : !this$redirectUris.equals(other$redirectUris))
            return false;
        final Object this$softwareStatementAssertion = this.getSoftwareStatementAssertion();
        final Object other$softwareStatementAssertion = other.getSoftwareStatementAssertion();
        if (this$softwareStatementAssertion == null ? other$softwareStatementAssertion != null : !this$softwareStatementAssertion.equals(other$softwareStatementAssertion))
            return false;
        final Object this$qWacPem = this.getQWacPem();
        final Object other$qWacPem = other.getQWacPem();
        if (this$qWacPem == null ? other$qWacPem != null : !this$qWacPem.equals(other$qWacPem)) return false;
        final Object this$appId = this.getAppId();
        final Object other$appId = other.getAppId();
        if (this$appId == null ? other$appId != null : !this$appId.equals(other$appId)) return false;
        final Object this$psd2Roles = this.getPsd2Roles();
        final Object other$psd2Roles = other.getPsd2Roles();
        if (this$psd2Roles == null ? other$psd2Roles != null : !this$psd2Roles.equals(other$psd2Roles)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ManualRegistrationRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $applicationName = this.getApplicationName();
        result = result * PRIME + ($applicationName == null ? 43 : $applicationName.hashCode());
        final Object $organisationId = this.getOrganisationId();
        result = result * PRIME + ($organisationId == null ? 43 : $organisationId.hashCode());
        final Object $directoryId = this.getDirectoryId();
        result = result * PRIME + ($directoryId == null ? 43 : $directoryId.hashCode());
        final Object $applicationDescription = this.getApplicationDescription();
        result = result * PRIME + ($applicationDescription == null ? 43 : $applicationDescription.hashCode());
        final Object $redirectUris = this.getRedirectUris();
        result = result * PRIME + ($redirectUris == null ? 43 : $redirectUris.hashCode());
        final Object $softwareStatementAssertion = this.getSoftwareStatementAssertion();
        result = result * PRIME + ($softwareStatementAssertion == null ? 43 : $softwareStatementAssertion.hashCode());
        final Object $qWacPem = this.getQWacPem();
        result = result * PRIME + ($qWacPem == null ? 43 : $qWacPem.hashCode());
        final Object $appId = this.getAppId();
        result = result * PRIME + ($appId == null ? 43 : $appId.hashCode());
        final Object $psd2Roles = this.getPsd2Roles();
        result = result * PRIME + ($psd2Roles == null ? 43 : $psd2Roles.hashCode());
        return result;
    }

    public String toString() {
        return "ManualRegistrationRequest(applicationName=" + this.getApplicationName() + ", organisationId=" + this.getOrganisationId() + ", directoryId=" + this.getDirectoryId() + ", applicationDescription=" + this.getApplicationDescription() + ", redirectUris=" + this.getRedirectUris() + ", softwareStatementAssertion=" + this.getSoftwareStatementAssertion() + ", qWacPem=" + this.getQWacPem() + ", appId=" + this.getAppId() + ", psd2Roles=" + this.getPsd2Roles() + ")";
    }

    public static class ManualRegistrationRequestBuilder {
        private String applicationName;
        private String organisationId;
        private String directoryId;
        private String applicationDescription;
        private List<String> redirectUris;
        private String softwareStatementAssertion;
        private String qWacPem;
        private String appId;
        private String psd2Roles;

        ManualRegistrationRequestBuilder() {
        }

        public ManualRegistrationRequestBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public ManualRegistrationRequestBuilder organisationId(String organisationId) {
            this.organisationId = organisationId;
            return this;
        }

        public ManualRegistrationRequestBuilder directoryId(String directoryId) {
            this.directoryId = directoryId;
            return this;
        }

        public ManualRegistrationRequestBuilder applicationDescription(String applicationDescription) {
            this.applicationDescription = applicationDescription;
            return this;
        }

        public ManualRegistrationRequestBuilder redirectUris(List<String> redirectUris) {
            this.redirectUris = redirectUris;
            return this;
        }

        public ManualRegistrationRequestBuilder softwareStatementAssertion(String softwareStatementAssertion) {
            this.softwareStatementAssertion = softwareStatementAssertion;
            return this;
        }

        public ManualRegistrationRequestBuilder qWacPem(String qWacPem) {
            this.qWacPem = qWacPem;
            return this;
        }

        public ManualRegistrationRequestBuilder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public ManualRegistrationRequestBuilder psd2Roles(String psd2Roles) {
            this.psd2Roles = psd2Roles;
            return this;
        }

        public ManualRegistrationRequest build() {
            return new ManualRegistrationRequest(applicationName, organisationId, directoryId, applicationDescription, redirectUris, softwareStatementAssertion, qWacPem, appId, psd2Roles);
        }

        public String toString() {
            return "ManualRegistrationRequest.ManualRegistrationRequestBuilder(applicationName=" + this.applicationName + ", organisationId=" + this.organisationId + ", directoryId=" + this.directoryId + ", applicationDescription=" + this.applicationDescription + ", redirectUris=" + this.redirectUris + ", softwareStatementAssertion=" + this.softwareStatementAssertion + ", qWacPem=" + this.qWacPem + ", appId=" + this.appId + ", psd2Roles=" + this.psd2Roles + ")";
        }
    }
}
