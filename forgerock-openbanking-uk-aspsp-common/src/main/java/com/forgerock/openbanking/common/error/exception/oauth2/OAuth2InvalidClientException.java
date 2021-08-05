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
 * Representation of The OAuth 2.0 Authorization Framework
 * <a href=https://www.rfc-editor.org/rfc/rfc6749#section-5.2>The OAuth 2.0 Authorization Framework</a>
 * invalid client error - see section 5.2.
 *
 * This should be thrown when;
 *      Client authentication failed (e.g., unknown client, no
 *      client authentication included, or unsupported
 *      authentication method).  The authorization server MAY
 *      return an HTTP 401 (Unauthorized) status code to indicate
 *      which HTTP authentication schemes are supported.  If the
 *      client attempted to authenticate via the "Authorization"
 *      request header field, the authorization server MUST
 *      respond with an HTTP 401 (Unauthorized) status code and
 *      include the "WWW-Authenticate" response header field
 *      matching the authentication scheme used by the client.
 */
public class OAuth2InvalidClientException extends OAuth2Exception {
    public OAuth2InvalidClientException(String message){
        super(message, OAuth2Exception.INVALID_CLIENT, HttpStatus.UNAUTHORIZED);
    }
}
