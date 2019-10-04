/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.tpp;


import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TppStoreServiceImpl implements TppStoreService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TppStoreServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Override
    public Optional<Tpp> findByCn(String cn) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/tpps/search/findByCertificateCn");
        builder.queryParam("certificateCn", cn);
        URI uri = builder.build().encode().toUri();
        LOGGER.debug("Find cn {}", cn);
        try {
            List<Tpp> tpps =  new ArrayList(restTemplate.exchange(uri, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Resources<Tpp>>() {}).getBody().getContent());
            if (tpps.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(tpps.get(0));
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public Optional<Tpp> findByClientId(String clientId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/tpps/search/findByClientId");
        builder.queryParam("clientId", clientId);
        URI uri = builder.build().encode().toUri();
        LOGGER.debug("Find client Id {}", clientId);
        try {
            ResponseEntity<Tpp> entity = restTemplate.exchange(uri, HttpMethod.GET, null, Tpp.class);
            return Optional.of(entity.getBody());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public Optional<Tpp> findById(String tppId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/tpps/" + tppId);
        URI uri = builder.build().encode().toUri();
        LOGGER.debug("Find tpp Id {}", tppId);
        try {
            ResponseEntity<Tpp> entity = restTemplate.exchange(uri, HttpMethod.GET, null, Tpp.class);
            log.debug("Fetched TPP: '{}' by id: {}", entity.getBody(), tppId);
            return Optional.of(entity.getBody());

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public Tpp createTpp(Tpp tpp) {
        LOGGER.debug("Create a new payment setup in the store. paymentSetup={}", tpp);
        HttpEntity<Tpp> request = new HttpEntity<>(tpp, new HttpHeaders());
        return restTemplate.postForObject(rsStoreRoot + "/tpps/", request, Tpp.class);
    }

    @Override
    public Tpp save(Tpp tpp) {
        LOGGER.debug("Create a new payment setup in the store. paymentSetup={}", tpp);
        HttpEntity<Tpp> request = new HttpEntity<>(tpp, new HttpHeaders());
        return restTemplate.exchange(rsStoreRoot + "/tpps/" + tpp.getId(), HttpMethod.PUT, request, Tpp.class).getBody();
    }

    @Override
    public void deleteTPP(Tpp tpp) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/tpps/" + tpp.getId());
        URI uri = builder.build().encode().toUri();
        LOGGER.debug("Delete TPP", tpp);
        restTemplate.exchange(uri, HttpMethod.DELETE, null, String.class);
    }

    @Override
    public List<Tpp> all() {
        ParameterizedTypeReference<PagedResources<Tpp>> ptr = new ParameterizedTypeReference<PagedResources<Tpp>>() {};
        ResponseEntity<PagedResources<Tpp>> entity;
        List<Tpp> tpps = new ArrayList<>();
        int currentPage = 0;
        do {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/tpps");
            builder.queryParam("page", currentPage);
            URI uri = builder.build().encode().toUri();
            entity = restTemplate.exchange(
                    uri, HttpMethod.GET, null, ptr);
            tpps.addAll(entity.getBody().getContent());
            currentPage++;
        } while (currentPage < entity.getBody().getMetadata().getTotalPages());
        return tpps;
    }

    @Override
    public List<Tpp> findByDateBetween(DateTime fromDate, DateTime toDate) {
        ParameterizedTypeReference<PagedResources<Tpp>> ptr = new ParameterizedTypeReference<PagedResources<Tpp>>() {};
        ResponseEntity<PagedResources<Tpp>> entity;
        List<Tpp> tpps = new ArrayList<>();
        int currentPage = 0;
        do {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(rsStoreRoot + "/tpps");
            builder.queryParam("page", currentPage);
            builder.queryParam("fromDate", fromDate);
            builder.queryParam("toDate", toDate);
            URI uri = builder.build().encode().toUri();
            entity = restTemplate.exchange(
                    uri, HttpMethod.GET, null, ptr);
            tpps.addAll(entity.getBody().getContent());
            currentPage++;
        } while (currentPage < entity.getBody().getMetadata().getTotalPages());
        return tpps;
    }


    @Override
    public List<Tpp> findByCreatedBetween(DateTime fromDate, DateTime toDate) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl
                (rsStoreRoot + "/tpps/search/findByCreatedBetween");
        builder.queryParam("from", fromDate.toString(ISODateTimeFormat.date()));
        builder.queryParam("to", toDate.plusMinutes(1).toString(ISODateTimeFormat.date()));
        URI uri = builder.build().encode().toUri();
        return  new ArrayList(restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<Resources<Tpp>>() {}).getBody().getContent());
    }

    @Override
    public String findPispIdByTppId(String tppId) throws OBErrorException {
        return findByClientId(tppId)
                .map(Tpp::getId)
                .orElseThrow(() -> new OBErrorException(OBRIErrorType.TPP_NOT_FOUND, tppId));
    }
}
