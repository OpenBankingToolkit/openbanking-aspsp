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
package com.forgerock.openbanking.common.services.openbanking.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDataSCASupportData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDataSCASupportData.FRAppliedAuthenticationApproach;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDataSCASupportData.FRRequestedSCAExemptionType;
import uk.org.openbanking.datamodel.payment.OBSCASupportData1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3DataSCASupportData;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent4DataSCASupportData;

public class FRDataSCASupportDataConverter {

    // OB to FR
    public static FRDataSCASupportData toFRDataSCASupportData(OBWriteDomesticConsent3DataSCASupportData scASupportData) {
        return scASupportData == null ? null : FRDataSCASupportData.builder()
                .requestedSCAExemptionType(toFRRequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(toFRAppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId())
                .build();
    }

    public static FRDataSCASupportData toFRDataSCASupportData(OBWriteDomesticConsent4DataSCASupportData scASupportData) {
        return scASupportData == null ? null : FRDataSCASupportData.builder()
                .requestedSCAExemptionType(toFRRequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(toFRAppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId())
                .build();
    }

    public static FRDataSCASupportData toFRDataSCASupportData(OBSCASupportData1 scASupportData) {
        return scASupportData == null ? null : FRDataSCASupportData.builder()
                .requestedSCAExemptionType(toFRRequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(toFRAppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId())
                .build();
    }

    public static FRAppliedAuthenticationApproach toFRAppliedAuthenticationApproach(OBWriteDomesticConsent3DataSCASupportData.AppliedAuthenticationApproachEnum appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : FRAppliedAuthenticationApproach.valueOf(appliedAuthenticationApproach.name());
    }

    public static FRAppliedAuthenticationApproach toFRAppliedAuthenticationApproach(OBWriteDomesticConsent4DataSCASupportData.AppliedAuthenticationApproachEnum appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : FRAppliedAuthenticationApproach.valueOf(appliedAuthenticationApproach.name());
    }

    public static FRAppliedAuthenticationApproach toFRAppliedAuthenticationApproach(OBSCASupportData1.AppliedAuthenticationApproachEnum appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : FRAppliedAuthenticationApproach.valueOf(appliedAuthenticationApproach.name());
    }

    public static FRRequestedSCAExemptionType toFRRequestedSCAExemptionType(OBWriteDomesticConsent3DataSCASupportData.RequestedSCAExemptionTypeEnum requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : FRRequestedSCAExemptionType.valueOf(requestedSCAExemptionType.name());
    }

    public static FRRequestedSCAExemptionType toFRRequestedSCAExemptionType(OBWriteDomesticConsent4DataSCASupportData.RequestedSCAExemptionTypeEnum requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : FRRequestedSCAExemptionType.valueOf(requestedSCAExemptionType.name());
    }

    public static FRRequestedSCAExemptionType toFRRequestedSCAExemptionType(OBSCASupportData1.RequestedSCAExemptionTypeEnum requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : FRRequestedSCAExemptionType.valueOf(requestedSCAExemptionType.name());
    }

    // FR to OB
    public static OBWriteDomesticConsent3DataSCASupportData toOBWriteDomesticConsent3DataSCASupportData(FRDataSCASupportData scASupportData) {
        return scASupportData == null ? null : new OBWriteDomesticConsent3DataSCASupportData()
                .requestedSCAExemptionType(to3DataRequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(to3DataAppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId());
    }

    public static OBWriteDomesticConsent4DataSCASupportData toOBWriteDomesticConsent4DataSCASupportData(FRDataSCASupportData scASupportData) {
        return scASupportData == null ? null : new OBWriteDomesticConsent4DataSCASupportData()
                .requestedSCAExemptionType(to4DataRequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(to4DataAppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId());
    }

    public static OBSCASupportData1 toOBSCASupportData1(FRDataSCASupportData scASupportData) {
        return scASupportData == null ? null : new OBSCASupportData1()
                .requestedSCAExemptionType(toOBSCASupportData1RequestedSCAExemptionType(scASupportData.getRequestedSCAExemptionType()))
                .appliedAuthenticationApproach(toOBSCASupportData1AppliedAuthenticationApproach(scASupportData.getAppliedAuthenticationApproach()))
                .referencePaymentOrderId(scASupportData.getReferencePaymentOrderId());
    }

    public static OBWriteDomesticConsent3DataSCASupportData.RequestedSCAExemptionTypeEnum to3DataRequestedSCAExemptionType(FRRequestedSCAExemptionType requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : OBWriteDomesticConsent3DataSCASupportData.RequestedSCAExemptionTypeEnum.valueOf(requestedSCAExemptionType.name());
    }

    public static OBWriteDomesticConsent4DataSCASupportData.RequestedSCAExemptionTypeEnum to4DataRequestedSCAExemptionType(FRRequestedSCAExemptionType requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : OBWriteDomesticConsent4DataSCASupportData.RequestedSCAExemptionTypeEnum.valueOf(requestedSCAExemptionType.name());
    }

    public static OBSCASupportData1.RequestedSCAExemptionTypeEnum toOBSCASupportData1RequestedSCAExemptionType(FRRequestedSCAExemptionType requestedSCAExemptionType) {
        return requestedSCAExemptionType == null ? null : OBSCASupportData1.RequestedSCAExemptionTypeEnum.valueOf(requestedSCAExemptionType.name());
    }

    public static OBWriteDomesticConsent3DataSCASupportData.AppliedAuthenticationApproachEnum to3DataAppliedAuthenticationApproach(FRAppliedAuthenticationApproach appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : OBWriteDomesticConsent3DataSCASupportData.AppliedAuthenticationApproachEnum.valueOf(appliedAuthenticationApproach.name());
    }

    public static OBWriteDomesticConsent4DataSCASupportData.AppliedAuthenticationApproachEnum to4DataAppliedAuthenticationApproach(FRAppliedAuthenticationApproach appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : OBWriteDomesticConsent4DataSCASupportData.AppliedAuthenticationApproachEnum.valueOf(appliedAuthenticationApproach.name());
    }

    public static OBSCASupportData1.AppliedAuthenticationApproachEnum toOBSCASupportData1AppliedAuthenticationApproach(FRAppliedAuthenticationApproach appliedAuthenticationApproach) {
        return appliedAuthenticationApproach == null ? null : OBSCASupportData1.AppliedAuthenticationApproachEnum.valueOf(appliedAuthenticationApproach.name());
    }
}
