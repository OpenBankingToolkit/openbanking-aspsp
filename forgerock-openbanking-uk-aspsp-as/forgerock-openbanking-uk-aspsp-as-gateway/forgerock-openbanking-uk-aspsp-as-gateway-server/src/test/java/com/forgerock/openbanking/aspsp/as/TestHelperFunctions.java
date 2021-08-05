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
package com.forgerock.openbanking.aspsp.as;

import com.forgerock.openbanking.aspsp.as.configuration.OpenBankingDirectoryConfiguration;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.DirectorySoftwareStatement;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.DirectorySoftwareStatementFactory;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.DirectorySoftwareStatementOpenBanking;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.DirectorySoftwareStatementOpenBanking.DirectorySoftwareStatementOpenBankingBuilder;
import com.forgerock.openbanking.aspsp.as.service.registrationrequest.OrganisationContact;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestHelperFunctions {

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

    public static String getValidRegistrationRequestJWTSerialised(){
        return "eyJraWQiOiI4MDBjODBhNzVjOGEwYWQ0Y2FiNzY0NTJlNGY1ZjlkODE0NDFmZjdjIiwiYWxnIjoiUFMyNTYifQ.eyJ0b2tlbl9lbmRwb2ludF9hdXRoX3NpZ25pbmdfYWxnIjoiUFMyNTYiLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2FsZyI6IlJTQS1PQUVQLTI1NiIsImdyYW50X3R5cGVzIjpbImF1dGhvcml6YXRpb25fY29kZSIsInJlZnJlc2hfdG9rZW4iLCJjbGllbnRfY3JlZGVudGlhbHMiXSwiaXNzIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwicmVkaXJlY3RfdXJpcyI6WyJodHRwczpcL1wvd3d3Lmdvb2dsZS5jb20iXSwidG9rZW5fZW5kcG9pbnRfYXV0aF9tZXRob2QiOiJwcml2YXRlX2tleV9qd3QiLCJzb2Z0d2FyZV9zdGF0ZW1lbnQiOiJleUpyYVdRaU9pSmhaR015TmpJMU1HWmtNMlV6TmpJMFlqWTJPR014TkRneE4yWXhaVFE1WTJGbU9ESTVNVGhpSWl3aVlXeG5Jam9pVUZNeU5UWWlmUS5leUp2Y21kZmFuZHJjMTlsYm1Sd2IybHVkQ0k2SWxSUFJFOGlMQ0p6YjJaMGQyRnlaVjl0YjJSbElqb2lWRVZUVkNJc0luTnZablIzWVhKbFgzSmxaR2x5WldOMFgzVnlhWE1pT2xzaWFIUjBjSE02WEM5Y0wyZHZiMmRzWlM1amJ5NTFheUpkTENKdmNtZGZjM1JoZEhWeklqb2lRV04wYVhabElpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgyNWhiV1VpT2lKS1lXMXBaU2R6SUZOdlpuUjNZWEpsSUVGd2NHeHBZMkYwYVc5dUlpd2ljMjltZEhkaGNtVmZZMnhwWlc1MFgybGtJam9pTmpCak56VmlZVE5qTkRVd05EVXdNREV4WldaaE5qYzVJaXdpYVhOeklqb2lSbTl5WjJWU2IyTnJJaXdpYzI5bWRIZGhjbVZmWTJ4cFpXNTBYMlJsYzJOeWFYQjBhVzl1SWpvaVZHVnpkQ0JoY0hBaUxDSnpiMlowZDJGeVpWOXFkMnR6WDJWdVpIQnZhVzUwSWpvaWFIUjBjSE02WEM5Y0wzTmxjblpwWTJVdVpHbHlaV04wYjNKNUxtUmxkaTF2WWk1bWIzSm5aWEp2WTJzdVptbHVZVzVqYVdGc09qZ3dOelJjTDJGd2FWd3ZjMjltZEhkaGNtVXRjM1JoZEdWdFpXNTBYQzgyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56bGNMMkZ3Y0d4cFkyRjBhVzl1WEM5cWQydGZkWEpwSWl3aWMyOW1kSGRoY21WZmFXUWlPaUkyTUdNM05XSmhNMk0wTlRBME5UQXdNVEZsWm1FMk56a2lMQ0p2Y21kZlkyOXVkR0ZqZEhNaU9sdGRMQ0p2WWw5eVpXZHBjM1J5ZVY5MGIzTWlPaUpvZEhSd2N6cGNMMXd2WkdseVpXTjBiM0o1TG1SbGRpMXZZaTVtYjNKblpYSnZZMnN1Wm1sdVlXNWphV0ZzT2pnd056UmNMM1J2YzF3dklpd2liM0puWDJsa0lqb2lOakJqTnpWaU9XTmpORFV3TkRVd01ERXhaV1poTmpjNElpd2ljMjltZEhkaGNtVmZhbmRyYzE5eVpYWnZhMlZrWDJWdVpIQnZhVzUwSWpvaVZFOUVUeUlzSW5OdlpuUjNZWEpsWDNKdmJHVnpJanBiSWtSQlZFRWlMQ0pEUWxCSlNTSXNJbEJKVTFBaUxDSkJTVk5RSWwwc0ltVjRjQ0k2TVRZeU5UYzFOVEU0T1N3aWIzSm5YMjVoYldVaU9pSkJibTl1ZVcxdmRYTWlMQ0p2Y21kZmFuZHJjMTl5WlhadmEyVmtYMlZ1WkhCdmFXNTBJam9pVkU5RVR5SXNJbWxoZENJNk1UWXlOVEUxTURNNE9Td2lhblJwSWpvaU1UQXlZVFF5WmpBdE9EUXdaaTAwWlRRMExXRTJOek10TVRnd05EQTNOV000TVRVekluMC5jWjhhUHpuMVo1MklCeGVnVDFmSmVGMTdTczRKSjVYSkF0MVd6TGF2WDRPSDJBS0l4QUdwMDM4Ni1Pd0twR0ZFQkxrVVB1MFp3VzUtczhNYl9uQjU1OHZDY2NObW14cFV4UExSMl9aZkNzSWlpWVRUNWpYYXh2UnVFa0xJc3Zocy15aVcyc05BRXlRRjJwY010M1NpS01YQTFVeTNIWV9lU0pqaURwQlhBMjZGMlF2b1lJdU1iTzd1WkVEeUp4aTd3RVJjMVFpWlB4UzMydUQ4eHhvd19hbHNEOXJpSVBzNG1HRkg2b1RIekdVSG04RUl3MkRleW9LZmMweFItWVoyUjk5NkxYSUpYMkpmOTJGT0RoSURnOU93eXNMMm1Va2QwbDBULTg4UzJYUmVQWHQ2Z0Z3eXV0bFd2MjkzMTY4R1IteExJeTRBcTIwREJkblBGaUc0eWciLCJzY29wZSI6Im9wZW5pZCBhY2NvdW50cyBwYXltZW50cyBmdW5kc2NvbmZpcm1hdGlvbnMiLCJyZXF1ZXN0X29iamVjdF9zaWduaW5nX2FsZyI6IlBTMjU2IiwiZXhwIjoxNjI1MTUwODcwLCJyZXF1ZXN0X29iamVjdF9lbmNyeXB0aW9uX2VuYyI6IkExMjhDQkMtSFMyNTYiLCJpYXQiOjE2MjUxNTA1NzAsImp0aSI6ImVlNmVjYzhkLTJiNmEtNDJkMS04ZGZlLTBjMmEyNmIyNzU1NiIsInJlc3BvbnNlX3R5cGVzIjpbImNvZGUgaWRfdG9rZW4iXSwiaWRfdG9rZW5fc2lnbmVkX3Jlc3BvbnNlX2FsZyI6IlBTMjU2In0.Ep9b9GXM0wFZtq1HSH4j6LDojAHTgUvxSQIjzxGX9QPklrJoAk_4Zg_Wooy3Jnw4OsoL92pzqoP8CtsQLDVYCvEfGh9TgbS31ItjXjcACBNAx6sWfT0NaE0T1bmjeSppj8pM18qgkNPXRv211AED0QVizE3b07arNjjj2SaVuarWp1AkSEysb4qepejZFazxAzEQuz8s66SxpPKdMfFKcaJUlr2xGbKiHFuAa6f0QrUSIfIUNQf-6DdrFL1w68EoAkkfbagAx5G4S2e_m0SraIbb9aZqm5LMvAVRYsG5tN8yBPfpWchHGI5_uJeFmNtipVfWuu7KuwiGJmGOd3OtiQ";
    }

    public static String getValidSsaSerialised(){
        return "eyJraWQiOiJhZGMyNjI1MGZkM2UzNjI0YjY2OGMxNDgxN2YxZTQ5Y2FmODI5MThiIiwiYWxnIjoiUFMyNTYifQ" +
                ".eyJvcmdfandrc19lbmRwb2ludCI6IlRPRE8iLCJzb2Z0d2FyZV9tb2RlIjoiVEVTVCIsInNvZnR3YXJlX3JlZGlyZWN0X3VyaXMiOlsiaHR0cHM6XC9cL2dvb2dsZS5jby51ayJdLCJvcmdfc3RhdHVzIjoiQWN0aXZlIiwic29mdHdhcmVfY2xpZW50X25hbWUiOiJKYW1pZSdzIFNvZnR3YXJlIEFwcGxpY2F0aW9uIiwic29mdHdhcmVfY2xpZW50X2lkIjoiNjBjNzViYTNjNDUwNDUwMDExZWZhNjc5IiwiaXNzIjoiRm9yZ2VSb2NrIiwic29mdHdhcmVfY2xpZW50X2Rlc2NyaXB0aW9uIjoiVGVzdCBhcHAiLCJzb2Z0d2FyZV9qd2tzX2VuZHBvaW50IjoiaHR0cHM6XC9cL3NlcnZpY2UuZGlyZWN0b3J5LmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzRcL2FwaVwvc29mdHdhcmUtc3RhdGVtZW50XC82MGM3NWJhM2M0NTA0NTAwMTFlZmE2NzlcL2FwcGxpY2F0aW9uXC9qd2tfdXJpIiwic29mdHdhcmVfaWQiOiI2MGM3NWJhM2M0NTA0NTAwMTFlZmE2NzkiLCJvcmdfY29udGFjdHMiOltdLCJvYl9yZWdpc3RyeV90b3MiOiJodHRwczpcL1wvZGlyZWN0b3J5LmRldi1vYi5mb3JnZXJvY2suZmluYW5jaWFsOjgwNzRcL3Rvc1wvIiwib3JnX2lkIjoiNjBjNzViOWNjNDUwNDUwMDExZWZhNjc4Iiwic29mdHdhcmVfandrc19yZXZva2VkX2VuZHBvaW50IjoiVE9ETyIsInNvZnR3YXJlX3JvbGVzIjpbIkRBVEEiLCJDQlBJSSIsIlBJU1AiLCJBSVNQIl0sImV4cCI6MTYyNTc1NTE4OSwib3JnX25hbWUiOiJBbm9ueW1vdXMiLCJvcmdfandrc19yZXZva2VkX2VuZHBvaW50IjoiVE9ETyIsImlhdCI6MTYyNTE1MDM4OSwianRpIjoiMTAyYTQyZjAtODQwZi00ZTQ0LWE2NzMtMTgwNDA3NWM4MTUzIn0.cZ8aPzn1Z52IBxegT1fJeF17Ss4JJ5XJAt1WzLavX4OH2AKIxAGp0386-OwKpGFEBLkUPu0ZwW5-s8Mb_nB558vCccNmmxpUxPLR2_ZfCsIiiYTT5jXaxvRuEkLIsvhs-yiW2sNAEyQF2pcMt3SiKMXA1Uy3HY_eSJjiDpBXA26F2QvoYIuMbO7uZEDyJxi7wERc1QiZPxS32uD8xxow_alsD9riIPs4mGFH6oTHzGUHm8EIw2DeyoKfc0xR-YZ2R996LXIJX2Jf92FODhIDg9OwysL2mUkd0l0T-88S2XRePXt6gFwyutlWv293168GR-xLIy4Aq20DBdnPFiG4yg";

    }

    public static String getValidOBSsaSerialised(){
        return
                "eyJhbGciOiJQUzI1NiIsImtpZCI6Imo4SFdZMDBhSUJtS0ExT1c3WW50dnRLVU0ycnVueDdvQWdiS2hJRE1IM0k9IiwidHlwIjoiSldUIn0.eyJpc3MiOiJPcGVuQmFua2luZyBMdGQiLCJpYXQiOjE2MjczOTQyNjAsImp0aSI6ImIyZDRkNzdkZmFjYTRhMWEiLCJzb2Z0d2FyZV9lbnZpcm9ubWVudCI6InNhbmRib3giLCJzb2Z0d2FyZV9tb2RlIjoiVGVzdCIsInNvZnR3YXJlX2lkIjoiT1g2c0o4RXYyUnJyeWxNV1FLaUplSiIsInNvZnR3YXJlX2NsaWVudF9pZCI6Ik9YNnNKOEV2MlJycnlsTVdRS2lKZUoiLCJzb2Z0d2FyZV9jbGllbnRfbmFtZSI6InRlc3QiLCJzb2Z0d2FyZV9jbGllbnRfZGVzY3JpcHRpb24iOiJodHRwczovL3Rlc3QuY29tIiwic29mdHdhcmVfdmVyc2lvbiI6MS4xLCJzb2Z0d2FyZV9jbGllbnRfdXJpIjoiaHR0cHM6Ly90ZXN0LmNvbSIsInNvZnR3YXJlX3JlZGlyZWN0X3VyaXMiOlsiaHR0cHM6Ly90ZXN0LmNvbSJdLCJzb2Z0d2FyZV9yb2xlcyI6WyJBSVNQIiwiUElTUCJdLCJvcmdhbmlzYXRpb25fY29tcGV0ZW50X2F1dGhvcml0eV9jbGFpbXMiOnsiYXV0aG9yaXR5X2lkIjoiT0JHQlIiLCJyZWdpc3RyYXRpb25faWQiOiJVbmtub3duMDAxNTgwMDAwMTA0MVJFQUFZIiwic3RhdHVzIjoiQWN0aXZlIiwiYXV0aG9yaXNhdGlvbnMiOlt7Im1lbWJlcl9zdGF0ZSI6IkdCIiwicm9sZXMiOlsiUElTUCIsIkFJU1AiLCJBU1BTUCJdfSx7Im1lbWJlcl9zdGF0ZSI6IklFIiwicm9sZXMiOlsiQVNQU1AiLCJBSVNQIiwiUElTUCJdfSx7Im1lbWJlcl9zdGF0ZSI6Ik5MIiwicm9sZXMiOlsiQVNQU1AiLCJQSVNQIiwiQUlTUCJdfV19LCJzb2Z0d2FyZV9sb2dvX3VyaSI6Imh0dHBzOi8vdGVzdC5jb20iLCJvcmdfc3RhdHVzIjoiQWN0aXZlIiwib3JnX2lkIjoiMDAxNTgwMDAwMTA0MVJFQUFZIiwib3JnX25hbWUiOiJGT1JHRVJPQ0sgTElNSVRFRCIsIm9yZ19jb250YWN0cyI6W3sibmFtZSI6IlRlY2huaWNhbCIsImVtYWlsIjoiamFtaWUuYm93ZW5AZm9yZ2Vyb2NrLmNvbSIsInBob25lIjoiNDQ3NzY1MTA5NTI3IiwidHlwZSI6IlRlY2huaWNhbCJ9LHsibmFtZSI6IkJ1c2luZXNzIiwiZW1haWwiOiJqb2huLnByb3VkZm9vdEBmb3JnZXJvY2suY29tIiwicGhvbmUiOiI0NDc3MTAzNTAyNjYiLCJ0eXBlIjoiQnVzaW5lc3MifV0sIm9yZ19qd2tzX2VuZHBvaW50IjoiaHR0cHM6Ly9rZXlzdG9yZS5vcGVuYmFua2luZ3Rlc3Qub3JnLnVrLzAwMTU4MDAwMDEwNDFSRUFBWS8wMDE1ODAwMDAxMDQxUkVBQVkuandrcyIsIm9yZ19qd2tzX3Jldm9rZWRfZW5kcG9pbnQiOiJodHRwczovL2tleXN0b3JlLm9wZW5iYW5raW5ndGVzdC5vcmcudWsvMDAxNTgwMDAwMTA0MVJFQUFZL3Jldm9rZWQvMDAxNTgwMDAwMTA0MVJFQUFZLmp3a3MiLCJzb2Z0d2FyZV9qd2tzX2VuZHBvaW50IjoiaHR0cHM6Ly9rZXlzdG9yZS5vcGVuYmFua2luZ3Rlc3Qub3JnLnVrLzAwMTU4MDAwMDEwNDFSRUFBWS9PWDZzSjhFdjJScnJ5bE1XUUtpSmVKLmp3a3MiLCJzb2Z0d2FyZV9qd2tzX3Jldm9rZWRfZW5kcG9pbnQiOiJodHRwczovL2tleXN0b3JlLm9wZW5iYW5raW5ndGVzdC5vcmcudWsvMDAxNTgwMDAwMTA0MVJFQUFZL3Jldm9rZWQvT1g2c0o4RXYyUnJyeWxNV1FLaUplSi5qd2tzIiwic29mdHdhcmVfcG9saWN5X3VyaSI6Imh0dHBzOi8vdGVzdC5jb20iLCJzb2Z0d2FyZV90b3NfdXJpIjoiaHR0cHM6Ly90ZXN0LmNvbSIsInNvZnR3YXJlX29uX2JlaGFsZl9vZl9vcmciOm51bGx9.geGFe0EcYQtpwG847TuC6tFRh35_25lhBss5jReTiet9tipqpqzQqtLCh_3ZJvV17KHtr8xbvyvhUhyNYtJPbYA60Jj2XZUTLk7dcQ8LHqH5IGH4gaV-41Qlv0S09fTkfca4i4K2-XF3noL7ijQtPOgl5uNQY6ljkwBiWesvwnRFgoNUl-iQc3E3PGtL1FTgBKEcjT6p192oZS37YbnPNkxbgyfYXSBYNQ6jviIb5PcZCxNw9n2OT0-RjZ1A1FLRZLOZqFyCkwuzrwM8hnPba-raBbYbTDY8PC_B3nfBfaRJCTKeTBGlfA-a6KtDv_hElFuK-53R1hLEheto4zcPgw";
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
}
