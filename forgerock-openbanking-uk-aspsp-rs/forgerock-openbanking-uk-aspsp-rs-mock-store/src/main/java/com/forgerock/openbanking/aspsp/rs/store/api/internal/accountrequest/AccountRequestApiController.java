/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.accountrequest;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.accountrequests.FRAccountRequest1Repository;
import com.forgerock.openbanking.commons.model.openbanking.v1_1.account.FRAccountRequest1;
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
public class AccountRequestApiController implements AccountRequestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestApiController.class);

    @Autowired
    private FRAccountRequest1Repository accountRequest1Repository;
    @Autowired
    private ConsentMetricService consentMetricService;

    @Override
    public ResponseEntity<FRAccountRequest1> save(
            @RequestBody FRAccountRequest1 accountRequest1
    ) {
        LOGGER.debug("Create an account request {}", accountRequest1);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(accountRequest1.getId(), accountRequest1.getStatus().name()));
        return new ResponseEntity<>(accountRequest1Repository.save(accountRequest1), HttpStatus.OK);
    }

    @Override
    public ResponseEntity read(
            @PathVariable("accountRequestId") String accountRequestId
    ) {
        LOGGER.debug("get an account request {}", accountRequestId);
        Optional<FRAccountRequest1> isAccountRequest = accountRequest1Repository.findById(accountRequestId);
        if (isAccountRequest.isPresent()) {
            return ResponseEntity.ok(isAccountRequest.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
