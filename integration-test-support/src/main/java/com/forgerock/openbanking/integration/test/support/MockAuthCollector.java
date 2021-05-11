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
package com.forgerock.openbanking.integration.test.support;

import com.forgerock.spring.security.multiauth.configurers.AuthCollector;
import com.forgerock.spring.security.multiauth.model.authentication.AuthenticationWithEditableAuthorities;
import com.forgerock.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class MockAuthCollector implements AuthCollector {
    private PasswordLessUserNameAuthentication authentication;

    public void mockAuthorities(GrantedAuthority ... authorities) {
        authentication = new PasswordLessUserNameAuthentication("test-tpp", Arrays.asList(authorities));
    }

    public void mockUser(String username, GrantedAuthority ... authorities) {
        authentication = new PasswordLessUserNameAuthentication(username, Arrays.asList(authorities));
    }

    @Override
    public String collectorName() {
        return "mock";
    }

    @Override
    public AuthenticationWithEditableAuthorities collectAuthentication(HttpServletRequest request) {
        return authentication;
    }

    @Override
    public AuthenticationWithEditableAuthorities collectAuthorisation(HttpServletRequest req, AuthenticationWithEditableAuthorities currentAuthentication) {
        return authentication;
    }

    @Override
    public boolean isSetupForAuthentication() {
        return true;
    }

    @Override
    public boolean isSetupForAuthorisation() {
        return true;
    }

}
