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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.data;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_5.data.FRAccountData5;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_5.data.FRUserData5;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/data")
public interface DataApi {

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    ResponseEntity<Page<FRAccountData5>> exportAccountData(
            @PageableDefault Pageable pageable
    );

    @RequestMapping(value = "/user/has-data", method = RequestMethod.GET)
    ResponseEntity<Boolean> hasData(
            @RequestParam("userId") String userId
    );

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    ResponseEntity<FRUserData5> exportUserData(
            @RequestParam("userId") String userId
    );

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    ResponseEntity updateUserData(
            @RequestBody FRUserData5 userData
    );

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    ResponseEntity importUserData(
            @RequestBody FRUserData5 userData
    );

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    ResponseEntity<Boolean> deleteUserData(
            @RequestParam("userId") String userId
    );
}
