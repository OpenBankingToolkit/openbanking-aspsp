/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as;

import brave.Tracer;
import com.forgerock.openbanking.authentication.configurers.MultiAuthenticationCollectorConfigurer;
import com.forgerock.openbanking.authentication.configurers.collectors.StaticUserCollector;
import com.forgerock.openbanking.common.error.ErrorHandler;
import com.forgerock.openbanking.common.services.security.FormValueSanitisationFilter;
import com.forgerock.openbanking.common.services.security.JsonRequestSanitisiationFilter;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.ssl.config.SslConfiguration;
import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
@EnableScheduling
@EnableWebSecurity
@ComponentScan(basePackages = {"com.forgerock"})
@EnableMongoRepositories(basePackages = "com.forgerock")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ForgerockOpenbankingAsApiApplication {

    public static void main(String[] args) {
        new SpringApplication(ForgerockOpenbankingAsApiApplication.class).run(args);
    }

    @Bean
    public Filter jsonSanitisationFilter(ErrorHandler errorHandler, Tracer tracer) {
        return new JsonRequestSanitisiationFilter(errorHandler, tracer);
    }

    @Bean
    public Filter formSanitisationFilter(ErrorHandler errorHandler, Tracer tracer) {
        return new FormValueSanitisationFilter(errorHandler, tracer);
    }

    @Bean
    public RestTemplate restTemplate(SslConfiguration sslConfiguration, @Value("${server.ssl.client-certs-key-alias}") String keyAlias) throws SslConfigurationFailure {
        RestTemplate restTemplate = new RestTemplate(sslConfiguration.factory(keyAlias, true));
        return restTemplate;
    }

    @Configuration
    static class CookieWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable() // We don't need CSRF for JWT based authentication
                    .authorizeRequests().anyRequest().permitAll()
                    .and()
                    .authenticationProvider(new CustomAuthProvider())
                    .apply(new MultiAuthenticationCollectorConfigurer<HttpSecurity>()
                            .collector(StaticUserCollector.builder()
                                    .usernameCollector( () -> "DemoTPP")
                                    .grantedAuthorities(Stream.of(
                                            OBRIRole.ROLE_PISP,
                                            OBRIRole.ROLE_AISP,
                                            OBRIRole.ROLE_CBPII
                                    ).collect(Collectors.toSet()))
                                    .build())
                    )
            ;
        }
    }

    public static class CustomAuthProvider implements AuthenticationProvider {

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            //You can load more GrantedAuthority based on the user subject, like loading the TPP details from the software ID
            return authentication;
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return true;
        }
    }

}
