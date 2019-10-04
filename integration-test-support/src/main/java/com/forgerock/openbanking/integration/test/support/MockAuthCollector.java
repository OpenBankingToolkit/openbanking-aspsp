/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.integration.test.support;

import com.forgerock.openbanking.authentication.configurers.AuthCollector;
import com.forgerock.openbanking.authentication.model.authentication.PasswordLessUserNameAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class MockAuthCollector implements AuthCollector {
    private Authentication authentication;

    public void mockAuthorities(GrantedAuthority ... authorities) {
        authentication = new PasswordLessUserNameAuthentication("test-tpp", Arrays.asList(authorities));
    }

    public void mockUser(String username, GrantedAuthority ... authorities) {
        authentication = new PasswordLessUserNameAuthentication(username, Arrays.asList(authorities));
    }

    @Override
    public Authentication collectAuthentication(HttpServletRequest request) {
        return authentication;
    }

    @Override
    public Authentication collectAuthorisation(HttpServletRequest req, Authentication currentAuthentication) {
        return authentication;
    }
}
