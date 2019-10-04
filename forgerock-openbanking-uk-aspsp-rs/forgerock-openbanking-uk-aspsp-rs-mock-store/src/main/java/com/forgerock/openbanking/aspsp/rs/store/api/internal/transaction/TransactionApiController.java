/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.transaction;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
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
    private FRTransaction5Repository transactionRepository;

    @Override
    public ResponseEntity<FRTransaction5> create(
            @RequestBody FRTransaction5 transaction
    ) {
        LOGGER.debug("Create transaction {}", transaction);
        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.OK);
    }
}
