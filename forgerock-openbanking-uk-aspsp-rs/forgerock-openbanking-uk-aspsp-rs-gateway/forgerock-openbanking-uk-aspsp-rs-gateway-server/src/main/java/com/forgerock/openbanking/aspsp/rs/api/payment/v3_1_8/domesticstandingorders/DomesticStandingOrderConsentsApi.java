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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_8.domesticstandingorders;


import com.forgerock.openbanking.api.annotations.OBGroupName;
import com.forgerock.openbanking.api.annotations.OBReference;
import com.forgerock.openbanking.api.annotations.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "domestic-standing-order-consents", description = "the domestic-standing-order-consents API")
@OpenBankingAPI(
        obVersion = "3.1.8",
        obGroupName = OBGroupName.PISP,
        obReference = OBReference.DOMESTIC_STANDING_ORDERS_PAYMENTS
)
@RequestMapping(value = "/open-banking/v3.1.8/pisp")
public interface DomesticStandingOrderConsentsApi extends com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_7.domesticstandingorders.DomesticStandingOrderConsentsApi {
}
