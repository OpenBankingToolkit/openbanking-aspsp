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
package com.forgerock.openbanking.aspsp.rs.store.repository.migration.legacy;

import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
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
import uk.org.openbanking.datamodel.payment.OBDomestic2;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

import java.util.Date;

/**
 * This class exists purely for migration purposes and should be removed once all clusters have been upgraded to a release of openbanking-reference-implementation
 * containing v3.1.5 (currently REM).
 *
 * <p>
 * Note that Prior to extensive refactoring, there were a series of these "FR" mongo document classes that were named in sequence (e.g. FRDomesticConsent2,
 * FRDomesticConsent3 etc.). The sequence number was incremented each time there was a new version of the OB model objects they contained. Instead of this, there
 * is now one FR document class (e.g. FRDomesticConsent) for each corresponding area of the payments API. Each one contains our own "domain" model object, rather
 * than the OB model ones. This means that if a new OB release adds new fields to an OB object, the fields only need to be added to the our domain objects
 * (and mapped accordingly). There should be no need to create a new version of the "FR" mongo documents and corresponding repositories.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@Deprecated
public class FRDomesticConsent2 implements Persistable<String> {
    @Id
    @Indexed
    public String id;
    @Indexed
    public ConsentStatusCode status;
    public OBWriteDomesticConsent2 domesticConsent;

    @Indexed
    public String accountId;
    @Indexed
    public String userId;
    @Indexed
    public String pispId;
    public String pispName;
    public String idempotencyKey;

    @CreatedDate
    public DateTime created;
    public DateTime statusUpdate;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    public OBDomestic2 getInitiation() {
        return domesticConsent.getData().getInitiation();
    }

    public OBRisk1 getRisk() {
        return domesticConsent.getRisk();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }

}
