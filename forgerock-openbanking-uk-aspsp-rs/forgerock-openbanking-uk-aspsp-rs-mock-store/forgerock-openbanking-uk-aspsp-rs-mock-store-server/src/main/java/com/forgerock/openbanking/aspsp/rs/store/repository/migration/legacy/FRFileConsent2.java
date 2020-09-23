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

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRFileConsent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.PaymentFileType;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.model.Tpp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.payment.OBFile2;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent2;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FRFileConsent2 implements FRFileConsent, Persistable<String> {
    @Id
    @Indexed
    public String id;

    @Indexed
    public ConsentStatusCode status;

    public OBWriteFileConsent2 writeFileConsent;

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

    @ToString.Exclude // Makes the log too verbose if large files are uploaded
    public List<FRFilePayment> payments;

    @ToString.Exclude // Makes the log too verbose if large files are uploaded
    public String fileContent;

    public OBVersion obVersion;

    public void setPisp(Tpp tpp) {
        this.pispId = tpp.getId();
        this.pispName = tpp.getOfficialName();
    }

    public OBFile2 getInitiation() {
        return writeFileConsent.getData().getInitiation();
    }

    public OBRisk1 getRisk() { return null; }

    public PaymentFileType getFileType() {
        return PaymentFileType.fromFileType(writeFileConsent.getData().getInitiation().getFileType());
    }

    public String getFileHash() {
        return writeFileConsent.getData().getInitiation().getFileHash();
    }

    @Override
    public boolean isNew() {
        return created == null;
    }
}