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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.party;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.party.FRParty2Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRParty2;
import com.forgerock.openbanking.common.services.openbanking.converter.FRPartyConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadParty1;
import uk.org.openbanking.datamodel.account.OBReadParty1Data;

import java.util.List;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("PartyApiV2.0")
public class PartyApiController implements PartyApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartyApiController.class);

    @Autowired
    private FRParty2Repository frParty2Repository;

    @Override
    public ResponseEntity<OBReadParty1> getAccountParty(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,
            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )

            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {

        LOGGER.info("Read party for account {} with minimumPermissions {}", accountId, permissions);
        FRParty2 party = frParty2Repository.byAccountIdWithPermissions(accountId, permissions);
        int totalPages = 1;

        return ResponseEntity.ok(new OBReadParty1().data(new OBReadParty1Data().party(
                FRPartyConverter.toOBParty1(party.getParty())))
                .links(PaginationUtil.generateLinks(httpUrl, 0, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<OBReadParty1> getParty(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The OB user ID")
            @RequestHeader(value = "x-ob-user-id", required = true) String userId,

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {

        LOGGER.info("Reading party from user id {}", userId);
        FRParty2 party = frParty2Repository.byUserIdWithPermissions(userId, permissions);
        int totalPages = 1;

        return ResponseEntity.ok(new OBReadParty1().data(new OBReadParty1Data().party(
                FRPartyConverter.toOBParty1(party.getParty())))
                .links(PaginationUtil.generateLinks(httpUrl, 0, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

}
