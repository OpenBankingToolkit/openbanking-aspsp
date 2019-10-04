/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v3_0.account;

import com.forgerock.openbanking.common.model.openbanking.status.StandingOrderStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.account.OBStandingOrder3;

import java.util.Date;

/**
 * Representation of an account. This model is only useful for the demo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Document
public class FRStandingOrder3 {

    @Id
    public String id;
    @Indexed
    public String accountId;
    public OBStandingOrder3 standingOrder;

    @Indexed
    public String pispId;

    @CreatedDate
    public Date created;
    @LastModifiedDate
    public Date updated;

    public String rejectionReason;

    public StandingOrderStatus status;

}
