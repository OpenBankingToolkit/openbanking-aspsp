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
 * <p></p>Allows exceptions to be thrown that will result in error responses as defined in
 * <a href=https://datatracker.ietf.org/doc/html/rfc6750#section-3.1>
 *     The OAuth 2.0 Authorization Framework: Bearer Token Usage</a></p><br>
 *
 * <p>This specific exception covers the error case described thus;</p>
 * <p>If the request lacks any authentication information (e.g., the client
 *    was unaware that authentication is necessary or attempted using an
 *    unsupported authentication method), the resource server SHOULD NOT
 *    include an error code or other error information.</p><br>
 *    <br>
 *    For example:<br>
 *    <code>
 *
 *      HTTP/1.1 401 Unauthorized<br>
 *      WWW-Authenticate: Bearer realm="example"
 *      </code>
 * </p>
 */
public class OAuth2BearerTokenUsageMissingAuthInfoException extends OAuth2BearerTokenUsageException {
    public OAuth2BearerTokenUsageMissingAuthInfoException(String message){
        super(message, OAuth2Constants.OAUTH2_WWW_AUTHENTICATE_HEADER_VALUE_BEARER,
                ErrorEnum.MISSING_AUTH_INFO);
    }
}
