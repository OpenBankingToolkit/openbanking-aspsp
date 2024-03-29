/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.common;

import com.forgerock.openbanking.common.services.onboarding.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.common.services.onboarding.registrationrequest.DirectorySoftwareStatementFactory;
import com.forgerock.openbanking.model.DirectorySoftwareStatement;
import com.forgerock.openbanking.model.DirectorySoftwareStatementOpenBanking;
import com.forgerock.openbanking.model.DirectorySoftwareStatementOpenBanking.DirectorySoftwareStatementOpenBankingBuilder;
import com.forgerock.openbanking.model.OrganisationContact;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestHelperFunctions {

    public static String FORGEROCK_OB_TEST_DIRECTORY_ORG_ID = "0015800001041REAAY";

    public static X509Certificate[] getCertChainFromFile(String path) throws IOException, CertificateException {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(path);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return new X509Certificate[]{(X509Certificate) certificateFactory.generateCertificate(fis)};
        } finally {
            if(fis != null) fis.close();
        }
    }

    public static String getValidForgeRockSsaRegistrationRequestJWTSerialised(){
        return "eyJraWQiOiI4MDBjODBhNzVjOGEwYWQ0Y2FiNzY0NTJlNGY1ZjlkODE0NDFmZjdjIiwiYWxnIjoiUFMyNTYifQ.eyJ0b2tlbl9lbmRwb2ludF9hdXRoX3NpZ25pbmdfYWxnIjoiUFMyNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2FsZyI6IlJTQS1PQUVQLTI1NiIsImdyYW50X3R5cGVzIjpbImF1dGhvcml6YXRpb25fY29kZSIsInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiXSwiaXNzIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwicmVkaXJlY3RfdXJpcyI6WyJodHRwczpcL1wvd3d3Lmdvb2dsZS5jb20iXSwidG9rZW5fZW5kcG9pbnRfYXV0aF9tZXRob2QiOiJwcml2YXRlX2tleV9qd3QiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJleUpyYVdRaU9pSmhaR015TmpJMU1HWmtNMlV6TmpJMFlqWTJPR014TkRneE4yWXhaVFE1WTJGbU9ESTVNVGhpSWl3aVlXeG5Jam9pVUZNeU5UWWlmUS5leUp2Y21kZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl0YjJSbElqb2lWRVZUVkNJc0luTnZablIzWVhKbFgzSmxaR2x5WldOMFgzVnlhWE1pT2xzaWFIUjBjSE02WEM5Y0wyZHZiMmRzWlM1amJ5NTFheUpkTENKdmNtZGZjM1JoZEhWeklqb2lRV04wYVhabElpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgyNWhiV1VpT2lKS1lXMXBaU2R6SUZOdlpuUjNZWEpsSUVGd2NHeHBZMkYwYVc5dUlpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgybGtJam9pTmpCak56VmlZVE5qTkRVd05EVXdNREV4WldaaE5qYzVJaXdpYVhOeklqb2lSbTl5WjJWU2IyTnJJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYMlJsYzJOeWFYQjBhVzl1SWpvaVZHVnpkQ0JoY0hBaUxDSnpiMlowZDJGeVpWOXFkMnR6WDJWdVpIQnZhVzUwSWpvaWFIUjBjSE02WEM5Y0wzTmxjblpwWTJVdVpHbHlaV04wYjNKNUxtUmxkaTF2WWk1bWIzSm5aWEp2WTJzdVptbHVZVzVqYVdGc09qZ3dOelJjTDJGd2FWd3ZjMjltZEhkaGNtVXRjM1JoZEdWdFpXNTBYQzgyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56bGNMMkZ3Y0d4cFkyRjBhVzl1WEM5cWQydGZkWEpwSWl3aWMyOW1kSGRoY21WZmFXUWlPaUkyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56a2lMQ0p2Y21kZlkyOXVkR0ZqZEhNaU9sdGRMQ0p2WWw5eVpXZHBjM1J5ZVY5MGIzTWlPaUpvZEhSd2N6cGNMMXd2WkdseVpXTjBiM0o1TG1SbGRpMXZZaTVtYjNKblpYSnZZMnN1Wm1sdVlXNWphV0ZzT2pnd056UmNMM1J2YzF3dklpd2liM0puWDJsa0lqb2lOakJqTnpWaU9XTmpORFV3TkRVd01ERXhaV1poTmpjNElpd2ljMjltZEhkaGNtVmZhbmRyYzE5eVpYWnZhMlZrWDJWdVpIQnZhVzUwSWpvaVZFOUVUeUlzSW5OdlpuUjNZWEpsWDNKdmJHVnpJanBiSWtSQlZFRWlMQ0pEUWxCSlNTSXNJbEJKVTFBaUxDSkJTVk5RSWwwc0ltVjRjQ0k2TVRZeU5UYzFOVEU0T1N3aWIzSm5YMjVoYldVaU9pSkJibTl1ZVcxdmRYTWlMQ0p2Y21kZmFuZHJjMTl5WlhadmEyVmtYMlZ1WkhCdmFXNTBJam9pVkU5RVR5SXNJbWxoZENJNk1UWXlOVEUxTURNNE9Td2lhblJwSWpvaU1UQXlZVFF5WmpBdE9EUXdaaTAwWlRRMExXRTJOek10TVRnd05EQTNOV000TVRVekluMC5jWjhhUHpuMVo1MklCeGVnVDFmSmVGMTdTczRKSjVYSkF0MVd6TGF2WDRPSDJBS0l4QUdwMDM4Ni1Pd0twR0ZFQkxrVVB1MFp3VzUtczhNYl9uQjU1OHZDY2NObW14cFV4UExSMl9aZkNzSWlpWVRUNWpYYXh2UnVFa0xJc3Zocy15aVcyc05BRXlRRjJwY010M1NpS01YQTFVeTNIWV9lU0pqaURwQlhBMjZGMlF2b1lJdU1iTzd1WkVEeUp4aTd3RVJjMVFpWlB4UzMydUQ4eHhvd19hbHNEOXJpSVBzNG1HRkg2b1RIekdVSG04RUl3MkRleW9LZmMweFItWVoyUjk5NkxYSUpYMkpmOTJGT0RoSURnOU93eXNMMm1Va2QwbDBULTg4UzJYUmVQWHQ2Z0Z3eXV0bFd2MjkzMTY4R1IteExJeTRBcTIwREJkblBGaUc0eWciLCJzY29wZSI6Im9wZW5pZCBhY2NvdW50cyBwYXltZW50cyBmdW5kc2NvbmZpcm1hdGlvbnMiLCJyZXF1ZXN0X29iamVjdF9zaWduaW5nX2FsZyI6IlBTMjU2IiwiZXhwIjoxNjI1MTUwODcwLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2VuYyI6IkExMjhDQkMtSFMyNTYiLCJpYXQiOjE2MjUxNTA1NzAsImp0aSI6ImVlNmVjYzhkLTJiNmEtNDJkMS04ZGZlLTBjMmEyNmIyNzU1NiIsInJlc3BvbnNlX3R5cGVzIjpbImNvZGUgaWRfdG9rZW4iXSwiaWRfdG9rZW5fc2lnbmVkX3Jlc3BvbnNlX2FsZyI6IlBTMjU2In0.Ep9b9GXM0wFZtq1HSH4j6LDojAHTgUvxSQIjzxGX9QPklrJoAk_4Zg_Wooy3Jnw4OsoL92pzqoP8CtsQLDVYCvEfGh9TgbS31ItjXjcACBNAx6sWfT0NaE0T1bmjeSppj8pM18qgkNPXRv211AED0QVizE3b07arNjjj2SaVuarWp1AkSEysb4qepejZFazxAzEQuz8s66SxpPKdMfFKcaJUlr2xGbKiHFuAa6f0QrUSIfIUNQf-6DdrFL1w68EoAkkfbagAx5G4S2e_m0SraIbb9aZqm5LMvAVRYsG5tN8yBPfpWchHGI5_uJeFmNtipVfWuu7KuwiGJmGOd3OtiQ";
    }

    public static String getValidOpenBankingSsaRegistrationRequestJWTSerialised(){
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJQUzI1NiIsImtpZCI6InpQZUZiWDduSm9rRVZweW56YXlXdmd0RkJ4byJ9.eyJpc3MiOiJlYlNxVE5xbVFYRll6NlZ0V0dYWkFhIiwiZXhwIjoxNjc5NTgzOTQ4LCJzY29wZSI6Im9wZW5pZCBhY2NvdW50cyBwYXltZW50cyBldmVudHBvbGxpbmcgZnVuZHNjb25maXJtYXRpb25zIiwicmVzcG9uc2VfdHlwZXMiOlsiY29kZSBpZF90b2tlbiJdLCJyZWRpcmVjdF91cmlzIjpbImh0dHBzOi8vcG9zdG1hbi1lY2hvLmNvbS9nZXQiXSwiYXBwbGljYXRpb25fdHlwZSI6IndlYiIsImdyYW50X3R5cGVzIjpbImF1dGhvcml6YXRpb25fY29kZSIsInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiXSwic29mdHdhcmVfc3RhdGVtZW50IjoiZXlKaGJHY2lPaUpRVXpJMU5pSXNJbXRwWkNJNkltUjZjWFYzVTFSdWJVRmlOMG93TVdSV1JHWkpkMm94UzFjdFlVUTRNMVJZVFRGdFZtSnZPV3RrUldzOUlpd2lkSGx3SWpvaVNsZFVJbjAuZXlKcGMzTWlPaUpQY0dWdVFtRnVhMmx1WnlCTWRHUWlMQ0pwWVhRaU9qRTJOemsxTnpneE1Ea3NJbXAwYVNJNkltVmxPVFpqWkdJMU5EUmlORFEyWkRNaUxDSnpiMlowZDJGeVpWOWxiblpwY205dWJXVnVkQ0k2SW5OaGJtUmliM2dpTENKemIyWjBkMkZ5WlY5dGIyUmxJam9pVkdWemRDSXNJbk52Wm5SM1lYSmxYMmxrSWpvaVpXSlRjVlJPY1cxUldFWlplalpXZEZkSFdGcEJZU0lzSW5OdlpuUjNZWEpsWDJOc2FXVnVkRjlwWkNJNkltVmlVM0ZVVG5GdFVWaEdXWG8yVm5SWFIxaGFRV0VpTENKemIyWjBkMkZ5WlY5amJHbGxiblJmYm1GdFpTSTZJa0YxZEc5dFlYUnBibWN0ZEdWemRHbHVaeUlzSW5OdlpuUjNZWEpsWDJOc2FXVnVkRjlrWlhOamNtbHdkR2x2YmlJNklsUlFVQ0IxYzJWa0lHWnZjaUJoZFhSdmJXRjBhVzVuSUhSbGMzUnBibWNpTENKemIyWjBkMkZ5WlY5MlpYSnphVzl1SWpvaU1TNHhJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYM1Z5YVNJNkltaDBkSEJ6T2k4dlptOXlaMlZ5YjJOckxtTnZiU0lzSW5OdlpuUjNZWEpsWDNKbFpHbHlaV04wWDNWeWFYTWlPbHNpYUhSMGNITTZMeTlzYjJOaGJHaHZjM1FpTENKb2RIUndjem92TDNkM2R5NW5iMjluYkdVdVkyOHVkV3NpTENKb2RIUndjem92TDNCdmMzUnRZVzR0WldOb2J5NWpiMjB2WjJWMElsMHNJbk52Wm5SM1lYSmxYM0p2YkdWeklqcGJJa0ZKVTFBaUxDSlFTVk5RSWwwc0ltOXlaMkZ1YVhOaGRHbHZibDlqYjIxd1pYUmxiblJmWVhWMGFHOXlhWFI1WDJOc1lXbHRjeUk2ZXlKaGRYUm9iM0pwZEhsZmFXUWlPaUpQUWtkQ1VpSXNJbkpsWjJsemRISmhkR2x2Ymw5cFpDSTZJbFZ1YTI1dmQyNHdNREUxT0RBd01EQXhNRFF4VWtWQlFWa2lMQ0p6ZEdGMGRYTWlPaUpCWTNScGRtVWlMQ0poZFhSb2IzSnBjMkYwYVc5dWN5STZXM3NpYldWdFltVnlYM04wWVhSbElqb2lSMElpTENKeWIyeGxjeUk2V3lKUVNWTlFJaXdpUVZOUVUxQWlMQ0pCU1ZOUUlpd2lRMEpRU1VraVhYMHNleUp0WlcxaVpYSmZjM1JoZEdVaU9pSkpSU0lzSW5KdmJHVnpJanBiSWtGSlUxQWlMQ0pCVTFCVFVDSXNJbEJKVTFBaUxDSkRRbEJKU1NKZGZTeDdJbTFsYldKbGNsOXpkR0YwWlNJNklrNU1JaXdpY205c1pYTWlPbHNpVUVsVFVDSXNJa0ZKVTFBaUxDSkJVMUJUVUNJc0lrTkNVRWxKSWwxOVhYMHNJbk52Wm5SM1lYSmxYMnh2WjI5ZmRYSnBJam9pYUhSMGNITTZMeTltYjNKblpYSnZZMnN1WTI5dElpd2liM0puWDNOMFlYUjFjeUk2SWtGamRHbDJaU0lzSW05eVoxOXBaQ0k2SWpBd01UVTRNREF3TURFd05ERlNSVUZCV1NJc0ltOXlaMTl1WVcxbElqb2lSazlTUjBWU1QwTkxJRXhKVFVsVVJVUWlMQ0p2Y21kZlkyOXVkR0ZqZEhNaU9sdDdJbTVoYldVaU9pSlVaV05vYm1sallXd2lMQ0psYldGcGJDSTZJbXBoYldsbExtSnZkMlZ1UUdadmNtZGxjbTlqYXk1amIyMGlMQ0p3YUc5dVpTSTZJalEwTnpjMk5URXdPVFV5TnlJc0luUjVjR1VpT2lKVVpXTm9ibWxqWVd3aWZTeDdJbTVoYldVaU9pSkNkWE5wYm1WemN5SXNJbVZ0WVdsc0lqb2ljM1JsZG1VdVptVnljbWx6UUdadmNtZGxjbTlqYXk1amIyMGlMQ0p3YUc5dVpTSTZJalEwTnpjeE1ETTFNREkyTmlJc0luUjVjR1VpT2lKQ2RYTnBibVZ6Y3lKOVhTd2liM0puWDJwM2EzTmZaVzVrY0c5cGJuUWlPaUpvZEhSd2N6b3ZMMnRsZVhOMGIzSmxMbTl3Wlc1aVlXNXJhVzVuZEdWemRDNXZjbWN1ZFdzdk1EQXhOVGd3TURBd01UQTBNVkpGUVVGWkx6QXdNVFU0TURBd01ERXdOREZTUlVGQldTNXFkMnR6SWl3aWIzSm5YMnAzYTNOZmNtVjJiMnRsWkY5bGJtUndiMmx1ZENJNkltaDBkSEJ6T2k4dmEyVjVjM1J2Y21VdWIzQmxibUpoYm10cGJtZDBaWE4wTG05eVp5NTFheTh3TURFMU9EQXdNREF4TURReFVrVkJRVmt2Y21WMmIydGxaQzh3TURFMU9EQXdNREF4TURReFVrVkJRVmt1YW5kcmN5SXNJbk52Wm5SM1lYSmxYMnAzYTNOZlpXNWtjRzlwYm5RaU9pSm9kSFJ3Y3pvdkwydGxlWE4wYjNKbExtOXdaVzVpWVc1cmFXNW5kR1Z6ZEM1dmNtY3VkV3N2TURBeE5UZ3dNREF3TVRBME1WSkZRVUZaTDJWaVUzRlVUbkZ0VVZoR1dYbzJWblJYUjFoYVFXRXVhbmRyY3lJc0luTnZablIzWVhKbFgycDNhM05mY21WMmIydGxaRjlsYm1Sd2IybHVkQ0k2SW1oMGRIQnpPaTh2YTJWNWMzUnZjbVV1YjNCbGJtSmhibXRwYm1kMFpYTjBMbTl5Wnk1MWF5OHdNREUxT0RBd01EQXhNRFF4VWtWQlFWa3ZjbVYyYjJ0bFpDOWxZbE54VkU1eGJWRllSbGw2TmxaMFYwZFlXa0ZoTG1wM2EzTWlMQ0p6YjJaMGQyRnlaVjl3YjJ4cFkzbGZkWEpwSWpvaWFIUjBjSE02THk5bWIzSm5aWEp2WTJzdVkyOXRJaXdpYzI5bWRIZGhjbVZmZEc5elgzVnlhU0k2SW1oMGRIQnpPaTh2Wm05eVoyVnliMk5yTG1OdmJTSXNJbk52Wm5SM1lYSmxYMjl1WDJKbGFHRnNabDl2Wmw5dmNtY2lPbTUxYkd4OS5FZE1DaVBRTUNfSHZYU0I5czBzMUtJby1yemNSQTloZ3MycXhsd2JkOVNFMmtEUGJxZWZKQWw1NzVfVTl3V3Q1clMwS2lCQm1VZ2wxQ0JvWkRVcV9tSGlBVU9YbUdSM1NTUzI2NTRza1hMVDRWYThRY0JxUkRTaG1rdW4yYXFrcjZNWjFFOTZmeWlwVnlsNjJMV2VMdW5CdUxSMnlwSi1VUkxyanBnV3VwQW5WVVdubUt6SlFDaElhclFORXVkMWZYMmhlcFdKYVpGZHdKQ3hWTG1uYlVqNVpkOFI2c2h6NXc1anNIUmJ4Z0dyTzRHSWxqa2VjUlREUjY0X0E0VkVrUjdpYk5Wcmx0Nkdmbk5fWmN3aHV6eUhwdC1WVExDRnFNVkFyUTFTTmZEajFPcllVLUFsVXROYXdBNEtNbU52ZjlMWHp0bThiQUJmNEx1SkhiYlRCSXciLCJ0b2tlbl9lbmRwb2ludF9hdXRoX21ldGhvZCI6InByaXZhdGVfa2V5X2p3dCIsInRva2VuX2VuZHBvaW50X2F1dGhfc2lnbmluZ19hbGciOiJQUzI1NiIsImlkX3Rva2VuX3NpZ25lZF9yZXNwb25zZV9hbGciOiJQUzI1NiIsInJlcXVlc3Rfb2JqZWN0X3NpZ25pbmdfYWxnIjoiUFMyNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2FsZyI6IlJTQS1PQUVQLTI1NiIsInJlcXVlc3Rfb2JqZWN0X2VuY3J5cHRpb25fZW5jIjoiQTEyOENCQy1IUzI1NiIsInN1YmplY3RfdHlwZSI6InBhaXJ3aXNlIn0.DlnMWhPnG-MVa7S0sdxZjGNcARtsFrVcI_8P6FzXsSRehs_wofOi7SgyDlfINekwGB3A93K_iVcpfOoiMOXDFYCtkx67A19cJ__ia343OcRCD28oNkROLyjgQkHJJUXrOehlxTNSxOHT7wkcu7Fax9rPDRe88QkOBxis7PR-TKsinxLSExf7eUVJoM0YIIvbpV0-DJqTTejKWax90wUc_wW9SvBQVqfe0D-jnEmz56OhJvdploBB0eJ1ePd7yWY6fToONislb0jceL926nhXHVm3_L0ltwxRFzMh1mBb-bJmOH6I5rjQ8YZFe8xtY9NdVqTIhYly-biZI0Rd6YBbcQ";
    }

    public static String getValidSsaSerialised(){
        return "eyJraWQiOiJhZGMyNjI1MGZkM2UzNjI0YjY2OGMxNDgxN2YxZTQ5Y2FmODI5MThiIiwiYWxnIjoiUFMyNTYifQ" +
                ".eyJvcmdfandrc19lbmRwb2ludCI6IlRPRE8iLCJzb2Z0d2FyZV9tb2RlIjoiVEVTVCIsInNvZnR3YXJlX3JlZGlyZWN0X3VyaXMiOlsiaHR0cHM6XC9cL2dvb2dsZS5jby51ayJdLCJvcmdfc3RhdHVzIjoiQWN0aXZlIiwic29mdHdhcmVfY2xpZW50X25hbWUiOiJKYW1pZSdzIFNvZnR3YXJlIEFwcGxpY2F0aW9uIiwic29mdHdhcmVfY2xpZW50X2lkIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwiaXNzIjoiRm9yZ2VSb2NrIiwic29mdHdhcmVfY2xpZW50X2Rlc2NyaXB0aW9uIjoiVGVzdCBhcHAiLCJzb2Z0d2FyZV9qd2tzX2VuZHBvaW50IjoiaHR0cHM6XC9cL3NlcnZpY2UuZGlyZWN0b3J5LmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzRcL2FwaVwvc29mdHdhcmUtc3RhdGVtZW50XC82MGM3NWJhM2M0NTA0NTAwMTFlZmE2NzlcL2FwcGxpY2F0aW9uXC9qd2tfdXJpIiwic29mdHdhcmVfaWQiOiI2MGM3NWJhM2M0NTA0NTAwMTFlZmE2NzkiLCJvcmdfY29udGFjdHMiOltdLCJvYl9yZWdpc3RyeV90b3MiOiJodHRwczpcL1wvZGlyZWN0b3J5LmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzRcL3Rvc1wvIiwib3JnX2lkIjoiNjBjNzViOWNjNDUwNDUwMDExZWZhNjc4Iiwic29mdHdhcmVfandrc19yZXZva2VkX2VuZHBvaW50IjoiVE9ETyIsInNvZnR3YXJlX3JvbGVzIjpbIkRBVEEiLCJDQlBJSSIsIlBJU1AiLCJBSVNQIl0sImV4cCI6MTYyNTc1NTE4OSwib3JnX25hbWUiOiJBbm9ueW1vdXMiLCJvcmdfandrc19yZXZva2VkX2VuZHBvaW50IjoiVE9ETyIsImlhdCI6MTYyNTE1MDM4OSwianRpIjoiMTAyYTQyZjAtODQwZi00ZTQ0LWE2NzMtMTgwNDA3NWM4MTUzIn0.cZ8aPzn1Z52IBxegT1fJeF17Ss4JJ5XJAt1WzLavX4OH2AKIxAGp0386-OwKpGFEBLkUPu0ZwW5-s8Mb_nB558vCccNmmxpUxPLR2_ZfCsIiiYTT5jXaxvRuEkLIsvhs-yiW2sNAEyQF2pcMt3SiKMXA1Uy3HY_eSJjiDpBXA26F2QvoYIuMbO7uZEDyJxi7wERc1QiZPxS32uD8xxow_alsD9riIPs4mGFH6oTHzGUHm8EIw2DeyoKfc0xR-YZ2R996LXIJX2Jf92FODhIDg9OwysL2mUkd0l0T-88S2XRePXt6gFwyutlWv293168GR-xLIy4Aq20DBdnPFiG4yg";

    }

    public static String getValidOBSsaSerialised(){
        return
                "eyJhbGciOiJQUzI1NiIsImtpZCI6ImR6cXV3U1RubUFiN0owMWRWRGZJd2oxS1ctYUQ4M1RYTTFtVmJvOWtkRWs9IiwidHlwIjoiSldUIn0.eyJpc3MiOiJPcGVuQmFua2luZyBMdGQiLCJpYXQiOjE2Nzk1MDEyNjUsImp0aSI6IjQ5YzIxNDg5MDYwZjRjMmEiLCJzb2Z0d2FyZV9lbnZpcm9ubWVudCI6InNhbmRib3giLCJzb2Z0d2FyZV9tb2RlIjoiVGVzdCIsInNvZnR3YXJlX2lkIjoiZWJTcVROcW1RWEZZejZWdFdHWFpBYSIsInNvZnR3YXJlX2NsaWVudF9pZCI6ImViU3FUTnFtUVhGWXo2VnRXR1haQWEiLCJzb2Z0d2FyZV9jbGllbnRfbmFtZSI6IkF1dG9tYXRpbmctdGVzdGluZyIsInNvZnR3YXJlX2NsaWVudF9kZXNjcmlwdGlvbiI6IlRQUCB1c2VkIGZvciBhdXRvbWF0aW5nIHRlc3RpbmciLCJzb2Z0d2FyZV92ZXJzaW9uIjoiMS4xIiwic29mdHdhcmVfY2xpZW50X3VyaSI6Imh0dHBzOi8vZm9yZ2Vyb2NrLmNvbSIsInNvZnR3YXJlX3JlZGlyZWN0X3VyaXMiOlsiaHR0cHM6Ly9sb2NhbGhvc3QiLCJodHRwczovL3d3dy5nb29nbGUuY28udWsiLCJodHRwczovL3Bvc3RtYW4tZWNoby5jb20vZ2V0Il0sInNvZnR3YXJlX3JvbGVzIjpbIkFJU1AiLCJQSVNQIl0sIm9yZ2FuaXNhdGlvbl9jb21wZXRlbnRfYXV0aG9yaXR5X2NsYWltcyI6eyJhdXRob3JpdHlfaWQiOiJPQkdCUiIsInJlZ2lzdHJhdGlvbl9pZCI6IlVua25vd24wMDE1ODAwMDAxMDQxUkVBQVkiLCJzdGF0dXMiOiJBY3RpdmUiLCJhdXRob3Jpc2F0aW9ucyI6W3sibWVtYmVyX3N0YXRlIjoiR0IiLCJyb2xlcyI6WyJQSVNQIiwiQVNQU1AiLCJBSVNQIiwiQ0JQSUkiXX0seyJtZW1iZXJfc3RhdGUiOiJJRSIsInJvbGVzIjpbIkFJU1AiLCJBU1BTUCIsIlBJU1AiLCJDQlBJSSJdfSx7Im1lbWJlcl9zdGF0ZSI6Ik5MIiwicm9sZXMiOlsiUElTUCIsIkFJU1AiLCJBU1BTUCIsIkNCUElJIl19XX0sInNvZnR3YXJlX2xvZ29fdXJpIjoiaHR0cHM6Ly9mb3JnZXJvY2suY29tIiwib3JnX3N0YXR1cyI6IkFjdGl2ZSIsIm9yZ19pZCI6IjAwMTU4MDAwMDEwNDFSRUFBWSIsIm9yZ19uYW1lIjoiRk9SR0VST0NLIExJTUlURUQiLCJvcmdfY29udGFjdHMiOlt7Im5hbWUiOiJUZWNobmljYWwiLCJlbWFpbCI6ImphbWllLmJvd2VuQGZvcmdlcm9jay5jb20iLCJwaG9uZSI6IjQ0Nzc2NTEwOTUyNyIsInR5cGUiOiJUZWNobmljYWwifSx7Im5hbWUiOiJCdXNpbmVzcyIsImVtYWlsIjoic3RldmUuZmVycmlzQGZvcmdlcm9jay5jb20iLCJwaG9uZSI6IjQ0NzcxMDM1MDI2NiIsInR5cGUiOiJCdXNpbmVzcyJ9XSwib3JnX2p3a3NfZW5kcG9pbnQiOiJodHRwczovL2tleXN0b3JlLm9wZW5iYW5raW5ndGVzdC5vcmcudWsvMDAxNTgwMDAwMTA0MVJFQUFZLzAwMTU4MDAwMDEwNDFSRUFBWS5qd2tzIiwib3JnX2p3a3NfcmV2b2tlZF9lbmRwb2ludCI6Imh0dHBzOi8va2V5c3RvcmUub3BlbmJhbmtpbmd0ZXN0Lm9yZy51ay8wMDE1ODAwMDAxMDQxUkVBQVkvcmV2b2tlZC8wMDE1ODAwMDAxMDQxUkVBQVkuandrcyIsInNvZnR3YXJlX2p3a3NfZW5kcG9pbnQiOiJodHRwczovL2tleXN0b3JlLm9wZW5iYW5raW5ndGVzdC5vcmcudWsvMDAxNTgwMDAwMTA0MVJFQUFZL2ViU3FUTnFtUVhGWXo2VnRXR1haQWEuandrcyIsInNvZnR3YXJlX2p3a3NfcmV2b2tlZF9lbmRwb2ludCI6Imh0dHBzOi8va2V5c3RvcmUub3BlbmJhbmtpbmd0ZXN0Lm9yZy51ay8wMDE1ODAwMDAxMDQxUkVBQVkvcmV2b2tlZC9lYlNxVE5xbVFYRll6NlZ0V0dYWkFhLmp3a3MiLCJzb2Z0d2FyZV9wb2xpY3lfdXJpIjoiaHR0cHM6Ly9mb3JnZXJvY2suY29tIiwic29mdHdhcmVfdG9zX3VyaSI6Imh0dHBzOi8vZm9yZ2Vyb2NrLmNvbSIsInNvZnR3YXJlX29uX2JlaGFsZl9vZl9vcmciOm51bGx9.EA4ulOhjRekcWv_tauAlI_43g8wVf8vpEY78VXd-r25YKzrDHKvVTlHCfoCxBt9wtJiRB8MNITP0kN3mvKhlpM4YIlRfwe6jz3ZjjorIROCLnt-SnCFkigub4LWYWw_zAKNLM1MYHyu_-sc8FgYVf59Z6-aJq5mdX3Sr1A-bKpq18DvtmKE6Mh7uJzyuhdeWLWNO_08qb8AhcPB47rAAyEXR93VO6xnuMYWRTICauUo9lW4F74ZfRtmcx2awaaP4_iVbVmvGvcS0dMn7tobMQeIvqpkOLn6uWNXeXE7LC0ykgecfBWXQ_hzFP4k4CElYtzI3uDzuFd0I-QLtKpOsiQ";
    }

    public static DirectorySoftwareStatementFactory getValidSoftwareStatementFactory(){
        OpenBankingDirectoryConfiguration openbankingDirectoryConfiguration = new OpenBankingDirectoryConfiguration();
        openbankingDirectoryConfiguration.issuerId = "OpenBanking ltd";
        return new DirectorySoftwareStatementFactory(openbankingDirectoryConfiguration);
    }

    public static DirectorySoftwareStatement getValidFRDirectorySoftwareStatement() {
        List<String> roles = new ArrayList<String>(Arrays.asList("AISP", "PISP"));

        OrganisationContact orgContact = OrganisationContact.builder()
                .email("fred.blogs@acme.com")
                .name("Fred Blogs")
                .phone("099849388548")
                .type("primary").build();
        List<OrganisationContact> contacts = new ArrayList<OrganisationContact>();
        contacts.add(orgContact);

        orgContact.setEmail("fred.blogs@acme.com");

        List<String> redirectUris = new ArrayList<>(Arrays.asList("https://google.com", "https://acme.com/redir"));

        DirectorySoftwareStatementOpenBankingBuilder builder = DirectorySoftwareStatementOpenBanking.builder();
        builder.iss("ForgeRock")
                .software_logo_uri("https://forgerock.com/")
                .iat(new Date())
                .org_contacts(contacts)
                .org_id("343234324")
                .jti("34325454")
                .org_jwks_endpoint("https://directory.master.forgerock.financial/jwkms")
                .software_id("softwareId")
                .org_status("Active")
                .org_jwks_revoked_endpoint("https://directory.master.forgerock.financial/jwkms/revoked")
                .org_name("Acme inc.")
                .software_client_description("Access your financial info...")
                .software_client_id("software clientId")
                .software_client_name("Acme financial app")
                .software_client_uri("https://acme.com/financial-app")
                .software_environment("Test")
                .software_mode("Test")
                .software_jwks_endpoint("https://service.directory.master.forgerock.financial:443/api/software-statement/61026060452223001501d78a/application/jwk_uri")
                .software_roles(roles)
                .software_redirect_uris(redirectUris);
        return builder.build();
    }


    public static String getValidOBWacAsPemStringFromPath(String path) throws URISyntaxException, IOException {

        String pemFile = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        pemFile = pemFile.replace("\n", "").replace("\r", "");
        return pemFile;
    }

    public static Tpp getValidTpp(String clientId, String tppName){
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setRegistrationAccessToken("tpps-registration-access-token");
        Tpp tpp =  new Tpp();
        tpp.setRegistrationResponse(registrationResponse);
        tpp.setClientId(clientId);
        tpp.setName(tppName);
        tpp.setAuthorisationNumber(tppName);
        return tpp;
    }
}
