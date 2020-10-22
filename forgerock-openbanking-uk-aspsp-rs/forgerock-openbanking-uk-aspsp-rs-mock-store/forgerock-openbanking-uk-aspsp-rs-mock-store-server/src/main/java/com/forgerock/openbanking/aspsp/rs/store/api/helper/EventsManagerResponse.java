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
package com.forgerock.openbanking.aspsp.rs.store.api.helper;

import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.event.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventsManagerResponse {

    ResourceLinkService resourceLinkService;

    public EventsManagerResponse(ResourceLinkService resourceLinkService) {
        this.resourceLinkService = resourceLinkService;
    }

    private final OBCallbackUrlsResponse1 packageResponse(final Collection<FRCallbackUrl1> frCallbackUrls, final OBVersion version) {
        return new OBCallbackUrlsResponse1()
                .data(new OBCallbackUrlsResponseData1()
                        .callbackUrl(
                                frCallbackUrls.stream()
                                        .filter(EventsHelper.matchingVersion(version))
                                        .map(this::toOBCallbackUrlResponseData1)
                                        .collect(Collectors.toList())
                        )
                );
    }


    private final OBCallbackUrlResponse1 packageResponse(FRCallbackUrl1 frCallbackUrl) {
        return new OBCallbackUrlResponse1()
                .data(
                        toOBCallbackUrlResponseData1(frCallbackUrl)
                );
    }

    private final OBCallbackUrlResponse1 packageResponse2(FRCallbackUrl1 frCallbackUrl, OBVersion version) {
        return new OBCallbackUrlResponse1()
                .data(toOBCallbackUrlResponseData1(frCallbackUrl))
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(frCallbackUrl, discovery -> discovery.getVersion(OBVersion.v3_0).getGetCallbackUrls()));
    }

    private OBCallbackUrlResponseData1 toOBCallbackUrlResponseData1(FRCallbackUrl1 frCallbackUrl) {
        final OBCallbackUrlData1 data = frCallbackUrl.getObCallbackUrl().getData();
        return new OBCallbackUrlResponseData1()
                .callbackUrlId(frCallbackUrl.getId())
                .url(data.getUrl())
                .version(data.getVersion());
    }
}
