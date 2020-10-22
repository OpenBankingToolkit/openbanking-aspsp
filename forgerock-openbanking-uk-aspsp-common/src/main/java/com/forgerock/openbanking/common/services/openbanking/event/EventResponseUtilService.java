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
package com.forgerock.openbanking.common.services.openbanking.event;

import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.event.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Util Service class to handler the Event Notification API responses thru all versions<br />
 */
@Service
@Slf4j
public class EventResponseUtilService {

    private final ResourceLinkService resourceLinkService;
    private OBVersion version;
    private boolean shouldHaveMetaSection;

    public EventResponseUtilService(ResourceLinkService resourceLinkService) {
        this.resourceLinkService = resourceLinkService;
        this.shouldHaveMetaSection = true;
        this.version = null;
    }

    public OBVersion getVersion() {
        return version;
    }

    public EventResponseUtilService setVersion(OBVersion version) {
        this.version = version;
        return this;
    }

    public boolean isShouldHaveMetaSection() {
        return shouldHaveMetaSection;
    }

    public EventResponseUtilService setShouldHaveMetaSection(boolean shouldHaveMetaSection) {
        this.shouldHaveMetaSection = shouldHaveMetaSection;
        return this;
    }

    /**
     * Provides the response collection object {@link OBCallbackUrlsResponse1} filtered by version instanced
     * @param frCallbackUrls collection {@link FRCallbackUrl1}
     * @return response collection object {@link OBCallbackUrlsResponse1}
     */
    public OBCallbackUrlsResponse1 packageResponse(final Collection<FRCallbackUrl1> frCallbackUrls) {
        return new OBCallbackUrlsResponse1()
                .data(new OBCallbackUrlsResponseData1()
                        .callbackUrl(
                                frCallbackUrls.stream()
                                        .filter(it -> it.obCallbackUrl.getData().getVersion().equals(version.getCanonicalVersion()))
                                        .map(this::toOBCallbackUrlResponseData1)
                                        .collect(Collectors.toList())
                        )
                ).meta(checkConditionMetaAndVersion() ? new Meta() : null)
                .links(
                        (checkConditionMetaAndVersion() ? toSelfLink(frCallbackUrls.stream().findFirst().get()) : null)
                );
    }

    /**
     * Create and provide the response object {@link OBCallbackUrlResponse1} for the callback API<br />
     * If the version doesn't exist the 'meta' section will be null or empty
     * @param frCallbackUrl
     *         {@link FRCallbackUrl1}
     * @return the callback object response
     */
    public OBCallbackUrlResponse1 packageResponse(FRCallbackUrl1 frCallbackUrl) {
        return new OBCallbackUrlResponse1()
                .data(toOBCallbackUrlResponseData1(frCallbackUrl))
                .meta(checkConditionMetaAndVersion() ? new Meta() : null)
                .links(checkConditionMetaAndVersion() ? toSelfLink(frCallbackUrl) : null);
    }

    public boolean allowOperationByVersion(String versionToCompare) {
        return this.version.equals(OBVersion.fromString(versionToCompare)) || this.version.isAfterVersion(OBVersion.fromString(versionToCompare));
    }

    /**
     * Provides response Object mapping
     * @param frCallbackUrl {@link FRCallbackUrl1}
     * @return object response {@link OBCallbackUrlResponseData1} mapped
     */
    private OBCallbackUrlResponseData1 toOBCallbackUrlResponseData1(FRCallbackUrl1 frCallbackUrl) {
        final OBCallbackUrlData1 data = frCallbackUrl.getObCallbackUrl().getData();
        return new OBCallbackUrlResponseData1()
                .callbackUrlId(frCallbackUrl.getId())
                .url(data.getUrl())
                .version(data.getVersion());
    }

    private Links toSelfLink(FRCallbackUrl1 frCallbackUrl) {
        return this.resourceLinkService.toSelfLink(frCallbackUrl, discovery -> discovery.getVersion(version).getGetCallbackUrls());
    }

    private boolean checkConditionMetaAndVersion() {
        return isShouldHaveMetaSection() && version != null;
    }
}
