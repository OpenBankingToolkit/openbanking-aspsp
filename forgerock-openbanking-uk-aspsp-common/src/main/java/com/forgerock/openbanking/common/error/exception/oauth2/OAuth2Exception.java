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

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * OAuth2 Exceptions as specified in <a href=https://www.rfc-editor.org/rfc/rfc6749#section-5.2>rfc6749 section 5.2</a>
 */
public class OAuth2Exception extends Exception {

    public static final String INVALID_CLIENT = "invalid_client";

    public String getRfc6750ErrorCode() {
        return rfc6750ErrorCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    @JsonProperty("error")
    private final String rfc6750ErrorCode;

    private final HttpStatus httpStatusCode;

    public OAuth2Exception(String message, String rfc6750ErrorCode, HttpStatus httpStatusCode){
        super(message);
        this.rfc6750ErrorCode = rfc6750ErrorCode;
        this.httpStatusCode = httpStatusCode;
    }
}
