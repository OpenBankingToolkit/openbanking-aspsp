/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.common.utils.DateTimeUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class TransactionsEndpointWrapper extends AccountsApiEndpointWrapper<TransactionsEndpointWrapper, TransactionsEndpointWrapper.Main> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsEndpointWrapper.class);



    public TransactionsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    @Override
    protected ResponseEntity run(Main main) throws OBErrorException {
        DateTime transactionFrom =
                accountRequest.getTransactionFromDateTime();
        DateTime transactionTo =
                accountRequest.getTransactionToDateTime();
        DateTime fromBookingDateTimeUPD =
                DateTimeUtils.getMaxDatetime(fromBookingDateTime, transactionFrom);
        DateTime toBookingDateTimeUPD =
                DateTimeUtils.getMinDatetime(toBookingDateTime, transactionTo);

        LOGGER.debug("transactionFrom {} transactionTo {} fromBookingDateTimeUPD {} toBookingDateTimeUPD {} ",
                transactionFrom, transactionTo, fromBookingDateTimeUPD, toBookingDateTimeUPD);

        return main.run(
                getAccountRequest(),
                getAccountRequest().getPermissions(),
                transactionFrom,
                transactionTo,
                fromBookingDateTimeUPD,
                toBookingDateTimeUPD,
                getPageNumber());
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.ACCOUNTS),
                Arrays.asList(
                        OIDCConstants.GrantType.AUTHORIZATION_CODE,
                        OIDCConstants.GrantType.HEADLESS_AUTH
                )
        );

        verifyMatlsFromAccountRequest();

        verifyAccountRequestStatus();

        verifyExpirationTime();

        verifyAccountId();

        verifyPermissions();

        verifyTransactionsPermissions();
    }

    public void verifyTransactionsPermissions() throws OBErrorException {
        @NotNull List<OBExternalPermissions1Code> permissions = getAccountRequest().getPermissions();
        if (permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                && permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSBASIC)) {
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "We can't allow basic and detail at the same time"
            );
        }
        if (!permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                && !permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSBASIC)) {
            LOGGER.error("We need the permission to get transactions information");
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "Need permission to get transaction information"
            );
        }

        if (!permissions.contains(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                && !permissions.contains(OBExternalPermissions1Code
                .READTRANSACTIONSDEBITS)) {
            LOGGER.error("We need the permission to debit or credit transactions");
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "Need permission for reading debit or credit transactions"
            );
        }
    }

    public interface Main {
        ResponseEntity run(
                FRAccountRequest accountRequest,
                List<OBExternalPermissions1Code> permissions,
                DateTime transactionFrom,
                DateTime transactionTo,
                DateTime fromBookingDateTimeUPD,
                DateTime toBookingDateTimeUPD,
                int pageNumber) throws OBErrorException;
    }
}
