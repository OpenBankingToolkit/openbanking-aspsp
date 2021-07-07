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
package com.forgerock.openbanking.common.error.exception.oauth2;

/**
 * Base class for all OAuth2Error responses body classes. This adds a text field that provides advice that is clearly
 * aimed at test facilities, and would not be seen in production systems.
 */
public class OAuth2ErrorAdviceResponseBody {

    public String getTest_facility_advice() {
        return test_facility_advice;
    }

    protected String test_facility_advice;

    public OAuth2ErrorAdviceResponseBody(String testFacilityAdviceMessage){
        this.test_facility_advice =  testFacilityAdviceMessage;
    }
}
