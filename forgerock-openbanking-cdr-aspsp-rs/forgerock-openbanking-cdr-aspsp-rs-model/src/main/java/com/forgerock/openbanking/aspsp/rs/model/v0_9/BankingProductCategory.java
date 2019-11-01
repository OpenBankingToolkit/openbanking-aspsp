/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
