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
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.openbanking.common.services.onboarding.RegistrationError;

public class OIDCException extends Exception {

    RegistrationError registrationError;

    public OIDCException(){
        this.registrationError = new RegistrationError();
    }
    public OIDCException(RegistrationError registrationError) {
        this.registrationError = registrationError;
    }

    public OIDCException(String message) {
        super(message);
        this.registrationError.errorDescription(message);
    }

    public OIDCException(String message, Throwable cause) {
        super(message, cause);
    }

    public OIDCException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return registrationError.getError().toString() + ", errorDescription: " + registrationError.getErrorDescription();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public RegistrationError getRegistrationError() {
        return this.registrationError;
    }
}
