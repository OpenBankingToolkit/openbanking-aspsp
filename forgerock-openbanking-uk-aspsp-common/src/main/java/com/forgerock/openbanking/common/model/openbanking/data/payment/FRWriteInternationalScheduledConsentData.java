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
package com.forgerock.openbanking.common.model.openbanking.data.payment;

import com.forgerock.openbanking.common.model.openbanking.data.payment.common.FRDataAuthorisation;
import com.forgerock.openbanking.common.model.openbanking.data.payment.common.FRDataSCASupportData;
import com.forgerock.openbanking.common.model.openbanking.data.payment.common.FRPermission;
import com.forgerock.openbanking.common.model.openbanking.data.payment.common.FRReadRefundAccount;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FRWriteInternationalScheduledConsentData {

    private FRPermission permission;
    private FRReadRefundAccount readRefundAccount;
    private FRWriteInternationalScheduledDataInitiation initiation;
    private FRDataAuthorisation authorisation;
    private FRDataSCASupportData scASupportData;
}
