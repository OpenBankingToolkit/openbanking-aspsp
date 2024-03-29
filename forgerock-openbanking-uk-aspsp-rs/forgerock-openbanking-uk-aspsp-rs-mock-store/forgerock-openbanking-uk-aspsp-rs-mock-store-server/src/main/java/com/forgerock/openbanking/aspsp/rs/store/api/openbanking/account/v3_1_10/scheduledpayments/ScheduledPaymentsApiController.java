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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_10.scheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.scheduledpayments.FRScheduledPaymentRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller("ScheduledPaymentsApiV3.1.10")
@Slf4j
public class ScheduledPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_9.scheduledpayments.ScheduledPaymentsApiController implements ScheduledPaymentsApi {

    public ScheduledPaymentsApiController(@Value("${rs.page.default.schedule-payments.size}") int pageLimitSchedulePayments,
                                          FRScheduledPaymentRepository frScheduledPaymentRepository,
                                          AccountDataInternalIdFilter accountDataInternalIdFilter) {
        super(pageLimitSchedulePayments, frScheduledPaymentRepository, accountDataInternalIdFilter);
    }

}
