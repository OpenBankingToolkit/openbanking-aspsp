/**
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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequestMapping(value = "/api/domestic-standing-orders")
public interface DomesticStandingOrderApi {

    @RequestMapping(value = "/{paymentId}",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity get(
            @PathVariable("paymentId") String paymentId
    );

    @RequestMapping(value = "/search/findByStatus",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.GET)
    ResponseEntity<Collection<FRDomesticStandingOrderConsent3>> findByStatus(
            @RequestParam("status") ConsentStatusCode status
    );

    @RequestMapping(value = "/",
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.PUT)
    ResponseEntity<FRDomesticStandingOrderConsent3> update(
            @RequestBody FRDomesticStandingOrderConsent3 payment1
    );
}

