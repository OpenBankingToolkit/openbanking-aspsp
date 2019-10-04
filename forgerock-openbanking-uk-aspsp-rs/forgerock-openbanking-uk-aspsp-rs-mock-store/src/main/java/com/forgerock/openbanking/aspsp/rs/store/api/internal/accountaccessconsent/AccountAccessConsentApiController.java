/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.accountaccessconsent;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.accounts.accountaccessconsents.FRAccountAccessConsent1Repository;
import com.forgerock.openbanking.common.model.openbanking.v3_0.account.FRAccountAccessConsent1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class AccountAccessConsentApiController implements AccountAccessConsentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountAccessConsentApiController.class);

    @Autowired
    private FRAccountAccessConsent1Repository accountAccessConsent1Repository;
    @Autowired
    private ConsentMetricService consentMetricService;

    @Override
    public ResponseEntity<FRAccountAccessConsent1> save(
            @RequestBody FRAccountAccessConsent1 accountAccessConsent1
    ) {
        LOGGER.debug("Create an account access consent {}", accountAccessConsent1);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(accountAccessConsent1.getConsentId(), accountAccessConsent1.getStatus().name()));
        return new ResponseEntity<>(accountAccessConsent1Repository.save(accountAccessConsent1), HttpStatus.OK);
    }

    @Override
    public ResponseEntity read(
            @PathVariable("consentId") String consentId
    ) {
        LOGGER.debug("get an account access consent {}", consentId);
        Optional<FRAccountAccessConsent1> isConsent = accountAccessConsent1Repository.findById(consentId);
        if (isConsent.isPresent()) {
            return ResponseEntity.ok(isConsent.get());

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
