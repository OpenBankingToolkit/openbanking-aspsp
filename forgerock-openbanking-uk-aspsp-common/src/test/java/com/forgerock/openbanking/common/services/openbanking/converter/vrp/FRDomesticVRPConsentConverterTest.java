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
package com.forgerock.openbanking.common.services.openbanking.converter.vrp;

import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetails;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsentDetailsData;
import com.github.jsonzou.jmockdata.JMockData;
import org.junit.Test;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequestData;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link FRDomesticVRPConsentConverter}.
 */
public class FRDomesticVRPConsentConverterTest {

    @Test
    public void convertOBDomesticVRPConsentRequestToFR() {
        // given
        OBDomesticVRPConsentRequest vrpConsentRequest = getOBDomesticVRPConsentRequest();
        FRDomesticVRPConsentDetails frDomesticVRPConsentDetails = FRDomesticVRPConsentConverter.toFRDomesticVRPConsentDetails(vrpConsentRequest);
        OBDomesticVRPConsentRequestData obData = vrpConsentRequest.getData();

        // when
        FRDomesticVRPConsentDetailsData frData = frDomesticVRPConsentDetails.getData();

        // then (check few fields)
        assertThat(frData.getReadRefundAccount().getValue()).isEqualTo(obData.getReadRefundAccount().getValue());
        assertThat(frData.getInitiation().getCreditorAccount().getIdentification())
                .isEqualTo(obData.getInitiation().getCreditorAccount().getIdentification());
        assertThat(frData.getInitiation().getDebtorAccount().getIdentification())
                .isEqualTo(obData.getInitiation().getDebtorAccount().getIdentification());
        assertThat(frData.getControlParameters().getVrpType().size()).isEqualTo(obData.getControlParameters().getVrPType().size());
        assertThat(frData.getControlParameters().getPeriodicLimits().get(0).getPeriodType().getValue())
                .isEqualTo(obData.getControlParameters().getPeriodicLimits().get(0).getPeriodType().getValue());
        assertThat(frData.getControlParameters().getPeriodicLimits().get(0).getPeriodAlignment().getValue())
                .isEqualTo(obData.getControlParameters().getPeriodicLimits().get(0).getPeriodAlignment().getValue());
    }

    private OBDomesticVRPConsentRequest getOBDomesticVRPConsentRequest() {
        return JMockData.mock(OBDomesticVRPConsentRequest.class);
    }
}
