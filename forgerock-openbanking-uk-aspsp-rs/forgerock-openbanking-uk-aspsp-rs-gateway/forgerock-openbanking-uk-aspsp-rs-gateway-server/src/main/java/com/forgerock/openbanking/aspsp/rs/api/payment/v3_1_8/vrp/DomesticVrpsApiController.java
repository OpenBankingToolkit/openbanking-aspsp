package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_8.vrp;

import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPDetails;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPResponse;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller
@RequestMapping("${openapi.oBIEVRPProfile.base-path:}")
public class DomesticVrpsApiController implements DomesticVrpsApi {

    @Override
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPDetails> domesticVrpPaymentDetailsGet(
            String domesticVRPId, String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress,
            String xFapiInteractionId, String xCustomerUserAgent, HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPDetails>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPResponse> domesticVrpPost(
            String authorization, String xJwsSignature, OBDomesticVRPRequest obDomesticVRPRequest, String xFapiAuthDate,
            String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent,
            HttpServletRequest request, Principal principal
    ) throws OBErrorResponseException {
        return new ResponseEntity<OBDomesticVRPResponse>(HttpStatus.NOT_IMPLEMENTED);
    }
}
