/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.service.event;

import com.forgerock.openbanking.aspsp.rs.store.repository.FRPendingEventsRepository;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.event.FREventNotification;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import uk.org.openbanking.datamodel.event.OBEventPolling1;
import uk.org.openbanking.datamodel.event.OBEventPolling1SetErrs;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EventPollingServiceTest {
    private static final String TPP_ID = "abc123";

    private FRPendingEventsRepository mockRepo;
    private EventPollingService eventPollingService;

    @Before
    public void setUp() throws Exception {
        mockRepo = mock(FRPendingEventsRepository.class);
        eventPollingService = new EventPollingService(mockRepo);
    }

    @Test
    public void acknowledgeEvents_findAndDeleteTwoEvents_ignoreEventNotfound() {
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .ack(ImmutableList.of("11111", "22222", "NotFound"));

        // When
        eventPollingService.acknowledgeEvents(pollingRequest, TPP_ID);

        // Then
        verify(mockRepo).deleteByTppIdAndJti(eq(TPP_ID), eq("11111"));
        verify(mockRepo).deleteByTppIdAndJti(eq(TPP_ID), eq("11111"));
    }

    @Test
    public void acknowledgeEvents_emptyList() {
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .ack(Collections.emptyList());

        // When
        eventPollingService.acknowledgeEvents(pollingRequest, TPP_ID);

        // Then
        verifyZeroInteractions(mockRepo);
    }

    @Test
    public void acknowledgeEvents_nullList() {
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .ack(null);

        // When
        eventPollingService.acknowledgeEvents(pollingRequest, TPP_ID);

        // Then
        verifyZeroInteractions(mockRepo);
    }

    @Test
    public void recordTppEventErrors_notificationExists_addError() {
        // Given
        final FREventNotification existingNotification = FREventNotification.builder()
                .id("1")
                .jti("11111")
                .errors(null)
                .build();
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .setErrs(Collections.singletonMap("11111", new OBEventPolling1SetErrs().err("err1").description("error msg")));
        when(mockRepo.findByTppIdAndJti(any(), any())).thenReturn(Optional.of(existingNotification));

        // When
        eventPollingService.recordTppEventErrors(pollingRequest, TPP_ID);

        // Then
        verify(mockRepo, times(1)).save(any());
        assertThat(existingNotification.getErrors().getErr()).isEqualTo("err1");
        assertThat(existingNotification.getErrors().getDescription()).isEqualTo("error msg");
    }

    @Test
    public void recordTppEventErrors_notificationDoesNotExist_doNothing() {
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .setErrs(Collections.singletonMap("11111", new OBEventPolling1SetErrs().err("err1").description("error msg")));
        when(mockRepo.findByTppIdAndJti(any(), any())).thenReturn(Optional.empty());

        // When
        eventPollingService.recordTppEventErrors(pollingRequest, TPP_ID);

        // Then
        verify(mockRepo, times(0)).save(any());
    }

    @Test
    public void recordTppEventErrors_noErrors_doNothing() {
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .setErrs(null);

        // When
        eventPollingService.recordTppEventErrors(pollingRequest, TPP_ID);

        // Then
        verifyZeroInteractions(mockRepo);
    }

    @Test
    public void fetchNewEvents_getAll() throws Exception{
        // Given
        final FREventNotification existingNotification1 = FREventNotification.builder()
                .id("1")
                .jti("11111")
                .signedJwt("test111")
                .build();
        final FREventNotification existingNotification2 = FREventNotification.builder()
                .id("2")
                .jti("22222")
                .signedJwt("test222")
                .build();
        when(mockRepo.findByTppId(eq(TPP_ID))).thenReturn(ImmutableList.of(existingNotification1, existingNotification2));

        // When
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .maxEvents(100)
                .returnImmediately(true);
        Map<String, String> eventNotifications = eventPollingService.fetchNewEvents(pollingRequest, TPP_ID);

        // Then
        assertThat(eventNotifications.size()).isEqualTo(2);
        assertThat(eventNotifications.get("11111")).isEqualTo("test111");
        assertThat(eventNotifications.get("22222")).isEqualTo("test222");
    }

    @Test
    public void fetchNewEvents_excludeEventsWithErrorsFromResults() throws Exception{
        // Given
        final FREventNotification existingNotificationWithoutError = FREventNotification.builder()
                .id("1")
                .jti("11111")
                .signedJwt("test111")
                .build();
        final FREventNotification existingNotificationWithError = FREventNotification.builder()
                .id("2")
                .jti("22222")
                .signedJwt("test222")
                .errors(new OBEventPolling1SetErrs().err("err1").description("error"))
                .build();
        when(mockRepo.findByTppId(eq(TPP_ID))).thenReturn(ImmutableList.of(existingNotificationWithoutError, existingNotificationWithError));

        // When
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .maxEvents(null) // Do not restrict
                .returnImmediately(true);
        Map<String, String> eventNotifications = eventPollingService.fetchNewEvents(pollingRequest, TPP_ID);

        // Then
        assertThat(eventNotifications.size()).isEqualTo(1);
        assertThat(eventNotifications.get("11111")).isEqualTo("test111");
    }

    @Test
    public void fetchNewEvents_zeroEventsRequested_returnNothing() throws Exception{
        // When
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .maxEvents(0)
                .returnImmediately(true);
        Map<String, String> eventNotifications = eventPollingService.fetchNewEvents(pollingRequest, TPP_ID);

        // Then
        assertThat(eventNotifications.size()).isEqualTo(0);
        verifyZeroInteractions(mockRepo);
    }


    @Test
    public void fetchNewEvents_longPollingRequest_rejectUnsupported() throws Exception{
        // Given
        OBEventPolling1 pollingRequest = new OBEventPolling1()
                .maxEvents(10)
                .returnImmediately(false);

        assertThatThrownBy(() ->
                // When
                eventPollingService.fetchNewEvents(pollingRequest, TPP_ID))
                // Then
                .isInstanceOf(OBErrorResponseException.class);
    }

    @Test
    public void truncateEvents_threeEventsExist_maxOfTwo_truncateEventsToTwo() {
        // Given
        final int maxEventsParam = 2;
        ImmutableMap<String, String> allEventsMap = ImmutableMap.of("11111", "jwt1", "22222", "jwt2", "33333", "jwt3");

        // When
        Map<String, String> truncatedEvents = eventPollingService.truncateEvents(maxEventsParam, allEventsMap, TPP_ID);

        // Then
        assertThat(truncatedEvents.size()).isEqualTo(2);
    }

    @Test
    public void truncateEvents_noEvents_doNothing() {
        // When
        Map<String, String> truncatedEvents = eventPollingService.truncateEvents(2, Collections.emptyMap(), TPP_ID);

        // Then
        assertThat(truncatedEvents.size()).isEqualTo(0);
    }
}