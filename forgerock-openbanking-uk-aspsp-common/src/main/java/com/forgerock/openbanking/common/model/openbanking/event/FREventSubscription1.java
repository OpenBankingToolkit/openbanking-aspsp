/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.event;

import com.forgerock.openbanking.common.model.version.OBVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.event.OBEventSubscription1;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class FREventSubscription1 implements Persistable<String> {

    @Id
    @Indexed
    public String id;

    public OBEventSubscription1 obEventSubscription1;

    @Indexed
    public String tppId;

    @CreatedDate
    public DateTime created;
    @LastModifiedDate
    public DateTime updated;

    public OBVersion version;

    @Override
    public boolean isNew() {
        return created == null;
    }
}
