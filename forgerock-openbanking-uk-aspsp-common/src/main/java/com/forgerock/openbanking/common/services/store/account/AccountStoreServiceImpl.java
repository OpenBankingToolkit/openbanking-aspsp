/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.account;

import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountStoreServiceImpl implements AccountStoreService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Override
    public FRAccount2 getAccount(String accountId) {
        ParameterizedTypeReference<FRAccount2> ptr = new ParameterizedTypeReference<FRAccount2>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/" + accountId);


        URI uri = builder.build().encode().toUri();
        ResponseEntity<FRAccount2> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    @Override
    public Optional findAccountByIdentification(String identification) {
        ParameterizedTypeReference<Optional<FRAccount2>> ptr = new ParameterizedTypeReference<Optional<FRAccount2>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByIdentification");
        builder.queryParam("identification", identification);

        URI uri = builder.build().encode().toUri();
        try {
            ResponseEntity<Optional<FRAccount2>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
            return entity.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public List<FRAccount2> get(String userID) {
        // This is necessary as auth server always uses lowercase user id
        String lowercaseUserId = userID.toLowerCase();
        log.debug("Searching for accounts with user ID: {}", lowercaseUserId);

        ParameterizedTypeReference<List<FRAccount2>> ptr = new ParameterizedTypeReference<List<FRAccount2>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByUserId");
        builder.queryParam("userId", lowercaseUserId);

        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRAccount2>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    @Override
    public List<FRAccountWithBalance> getAccountWithBalances(String userID) {
        // This is necessary as auth server always uses lowercase user id
        String lowercaseUserId = userID.toLowerCase();
        log.debug("Searching for accounts with user ID: {}", lowercaseUserId);

        ParameterizedTypeReference<List<FRAccountWithBalance>> ptr = new ParameterizedTypeReference<List<FRAccountWithBalance>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/accounts/search/findByUserId");
        builder.queryParam("userId", lowercaseUserId);
        builder.queryParam("withBalance", true);

        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRAccountWithBalance>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }
}
