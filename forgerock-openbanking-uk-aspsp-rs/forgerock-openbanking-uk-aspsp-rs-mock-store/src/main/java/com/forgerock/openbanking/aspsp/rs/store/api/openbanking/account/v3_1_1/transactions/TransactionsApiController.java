/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.transactions;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRTransaction5;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadTransaction5;
import uk.org.openbanking.datamodel.account.OBReadTransaction5Data;
import uk.org.openbanking.datamodel.account.OBTransaction5;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.*;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

@Controller("TransactionsApiV3.1.1")
@Slf4j
public class TransactionsApiController implements TransactionsApi {

    @Value("${rs.page.default.transaction.size}")
    private int PAGE_LIMIT_TRANSACTIONS;
    @Autowired
    private FRTransaction5Repository frTransaction5Repository;
    @Autowired
    private AccountDataInternalIdFilter accountDataInternalIdFilter;

    @Override
    public ResponseEntity<OBReadTransaction5> getAccountTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true )
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional - set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional - set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-first-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime firstAvailableDate,

            @RequestHeader(value = "x-ob-last-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime lastAvailableDate,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Read transactions for account  {} with minimumPermissions {}", accountId,
                permissions);
        log.debug("transactionStore request transactionFrom {} transactionTo {} ",
                fromBookingDateTime, toBookingDateTime);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction5> response = frTransaction5Repository.byAccountIdAndBookingDateTimeBetweenWithPermissions(accountId,
                fromBookingDateTime, toBookingDateTime, permissions, PageRequest.of(page, PAGE_LIMIT_TRANSACTIONS, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction5> transactions = response.getContent()
                .stream()
                .map(FRTransaction5::getTransaction)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = response.getTotalPages();

        return ResponseEntity.ok(new OBReadTransaction5()
                .data(new OBReadTransaction5Data().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getTransactions(
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

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-first-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime firstAvailableDate,

            @RequestHeader(value = "x-ob-last-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime lastAvailableDate,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Reading transations from account ids {}, fromBookingDate {} toBookingDate {} " +
                        "minimumPermissions {} pageNumber {} ", accountIds,
                fromBookingDateTime, toBookingDateTime, permissions, page);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction5> body = frTransaction5Repository.byAccountIdInAndBookingDateTimeBetweenWithPermissions(accountIds,
                fromBookingDateTime, toBookingDateTime, permissions, PageRequest.of(page, PAGE_LIMIT_TRANSACTIONS, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction5> transactions = body.getContent()
                .stream()
                .map(FRTransaction5::getTransaction)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = body.getTotalPages();

        return  ResponseEntity.ok(new OBReadTransaction5().data(new OBReadTransaction5Data().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }

    @Override
    public ResponseEntity<OBReadTransaction5> getAccountStatementTransactions(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "A unique identifier used to identify the statement resource.",required=true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime fromBookingDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter transactions TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_BOOKING_DATE_TIME, required = false)
            @DateTimeFormat(pattern = BOOKED_TIME_DATE_FORMAT) DateTime toBookingDateTime,

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

            @RequestHeader(value = "x-ob-first-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime firstAvailableDate,

            @RequestHeader(value = "x-ob-last-available-date", required = false)
            @DateTimeFormat(pattern = AVAILABLE_DATE_FORMAT) DateTime lastAvailableDate,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Reading transations from account id {}, statement id {}, fromBookingDate {} toBookingDate {} " +
                        "minimumPermissions {} pageNumber {} ", accountId, statementId,
                fromBookingDateTime, toBookingDateTime, permissions, page);

        if (toBookingDateTime == null) {
            toBookingDateTime = DateTime.now();
        }
        if (fromBookingDateTime == null) {
            fromBookingDateTime = toBookingDateTime.minusYears(100);
        }

        Page<FRTransaction5> response = frTransaction5Repository.byAccountIdAndStatementIdAndBookingDateTimeBetweenWithPermissions(accountId, statementId,
                fromBookingDateTime, toBookingDateTime, permissions, PageRequest.of(page, PAGE_LIMIT_TRANSACTIONS, Sort.Direction.ASC, "bookingDateTime"));

        List<OBTransaction5> transactions = response.getContent()
                .stream()
                .map(FRTransaction5::getTransaction)
                .map(t -> accountDataInternalIdFilter.apply(t))
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = response.getTotalPages();

        return ResponseEntity.ok(new OBReadTransaction5().data(new OBReadTransaction5Data().transaction(transactions))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages, firstAvailableDate, lastAvailableDate)));
    }
}
