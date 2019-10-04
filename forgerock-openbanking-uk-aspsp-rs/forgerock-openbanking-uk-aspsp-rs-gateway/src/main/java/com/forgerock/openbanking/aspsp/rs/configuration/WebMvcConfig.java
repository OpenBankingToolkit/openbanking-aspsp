/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.configuration;

import com.forgerock.openbanking.aspsp.rs.filter.AuthorisationHeaderInterceptor;
import com.forgerock.openbanking.aspsp.rs.filter.DisabledEndpointInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 @Configuration
 public class WebMvcConfig implements WebMvcConfigurer {

     @Autowired
     DisabledEndpointInterceptor disabledEndpointInterceptor;
     @Autowired
     AuthorisationHeaderInterceptor authorisationHeaderInterceptor;

     @Override
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(disabledEndpointInterceptor);
         registry.addInterceptor(authorisationHeaderInterceptor).addPathPatterns("/open-banking/v*/**");
     }
}
