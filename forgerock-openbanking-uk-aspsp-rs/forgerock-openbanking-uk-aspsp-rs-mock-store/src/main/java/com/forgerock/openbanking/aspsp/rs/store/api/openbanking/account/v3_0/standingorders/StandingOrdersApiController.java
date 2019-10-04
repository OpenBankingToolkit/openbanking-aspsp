/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_0.standingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.standingorders.FRStandingOrder5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.commons.model.openbanking.v3_0.account.FRStandingOrder3;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRStandingOrder5;
import com.forgerock.openbanking.commons.services.openbanking.converter.FRStandingOrderConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadStandingOrder3;
import uk.org.openbanking.datamodel.account.OBReadStandingOrder3Data;
import uk.org.openbanking.datamodel.account.OBStandingOrder3;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("StandingOrdersApiV3.0")
public class StandingOrdersApiController implements StandingOrdersApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandingOrdersApiController.class);

    @Value("${rs.page.default.standing-order.size}")
    private int PAGE_LIMIT_STANDING_ORDERS;

    @Autowired
    private FRStandingOrder5Repository frStandingOrderRepository;

    @Autowired
    private AccountDataInternalIdFilter accountDataInternalIdFilter;

    @Override
    public ResponseEntity<OBReadStandingOrder3> getAccountStandingOrders(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true )
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        LOGGER.info("Read standing orders for account {} with minimumPermissions {}",
                accountId, permissions);
        Page<FRStandingOrder5> standingOrdersResponse =
                frStandingOrderRepository.byAccountIdWithPermissions(accountId, permissions,
                        PageRequest.of(page, PAGE_LIMIT_STANDING_ORDERS));
        List<OBStandingOrder3> standingOrders = standingOrdersResponse.stream()
                .map(FRStandingOrderConverter::toStandingOrder3)
                .map(FRStandingOrder3::getStandingOrder)
                .map(so -> accountDataInternalIdFilter.apply(so))
                .collect(Collectors.toList());

        int totalPages = standingOrdersResponse.getTotalPages();

        return ResponseEntity.ok(new OBReadStandingOrder3()
                .data(new OBReadStandingOrder3Data().standingOrder(standingOrders))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<OBReadStandingOrder3> getStandingOrders(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        LOGGER.info("Reading standing orders from account ids {}", accountIds);
        Page<FRStandingOrder5> standingOrdersResponse = frStandingOrderRepository.byAccountIdInWithPermissions(accountIds, permissions,
                PageRequest.of(page, PAGE_LIMIT_STANDING_ORDERS));
        List<OBStandingOrder3> standingOrders = standingOrdersResponse.stream()
                .map(FRStandingOrderConverter::toStandingOrder3)
                .map(FRStandingOrder3::getStandingOrder)
                .map(so -> accountDataInternalIdFilter.apply(so))
                .collect(Collectors.toList());

        int totalPages = standingOrdersResponse.getTotalPages();

        return ResponseEntity.ok(new OBReadStandingOrder3()
                .data(new OBReadStandingOrder3Data().standingOrder(standingOrders))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }
}
