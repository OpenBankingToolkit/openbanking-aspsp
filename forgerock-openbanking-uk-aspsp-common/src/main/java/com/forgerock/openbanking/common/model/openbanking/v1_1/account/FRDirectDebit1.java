/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBDirectDebit1;

import java.util.Date;

/**
 * Representation of an account. This model is only useful for the demo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class FRDirectDebit1 {

    @Id
    @Indexed
    public String id;
    @Indexed
    public String accountId;
    public OBDirectDebit1 directDebit;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

}
