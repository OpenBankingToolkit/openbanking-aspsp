/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.rcs;

import lombok.ToString;
import org.springframework.http.HttpMethod;

/**
 * Bean to store and send the redirection information from the server side to the UI for RS accounts and payments.
 */
@ToString
public class RedirectionAction {

    private String consentJwt;
    private String redirectUri;
    private HttpMethod requestMethod;

    public RedirectionAction() {
    }

    public RedirectionAction(String consentJwt, String redirectUri, HttpMethod requestMethod) {
        this.consentJwt = consentJwt;
        this.redirectUri = redirectUri;
        this.requestMethod = requestMethod;
    }

    public String getConsentJwt() {
        return consentJwt;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public HttpMethod getRequestMethod() {
        return requestMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String consentJwt;
        private String redirectUri;
        private HttpMethod requestMethod = HttpMethod.POST;

        public Builder consentJwt(String consentJwt) {
            this.consentJwt = consentJwt;
            return this;
        }

        public Builder redirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public Builder requestMethod(HttpMethod requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public RedirectionAction build() {
            return new RedirectionAction(consentJwt, redirectUri, requestMethod);
        }
    }
}
