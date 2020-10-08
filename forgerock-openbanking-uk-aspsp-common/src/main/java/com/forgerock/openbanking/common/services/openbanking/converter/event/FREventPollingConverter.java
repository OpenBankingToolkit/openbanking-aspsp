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
package com.forgerock.openbanking.common.services.openbanking.converter.event;

import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventPolling;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventPollingError;
import uk.org.openbanking.datamodel.event.OBEventPolling1;
import uk.org.openbanking.datamodel.event.OBEventPolling1SetErrs;

import java.util.Map;
import java.util.stream.Collectors;

public class FREventPollingConverter {

    public static FREventPolling toFREventPolling(OBEventPolling1 obEventPolling) {
        return obEventPolling == null ? null : FREventPolling.builder()
                .maxEvents(obEventPolling.getMaxEvents())
                .returnImmediately(obEventPolling.isReturnImmediately())
                .ack(obEventPolling.getAck())
                .setErrs(toFREventPollingErrors(obEventPolling.getSetErrs()))
                .build();
    }

    public static Map<String, FREventPollingError> toFREventPollingErrors(Map<String, OBEventPolling1SetErrs> setErrs) {
        return setErrs == null ? null : setErrs.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> toFREventPollingError(e.getValue())));

    }

    public static FREventPollingError toFREventPollingError(OBEventPolling1SetErrs eventPolling1SetErrs) {
        return eventPolling1SetErrs == null ? null : FREventPollingError.builder()
                .error(eventPolling1SetErrs.getErr())
                .description(eventPolling1SetErrs.getDescription())
                .build();
    }
}
