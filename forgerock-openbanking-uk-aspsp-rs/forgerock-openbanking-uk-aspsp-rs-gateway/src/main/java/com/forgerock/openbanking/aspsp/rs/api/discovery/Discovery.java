/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.discovery;

import com.forgerock.openbanking.commons.configuration.discovery.RSAPIsConfigurationProperties;
import com.forgerock.openbanking.commons.rest.annotations.openbanking.OBGroupName;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.org.openbanking.datamodel.discovery.OBDiscovery;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPI;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryResponse;

import java.util.Map;

@Controller
@Slf4j
@Api(tags = "Discovery", description = "the discovery API")
public class Discovery {

    @Autowired
    private RSAPIsConfigurationProperties rsConfiguration;

    @ApiOperation(
            value = "Discover the RS api",
            notes = "Get all the endpoints you need to consume the RS API",
            response = OIDCRegistrationResponse.class,
            tags = {"Discovery",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Discovery endpoints",
                    response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Void.class)
    })

    @RequestMapping(
            value = "/open-banking/discovery",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    public ResponseEntity<OBDiscoveryResponse> discovery() {
        OBDiscoveryResponse response = new OBDiscoveryResponse();
        OBDiscovery discovery = new OBDiscovery();
        discovery.setFinancialId(rsConfiguration.getFinancialID());

        for (Map.Entry<OBGroupName, Map<String, OBDiscoveryAPI>> byGroup : rsConfiguration.getDiscoveryApisByVersionAndGroupName().entrySet()) {
            if (byGroup.getValue().isEmpty()) {
                continue;
            }
            for (OBDiscoveryAPI obDiscoveryAPI : byGroup.getValue().values()) {
                switch (byGroup.getKey()) {
                    case AISP:
                        discovery.addAccountAndTransactionAPI(obDiscoveryAPI);
                        break;
                    case PISP:
                        discovery.addPaymentInitiationAPI(obDiscoveryAPI);
                        break;
                    case CBPII:
                        discovery.addFundsConfirmationAPI(obDiscoveryAPI);
                        break;
                    case EVENT:
                        discovery.addEventNotificationAPI(obDiscoveryAPI);
                        break;
                }
            }
        }
        return ResponseEntity.ok(response.data(discovery));
    }
}
