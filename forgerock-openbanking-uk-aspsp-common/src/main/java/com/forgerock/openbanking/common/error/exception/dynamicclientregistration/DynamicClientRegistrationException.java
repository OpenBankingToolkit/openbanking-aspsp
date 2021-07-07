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
package com.forgerock.openbanking.common.error.exception.dynamicclientregistration;

/**
 * Representation of the errors described in <a href=https://datatracker.ietf.org/doc/html/rfc7591#section-3.2.2>
 *     OAuth 2.0 Dynamic Client Registration Protocol</a>
 */
public class DynamicClientRegistrationException extends Exception {

    DynamicClientRegistrationErrorType errorType;


    public DynamicClientRegistrationException(String message, DynamicClientRegistrationErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public DynamicClientRegistrationErrorType getErrorType(){
        return this.errorType;
    }
}
