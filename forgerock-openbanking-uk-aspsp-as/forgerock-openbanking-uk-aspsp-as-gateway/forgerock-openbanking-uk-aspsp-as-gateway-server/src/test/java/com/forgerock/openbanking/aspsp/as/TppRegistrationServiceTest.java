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
package com.forgerock.openbanking.aspsp.as;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class TppRegistrationServiceTest {

    @org.junit.Test
    public void parseNullContacts() throws ParseException {
        String emailAddress = "fred.bloggs@forgerock.com";
        StringBuilder contact = new StringBuilder();
        HashMap<String, String> contactData = new HashMap<String, String>();
        contactData.put("email", "fred.bloggs@forgerock.com");
        JSONObject contactJsonObject = new JSONObject(contactData);
        String actualAddress = JSONObjectUtils.getString(contactJsonObject, "emails");
        assertThat(actualAddress).isEqualTo(null);
    }

    @org.junit.Test
    public void testGetFieldValue(){

    }
}