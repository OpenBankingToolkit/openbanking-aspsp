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
package com.forgerock.openbanking.common.error.exception.oauth2;

import org.springframework.http.HttpStatus;

/**
 * Allows exceptions to be thrown that will result in error responses as defined in
 * <a href=https://datatracker.ietf.org/doc/html/rfc6750#section-3.1>
 *     The OAuth 2.0 Authorization Framework: Bearer Token Usage</a>
 */
public class OAuth2BearerTokenUsageException extends Exception{

    public enum ErrorEnum {
        INVALID_REQUEST("invalid_request"),

        INVALID_TOKEN("invalid_token"),

        INSUFFICIENT_SCOPE("insufficient_scope"),

        MISSING_AUTH_INFO("missing_auth_information");

        private final String value;

        ErrorEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static OAuth2BearerTokenUsageException.ErrorEnum fromValue(String text) {
            for (OAuth2BearerTokenUsageException.ErrorEnum b : OAuth2BearerTokenUsageException.ErrorEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private final HttpStatus httpStatusCode = HttpStatus.UNAUTHORIZED;

    private final String wwwAuthenticateResponseHeaderValue;

    private ErrorEnum errorCode;

    public OAuth2BearerTokenUsageException(String message, String wwwAuthenticateResponseHeaderValue,
                                           ErrorEnum errorCode){
        super(message);
        this.wwwAuthenticateResponseHeaderValue = wwwAuthenticateResponseHeaderValue;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public String getWwwAuthenticateResponseHeaderValue() {
        return this.wwwAuthenticateResponseHeaderValue;
    }

    public ErrorEnum getErrorCode() { return errorCode; }

}
