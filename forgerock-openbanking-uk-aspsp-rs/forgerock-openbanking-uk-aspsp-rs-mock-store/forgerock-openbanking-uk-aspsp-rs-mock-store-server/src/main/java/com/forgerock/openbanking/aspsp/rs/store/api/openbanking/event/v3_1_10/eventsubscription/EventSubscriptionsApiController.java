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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_10.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.store.repository.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.event.EventResponseUtil;
import com.forgerock.openbanking.repositories.TppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("EventSubscriptionApiV3.1.10")
public class EventSubscriptionsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_9.eventsubscription.EventSubscriptionsApiController implements EventSubscriptionsApi {

    @Autowired
    public EventSubscriptionsApiController(EventSubscriptionsRepository eventSubscriptionsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        super(eventSubscriptionsRepository, tppRepository, resourceLinkService, new EventResponseUtil(resourceLinkService, OBVersion.v3_1_9, true));
    }

    public EventSubscriptionsApiController(EventSubscriptionsRepository eventSubscriptionsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService, EventResponseUtil eventResponseUtil) {
        super(eventSubscriptionsRepository, tppRepository, resourceLinkService, eventResponseUtil);
    }

}
