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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.statement;

import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements.FRStatement1Repository;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStatement1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class StatementApiController implements StatementApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementApiController.class);

    @Autowired
    private FRStatement1Repository statementRepository;

    @Override
    public ResponseEntity getStatement(
            @PathVariable("statementId") String statementId
    ) {
        LOGGER.debug("Read statement with id {}", statementId);
        Optional<FRStatement1> byId = statementRepository.findById(statementId);
        if (!byId.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cant find statement with ID '" + statementId + "'");
        }
        return ResponseEntity.ok(byId.get());
    }
}
