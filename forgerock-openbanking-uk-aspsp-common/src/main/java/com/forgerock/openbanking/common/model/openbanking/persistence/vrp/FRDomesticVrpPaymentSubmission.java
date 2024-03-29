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
package com.forgerock.openbanking.common.model.openbanking.persistence.vrp;

import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentSubmission;
import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequestData;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPResponseData;

import java.util.Date;

@Builder
@Data
@Document
public class FRDomesticVrpPaymentSubmission implements PaymentSubmission {
    @Id
    @Indexed
    public String id;

    @Indexed
    public String pispId;

    @Indexed
    public String idempotencyKey;

    public FRDomesticVRPRequest domesticVrpPayment;

    // Represent OBDomesticVRPResponseData.StatusEnum as .name()
    public String status;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    public OBVersion obVersion;

    /**
     * Status of an individual entry in a file payment
     */
    public enum PaymentStatus {
        PENDING, COMPLETED, REJECTED;
    }
}
