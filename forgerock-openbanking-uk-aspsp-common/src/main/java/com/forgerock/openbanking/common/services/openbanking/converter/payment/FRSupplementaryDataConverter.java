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

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import uk.org.openbanking.datamodel.payment.OBSupplementaryData1;

public class FRSupplementaryDataConverter {

    public static FRSupplementaryData toFRSupplementaryData(OBSupplementaryData1 obSupplementaryData) {
        return obSupplementaryData == null ? null : FRSupplementaryData.builder()
                .data(obSupplementaryData.getData())
                .build();
    }

    public static OBSupplementaryData1 toOBSupplementaryData1(FRSupplementaryData frSupplementaryData) {
        if (frSupplementaryData == null) {
            return null;
        }
        OBSupplementaryData1 obSupplementaryData1 = new OBSupplementaryData1();
        obSupplementaryData1.setData(frSupplementaryData.getData());
        return obSupplementaryData1;
    }
}
