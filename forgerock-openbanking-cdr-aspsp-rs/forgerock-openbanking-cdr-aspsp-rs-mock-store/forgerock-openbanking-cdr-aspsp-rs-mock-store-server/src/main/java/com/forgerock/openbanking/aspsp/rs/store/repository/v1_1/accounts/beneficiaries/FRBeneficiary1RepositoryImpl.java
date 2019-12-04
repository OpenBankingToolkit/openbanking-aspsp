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
package com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.beneficiaries;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBeneficiary1;
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
public class FRBeneficiary1RepositoryImpl implements FRBeneficiary1RepositoryCustom {
    private static final Logger LOGGER = LoggerFactory.getLogger(FRBeneficiary1RepositoryImpl.class);

    @Autowired
    @Lazy
    private FRBeneficiary1Repository beneficiaryRepository;

    @Override
    public Page<FRBeneficiary1> byAccountIdWithPermissions(String accountId, List<OBExternalPermissions1Code> permissions,
                                                          Pageable pageable) {
        return filter(beneficiaryRepository.findByAccountId(accountId, pageable), permissions);
    }

    @Override
    public Page<FRBeneficiary1> byAccountIdInWithPermissions(List<String> accountIds, List<OBExternalPermissions1Code> permissions,
                                                            Pageable pageable) {
        return filter(beneficiaryRepository.findByAccountIdIn(accountIds, pageable), permissions);
    }

    private Page<FRBeneficiary1> filter(Page<FRBeneficiary1> beneficiaries, List<OBExternalPermissions1Code> permissions) {
        for (OBExternalPermissions1Code permission : permissions) {
            switch (permission) {

                case READBENEFICIARIESBASIC:
                    for (FRBeneficiary1 beneficiary : beneficiaries) {
                        beneficiary.getBeneficiary().creditorAccount(null);
                        beneficiary.getBeneficiary().setServicer(null);
                    }
                    break;
            }
        }
        return beneficiaries;
    }
}
