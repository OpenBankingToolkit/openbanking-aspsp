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

import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FRCallbackUrlData;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.extern.slf4j.Slf4j;
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinks;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksEventNotification3;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksEventNotification4;
import uk.org.openbanking.datamodel.event.OBCallbackUrlResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlResponseData1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponse1;
import uk.org.openbanking.datamodel.event.OBCallbackUrlsResponseData1;

import java.util.Collection;
import java.util.function.Function;
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
     * @param frCallbackUrls collection {@link FRCallbackUrl}
     * @return response collection object {@link OBCallbackUrlsResponse1}
     */
    public OBCallbackUrlsResponse1 packageResponse(final Collection<FRCallbackUrl> frCallbackUrls) {
        return new OBCallbackUrlsResponse1()
                .data(new OBCallbackUrlsResponseData1()
                        .callbackUrl(
                                frCallbackUrls.stream()
                                        // The resource can be accessed from an equal or newer api version instanced
                                        // filtering the resource can be accessed (resource version <= api version instanced)
                                        .filter(it -> isAccessToResourceAllowedFromApiVersion(it.callbackUrl.getVersion()))
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
     *         {@link FRCallbackUrl}
     * @return the callback object response
     */
    public OBCallbackUrlResponse1 packageResponse(FRCallbackUrl frCallbackUrl) {
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
     * @param frCallbackUrl {@link FRCallbackUrl}
     * @return object response {@link OBCallbackUrlResponseData1} mapped
     */
    private OBCallbackUrlResponseData1 toOBCallbackUrlResponseData1(FRCallbackUrl frCallbackUrl) {
        final FRCallbackUrlData data = frCallbackUrl.getCallbackUrl();
        return new OBCallbackUrlResponseData1()
                .callbackUrlId(frCallbackUrl.getId())
                .url(data.getUrl())
                .version(data.getVersion());
    }

    private Links toSelfLink(FRCallbackUrl frCallbackUrl) {
        return this.resourceLinkService.toSelfLink(frCallbackUrl, getUrlCallbackFunction());
    }

    private boolean shouldHaveMetaSection() {
        return haveMetaSection && version != null;
    }

    public Function<DiscoveryConfigurationProperties.EventNotificationApis, String> getUrlCallbackFunction() {
        return
                eventNotificationApis -> {
                    OBDiscoveryAPILinks obDiscoveryAPILinks = eventNotificationApis.getVersion(version);
                    if (obDiscoveryAPILinks instanceof OBDiscoveryAPILinksEventNotification4) {
                        return ((OBDiscoveryAPILinksEventNotification4) obDiscoveryAPILinks).getGetCallbackUrls();
                    } else {
                        return ((OBDiscoveryAPILinksEventNotification3) obDiscoveryAPILinks).getGetCallbackUrls();
                    }
                };
    }

    public Function<DiscoveryConfigurationProperties.EventNotificationApis, String> getUrlEventSubscriptionsFunction() {
        return
                eventNotificationApis -> {
                    OBDiscoveryAPILinks obDiscoveryAPILinks = eventNotificationApis.getVersion(version);
                    if (obDiscoveryAPILinks instanceof OBDiscoveryAPILinksEventNotification4) {
                        return ((OBDiscoveryAPILinksEventNotification4) obDiscoveryAPILinks).getCreateEventSubscription();
                    } else {
                        return ((OBDiscoveryAPILinksEventNotification3) obDiscoveryAPILinks).getGetCallbackUrls();
                    }
                };
    }
}
