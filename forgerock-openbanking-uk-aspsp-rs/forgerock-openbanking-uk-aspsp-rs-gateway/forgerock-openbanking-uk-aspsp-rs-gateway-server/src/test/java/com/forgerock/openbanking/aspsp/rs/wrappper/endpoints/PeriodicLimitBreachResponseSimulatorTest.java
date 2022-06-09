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

import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.DomesticVrpPaymentsEndpointWrapper.PeriodicLimitBreachResponseSimulator;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetailsData;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPControlParameters;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits.PeriodAlignmentEnum;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits.PeriodTypeEnum;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class PeriodicLimitBreachResponseSimulatorTest {
    private final PeriodicLimitBreachResponseSimulator limitBreachSimulator = new PeriodicLimitBreachResponseSimulator();

    @Test
    public void testSimulateLimitBreach() {
        final FRDomesticVRPConsent consent = createConsent(createPeriodicLimit("GBP", "20", PeriodTypeEnum.WEEK, PeriodAlignmentEnum.CONSENT),
                createPeriodicLimit("GBP", "80", PeriodTypeEnum.MONTH, PeriodAlignmentEnum.CONSENT),
                createPeriodicLimit("GBP", "500.00", PeriodTypeEnum.YEAR, PeriodAlignmentEnum.CONSENT));

        OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class,
                () -> limitBreachSimulator.processRequest("Year-Consent", consent));
        Assert.assertEquals(OBRIErrorType.REQUEST_VRP_CONTROL_PARAMETERS_PAYMENT_PERIODIC_LIMIT_BREACH.getCode().getValue(),
                obErrorException.getOBError().getErrorCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, obErrorException.getObriErrorType().getHttpStatus());
        Assert.assertEquals("Unable to complete payment due to payment limit breach, periodic limit of '500.00' 'GBP' for period 'Year' 'Consent' has been breached",
                obErrorException.getMessage());
    }

    @Test
    public void testUnsupportedHeaderValueFails() {
        String[] badHeaderValues = {
                null, // NOTE: it is the job of the caller to check for nulls
                "",
                " ",
                "a",
                "badValue",
        };
        for (String badHeaderValue : badHeaderValues) {
            OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class,
                    () -> limitBreachSimulator.processRequest(badHeaderValue,
                            createConsent(createPeriodicLimit("GBP", "50.0", PeriodTypeEnum.DAY, PeriodAlignmentEnum.CALENDAR))));
            Assert.assertEquals("Error processing header value: " + badHeaderValue,
                                ErrorCode.OBRI_REQUEST_VRP_LIMIT_BREACH_SIMULATION_INVALID_HEADER_VALUE.getValue(),
                                obErrorException.getOBError().getErrorCode());
            Assert.assertEquals(HttpStatus.BAD_REQUEST, obErrorException.getObriErrorType().getHttpStatus());
            Assert.assertEquals("Invalid Header value '" + badHeaderValue+ "', unable to simulate the payment limitation breach",
                                obErrorException.getMessage());
        }
    }

    @Test
    public void testSimulateLimitBreachHeaderValueNotInConsentFails() {
        OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class,
                () -> limitBreachSimulator.processRequest("Year-Consent",
                        createConsent(createPeriodicLimit("EUR", "50.00", PeriodTypeEnum.DAY, PeriodAlignmentEnum.CALENDAR),
                                      createPeriodicLimit("EUR", "100.00", PeriodTypeEnum.MONTH, PeriodAlignmentEnum.CALENDAR))));
        Assert.assertEquals(OBRIErrorType.REQUEST_VRP_LIMIT_BREACH_SIMULATION_NO_MATCHING_LIMIT_IN_CONSENT.getCode().getValue(),
                obErrorException.getOBError().getErrorCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, obErrorException.getObriErrorType().getHttpStatus());
        Assert.assertEquals("No Periodic Limit found in the consent for Header value 'Year-Consent', unable to simulate the payment limitation breach",
                obErrorException.getMessage());
    }

    @Test
    public void testSimulateLimitBreachNoLimitsOnConsentFails() {
        OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class,
                () -> limitBreachSimulator.processRequest("Month-Calendar", createConsent()));
        Assert.assertEquals(OBRIErrorType.REQUEST_VRP_LIMIT_BREACH_SIMULATION_NO_MATCHING_LIMIT_IN_CONSENT.getCode().getValue(),
                obErrorException.getOBError().getErrorCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, obErrorException.getObriErrorType().getHttpStatus());
        Assert.assertEquals("No Periodic Limit found in the consent for Header value 'Month-Calendar', unable to simulate the payment limitation breach",
                obErrorException.getMessage());
    }

    private FRDomesticVRPConsent createConsent(FRPeriodicLimits... periodicLimits) {
        final FRDomesticVRPConsent consent = new FRDomesticVRPConsent();
        final FRDomesticVRPConsentDetails vrpDetails = new FRDomesticVRPConsentDetails();
        final FRDomesticVRPConsentDetailsData data = new FRDomesticVRPConsentDetailsData();
        final FRDomesticVRPControlParameters controlParameters = new FRDomesticVRPControlParameters();
        if (periodicLimits != null) {
            controlParameters.setPeriodicLimits(Arrays.asList(periodicLimits));
        }
        data.setControlParameters(controlParameters);
        vrpDetails.setData(data);
        consent.setVrpDetails(vrpDetails);
        return consent;
    }

    private FRPeriodicLimits createPeriodicLimit(String currency, String amount, PeriodTypeEnum periodType,
                                                 PeriodAlignmentEnum periodAlignment) {
        final FRPeriodicLimits limit = new FRPeriodicLimits();
        limit.setPeriodAlignment(periodAlignment);
        limit.setPeriodType(periodType);
        limit.setCurrency(currency);
        limit.setAmount(amount);
        return limit;
    }
}
