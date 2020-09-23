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
package com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBTransaction2;
import uk.org.openbanking.jackson.DateTimeDeserializer;
import uk.org.openbanking.jackson.DateTimeSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representation of an account. This model is only useful for the demo
 */
@Document
public class FRTransaction2 {

    @Id
    public String id;
    @Indexed
    public String accountId;
    public List<String> statementIds = new ArrayList<>();
    public OBTransaction2 transaction;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime bookingDateTime = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getStatementIds() {
        return statementIds;
    }

    public void setStatementIds(List<String> statementIds) {
        this.statementIds = statementIds;
    }

    public FRTransaction2 addStatementId(String statementId) {
        this.statementIds.add(statementId);
        return this;
    }

    public OBTransaction2 getTransaction() {
        return transaction;
    }

    public void setTransaction(OBTransaction2 transaction) {
        this.transaction = transaction;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public DateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(DateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
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

    @Override
    public String toString() {
        return "FRTransaction4{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", statementIds=" + statementIds +
                ", transaction=" + transaction +
                ", created=" + created +
                ", updated=" + updated +
                ", bookingDateTime=" + bookingDateTime +
                '}';
    }
}
