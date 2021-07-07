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
package com.forgerock.openbanking.aspsp.as.service.apiclient;

public enum ApiClientCertificateType {

    FR_TRANSPORT(true), FR_SIGNING(true), OBWAC(true), OBSEAL(true), OB_TRANSPORT(true), OB_SIGNING(true), QWAC(true)
    , QSEAL(true), INVALID(false);

    private boolean isValid;

    ApiClientCertificateType(boolean isValid){
        this.isValid = isValid;
    }
}
