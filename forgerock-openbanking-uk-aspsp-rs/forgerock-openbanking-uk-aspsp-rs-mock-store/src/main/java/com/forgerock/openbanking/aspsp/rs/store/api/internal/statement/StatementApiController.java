/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.statement;

import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements.FRStatement1Repository;
import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRStatement1;
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
