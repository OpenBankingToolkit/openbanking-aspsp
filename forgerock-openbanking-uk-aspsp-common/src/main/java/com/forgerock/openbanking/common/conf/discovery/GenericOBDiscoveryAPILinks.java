/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf.discovery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericOBDiscoveryAPILinks implements OBDiscoveryAPILinks  {
    private Map<String, String> links = new HashMap<>();

    public GenericOBDiscoveryAPILinks addLink(String reference, String endpoint) {
        links.put(reference, endpoint);
        return this;
    }

    public Collection<String> getLinkValues() {
        return new ArrayList<>(links.values());
    };
}
