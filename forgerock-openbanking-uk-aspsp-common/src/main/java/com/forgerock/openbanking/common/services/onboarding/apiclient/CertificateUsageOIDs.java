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
package com.forgerock.openbanking.common.services.onboarding.apiclient;

public interface CertificateUsageOIDs {
    String SERVER_AUTH  = "1.3.6.1.5.5.7.3.1";
    String CLIENT_AUTH  = "1.3.6.1.5.5.7.3.2";
    String CODE_SIGNING  = "1.3.6.1.5.5.7.3.3";
    String EMAIL_PROTECTION = "1.3.6.1.5.5.7.3.4";
    String SIGNER_OF_DOCUMENTS = "1.3.6.1.4.1.311.10.3.12";
}
