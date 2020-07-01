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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification2;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification3;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification4;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification5;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification50;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification51;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification60;

public final class OBBranchAndFinancialInstitutionIdentificationConverter {

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification51 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification4.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification4.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification4 toOBBranchAndFinancialInstitutionIdentification4(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification4.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification5 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification2.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification2.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification2.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification50 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification5.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification3 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification6.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification4 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification5.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification5 toOBBranchAndFinancialInstitutionIdentification5(OBBranchAndFinancialInstitutionIdentification51 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification5.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification6 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification3.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification3 toOBBranchAndFinancialInstitutionIdentification3(OBBranchAndFinancialInstitutionIdentification60 obBranchAndFinancialInstitutionIdentification60) {
        return FRModelMapper.map(obBranchAndFinancialInstitutionIdentification60, OBBranchAndFinancialInstitutionIdentification3.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification2 toOBBranchAndFinancialInstitutionIdentification2(OBBranchAndFinancialInstitutionIdentification60 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification2.class);
    }

    public static OBBranchAndFinancialInstitutionIdentification6 toOBBranchAndFinancialInstitutionIdentification6(OBBranchAndFinancialInstitutionIdentification60 institutionIdentification) {
        return FRModelMapper.map(institutionIdentification, OBBranchAndFinancialInstitutionIdentification6.class);
    }
}
