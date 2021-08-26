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
package com.forgerock.openbanking.common.model.openbanking.persistence.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import org.joda.time.DateTime;

import java.util.List;

public interface AccountRequest {

    String getClientId();

    String getUserId();

    String getAispId();

    String getAispName();

    List<FRExternalPermissionsCode> getPermissions();

    DateTime getExpirationDateTime();

    DateTime getTransactionFromDateTime();

    DateTime getTransactionToDateTime();

    DateTime getStatusUpdateDateTime();

    void setUserId(String userId);

    void setAccountIds(List<String> accountIds);

    void setStatus(FRExternalRequestStatusCode code);

    void setStatusUpdateDateTime(DateTime date);
    String getId();

    List<String> getAccountIds();

    FRExternalRequestStatusCode getStatus();
}
