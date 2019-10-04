/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.balance;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRBalance;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRBalance1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;

import java.net.URI;
import java.util.Optional;

@Service
public class BalanceStoreServiceImpl implements BalanceStoreService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Override
    public Optional getBalance(String accountId, OBBalanceType1Code type) {

        ParameterizedTypeReference<Optional<FRBalance1>> ptr = new ParameterizedTypeReference<Optional<FRBalance1>>(){};

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/balances/search/findByAccountId");
        builder.queryParam("accountId", accountId);
        builder.queryParam("type", type.name());
        URI uri = builder.build().encode().toUri();

        ResponseEntity<Optional<FRBalance1>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    /**
     * update balance
     *
     * @param balance a balance
     */
    @Override
    public void updateBalance(FRBalance balance) {
        HttpEntity<FRBalance> request = new HttpEntity<>(balance, new HttpHeaders());
        restTemplate.exchange(
                rsStoreRoot + "/api/balances/",
                HttpMethod.PUT, request, Void.class);
    }
}
