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
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    public Optional<AccountWithBalance> findAccountByIdentification(final String identification, final Collection<AccountWithBalance> accounts) {
        if (StringUtils.isEmpty(identification)) {
            log.error("Debtor account has null or empty identification string");
            return Optional.empty();
        }
        for (AccountWithBalance account : accounts) {
            if (!CollectionUtils.isEmpty(account.getAccount().getAccounts())) {
                for (FRAccountIdentifier accountIdentifier : account.getAccount().getAccounts()) {
                    if (identification.equals(accountIdentifier.getIdentification())) {
                        log.debug("Found matching user account to provided debtor account. Identification: {}. Account Id: {}", accountIdentifier.getIdentification(),account.getId());
                        return Optional.of(account);
                    }
                }
            }
        }
        log.debug("A user account matching the identification: {} was not found", identification);
        return Optional.empty();
    }
}