/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.error.exception.PermissionDenyException;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AccountsApiEndpointWrapper<T extends AccountsApiEndpointWrapper<T, R>, R> extends  RSEndpointWrapper<T, R> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsApiEndpointWrapper.class);

    protected List<OBExternalPermissions1Code> minimumPermissions;
    protected String page = "";
    protected String accountId = null;
    protected FRAccountRequest accountRequest;
    protected DateTime fromBookingDateTime;
    protected DateTime toBookingDateTime;

    public AccountsApiEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService) {
        super(rsEndpointWrapperService);
    }

    public T minimumPermissions(OBExternalPermissions1Code... permissions) {
        this.minimumPermissions = Arrays.asList(permissions);
        return (T) this;
    }

    public T accountId(String accountId) {
        this.accountId = accountId;
        return (T) this;
    }

    public T page(String page) {
        this.page = page;
        return (T) this;
    }

    public T fromBookingDateTime(DateTime fromBookingDateTime) {
        this.fromBookingDateTime = fromBookingDateTime;
        return (T) this;
    }

    public T toBookingDateTime(DateTime toBookingDateTime) {
        this.toBookingDateTime = toBookingDateTime;
        return (T) this;
    }

    protected int getPageNumber() throws OBErrorException {
        if (page != null && !page.isEmpty()) {
            LOGGER.info("Parse the page number {} received in string into an integer", page);
            try {
                return Integer.valueOf(page);
            } catch (NumberFormatException e) {
                LOGGER.info("Page number {} wasn't an integer", page);
                throw new OBErrorException(OBRIErrorType.INVALID_PAGE_NUMBER);
            }
        }
        return 0;
    }

    public void verifyMatlsFromAccountRequest() throws OBErrorException {
        //MTLS check. We verify that the certificate is associated with the expected AISP ID
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        if (!currentUser.getUsername().equals(getAccountRequest().getClientId())) {
            LOGGER.warn("AISP ID from account request {} is not the one associated with the certificate {}",
                    getAccountRequest().getClientId(), currentUser.getUsername());
            throw new OBErrorException(OBRIErrorType.MATLS_TPP_AUTHENTICATION_INVALID_FROM_ACCOUNT_REQUEST,
                    currentUser.getUsername(),
                    getAccountRequest().getClientId()
            );
        }
        LOGGER.info("AISP ID {} has been verified against X509 certificate (MTLS)", currentUser.getUsername());
    }

    public void verifyAccountRequestStatus() throws OBErrorException {
        OBExternalRequestStatus1Code status = getAccountRequest().getStatus();
        switch (status) {
            case AWAITINGAUTHORISATION:
                LOGGER.info("Account request hasn't been authorised yet. Account request: {}", getAccountRequest());
                throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_WAITING_PSU_CONSENT,
                        status
                );
            case REJECTED:
                LOGGER.info("Account request hasn't been rejected. Account request: {}", getAccountRequest());
                throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_REJECTED,
                        status
                );
            case REVOKED:
                LOGGER.info("Account request was revoked. Account request: {}", getAccountRequest());
                throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_REVOKED,
                        status
                );
            case AUTHORISED:
                LOGGER.info("Account request is authorised. Account request: {}", getAccountRequest());
                //The account request was indeed approved by the PSU!
        }
    }

    public void verifyExpirationTime() throws OBErrorException {
        @Valid DateTime expirationDateTime = getAccountRequest().getExpirationDateTime();
        if (expirationDateTime != null
                && expirationDateTime.isBeforeNow()) {
            LOGGER.debug("Account request {} expired", getAccountRequest().getId());
            throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_EXPIRED,
                    expirationDateTime
            );
        }
    }

    public FRAccountRequest getAccountRequest() throws OBErrorException {

        if (accountRequest == null) {
            try {
                LOGGER.info("We introspect the access token locally, as it is a JWS");
                String accountRequestId = rsEndpointWrapperService.accessTokenService.getIntentId(accessToken);

                LOGGER.info("Account request id {}", accountRequestId);
                Optional<FRAccountRequest> isAccountRequest = rsEndpointWrapperService.accountRequestStore.get(accountRequestId);
                if (!isAccountRequest.isPresent()) {
                    LOGGER.warn("Couldn't not find the account request {}", accountRequestId);
                    throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_NOT_FOUND,
                            accountRequestId
                    );
                }
                accountRequest = isAccountRequest.get();
            } catch (ParseException | IOException e) {
                LOGGER.warn("Could not parse the claims of the access token '{}'", accessToken.serialize());
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
            }
        }
        return accountRequest;
    }

    public void verifyAccountId() throws OBErrorException {
        if (accountId != null && !getAccountRequest().getAccountIds().contains(accountId)) {
            LOGGER.warn("TPP is trying to access an account {} not allowed by this account request {}.", accountId, getAccountRequest());
            throw new OBErrorException(OBRIErrorType.UNAUTHORISED_ACCOUNT,
                    accountId,
                    getAccountRequest().getId(),
                    getAccountRequest().getAccountIds()
            );

        }
    }

    public void verifyPermissions() throws OBErrorException {
        try {
            rsEndpointWrapperService.accessTokenService.isAllowed(getAccountRequest(), minimumPermissions);
        } catch (PermissionDenyException e) {
                LOGGER.warn("Permission deny for this endpoint. The AISP is not authorise to use this endpoint.");
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_INVALID,
                    minimumPermissions,
                    getAccountRequest().getPermissions()
            );
        }
    }
}
