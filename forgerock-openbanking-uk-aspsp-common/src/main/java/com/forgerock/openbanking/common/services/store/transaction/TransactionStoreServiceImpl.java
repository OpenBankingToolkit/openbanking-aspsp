/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.transaction;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRTransaction5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionStoreServiceImpl implements TransactionStoreService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;
    /**
     * add transaction
     *
     * @param transaction a transaction
     */
    @Override
    public FRTransaction5 create(FRTransaction5 transaction) {
        HttpEntity<FRTransaction5> request = new HttpEntity<>(transaction, new HttpHeaders());
        return restTemplate.exchange(
                rsStoreRoot + "/api/transactions/", HttpMethod.POST, request, FRTransaction5.class).getBody();
    }

}
