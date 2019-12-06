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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;


import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AccountAccessConsentPermittedPermissionsFilter {

    private final Set<OBExternalPermissions1Code> disabledPermissions;

    public AccountAccessConsentPermittedPermissionsFilter(@Value("${account-consent.permissions.disabled}") List<String> disabledPermissionStrings) {
        log.debug("Configured account consent permissions to be disabled is {}", disabledPermissionStrings);
        disabledPermissions = ImmutableSet.copyOf(
                disabledPermissionStrings.stream()
                        .filter(p -> !StringUtils.isEmpty(p))
                        .map(OBExternalPermissions1Code::fromValue)
                        .peek(p -> {
                            if (p==null) {
                                // Be strict and fail to start-up if any config values are not correct
                                log.error("Cannot parse: 'account-consent.permissions.disabled: {}' because some values do not match to OBExternalPermissions1Code values: {}", disabledPermissionStrings, OBExternalPermissions1Code.values());
                                throw new IllegalStateException("Cannot parse the configured values for 'account-consent.permissions.disabled' in rs-api yml configuration. See previous error log message for more details.");
                            }
                        })
                        .collect(Collectors.toSet())
        );
        log.debug("Actual permissions to be disabled after parsing values is {}");
    }

    public void filter(Collection<OBExternalPermissions1Code> requestedPermissions) throws OBErrorException {
        if (!disabledPermissions.isEmpty()) {
            Set<OBExternalPermissions1Code> forbiddenPermissions = requestedPermissions.stream()
                    .filter(disabledPermissions::contains)
                    .collect(Collectors.toSet());
            if (!forbiddenPermissions.isEmpty()) {
                log.debug("This Account Consent request will be rejected due to it containing the following disabled permissions: {}. Full list of disabled permissions in config is: {}", forbiddenPermissions, disabledPermissions);
                throw new OBErrorException(OBRIErrorType.REQUEST_ACCOUNT_ACCESS_CONSENT_PERMISSIONS_ARE_NOT_PERMITTED, forbiddenPermissions.toString());
            }
        }
    }
}
