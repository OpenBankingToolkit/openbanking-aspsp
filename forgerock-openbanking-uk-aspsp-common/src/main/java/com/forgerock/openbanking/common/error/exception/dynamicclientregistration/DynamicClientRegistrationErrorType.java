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

public enum DynamicClientRegistrationErrorType {

    INVALID_CLIENT_METADATA("invalid_client_metadata"),
    INVALID_REDIRECT_URI("invalid_redirect_uri"),
    INVALID_SOFTWARE_STATEMENT("invalid_software_statement"),
    UNAPPROVED_SOFTWARE_STATEMENT("unapproved_software_statement");

    private String stringRepresentation;

    DynamicClientRegistrationErrorType(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    @Override
    public String toString() {
        return this.stringRepresentation;
    }
}
