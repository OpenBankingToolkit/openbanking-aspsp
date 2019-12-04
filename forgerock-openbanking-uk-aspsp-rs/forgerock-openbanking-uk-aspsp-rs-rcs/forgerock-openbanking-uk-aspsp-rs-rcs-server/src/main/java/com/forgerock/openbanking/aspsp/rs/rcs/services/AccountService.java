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
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.account.OBCashAccount5;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class AccountService {

    public Optional<FRAccountWithBalance> findAccountByIdentification(final String identification, final Collection<FRAccountWithBalance> accounts) {
        if (StringUtils.isEmpty(identification)) {
            log.error("Debtor account has null or empty identification string");
            return Optional.empty();
        }
        for (FRAccountWithBalance account : accounts) {
            if (!CollectionUtils.isEmpty(account.getAccount().getAccount())) {
                for (OBCashAccount5 obCashAccount : account.getAccount().getAccount()) {
                    if (identification.equals(obCashAccount.getIdentification())) {
                        log.debug("Found matching user account to provided debtor account. Identification: {}. Account Id: {}", obCashAccount.getIdentification(),account.getId());
                        return Optional.of(account);
                    }
                }
            }
        }
        log.debug("A user account matching the identification: {} was not found", identification);
        return Optional.empty();
    }
}