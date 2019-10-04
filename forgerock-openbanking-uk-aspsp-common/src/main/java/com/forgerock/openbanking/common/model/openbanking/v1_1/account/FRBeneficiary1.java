/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.account;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBBeneficiary1;

import java.util.Date;

/**
 * Representation of an account. This model is only useful for the demo
 */
@Document
public class FRBeneficiary1 {

    @Id
    @Indexed
    public String id;
    @Indexed
    public String accountId;
    public OBBeneficiary1 beneficiary;
    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OBBeneficiary1 getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(OBBeneficiary1 beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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
}
