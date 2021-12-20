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
package com.forgerock.openbanking.common.constants;

public class OpenBankingHttpHeaders {
    /**
     * Contains the client ID that is used to identify the TPP. Mostly used to pass a TPP identity extracted from client certificate to a backend service like rs-store.
     */
    public static final String X_OB_CLIENT_ID = "x-ob-client-id";

    /**
     * The unique Id of the ASPSP to which the request is issued.
     * Issued by OBIE and corresponds to the Organization Id of the ASPSP in the Open Banking Directory.
     */
    public static final String X_FAPI_FINANCIAL_ID = "x-fapi-financial-id";

    /**
     * Contains a flag to identify if the request is in mode test to adapt the behaviour for test purposes.
     */
    public static final String X_OB_MODE_TEST = "x-ob-mode-test";
}
