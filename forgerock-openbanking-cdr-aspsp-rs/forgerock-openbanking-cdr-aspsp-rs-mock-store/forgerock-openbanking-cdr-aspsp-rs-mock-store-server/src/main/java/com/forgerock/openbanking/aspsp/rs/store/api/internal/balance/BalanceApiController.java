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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.balance;

import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.common.model.openbanking.persistence.v1_1.account.FRBalance1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.util.Optional;

@Controller
public class BalanceApiController implements BalanceApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceApiController.class);

    @Autowired
    private FRBalance1Repository balanceRepository;

    @Override
    public ResponseEntity<Optional<FRBalance1>> findByAcountId(
            @RequestParam("accountId") String accountId,
            @RequestParam("type") OBBalanceType1Code type
    ) {
        LOGGER.debug("Read balances for account {} with page {}", accountId);
        return ResponseEntity.ok(balanceRepository.findByAccountIdAndBalanceType(accountId, type));
    }

    @Override
    public ResponseEntity save(
            @RequestBody FRBalance1 balance1
    ) {
        LOGGER.debug("Save balance1 {}", balance1);
        return ResponseEntity.ok(balanceRepository.save(balance1));
    }
}
