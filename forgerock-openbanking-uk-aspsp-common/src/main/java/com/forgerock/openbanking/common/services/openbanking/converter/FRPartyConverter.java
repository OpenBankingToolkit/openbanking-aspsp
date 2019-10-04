/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
