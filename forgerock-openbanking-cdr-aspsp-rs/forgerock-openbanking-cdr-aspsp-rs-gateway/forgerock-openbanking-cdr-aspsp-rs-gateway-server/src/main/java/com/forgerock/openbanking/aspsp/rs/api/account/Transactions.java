/**
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
package com.forgerock.openbanking.aspsp.rs.api.account;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.BOOKED_TIME_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_BOOKING_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_BOOKING_DATE_TIME;

public class Transactions {
    public static Map<String, String> dateTransactionParameters(DateTime fromBookingDateTimeUPD, DateTime toBookingDateTimeUPD) {
        Map<String, String> params = new HashMap<>();
        if (fromBookingDateTimeUPD != null) {
            params.put(FROM_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(fromBookingDateTimeUPD));
        }
        if (toBookingDateTimeUPD != null) {
            params.put(TO_BOOKING_DATE_TIME, DateTimeFormat.forPattern(BOOKED_TIME_DATE_FORMAT).print(toBookingDateTimeUPD));
        }
        return params;
    }

    public static HttpHeaders defaultTransactionHttpHeaders(HttpServletRequest request, List<OBExternalPermissions1Code> permissions, DateTime transactionFrom, DateTime transactionTo) {
        HttpHeaders additionalHttpHeaders = new HttpHeaders();
        additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(OBExternalPermissions1Code::name).collect(Collectors.toList()));
        if (transactionFrom != null) {
            additionalHttpHeaders.add("x-ob-first-available-date", ISODateTimeFormat.dateTimeNoMillis().print(transactionFrom));
        }
        if (transactionTo != null) {
            additionalHttpHeaders.add("x-ob-last-available-date", ISODateTimeFormat.dateTimeNoMillis().print(transactionTo));
        }
        additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());
        return additionalHttpHeaders;
    }
}
