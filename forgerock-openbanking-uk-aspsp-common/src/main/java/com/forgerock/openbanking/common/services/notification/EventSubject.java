/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The subject of an event notifications. e.g. a completed payment, revoked consent etc
 */
@Data
@AllArgsConstructor
@Builder
public class EventSubject {
    private final String id;
    private final String version;
    private final String url;
    private final String type;
}
