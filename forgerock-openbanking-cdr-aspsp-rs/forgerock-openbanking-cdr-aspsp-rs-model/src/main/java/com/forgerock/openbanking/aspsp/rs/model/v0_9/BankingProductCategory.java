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
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The category to which a product or account belongs. See [here](#product-categories) for more details
 */
public enum BankingProductCategory {
  TRANS_AND_SAVINGS_ACCOUNTS("TRANS_AND_SAVINGS_ACCOUNTS"),
    TERM_DEPOSITS("TERM_DEPOSITS"),
    TRAVEL_CARDS("TRAVEL_CARDS"),
    REGULATED_TRUST_ACCOUNTS("REGULATED_TRUST_ACCOUNTS"),
    RESIDENTIAL_MORTGAGES("RESIDENTIAL_MORTGAGES"),
    CRED_AND_CHRG_CARDS("CRED_AND_CHRG_CARDS"),
    PERS_LOANS("PERS_LOANS"),
    MARGIN_LOANS("MARGIN_LOANS"),
    LEASES("LEASES"),
    TRADE_FINANCE("TRADE_FINANCE"),
    OVERDRAFTS("OVERDRAFTS"),
    BUSINESS_LOANS("BUSINESS_LOANS");

  private String value;

  BankingProductCategory(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BankingProductCategory fromValue(String text) {
    for (BankingProductCategory b : BankingProductCategory.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
