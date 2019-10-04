/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
