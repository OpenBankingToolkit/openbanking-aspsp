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

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * OAuth2 Dynamic Client Registration errors as specified by
 * <a href=https://datatracker.ietf.org/doc/html/rfc7591#section-3.2.2>OAuth 2.0 Dynamic Client Registration
 * Protocol</a>
 */
@Slf4j
public class OAuth2RegistrationException {

    public enum ErrorEnum {
        INVALID_REDIRECT_URI("invalid_redirect_uri"),

        INVALID_CLIENT_METADATA("invalid_client_metadata"),

        INVALID_SOFTWARE_STATEMENT("invalid_software_statement"),

        UNAPPROVED_SOFTWARE_STATEMENT("unapproved_software_statement");

        private String value;

        ErrorEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ErrorEnum fromValue(String text) {
            for (ErrorEnum b : ErrorEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private final ErrorEnum errorCode;
    private final String errorDescription;

    /**
     * Create an OAuth2RegistrationException containing an error and optionally an error description
     * @param errorDescription - OPTIONAL.  Human-readable ASCII text description of the error used
     *       for debugging.
     * @param errorCode - REQUIRED.  Single ASCII error code string. - one of the public static definitions specified
     *                 in this class
     */
    public OAuth2RegistrationException(String errorDescription, ErrorEnum errorCode){
        this.errorCode = errorCode;
        if(StringUtils.isEmpty(errorDescription)){
            errorDescription = "";
        }
        this.errorDescription = errorDescription;
    }

    public ErrorEnum getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
