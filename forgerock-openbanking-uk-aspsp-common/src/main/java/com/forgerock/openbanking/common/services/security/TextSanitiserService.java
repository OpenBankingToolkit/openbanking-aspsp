/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.security;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * Helper class to wrap the Jsoup implementation. This makes it easier to use a different type of sanitiser library in future by hiding implementation from the rest of the code.
 */
@Slf4j
public class TextSanitiserService {
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings()
            .prettyPrint(false);

    public static boolean hasHtmlContent(final Map.Entry<String, String[]> formEntry) {
        if (formEntry == null || formEntry.getValue() == null || formEntry.getValue().length == 0) {
            return false;
        }
        return Arrays.stream(formEntry.getValue())
                .anyMatch(TextSanitiserService::hasHtmlContent);
    }

    public static boolean hasHtmlContent(final String unsanitised) {
        if (StringUtils.isEmpty(unsanitised)) {
            return false;
        }
        String sanitised = Jsoup.clean(unsanitised, "", Whitelist.none(), OUTPUT_SETTINGS);
        // If different then html was removed
        boolean different =  !unsanitised.equals(sanitised) &&
                // This comparison is needed for scenarios when user posts escapable (e.g. &) or escaped (&amp;) values in string.
                // We do not want to reject these kinds of string data so we compare with escaped and unescaped content and accept either if matching.
                !unsanitised.equals(StringEscapeUtils.unescapeXml(sanitised));
        if (different) {
            log.debug("Supplied text contains HTML content. Before sanitisation: '{}', after sanitisation '{}'", unsanitised, sanitised);
        }
        return different;
    }
}
