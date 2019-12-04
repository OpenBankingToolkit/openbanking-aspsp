/**
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
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRParty1;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRParty2;
import uk.org.openbanking.datamodel.account.OBParty1;
import uk.org.openbanking.datamodel.account.OBParty2;

public class FRPartyConverter {


    public static OBParty1 toOBParty1(OBParty2 party) {
        if (party==null) return null;
        return new OBParty1()
                .partyId(party.getPartyId())
                .name(party.getName())
                .phone(party.getPhone())
                .partyNumber(party.getPartyNumber())
                .partyType(party.getPartyType())
                .address(party.getAddress())
                .emailAddress(party.getEmailAddress())
                .mobile(party.getMobile());
    }

    public static FRParty2 toParty2(FRParty1 frParty1) {
        if (frParty1==null) return null;

        FRParty2 frParty2 = FRParty2.builder()
                .accountId(frParty1.getAccountId())
                .id(frParty1.getId())
                .created(frParty1.getCreated())
                .updated(frParty1.getUpdated())
                .userId(frParty1.getUserId())
                .build();

        frParty2.setParty(toOBParty2(frParty1.getParty()));
        return frParty2;
    }

    private static OBParty2 toOBParty2(OBParty1 party) {
        if (party==null) return null;

        return new OBParty2()
                .partyId(party.getPartyId())
                .name(party.getName())
                .phone(party.getPhone())
                .partyNumber(party.getPartyNumber())
                .partyType(party.getPartyType())
                .address(party.getAddress())
                .emailAddress(party.getEmailAddress())
                .mobile(party.getMobile());
    }
}
