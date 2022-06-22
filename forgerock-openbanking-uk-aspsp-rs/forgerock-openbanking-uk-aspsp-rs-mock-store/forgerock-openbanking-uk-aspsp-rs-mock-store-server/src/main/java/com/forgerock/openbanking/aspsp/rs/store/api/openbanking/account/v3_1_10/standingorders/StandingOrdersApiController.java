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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_10.standingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.standingorders.FRStandingOrderRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller("StandingOrdersApiV3.1.10")
public class StandingOrdersApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_9.standingorders.StandingOrdersApiController implements StandingOrdersApi {

    public StandingOrdersApiController(@Value("${rs.page.default.standing-order.size}") int pageLimitStandingOrders,
                                       FRStandingOrderRepository frStandingOrderRepository,
                                       AccountDataInternalIdFilter accountDataInternalIdFilter) {
        super(pageLimitStandingOrders, frStandingOrderRepository, accountDataInternalIdFilter);
    }
}
