/**
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
package com.forgerock.openbanking.aspsp.rs.rcs.services;


import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.integration.test.support.Tracer;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class RCSErrorServiceTest {
    private RCSErrorService rcsErrorService;

    @Before
    public void setup() {
        rcsErrorService = new RCSErrorService(Tracer.mockTracer());
    }

    @Test
    public void invalidConsentError() throws Exception {
        // Given
        String consentJWT = "eyJ0eXAiOiJKV1QiLCJraWQiOiJ3VTNpZklJYUxPVUFSZVJCL0ZHNmVNMVAxUU09IiwiYWxnIjoiUFMyNTYifQ.eyJjbGllbnRJZCI6IjM3M2MyYzE3LTczNDEtNDRhMy04NzMyLWIxZGI1ZDBkMTAxMCIsImlzcyI6Imh0dHBzOi8vbWF0bHMuYXMuYXNwc3AuZGV2LW9iLmZvcmdlcm9jay5maW5hbmNpYWw6ODA3NC9vYXV0aDIiLCJjc3JmIjoiSkR5SzM3cFlvQmY1RkJHR1ptYVFnZWhCbUQ3YzNMM2I1cHozSVNNeThTZz0iLCJjbGllbnRfZGVzY3JpcHRpb24iOiIiLCJhdWQiOiJmb3JnZXJvY2stcmNzIiwic2F2ZV9jb25zZW50X2VuYWJsZWQiOmZhbHNlLCJjbGFpbXMiOnsidXNlcl9pbmZvIjp7ImFjciI6eyJ2YWx1ZSI6InVybjpvcGVuYmFua2luZzpwc2QyOnNjYSIsImVzc2VudGlhbCI6dHJ1ZX0sIm9wZW5iYW5raW5nX2ludGVudF9pZCI6eyJ2YWx1ZSI6IlBEU0NfN2I4NWRhMDgtMmZjOC00ZTQxLWI3YjItYjY1MjQ3NDBmMDQiLCJlc3NlbnRpYWwiOnRydWV9fSwiaWRfdG9rZW4iOnsiYWNyIjp7InZhbHVlIjoidXJuOm9wZW5iYW5raW5nOnBzZDI6c2NhIiwiZXNzZW50aWFsIjp0cnVlfSwib3BlbmJhbmtpbmdfaW50ZW50X2lkIjp7InZhbHVlIjoiUERTQ183Yjg1ZGEwOC0yZmM4LTRlNDEtYjdiMi1iNjUyNDc0MGYwNCIsImVzc2VudGlhbCI6dHJ1ZX19fSwic2NvcGVzIjp7ImZ1bmRzY29uZmlybWF0aW9ucyI6ImZ1bmRzY29uZmlybWF0aW9ucyIsImFjY291bnRzIjoiYWNjb3VudHMiLCJvcGVuaWQiOiJvcGVuaWQiLCJwYXltZW50cyI6InBheW1lbnRzIn0sImV4cCI6MTU1MjA1NzQ1NCwiaWF0IjoxNTUyMDU3Mjc0LCJjbGllbnRfbmFtZSI6IkZvclRlc3RfMmU2NzQ0NzMtMzkzYS00MTUyLWFjMjQtZTQ2YzllNzMxZmQ3IiwiY29uc2VudEFwcHJvdmFsUmVkaXJlY3RVcmkiOiJodHRwczovL21hdGxzLmFzLmFzcHNwLmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzQvb2F1dGgyL2F1dGhvcml6ZT9yZXNwb25zZV90eXBlPWNvZGUlMjBpZF90b2tlbiZjbGllbnRfaWQ9MzczYzJjMTctNzM0MS00NGEzLTg3MzItYjFkYjVkMGQxMDEwJnN0YXRlPTEwZDI2MGJmLWE3ZDktNDQ0YS05MmQ5LTdiN2E1ZjA4ODIwOCZub25jZT0xMGQyNjBiZi1hN2Q5LTQ0NGEtOTJkOS03YjdhNWYwODgyMDgmc2NvcGU9b3BlbmlkJTIwYWNjb3VudHMlMjBwYXltZW50cyUyMGZ1bmRzY29uZmlybWF0aW9ucyZyZWRpcmVjdF91cmk9aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbSZ4LW9iLW1vbml0b3Jpbmc9MmU2NzQ0NzMtMzkzYS00MTUyLWFjMjQtZTQ2YzllNzMxZmQ3JmFjcj11cm46b3BlbmJhbmtpbmc6cHNkMjpzY2EmYWNyX3NpZz02NFU0VXRvajJZTDJEM01BdTZfSUJHOUJ0VEpFc214SE9pTzJMcHMwZ0ZJJnJlcXVlc3Q9ZXlKcmFXUWlPaUptTUdVeU9EZzBPR1pqTWpKbU16QTRZbVppTURJeE9UVTRNVGsyWlRabE9XSmpPR1JoTlRjMUlpd2lZV3huSWpvaVVGTXlOVFlpZlEuZXlKaGRXUWlPaUpvZEhSd2N6cGNMMXd2YldGMGJITXVZWE11WVhOd2MzQXVaR1YyTFc5aUxtWnZjbWRsY205amF5NW1hVzVoYm1OcFlXdzZPREEzTkZ3dmIyRjFkR2d5SWl3aWMyTnZjR1VpT2lKdmNHVnVhV1FnWVdOamIzVnVkSE1nY0dGNWJXVnVkSE1nWm5WdVpITmpiMjVtYVhKdFlYUnBiMjV6SWl3aWFYTnpJam9pTXpjell6SmpNVGN0TnpNME1TMDBOR0V6TFRnM016SXRZakZrWWpWa01HUXhNREV3SWl3aVkyeGhhVzF6SWpwN0ltbGtYM1J2YTJWdUlqcDdJbUZqY2lJNmV5SjJZV3gxWlNJNkluVnlianB2Y0dWdVltRnVhMmx1Wnpwd2MyUXlPbk5qWVNJc0ltVnpjMlZ1ZEdsaGJDSTZkSEoxWlgwc0ltOXdaVzVpWVc1cmFXNW5YMmx1ZEdWdWRGOXBaQ0k2ZXlKMllXeDFaU0k2SWxCRVUwTmZOMkk0TldSaE1EZ3RNbVpqT0MwMFpUUXhMV0kzWWpJdFlqWTFNalEzTkRCbU1EUWlMQ0psYzNObGJuUnBZV3dpT25SeWRXVjlmU3dpZFhObGNtbHVabThpT25zaWIzQmxibUpoYm10cGJtZGZhVzUwWlc1MFgybGtJanA3SW5aaGJIVmxJam9pVUVSVFExODNZamcxWkdFd09DMHlabU00TFRSbE5ERXRZamRpTWkxaU5qVXlORGMwTUdZd05DSXNJbVZ6YzJWdWRHbGhiQ0k2ZEhKMVpYMTlmU3dpY21WemNHOXVjMlZmZEhsd1pTSTZJbU52WkdVZ2FXUmZkRzlyWlc0aUxDSnlaV1JwY21WamRGOTFjbWtpT2lKb2RIUndjenBjTDF3dmQzZDNMbWR2YjJkc1pTNWpiMjBpTENKemRHRjBaU0k2SWpFd1pESTJNR0ptTFdFM1pEa3RORFEwWVMwNU1tUTVMVGRpTjJFMVpqQTRPREl3T0NJc0ltVjRjQ0k2TVRVMU1qQTFOemd4TUN3aWJtOXVZMlVpT2lJeE1HUXlOakJpWmkxaE4yUTVMVFEwTkdFdE9USmtPUzAzWWpkaE5XWXdPRGd5TURnaUxDSnBZWFFpT2pFMU5USXdOVGMxTVRBc0ltTnNhV1Z1ZEY5cFpDSTZJak0zTTJNeVl6RTNMVGN6TkRFdE5EUmhNeTA0TnpNeUxXSXhaR0kxWkRCa01UQXhNQ0lzSW1wMGFTSTZJbUUyT0RRM056ZzFMV1UzTm1FdE5HSTROaTFpTkdKbUxXUmxPV1JqWkdReU5HUXlOU0o5LkVvOVZxUjlnOXJUMkNwOWV3Y2VLUVhuaWlQR25TZVZfVzM1SlE0V3RPYzU4X2p2T05MYXlJeXFBYkhyN1NxSzJTZ2hNbTJPTzhLTEw0LU5UY2hvYS1sbS1yY1JRdmM4bHAycWJQTTdHems2R01nWHFVeU5kYll5eEVrUl8wRWE1UlUwckhhdGZRQWM1UGVucmkzT081TVdpbEpPaWpMNWhURHFLTE96Q2w4NTIzUEYwdTlDd3lHSFpGM2ZtQTZpQXp5a21IVEJLZnplSEEyd1p5MW1SUEw5eUJ1TkxHVWZkZndFRUtkUG9SSEt5dEVtb1RWRVg3X2E4eDVZcTVzcjdNTlJvcUMyNjV6WktESEE5c1JNcGJiWmMtc1phb21uT2VVVGthbUpFQW5xMkM2dUliQkx5VmE1MHJwbFJOYmxBUnM0U1l0VWxKbWJHeTIwaVJieGJtUSIsInVzZXJuYW1lIjoiZGF2aWQuaGlnZ2lucyJ9.x129SkRwuGwjMkyE5BD4cmw_Sa7edOiN9kj3p1DJLww2vNyE7HAZPei28xK4l08dVUdip1hwNpR5PwLlEh7znNkiwokAKKdvYsRfkTPdy6WawqllY8FKhVFwlewParrBZZ1E0L4zzIlweKvxNZZ9ERx1SqG3sJ65iBxEIHjKAxiGKmxK-XdWbBll06L-dKx0YpldB3MT68NhTCTvRvPBNfb1_9_342MU6XyE-6rAiWi_tjxJR6v9wT5b6Qd-idsMHiFT-i_aZ_7sTmIVjJoqea3LKPSVYIgXDAreo-fB5wXeIC2yttQy2qERsgyiFrLGQTbBBmV1MLVuoXDr3-8sHw";
        OBErrorException obErrorException =  new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID, "No intent ID");

        // When
        ResponseEntity<RedirectionAction> response = rcsErrorService.invalidConsentError(consentJWT, obErrorException);

        // Then
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://www.google.com")
                .fragment("error=invalid_request_object&state=10d260bf-a7d9-444a-92d9-7b7a5f088208&error_description="
                        + String.format(obErrorException.getOBError().getMessage(), obErrorException.getArgs()))
                .encode()
                .build();

        assertThat(response.getStatusCode()).isEqualTo(obErrorException.getObriErrorType().getHttpStatus());
        RedirectionAction body = Objects.requireNonNull(response.getBody());
        assertThat(body.getRedirectUri()).isEqualTo(uriComponents.toUriString());
    }
}