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
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.event.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Util class to handler the Event Notification API responses and conditional checks thru all versions<br />
 */
@Slf4j
public class EventResponseUtil {

    private final ResourceLinkService resourceLinkService;
    public final OBVersion version;
    public final boolean haveMetaSection;

    public EventResponseUtil(ResourceLinkService resourceLinkService, OBVersion version, boolean haveMetaSection) {
        this.resourceLinkService = resourceLinkService;
        this.haveMetaSection = haveMetaSection;
        this.version = version;
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
                                        // The resource can be accessed from an equal or newer api version instanced
                                        // filtering the resource can be accessed (resource version <= api version instanced)
                                        .filter(it -> isAccessToResourceAllowedFromApiVersion(it.obCallbackUrl.getData().getVersion()))
                                        .map(this::toOBCallbackUrlResponseData1)
                                        .collect(Collectors.toList())
                        )
                ).meta(shouldHaveMetaSection() ? new Meta() : null)
                .links(
                        (shouldHaveMetaSection() ? (!frCallbackUrls.isEmpty() ? toSelfLink(frCallbackUrls.stream().findFirst().get()) : null) : null)
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
                .meta(shouldHaveMetaSection() ? new Meta() : null)
                .links(shouldHaveMetaSection() ? toSelfLink(frCallbackUrl) : null);
    }

    /**
     * Check if is allowed to invoke the operation or access the resources comparing the Api version with the event resource version <br />
     * A TPP must not allowed to invoke a operation of event from an older api version if the resource was created in a newer version. <br />
     * This method is being used also to filter the resources that can be accessed by the api instanced <br />
     * @param resourceVersion the value from version field contained on the event resource
     * @return true when the operation is allow to invoke, otherwise false
     */
    public boolean isAccessToResourceAllowedFromApiVersion(String resourceVersion) {
        return this.version.equals(OBVersion.fromString(resourceVersion)) || this.version.isAfterVersion(OBVersion.fromString(resourceVersion));
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

    private boolean shouldHaveMetaSection() {
        return haveMetaSection && version != null;
    }
}
