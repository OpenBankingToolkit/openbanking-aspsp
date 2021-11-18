package com.forgerock.openbanking.aspsp.rs.api.vrp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-11-17T13:54:56.728Z[Europe/London]")
@Controller
@RequestMapping("${openapi.oBIEVRPProfile.base-path:}")
public class DomesticVrpsApiController implements DomesticVrpsApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public DomesticVrpsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
