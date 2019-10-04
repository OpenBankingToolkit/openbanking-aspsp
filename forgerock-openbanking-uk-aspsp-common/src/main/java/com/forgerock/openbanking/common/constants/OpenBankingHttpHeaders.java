/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.constants;

public class OpenBankingHttpHeaders {
    /**
     * Contains the client ID that is used to identify the TPP. Mostly used to pass a TPP identity extracted from client certificate to a backend service like rs-store.
     */
    public static final String X_OB_CLIENT_ID = "x-ob-client-id";

    /**
     * The unique Id of the ASPSP to which the request is issued.
     * Issued by OBIE and corresponds to the Organization Id of the ASPSP in the Open Banking Directory.
     */
    public static final String X_FAPI_FINANCIAL_ID = "x-fapi-financial-id";
}
