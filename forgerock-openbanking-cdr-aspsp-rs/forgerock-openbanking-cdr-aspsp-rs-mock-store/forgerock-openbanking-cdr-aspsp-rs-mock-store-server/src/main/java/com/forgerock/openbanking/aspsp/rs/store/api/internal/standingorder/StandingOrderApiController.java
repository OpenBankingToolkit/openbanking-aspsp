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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.standingorder;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
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
public class StandingOrderApiController implements StandingOrderApi {
    private FRStandingOrder5Repository standingOrderRepository;

    @Autowired
    public StandingOrderApiController(FRStandingOrder5Repository standingOrderRepository) {
        this.standingOrderRepository = standingOrderRepository;
    }

    @Override
    public ResponseEntity<FRStandingOrder5> create(
            @RequestBody FRStandingOrder5 standingOrder) {
        log.debug("Create standing order {}", standingOrder);
        return new ResponseEntity<>(standingOrderRepository.save(standingOrder), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity update(
            @RequestBody FRStandingOrder5 standingOrder,
            @PathVariable("id") String id) {
        Preconditions.checkArgument(id.equals(standingOrder.getId()), "id in URL does not match id in provided update");
        log.debug("Update standing order {}", standingOrder);

        Optional<FRStandingOrder5> byId = standingOrderRepository.findById(id);
        if (byId.isPresent()) {
            standingOrder.setId(id);
            return ResponseEntity.ok(standingOrderRepository.save(standingOrder));
        } else {
            log.debug("Standing order not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Standing order not found: '"+id+"'");
        }
    }

    @Override
    public ResponseEntity<List<FRStandingOrder5>> getActive() {
        log.debug("Find Active Standing Orders");
        return ResponseEntity.ok(standingOrderRepository.findByStatusIn(ImmutableList.of(StandingOrderStatus.PENDING, StandingOrderStatus.ACTIVE)));
    }
}
