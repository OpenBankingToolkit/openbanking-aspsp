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
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.beneficiaries;

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0.FRBeneficiary2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.List;

@Repository
public class FRBeneficiary2RepositoryImpl implements FRBeneficiary2RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRBeneficiary2RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRBeneficiary2Repository beneficiaryRepository;

    @Override
    public Page<FRBeneficiary2> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions,
                                                          Pageable pageable) {
        return filter(beneficiaryRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRBeneficiary2> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions,
                                                            Pageable pageable) {
        return filter(beneficiaryRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRBeneficiary2> filter(Page<FRBeneficiary2> beneficiaries, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            switch (permission) {

                case READBENEFICIARIESBASIC:
                    for (FRBeneficiary2 beneficiary: beneficiaries) {
                        beneficiary.getBeneficiary().creditorAccount(null);
                    }
                    break;
            }
        }
        return beneficiaries;
    }
}
