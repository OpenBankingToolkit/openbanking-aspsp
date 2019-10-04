/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.2.3).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.beneficiaries;

import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBReference;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OpenBankingAPI;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-16T08:37:28.078Z")

@Api(tags = "v3.0-Beneficiaries", description = "the beneficiaries API")
@OpenBankingAPI(
        obVersion = "3.0",
        obGroupName = OBGroupName.AISP,
        obReference = OBReference.BENEFICIARIES
)
@RequestMapping(value = "/open-banking/v3.0/aisp")
public interface BeneficiariesApi extends com.forgerock.openbanking.aspsp.rs.api.account.v2_0.beneficiaries.BeneficiariesApi {

}
