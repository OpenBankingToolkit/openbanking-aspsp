/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.beneficiaries;

import com.forgerock.openbanking.commons.model.openbanking.v2_0.account.FRBeneficiary2;
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
