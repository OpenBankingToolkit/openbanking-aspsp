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
package com.forgerock.openbanking.aspsp.rs.configuration;

import com.forgerock.openbanking.common.conf.discovery.GenericOBDiscoveryAPILinks;
import com.forgerock.openbanking.common.conf.discovery.RSAPIsConfigurationProperties;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.ant;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-11T14:17:28.882Z")

@Configuration
public class SwaggerDocumentationConfig {

    @Value("${swagger.title}")
    public String swaggerTitle;
    @Value("${swagger.description}")
    public String swaggerDescription;
    @Value("${swagger.license}")
    public String swaggerLicense;
    @Value("${swagger.license-url}")
    public String swaggerLicenseUrl;
    @Value("${swagger.terms-of-service-url}")
    public String swaggerTermsOfServiceUrl;
    @Value("${swagger.version}")
    public String swaggerVersion;
    @Value("${swagger.contact.name}")
    public String swaggerContactName;
    @Value("${swagger.contact.url}")
    public String swaggerContactUrl;
    @Value("${swagger.contact.email}")
    public String swaggerContactEmail;

    @Autowired
    private RSAPIsConfigurationProperties rsConfiguration;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerTitle)
                .description(swaggerDescription)
                .license(swaggerLicense)
                .licenseUrl(swaggerLicenseUrl)
                .termsOfServiceUrl(swaggerTermsOfServiceUrl)
                .contact(new Contact(swaggerContactName,swaggerContactUrl, swaggerContactEmail))
                .build();
    }

    @Bean
    public Docket customImplementation(){
        ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Principal.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.forgerock.openbanking.aspsp.rs.api"));

        List<Predicate> predicatedForPath = new ArrayList<>();
        for(OBDiscoveryAPI obDiscoveryAPI : rsConfiguration.getObDiscoveryAPIS()) {
            GenericOBDiscoveryAPILinks genericOBDiscoveryAPILinks = (GenericOBDiscoveryAPILinks) obDiscoveryAPI.getLinks();
            genericOBDiscoveryAPILinks.getLinks().values().forEach(link -> {
                         predicatedForPath.add(ant(link
                                 .replaceFirst(rsConfiguration.getRsBaseUrl(), "")
                         ));
                    }
            );
        }
        return select
                .paths(Predicates.or(predicatedForPath.toArray(new Predicate[0])))
                .build()
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }

}
