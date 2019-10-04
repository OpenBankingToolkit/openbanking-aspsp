/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.account;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBTransaction3;
import uk.org.openbanking.jackson.DateTimeDeserializer;
import uk.org.openbanking.jackson.DateTimeSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representation of an account. This model is only useful for the demo
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FRTransaction3 {

    @Id
    public String id;
    @Indexed
    public String accountId;
    public List<String> statementIds;
    public OBTransaction3 transaction;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime bookingDateTime = null;

    public FRTransaction3 addStatementId(String statementId) {
        if (statementIds==null) {
            statementIds = new ArrayList<>();
        }
        this.statementIds.add(statementId);
        return this;
    }
}
