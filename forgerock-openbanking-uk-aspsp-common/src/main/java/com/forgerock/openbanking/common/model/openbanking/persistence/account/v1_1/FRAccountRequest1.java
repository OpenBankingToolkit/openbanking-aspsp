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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v1_1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadResponse1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class FRAccountRequest1 implements AccountRequest {
    @Id
    @Indexed
    public String id;
    private OBReadResponse1 accountRequest;
    @Indexed
    private String clientId;
    private String aispId;
    private String aispName;
    private String accountRequestId;
    private List<String> accountIds = new ArrayList<>();
    private String userId;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountRequestId() {
        return accountRequestId;
    }

    public void setAccountRequestId(String accountRequestId) {
        this.accountRequestId = accountRequestId;
    }

    public OBReadResponse1 getAccountRequest() {
        return accountRequest;
    }

    public void setAccountRequest(OBReadResponse1 accountRequest) {
        this.accountRequest = accountRequest;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public String getAispId() {
        return aispId;
    }

    public void setAisp(Tpp tpp) {
        this.clientId = tpp.getClientId();
        this.aispId = tpp.getId();
        this.aispName = tpp.getOfficialName();
    }

    public String getAispName() {
        return aispName;
    }

    public List<String> getAccountIds() {
        return accountIds;
    }

    @Override
    @JsonIgnore
    public OBExternalRequestStatus1Code getStatus() {
        return accountRequest.getData().getStatus();
    }

    public void setAccountIds(List<String> accountIds) {
        this.accountIds = accountIds;
    }

    @Override
    @JsonIgnore
    public void setStatus(OBExternalRequestStatus1Code code) {
        getAccountRequest().getData().setStatus(code);
    }

    public String getUserId() {
        return userId;
    }

    @Override
    @JsonIgnore
    public List<OBExternalPermissions1Code> getPermissions() {
        return getAccountRequest().getData().getPermissions();
    }

    @Override
    @JsonIgnore
    public DateTime getExpirationDateTime() {
        return getAccountRequest().getData().getExpirationDateTime();
    }

    @Override
    @JsonIgnore
    public DateTime getTransactionFromDateTime() {
        return getAccountRequest().getData().getTransactionFromDateTime();
    }

    @Override
    @JsonIgnore
    public DateTime getTransactionToDateTime() {
        return getAccountRequest().getData().getTransactionToDateTime();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAispId(String aispId) {
        this.aispId = aispId;
    }

    public void setAispName(String aispName) {
        this.aispName = aispName;
    }

    public OBVersion getObVersion() {
        return obVersion;
    }

    public void setObVersion(OBVersion obVersion) {
        this.obVersion = obVersion;
    }

    @Override
    public String toString() {
        return "FRAccountRequest1{" +
                "id='" + id + '\'' +
                ", accountRequest=" + accountRequest +
                ", aispIssuerId='" + clientId + '\'' +
                ", accountRequestId='" + accountRequestId + '\'' +
                ", accountIds=" + accountIds +
                ", userId='" + userId + '\'' +
                '}';
    }
}
