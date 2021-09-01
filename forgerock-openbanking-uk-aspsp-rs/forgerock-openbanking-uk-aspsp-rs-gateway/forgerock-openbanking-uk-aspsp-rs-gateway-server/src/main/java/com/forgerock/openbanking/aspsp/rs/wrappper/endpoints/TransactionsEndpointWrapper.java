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

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.DateTimeUtils;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class TransactionsEndpointWrapper extends AccountsApiEndpointWrapper<TransactionsEndpointWrapper, TransactionsEndpointWrapper.Main> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsEndpointWrapper.class);



    public TransactionsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                       TppStoreService tppStoreService) {
        super(RSEndpointWrapperService, tppStoreService);
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
        @NotNull List<FRExternalPermissionsCode> permissions = getAccountRequest().getPermissions();
        if (permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSDETAIL)
                && permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSBASIC)) {
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "We can't allow basic and detail at the same time"
            );
        }
        if (!permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSDETAIL)
                && !permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSBASIC)) {
            LOGGER.error("We need the permission to get transactions information");
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "Need permission to get transaction information"
            );
        }

        if (!permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSCREDITS)
                && !permissions.contains(FRExternalPermissionsCode.READTRANSACTIONSDEBITS)) {
            LOGGER.error("We need the permission to debit or credit transactions");
            throw new OBErrorException(OBRIErrorType.PERMISSIONS_TRANSACTIONS_INVALID,
                    "Need permission for reading debit or credit transactions"
            );
        }
    }

    public interface Main {
        ResponseEntity run(
                AccountRequest accountRequest,
                List<FRExternalPermissionsCode> permissions,
                DateTime transactionFrom,
                DateTime transactionTo,
                DateTime fromBookingDateTimeUPD,
                DateTime toBookingDateTimeUPD,
                int pageNumber) throws OBErrorException;
    }
}
