/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.utils;

import org.joda.time.DateTime;

public class DateTimeUtils {

    public static DateTime getMaxDatetime(DateTime dateA, DateTime dateB) {
        if (dateA == null && dateB == null) {
            return null;
        } else if (dateB == null) {
            return dateA;
        } else if (dateA == null) {
            return dateB;
        } else if (dateA.isBefore(dateB)) {
            return dateB;
        } else {
            return dateA;
        }
    }

    public static DateTime getMinDatetime(DateTime dateA, DateTime dateB) {
        if (dateA == null && dateB == null) {
            return null;
        } else if (dateB == null) {
            return dateA;
        } else if (dateA == null) {
            return dateB;
        } else if (dateA.isAfter(dateB)) {
            return dateB;
        } else {
            return dateA;
        }
    }
}
