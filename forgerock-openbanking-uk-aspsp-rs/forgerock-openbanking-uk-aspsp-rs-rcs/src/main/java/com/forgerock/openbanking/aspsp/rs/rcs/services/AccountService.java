/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccountWithBalance;
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