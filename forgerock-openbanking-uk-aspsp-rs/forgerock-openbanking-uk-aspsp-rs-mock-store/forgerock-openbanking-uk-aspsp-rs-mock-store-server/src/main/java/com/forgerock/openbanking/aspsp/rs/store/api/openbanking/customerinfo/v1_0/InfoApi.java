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
/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.customerinfo.v1_0;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-12-20T11:13:54.447312Z[Europe/London]")
@Validated
@Api(value = "info", description = "the info API")
@RequestMapping(value = "/customer-info/v1.0")
public interface InfoApi {

    /**
     * GET /info : Get Customer info details
     *
     * @param authorization An Authorisation Token as per https://tools.ietf.org/html/rfc6750 (required)
     * @param xFapiAuthDate The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC (optional)
     * @param xFapiCustomerIpAddress The PSU&#39;s IP address if the PSU is currently logged in with the TPP. (optional)
     * @param xFapiInteractionId An RFC4122 UID used as a correlation id. (optional)
     * @param xCustomerUserAgent Indicates the user-agent that the PSU is using. (optional)
     * @return Parties Read (status code 200)
     *         or Bad request (status code 400)
     *         or Unauthorized (status code 401)
     *         or Forbidden (status code 403)
     *         or Not found (status code 404)
     *         or Method Not Allowed (status code 405)
     *         or Not Acceptable (status code 406)
     *         or Too Many Requests (status code 429)
     *         or Internal Server Error (status code 500)
     */
    @ApiOperation(value = "Get Customer info details", nickname = "getCustomerInfo", notes = "", response = ReadCustomerInfo.class, authorizations = {
        @Authorization(value = "PSUOAuth2Security", scopes = {
            @AuthorizationScope(scope = "accounts", description = "Ability to read Accounts information") })
         }, tags={ "CustomerInfo", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Parties Read", response = ReadCustomerInfo.class),
        @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden", response = OBErrorResponse1.class),
        @ApiResponse(code = 404, message = "Not found"),
        @ApiResponse(code = 405, message = "Method Not Allowed"),
        @ApiResponse(code = 406, message = "Not Acceptable"),
        @ApiResponse(code = 429, message = "Too Many Requests"),
        @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class) })
    @RequestMapping(
            value = "/info",
            produces = { "application/json; charset=utf-8", "application/jose+jwe" },
            method = RequestMethod.GET
    )
    ResponseEntity<ReadCustomerInfo> getCustomerInfo(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" , required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,
            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are " +
                    "represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-auth-date", required=false) String xFapiAuthDate,
            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,
            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,
            @ApiParam(value = "Indicates the user-agent that the PSU is using." )
            @RequestHeader(value="x-customer-user-agent", required=false) String xCustomerUserAgent,
            @ApiParam(value = "An ID that the RS store can use to identify the PSU user that has given consent")
            @RequestHeader(value="x-ob-psu-user-id", required = true) String xObPsuUserId);


}