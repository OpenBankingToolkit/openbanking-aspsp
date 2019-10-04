/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.rs.ui;

import brave.Tracer;
import com.forgerock.openbanking.commons.rest.ErrorHandler;
import com.forgerock.openbanking.commons.services.security.FormValueSanitisationFilter;
import com.forgerock.openbanking.commons.services.security.JsonRequestSanitisiationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.servlet.Filter;

@EnableDiscoveryClient

@SpringBootApplication(scanBasePackages = {"com.forgerock"})
@EnableMongoRepositories(basePackages = "com.forgerock")
public class ForgerockOpenbankingRsUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForgerockOpenbankingRsUiApplication.class, args);
	}

	@Bean
	public Filter jsonSanitisationFilter(ErrorHandler errorHandler, Tracer tracer) {
		return new JsonRequestSanitisiationFilter(errorHandler, tracer);
	}

	@Bean
	public Filter formSanitisationFilter(ErrorHandler errorHandler, Tracer tracer) {
		return new FormValueSanitisationFilter(errorHandler, tracer);
	}
}