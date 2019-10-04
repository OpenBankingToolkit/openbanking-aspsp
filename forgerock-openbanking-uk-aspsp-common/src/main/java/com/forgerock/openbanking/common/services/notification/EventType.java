/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.notification;

/**
 * Event types defined by the OB spec that are supported on this sandbox
 */
public enum EventType {
    /** ASPSP has updated a resource such as a consent - most commonly used for payment processing completion */
    RESOURCE_UPDATE_EVENT("urn:uk:org:openbanking:events:resource-update");

    private String eventName;

    EventType(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
