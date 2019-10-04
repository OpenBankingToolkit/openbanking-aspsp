/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.account.scheduledpayment;

import com.forgerock.openbanking.common.model.openbanking.status.ScheduledPaymentStatus;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRScheduledPayment1;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.OBScheduledPayment1;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class ScheduledPaymentService {
    private static final String BASE_RESOURCE_PATH = "/api/accounts/scheduled-payments/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public ScheduledPaymentService(RestTemplate restTemplate,
                                   @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public void createSchedulePayment(OBScheduledPayment1 scheduledPayment,String pispId) {
        log.debug("Create a scheduled payment in the store. {}", scheduledPayment);
        FRScheduledPayment1 frScheduledPayment = FRScheduledPayment1.builder()
                .scheduledPayment(scheduledPayment)
                .id(scheduledPayment.getScheduledPaymentId())
                .accountId(scheduledPayment.getAccountId())
                .status(ScheduledPaymentStatus.PENDING)
                .pispId(pispId)
                .build();
        restTemplate.postForObject(rsStoreRoot + BASE_RESOURCE_PATH, frScheduledPayment, String.class);
    }

    public Collection<FRScheduledPayment1> getPendingAndDueScheduledPayments() {
        log.debug("Get pending scheduled payments in the store.");
        ParameterizedTypeReference<List<FRScheduledPayment1>> ptr =
                new ParameterizedTypeReference<List<FRScheduledPayment1>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/find"
        );
        builder.queryParam("status", ScheduledPaymentStatus.PENDING);
        builder.queryParam("toDate", DateTime.now().toString(ISODateTimeFormat.dateTimeNoMillis()));
        URI uri = builder.build().encode().toUri();
        log.debug("Calling URI: {}", uri);
        ResponseEntity<List<FRScheduledPayment1>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);
        return entity.getBody();
    }

    public void updateSchedulePayment(FRScheduledPayment1 scheduledPayment) {
        log.debug("Update a scheduled payment in the store. {}", scheduledPayment);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH+"/"+scheduledPayment.getId(), scheduledPayment);
    }


}
