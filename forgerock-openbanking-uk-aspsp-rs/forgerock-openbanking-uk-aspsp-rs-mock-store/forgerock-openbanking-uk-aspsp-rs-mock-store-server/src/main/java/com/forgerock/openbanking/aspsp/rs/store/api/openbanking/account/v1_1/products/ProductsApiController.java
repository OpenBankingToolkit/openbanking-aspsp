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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v1_1.products;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.products.FRProductRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRProduct;
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
import uk.org.openbanking.datamodel.account.OBProduct1;
import uk.org.openbanking.datamodel.account.OBReadDataProduct1;
import uk.org.openbanking.datamodel.account.OBReadProduct1;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRExternalPermissionsCodeConverter.toFRExternalPermissionsCodeList;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRProductConverter.toOBProduct1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("ProductsApiV1.1")
public class ProductsApiController implements ProductsApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsApiController.class);

    @Value("${rs.page.default.products.size}")
    private int PAGE_LIMIT_PRODUCTS;
    @Autowired
    private FRProductRepository frProductRepository;

    @Override
    public ResponseEntity<OBReadProduct1> getAccountProduct(
            @PathVariable("AccountId") String accountId,

            @RequestParam(value = "page", defaultValue = "0") int page,

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) {
        LOGGER.info("Read product for account  {} with minimumPermissions {}", accountId, permissions);

        Page<FRProduct> productsResponse = frProductRepository.byAccountIdWithPermissions(accountId, toFRExternalPermissionsCodeList(permissions), PageRequest.of(page, PAGE_LIMIT_PRODUCTS));
        List<OBProduct1> products = productsResponse.getContent().stream()
                .map(b -> toOBProduct1(b.getProduct()))
                .collect(Collectors.toList());

        int totalPage = productsResponse.getTotalPages();

        return ResponseEntity.ok(new OBReadProduct1()
                .data(new OBReadDataProduct1().product(products))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPage))
                .meta(PaginationUtil.generateMetaData(totalPage)));
    }

    @Override
    public ResponseEntity<OBReadProduct1> getProducts(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. " +
                    "The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        LOGGER.info("Reading products from account ids {}", accountIds);
        Page<FRProduct> frProducts = frProductRepository.byAccountIdInWithPermissions(accountIds, toFRExternalPermissionsCodeList(permissions), PageRequest.of(page, PAGE_LIMIT_PRODUCTS));

        int totalPage = frProducts.getTotalPages();

        return ResponseEntity.ok(new OBReadProduct1()
                .data(new OBReadDataProduct1()
                        .product(frProducts.getContent().stream()
                                .map(p -> toOBProduct1(p.getProduct()))
                                .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPage))
                .meta(PaginationUtil.generateMetaData(totalPage)));
    }
}
