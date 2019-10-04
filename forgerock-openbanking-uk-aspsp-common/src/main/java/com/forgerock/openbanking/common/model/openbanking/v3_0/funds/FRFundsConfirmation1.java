/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.funds;

import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRFundsConfirmation1 {

    @Id
    @Indexed
    public String id;

    public OBFundsConfirmation1 fundsConfirmation;

    public boolean fundsAvailable;

    @CreatedDate
    public DateTime created;
    @LastModifiedDate
    public DateTime updated;

    public OBVersion obVersion;

}
