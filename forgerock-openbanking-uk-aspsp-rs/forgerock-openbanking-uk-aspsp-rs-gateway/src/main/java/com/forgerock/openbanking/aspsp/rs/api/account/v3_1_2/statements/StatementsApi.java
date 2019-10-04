/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.statements;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "v3.1.2-Statements", description = "the statements API")
@OpenBankingAPI(
        obVersion = "3.1.2",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.STATEMENTS
)
@RequestMapping(value = "/open-banking/v3.1.2/aisp")
public interface StatementsApi extends com.forgerock.openbanking.aspsp.rs.api.account.v3_1_1.statements.StatementsApi {
}
