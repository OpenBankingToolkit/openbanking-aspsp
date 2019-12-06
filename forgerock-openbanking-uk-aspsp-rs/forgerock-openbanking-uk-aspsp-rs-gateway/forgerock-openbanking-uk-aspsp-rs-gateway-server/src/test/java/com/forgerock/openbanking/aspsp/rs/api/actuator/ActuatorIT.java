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
package com.forgerock.openbanking.aspsp.rs.api.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.integration.test.support.SpringSecForTest;
import com.forgerock.openbanking.model.OBRIRole;
import kong.unirest.HttpResponse;
import kong.unirest.JacksonObjectMapper;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActuatorIT {

    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SpringSecForTest springSecForTest;



    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void actuatorHealthEndpointShouldReturnUp() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse<JsonNode> response = Unirest.get("https://rs-api:" + port + "/actuator/health")
                .asJson();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getObject().get("status")).isEqualTo("UP");
    }

    //@Test
    public void actuatorInfoEndpointShouldReturnBuildData() throws Exception {
        // Given
        springSecForTest.mockAuthCollector.mockAuthorities(OBRIRole.ROLE_PISP);

        // When
        HttpResponse<ActuatorInfo> response = Unirest.get("https://rs-api:" + port + "/actuator/info")
                .asObject(ActuatorInfo.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getGit().getBranch()).isNotNull();
        assertThat(response.getBody().getGit().getCommit()).isNotNull();
        assertThat(response.getBody().getBuild().getArtifact()).isEqualTo("forgerock-openbanking-rs-api");
        assertThat(response.getBody().getBuild().getName()).isEqualTo("rs-api");
        assertThat(response.getBody().getBuild().getGroup()).isEqualTo("com.forgerock.openbanking.aspsp.rs");
        assertThat(response.getBody().getBuild().getTime()).isNotNull();
        assertThat(response.getBody().getBuild().getVersion()).isNotNull();
    }

    @Data
    @NoArgsConstructor
    public static class ActuatorInfo {
        private ActuatorGitInfo git;
        private ActuatorBuildInfo build;
    }

    @Data
    @NoArgsConstructor
    public static class ActuatorBuildInfo {
        private String version;
        private String artifact;
        private String name;
        private String group;
        private double time;
    }

    @Data
    @NoArgsConstructor
    public static class ActuatorGitInfo {
        private ActuatorGitCommit commit;
        private String branch;
    }

    @Data
    @NoArgsConstructor
    public static class ActuatorGitCommit {
        private long time;
        private String id;
    }
}