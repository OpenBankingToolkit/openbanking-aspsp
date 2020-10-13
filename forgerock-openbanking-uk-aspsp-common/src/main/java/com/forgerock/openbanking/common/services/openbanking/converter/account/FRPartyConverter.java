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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRPartyData;
import uk.org.openbanking.datamodel.account.OBExternalPartyType1Code;
import uk.org.openbanking.datamodel.account.OBParty1;
import uk.org.openbanking.datamodel.account.OBParty2;
import uk.org.openbanking.datamodel.account.OBPartyRelationships1;
import uk.org.openbanking.datamodel.account.OBRelationship1;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountPostalAddressConverter.toOBPostalAddress8List;

public class FRPartyConverter {

    public static OBParty1 toOBParty1(FRPartyData party) {
        return party == null ? null : new OBParty1()
                .partyId(party.getPartyId())
                .partyNumber(party.getPartyNumber())
                .partyType(toOBExternalPartyType1Code(party.getPartyType()))
                .name(party.getName())
                .emailAddress(party.getEmailAddress())
                .phone(party.getPhone())
                .mobile(party.getMobile())
                .address(toOBPostalAddress8List(party.getAddresses()));
    }

    public static OBParty2 toOBParty2(FRPartyData party) {
        return party == null ? null : new OBParty2()
                .partyId(party.getPartyId())
                .partyNumber(party.getPartyNumber())
                .partyType(toOBExternalPartyType1Code(party.getPartyType()))
                .name(party.getName())
                .fullLegalName(party.getFullLegalName())
                .legalStructure(party.getLegalStructure())
                .beneficialOwnership(party.getBeneficialOwnership())
                .accountRole(party.getAccountRole())
                .emailAddress(party.getEmailAddress())
                .phone(party.getPhone())
                .mobile(party.getMobile())
                .relationships(toOBPartyRelationships1(party.getRelationship()))
                .address(toOBPostalAddress8List(party.getAddresses()));
    }

    public static OBExternalPartyType1Code toOBExternalPartyType1Code(FRPartyData.FRPartyType partyType) {
        return partyType == null ? null : OBExternalPartyType1Code.valueOf(partyType.name());
    }

    public static OBPartyRelationships1 toOBPartyRelationships1(FRPartyData.FRRelationship relationship) {
        return relationship == null ? null : new OBPartyRelationships1()
                .account(new OBRelationship1()
                        .related(relationship.getRelated())
                        .id(relationship.getId()));
    }
}
