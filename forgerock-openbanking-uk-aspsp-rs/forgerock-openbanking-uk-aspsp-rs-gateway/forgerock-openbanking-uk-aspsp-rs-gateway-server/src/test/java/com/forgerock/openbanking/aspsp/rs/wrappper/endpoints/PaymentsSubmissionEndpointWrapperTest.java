package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticConsent1;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.ErrorCode;
import dev.openbanking4.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentsSubmissionEndpointWrapperTest {

    private static PaymentsSubmissionsEndpointWrapper wrapper;

    @MockBean(name = "cryptoApiClient")
    private CryptoApiClient cryptoApiClient;

    @MockBean(name = "amOpenBankingConfiguration")
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    @MockBean(name = "amResourceServerService")
    private RSEndpointWrapperService rsEndpointWrapperService;


    @Before
    public void setup() {

        // setting required objects to the perform test
        UUID uuid = UUID.randomUUID();
        ReflectionTestUtils.setField(amOpenBankingConfiguration, "audiences", Arrays.asList("https://am.dev-ob.forgerock.financial:443/oauth2/auth"));
        AMResourceServerService amService = new AMResourceServerService();
        ReflectionTestUtils.setField(amService, "cryptoApiClient", cryptoApiClient);
        ReflectionTestUtils.setField(amService, "amOpenBankingConfiguration", amOpenBankingConfiguration);
        ReflectionTestUtils.setField(rsEndpointWrapperService, "amResourceServerService", amService);
        // rs configuration
        RSConfiguration rsConfiguration = new RSConfiguration("IssuerID", uuid.toString(), "jwks_uri");
        OBHeaderCheckerService obHeaderCheckerService = new OBHeaderCheckerService(rsConfiguration);
        ReflectionTestUtils.setField(rsEndpointWrapperService, "obHeaderCheckerService", obHeaderCheckerService);

        wrapper = new PaymentsSubmissionsEndpointWrapper(rsEndpointWrapperService) {
            @Override
            protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
                return super.run(main);
            }
        };
        wrapper.xFapiFinancialId(uuid.toString());
        wrapper.principal(new PasswordLessUserNameAuthentication("test-tpp", Collections.EMPTY_LIST));
    }

    @Test
    public void verifyAccessUsing_GrantTypeOK() throws Exception {

        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        wrapper.authorization("Bearer " + jws);

        // then
        assertThatCode(() -> {
            wrapper.payment(payment).applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessUsing_GrantTypeWrong() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);

        // then
        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).applyFilters(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(403);
        assertThat(obErrorException.getOBError().getErrorCode()).isEqualTo(ErrorCode.OBRI_ACCESS_TOKEN_INVALID.getValue());
        assertThat(obErrorException.getMessage()).isEqualTo("The access token grant type CLIENT_CREDENTIAL doesn't match one of the expected grant types [AUTHORIZATION_CODE, HEADLESS_AUTH]");
    }

    @Test
    public void verifyPaymentStatus_complete() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();
        // then
        assertThatCode(() -> {
            wrapper.payment(payment).verifyPaymentStatus();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyPaymentStatus_notConsented() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.ACCEPTEDTECHNICALVALIDATION)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(406);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment request hasn't been authorised by the PSU yet. Payment request status: 'ACCEPTEDTECHNICALVALIDATION'");
    }

    @Test
    public void verifyPaymentStatus_pending() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.PENDING)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(406);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment still pending. Payment request status: 'PENDING'");
    }

    @Test
    public void verifyPaymentStatus_rejected() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.REJECTED)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(401);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment has been rejected. Payment request status: 'REJECTED'");
    }
}
