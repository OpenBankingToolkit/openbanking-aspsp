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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccount;

public class FRAccountTestDataFactory {

    public static FRAccount aValidFRAccount() {
        return FRAccount.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("40400411290112")
                .name("Mr A Jones")
                .build();
    }

    public static FRAccount aValidFRAccount2() {
        return FRAccount.builder()
                .schemeName("UK.OBIE.SortCodeAccountNumber")
                .identification("40400422390112")
                .name("Mrs B Smith")
                .build();
    }
}
