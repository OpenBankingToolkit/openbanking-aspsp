/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.actuator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.commons.auth.Authenticator;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.forgerock.openbanking.integration.test.support.Authentication.mockAuthentication;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActuatorIT {

    @LocalServerPort
    private int port;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authenticator authenticator;


    @Before
    public void setUp() {
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)).verifySsl(false);
    }

    @Test
    public void actuatorHealthEndpointShouldReturnUp() throws Exception {
        // Given
        mockAuthentication(authenticator, "PISP");

        // When
        HttpResponse<JsonNode> response = Unirest.get("https://rs-api:" + port + "/actuator/health")
                .asJson();

        // Then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getObject().get("status")).isEqualTo("UP");
    }

    @Test
    public void actuatorInfoEndpointShouldReturnBuildData() throws Exception {
        // Given
        mockAuthentication(authenticator, "PISP");

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