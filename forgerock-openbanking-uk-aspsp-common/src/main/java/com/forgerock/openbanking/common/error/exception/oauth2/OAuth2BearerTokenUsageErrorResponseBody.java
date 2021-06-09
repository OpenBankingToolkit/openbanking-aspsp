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

/**
 * Used to provide a response body that can carry the information found in an OAuth2BearerTokenUsageException @see
 * com.forgerock.openbanking.common.error.exception.oauth2.OAuth2BearerTokenUsageException
 */
public class OAuth2BearerTokenUsageErrorResponseBody extends OAuth2ErrorAdviceResponseBody {

    String error_code;

    public OAuth2BearerTokenUsageErrorResponseBody(String errorCode, String testFacilityAdviceMessage){
        super(testFacilityAdviceMessage);
        this.error_code = errorCode;
    }
}
