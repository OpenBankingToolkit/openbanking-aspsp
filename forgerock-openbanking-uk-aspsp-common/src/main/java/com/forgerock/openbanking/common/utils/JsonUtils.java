/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonUtils {
    public static Stream<JsonNode> streamArray(JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false);
    }
}
