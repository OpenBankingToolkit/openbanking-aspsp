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
package com.forgerock.openbanking.rs.ui.api.data;

import com.forgerock.openbanking.analytics.model.entries.PsuCounterEntry;
import com.forgerock.openbanking.analytics.services.PsuCounterEntryKPIService;
import com.forgerock.openbanking.common.conf.data.DataConfigurationProperties;
import com.forgerock.openbanking.common.model.data.FRUserData;
import com.forgerock.openbanking.common.services.store.data.UserDataService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
public class DataApiController implements DataApi {

    @Autowired
    private UserDataService userDataService;
    @Autowired
    private DataConfigurationProperties dataConfig;
    @Autowired
    private PsuCounterEntryKPIService psuCounterEntryKPIService;

    @Override
    public ResponseEntity hasData(
            Principal principal
    ) {
        return ResponseEntity.ok(userDataService.hasData(principal.getName()));
    }

    @Override
    public ResponseEntity exportUserData(
            Principal principal
    ) {
        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        String username = currentUser.getUsername();
        return ResponseEntity.ok(userDataService.exportUserData(username));
    }

    @Override
    public ResponseEntity updateUserData(
            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData userData,

            Principal principal
    ) throws OBErrorException {
       try {
           UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
           String username = currentUser.getUsername();

           userData.setUserName(username);
           if (!userDataService.hasData(username)) {
               psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                       .count(1l)
                       .day(DateTime.now())
                       .build());
           }

           return ResponseEntity.ok(userDataService.updateUserData(userData));
       } catch (HttpClientErrorException e) {

           if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
               log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
               throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                       e.getResponseBodyAsString()
               );
           } else {
               log.error("Internal server: {}", e.getResponseBodyAsString(), e);
               throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
           }
       }
    }

    @Override
    public ResponseEntity createUserData(
            @ApiParam(value = "User financial data", required = true)
            @RequestBody FRUserData userData,

            Principal principal
    ) throws OBErrorException {
        try {
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            String username = currentUser.getUsername();

            userData.setUserName(username);
            if (!userDataService.hasData(username)) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(userDataService.createUserData(userData));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity deleteUserData(
            Principal principal
    ) throws OBErrorException {
        try {
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            String username = currentUser.getUsername();

            userDataService.deleteUserData(username);
            return ResponseEntity.ok(userDataService.exportUserData(username));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity generateData(
            @ApiParam(value = "Data profile", required = false)
            @RequestParam(name = "profile", required = false) String profile,

            Principal principal
    ) throws OBErrorException {
        try {
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            String username = currentUser.getUsername();

            final String defaultProfile = profile != null ? profile : dataConfig.getDefaultProfile();

            Optional<DataConfigurationProperties.DataTemplateProfile> any = dataConfig.getProfiles().stream().filter(t -> t.getId().equals(defaultProfile)).findAny();
            if (!any.isPresent()) {
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        "Profile '" + profile + "' doesn't exist."
                );
            }

            if (!userDataService.deleteUserData(username)) {
                psuCounterEntryKPIService.pushPsuCounterEntry(PsuCounterEntry.builder()
                        .count(1l)
                        .day(DateTime.now())
                        .build());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(userDataService.generateUserData(username, defaultProfile));
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.debug("TPP bad request: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.DATA_INVALID_REQUEST,
                        e.getResponseBodyAsString()
                );
            } else {
                log.error("Internal server: {}", e.getResponseBodyAsString(), e);
                throw new OBErrorException(OBRIErrorType.SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity getProfiles() {
        return ResponseEntity.ok(dataConfig.getProfiles());
    }
}
