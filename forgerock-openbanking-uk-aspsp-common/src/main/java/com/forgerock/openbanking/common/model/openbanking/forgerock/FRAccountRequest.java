/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import java.util.List;

public interface FRAccountRequest {

    String getClientId();

    String getUserId();

    String getAispId();

    String getAispName();

    List<OBExternalPermissions1Code> getPermissions();

    DateTime getExpirationDateTime();

    DateTime getTransactionFromDateTime();

    DateTime getTransactionToDateTime();

    void setUserId(String userId);

    void setAccountIds(List<String> accountIds);

    void setStatus(OBExternalRequestStatus1Code code);

    String getId();

    List<String> getAccountIds();

    OBExternalRequestStatus1Code getStatus();
}
