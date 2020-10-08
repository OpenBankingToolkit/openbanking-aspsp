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

import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventSubscriptionData;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.event.OBEventSubscription1;
import uk.org.openbanking.datamodel.event.OBEventSubscriptionResponse1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

@Slf4j
public class EventValidationService {

    public static void verifyValidCallbackUrl(final OBEventSubscriptionResponse1 obEventSubscription) throws OBErrorException {
        Preconditions.checkNotNull(obEventSubscription, "There should not be a null request body here");
        verifyValidCallbackUrl(obEventSubscription.getData().getCallbackUrl());
    }

    public static void verifyValidCallbackUrl(final OBEventSubscription1 obEventSubscription) throws OBErrorException {
        Preconditions.checkNotNull(obEventSubscription, "There should not be a null request body here");
        verifyValidCallbackUrl(obEventSubscription.getData().getCallbackUrl());
    }

    public static void verifyValidCallbackUrl(final String callbackUrl) throws OBErrorException {
        // It is valid to not use a callback URL here as TPP may be using polling only. But, if submitted, callback URL must be valid.
        if (!StringUtils.isEmpty(callbackUrl)) {
            try {
                // Will throw exception is bad URL
                new URL(callbackUrl);

                // Already checked URL above so just check path must end /<OB_Version>/event-subscriptions
                String regex = "[^\\s]+\\/(v(\\d+\\.)?(\\d+\\.)?(\\*|\\d+))\\/event-notifications$";
                boolean matches = Pattern.matches(regex, callbackUrl);
                if (!matches) {
                    log.debug("Event subscription callback URL must end with /{OB_VERSION>/event-notifications (e.g. /v3.1.1/). Submitted callback: was '{}'", callbackUrl);
                    throw new OBErrorException(OBRIErrorType.INVALID_CALLBACK_URL, callbackUrl);
                }
            } catch (MalformedURLException e) {
                log.debug("Event subscription callback URL is malformed. Submitted callback: was '{}'", callbackUrl, e);
                throw new OBErrorException(OBRIErrorType.INVALID_CALLBACK_URL, callbackUrl);
            }
        }
    }

    /**
     * Throws exception if the new version is an older version than existing version.
     * @param existingSubscription Existing
     * @param newSubscription New
     */
    public static void checkEqualOrNewerVersion(final FREventSubscriptionData existingSubscription, final FREventSubscriptionData newSubscription) throws OBErrorResponseException {
        String existingVersion = existingSubscription.getVersion();
        String newVersion = newSubscription.getVersion();

        try {

            if ( versionCompare(newVersion, existingVersion) < 0) {
                throw new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.INVALID_CALLBACK_URL_VERSION.toOBError1(existingVersion,newVersion));
            }
        } catch (NumberFormatException nfe) {
            log.warn("Unable to check the versions on event subscriptions. They should be both be numbers. Existing version: {}, new version: {}", existingVersion, newVersion, nfe);
        }

    }

    private  static int versionCompare(String str1, String str2) {
        try (Scanner s1 = new Scanner(str1); Scanner s2 = new Scanner(str2)) {
            s1.useDelimiter("\\.");
            s2.useDelimiter("\\.");

            while (s1.hasNextInt() && s2.hasNextInt()) {
                int v1 = s1.nextInt();
                int v2 = s2.nextInt();
                if (v1 < v2) {
                    return -1;
                } else if (v1 > v2) {
                    return 1;
                }
            }

            if (s1.hasNextInt() && s1.nextInt() != 0)
                return 1;
            if (s2.hasNextInt() && s2.nextInt() != 0)
                return -1;
        }
        return 0;
    }

}
