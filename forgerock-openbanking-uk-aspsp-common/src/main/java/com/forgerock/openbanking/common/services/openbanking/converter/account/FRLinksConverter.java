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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRLinks;
import uk.org.openbanking.datamodel.account.Links;

public class FRLinksConverter {

    // OB to FR
    public static FRLinks toFRLinks(Links links) {
        return links == null ? null : FRLinks.builder()
                .self(links.getSelf())
                .first(links.getFirst())
                .prev(links.getPrev())
                .next(links.getNext())
                .last(links.getLast())
                .build();
    }

    // FR to OB
    public static Links toLinks(FRLinks links) {
        return links == null ? null : new Links()
                .self(links.getSelf())
                .first(links.getFirst())
                .prev(links.getPrev())
                .next(links.getNext())
                .last(links.getLast());
    }
}
