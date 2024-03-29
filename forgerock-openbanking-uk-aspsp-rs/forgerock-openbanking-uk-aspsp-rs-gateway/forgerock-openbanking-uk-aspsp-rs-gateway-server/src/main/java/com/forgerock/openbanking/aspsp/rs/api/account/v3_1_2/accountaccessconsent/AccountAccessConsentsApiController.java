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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.accountaccessconsent;

import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentBasicAndDetailPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import org.springframework.stereotype.Controller;

@Controller("AccountAccessConsentsApiV3.1.2")
public class AccountAccessConsentsApiController
        extends com.forgerock.openbanking.aspsp.rs.api.account.v3_1_1.accountaccessconsent.AccountAccessConsentsApiController implements AccountAccessConsentsApi {
    public AccountAccessConsentsApiController(RsStoreGateway rsStoreGateway, com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService RSEndpointWrapperService, AccountAccessConsentPermittedPermissionsFilter accountAccessConsentPermittedPermissionsFilter, AccountAccessConsentBasicAndDetailPermissionsFilter accountAccessConsentBasicAndDetailPermissionsFilter, RSConfiguration rsConfiguration) {
        super(rsStoreGateway, RSEndpointWrapperService, accountAccessConsentPermittedPermissionsFilter, accountAccessConsentBasicAndDetailPermissionsFilter, rsConfiguration);
    }
}
