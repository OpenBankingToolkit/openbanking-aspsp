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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0.FRAccount2;
import com.forgerock.openbanking.exceptions.OBErrorException;

import java.io.IOException;
import java.util.List;

public interface ConsentDecisionDelegate {
    String getTppIdBehindConsent();

    String getUserIDBehindConsent();

    void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException;

    void autoaccept(List<FRAccount2> accounts, String username) throws OBErrorException;
}
