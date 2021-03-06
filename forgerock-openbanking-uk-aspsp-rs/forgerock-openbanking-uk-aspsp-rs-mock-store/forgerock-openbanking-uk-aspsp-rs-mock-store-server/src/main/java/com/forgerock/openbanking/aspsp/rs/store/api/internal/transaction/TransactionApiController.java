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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.transaction;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.transactions.FRTransactionRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TransactionApiController implements TransactionApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionApiController.class);

    @Autowired
    private FRTransactionRepository transactionRepository;

    @Override
    public ResponseEntity<FRTransaction> create(
            @RequestBody FRTransaction transaction
    ) {
        LOGGER.debug("Create transaction {}", transaction);
        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.OK);
    }
}
