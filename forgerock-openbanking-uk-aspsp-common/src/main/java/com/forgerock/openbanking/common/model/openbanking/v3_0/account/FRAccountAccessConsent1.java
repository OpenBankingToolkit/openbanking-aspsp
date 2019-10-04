/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FRAccountAccessConsent1 implements FRAccountRequest, Persistable<String> {
    @Id
    @Indexed
    public String id;
    private OBReadConsentResponse1 accountAccessConsent;
    @Indexed
    private String clientId;
    private String aispId;
    private String aispName;
    private String consentId;
    private List<String> accountIds = new ArrayList<>();
    private String userId;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    OBVersion obVersion;

    @Override
    public boolean isNew() {
        return created == null;
    }

    public void setAisp(Tpp tpp) {
        this.clientId = tpp.getClientId();
        this.aispId = tpp.getId();
        this.aispName = tpp.getOfficialName();
    }

    @Override
    @JsonIgnore
    public OBExternalRequestStatus1Code getStatus() {
        return accountAccessConsent.getData().getStatus();
    }

    @Override
    @JsonIgnore
    public void setStatus(OBExternalRequestStatus1Code code) {
        getAccountAccessConsent().getData().setStatus(code);
    }

    @Override
    @JsonIgnore
    public List<OBExternalPermissions1Code> getPermissions() {
        return getAccountAccessConsent().getData().getPermissions();
    }

    @Override
    @JsonIgnore
    public DateTime getExpirationDateTime() {
        return getAccountAccessConsent().getData().getExpirationDateTime();
    }

    @Override
    @JsonIgnore
    public DateTime getTransactionFromDateTime() {
        return getAccountAccessConsent().getData().getTransactionFromDateTime();
    }

    @Override
    @JsonIgnore
    public DateTime getTransactionToDateTime() {
        return getAccountAccessConsent().getData().getTransactionToDateTime();
    }

}
