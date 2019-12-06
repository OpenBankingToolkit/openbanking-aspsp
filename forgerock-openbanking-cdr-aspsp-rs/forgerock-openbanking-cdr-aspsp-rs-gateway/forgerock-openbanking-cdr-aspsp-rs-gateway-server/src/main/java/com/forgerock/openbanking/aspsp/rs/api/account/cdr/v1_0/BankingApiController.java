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
package com.forgerock.openbanking.aspsp.rs.api.account.cdr.v1_0;

import com.forgerock.openbanking.aspsp.rs.model.v0_9.*;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BankingApiController implements BankingApi {

    @Autowired
    private RsStoreGateway rsStoreGateway;
    @Autowired
    private com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService aispAccountRestEndpoint;

    @Override
    public ResponseEntity<ResponseBankingAccountList> listAccounts(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String productCategory,
            @Valid String openStatus,
            @Valid Boolean isOwned,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READACCOUNTSBASIC, OBExternalPermissions1Code.READACCOUNTSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingAccountList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingAccountById> getAccountDetail(String accountId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READACCOUNTSBASIC, OBExternalPermissions1Code.READACCOUNTSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingAccountById.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingPayeeById> getPayeeDetail(String payeeId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READPARTY, OBExternalPermissions1Code.READPARTYPSU)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingPayeeById.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingProductById> getProductDetail(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String productId, String xV, String xMinV,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READPRODUCTS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingProductById.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingTransactionById> getTransactionDetail(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String accountId,
            String transactionId,
            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READTRANSACTIONSCREDITS, OBExternalPermissions1Code.READTRANSACTIONSDEBITS, OBExternalPermissions1Code.READTRANSACTIONSBASIC, OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingTransactionById.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingTransactionList> getTransactions(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String accountId,
            String xV,
            @Valid String oldestTime,
            @Valid String newestTime,
            @Valid String minAmount,
            @Valid String maxAmount,
            @Valid String text,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READTRANSACTIONSCREDITS, OBExternalPermissions1Code.READTRANSACTIONSDEBITS, OBExternalPermissions1Code.READTRANSACTIONSBASIC, OBExternalPermissions1Code.READTRANSACTIONSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingTransactionList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceById> listBalance(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String accountId,
            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READBALANCES)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingAccountsBalanceById.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceList> listBalancesBulk(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String productCategory,
            @Valid String openStatus,
            @Valid Boolean isOwned,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READBALANCES)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingAccountsBalanceList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceList> listBalancesSpecificAccounts(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @Valid RequestAccountIds body,
            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,
            @Valid Integer page,
            @Valid Integer pageSize,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READBALANCES)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingAccountsBalanceList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebits(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String accountId,
            String xV,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READDIRECTDEBITS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingDirectDebitAuthorisationList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebitsBulk(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String productCategory,
            @Valid String openStatus,
            @Valid Boolean isOwned,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READDIRECTDEBITS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingDirectDebitAuthorisationList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebitsSpecificAccounts(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @Valid RequestAccountIds body,
            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,
            @Valid Integer page,
            @Valid Integer pageSize,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READDIRECTDEBITS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingDirectDebitAuthorisationList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingPayeeList> listPayees(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String type,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READPARTYPSU, OBExternalPermissions1Code.READPARTY)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingPayeeList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingProductList> listProducts(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String effective,
            @Valid String updatedSince,
            @Valid String brand,
            @Valid String productCategory,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READPRODUCTS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingProductList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPayments(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String accountId,
            String xV,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .page(page.toString())
                .principal(principal)
                .minimumPermissions(OBExternalPermissions1Code.READSCHEDULEDPAYMENTSBASIC, OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingScheduledPaymentsList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPaymentsBulk(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            String xV,
            @Valid String productCategory,
            @Valid String openStatus,
            @Valid Boolean isOwned,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READSCHEDULEDPAYMENTSBASIC, OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingScheduledPaymentsList.class);
                        }
                );
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPaymentsSpecificAccounts(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @Valid RequestAccountIds body,
            String xV,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,
            @Valid Integer page,
            @Valid Integer pageSize,

            HttpServletRequest request,

            Principal principal) throws OBErrorResponseException {
        return aispAccountRestEndpoint.accountAndTransactionEndpoint()
                .authorization(authorization)
                .principal(principal)
                .page(page.toString())
                .minimumPermissions(OBExternalPermissions1Code.READSCHEDULEDPAYMENTSBASIC, OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read all accounts behind account request {} with permissions {}", accountRequest,
                                    permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, ResponseBankingScheduledPaymentsList.class);
                        }
                );
    }
}
