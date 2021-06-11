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

import brave.Tracer;
import com.forgerock.openbanking.common.error.ErrorHandler;
import com.forgerock.openbanking.common.services.security.FormValueSanitisationFilter;
import com.forgerock.openbanking.common.services.security.JsonRequestSanitisiationFilter;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.error.ClientResponseErrorHandler;
import com.forgerock.openbanking.ssl.config.SslConfiguration;
import com.forgerock.openbanking.ssl.exceptions.SslConfigurationFailure;
import com.forgerock.spring.security.multiauth.configurers.MultiAuthenticationCollectorConfigurer;
import com.forgerock.spring.security.multiauth.configurers.collectors.StaticUserCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableWebSecurity
@ComponentScan(basePackages = {"com.forgerock"})
@EnableMongoRepositories(basePackages = "com.forgerock")
public class ForgerockOpenbankingAsApiApplication {

    @Autowired
    private SslConfiguration sslConfiguration;
    @Value("${server.ssl.client-certs-key-alias}")
    private String keyAlias;

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
    public RestTemplate restTemplate(@Qualifier("mappingJacksonHttpMessageConverter") MappingJackson2HttpMessageConverter converter) throws SslConfigurationFailure {
        RestTemplate restTemplate = new RestTemplate(sslConfiguration.factory(keyAlias, true));
        customiseRestTemplate(converter, restTemplate);
        return restTemplate;
    }

    private void customiseRestTemplate(@Qualifier("mappingJacksonHttpMessageConverter") MappingJackson2HttpMessageConverter converter, RestTemplate restTemplate) {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        messageConverters.add(converter);
        restTemplate.setErrorHandler(new ClientResponseErrorHandler());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter(@Qualifier("objectMapperBuilderCustomizer") Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();
        objectMapperBuilderCustomizer.customize(objectMapperBuilder);
        converter.setObjectMapper(objectMapperBuilder.build());
        return converter;
    }

    @Configuration
    @Order(1)
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
