/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.internal.scheduledpayment;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments.FRScheduledPayment2Repository;
import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ScheduledPaymentApiController implements ScheduledPaymentApi {

    private FRScheduledPayment2Repository scheduledPayment1Repository;

    @Autowired
    public ScheduledPaymentApiController(FRScheduledPayment2Repository scheduledPayment1Repository) {
        this.scheduledPayment1Repository = scheduledPayment1Repository;
    }

    public ResponseEntity<FRScheduledPayment2> create(
            @RequestBody FRScheduledPayment2 scheduledPayment
    ) {
        log.debug("Create scheduled payment {}", scheduledPayment);
        return new ResponseEntity<>(scheduledPayment1Repository.save(scheduledPayment), HttpStatus.CREATED);
    }

    public ResponseEntity update(
            @RequestBody FRScheduledPayment2 scheduledPayment,
            @PathVariable("id") String id
    ) {
        Preconditions.checkArgument(id.equals(scheduledPayment.getId()), "id in URL does not match id in provided update");
        log.debug("Update scheduled payment {}", scheduledPayment);

        Optional<FRScheduledPayment2> byId = scheduledPayment1Repository.findById(id);
        if (byId.isPresent()) {
            scheduledPayment.setId(id);
            return ResponseEntity.ok(scheduledPayment1Repository.save(scheduledPayment));
        } else {
            log.debug("Scheduled payment not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Scheduled payment not found: '"+id+"'");
        }

    }

    @Override
    public ResponseEntity<List<FRScheduledPayment2>> getAll(ScheduledPaymentStatus status, DateTime toDateTime) {
        log.debug("Find FR Scheduled Payment by status {} and before date/time: {}", status, toDateTime);
        return ResponseEntity.ok(scheduledPayment1Repository.findByStatus(status, toDateTime));
    }
}
