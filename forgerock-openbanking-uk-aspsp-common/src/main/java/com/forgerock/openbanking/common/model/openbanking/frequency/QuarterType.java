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
package com.forgerock.openbanking.common.model.openbanking.frequency;

import org.joda.time.DateTime;

public enum QuarterType {
    ENGLISH (
            DateTime.now().withMonthOfYear(3).withDayOfMonth(25),
            DateTime.now().withMonthOfYear(6).withDayOfMonth(24),
            DateTime.now().withMonthOfYear(9).withDayOfMonth(29),
            DateTime.now().withMonthOfYear(12).withDayOfMonth(25)
    ),
    SCOTTISH (
            DateTime.now().withMonthOfYear(2).withDayOfMonth(2),
            DateTime.now().withMonthOfYear(5).withDayOfMonth(15),
            DateTime.now().withMonthOfYear(8).withDayOfMonth(1),
            DateTime.now().withMonthOfYear(11).withDayOfMonth(11)
    ),
    RECEIVED (
            DateTime.now().withMonthOfYear(3).withDayOfMonth(20),
            DateTime.now().withMonthOfYear(6).withDayOfMonth(19),
            DateTime.now().withMonthOfYear(9).withDayOfMonth(24),
            DateTime.now().withMonthOfYear(12).withDayOfMonth(20)
    );

    private String quarterStr;
    private DateTime quarter1;
    private DateTime quarter2;
    private DateTime quarter3;
    private DateTime quarter4;

    QuarterType(DateTime quarter1, DateTime quarter2, DateTime quarter3, DateTime quarter4) {
        this.quarter1 = quarter1;
        this.quarter2 = quarter2;
        this.quarter3 = quarter3;
        this.quarter4 = quarter4;
    }

    public DateTime getQuarter1() {
        return quarter1;
    }

    public DateTime getQuarter2() {
        return quarter2;
    }

    public DateTime getQuarter3() {
        return quarter3;
    }

    public DateTime getQuarter4() {
        return quarter4;
    }

    public static QuarterType fromQuarterTypeString(String quarterStr) {
        for (QuarterType quarterType : QuarterType.values()) {
            if (quarterType.name().equals(quarterStr)) {
                return quarterType;
            }
        }
        throw new IllegalArgumentException("Quarter type value not found: " + quarterStr);
    }

    public boolean matchOneQuarter(DateTime date) {
        return matchDayOfMonthAndMonth(date, quarter1)
                || matchDayOfMonthAndMonth(date, quarter2)
                || matchDayOfMonthAndMonth(date, quarter3)
                || matchDayOfMonthAndMonth(date, quarter4);
    }

    private boolean matchDayOfMonthAndMonth(DateTime date, DateTime quarter) {
        return date.dayOfMonth().get() == quarter.dayOfMonth().get()
                && date.monthOfYear().get() == quarter.monthOfYear().get();
    }
}