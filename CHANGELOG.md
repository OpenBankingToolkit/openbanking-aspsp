# Git Changelog Maven plugin changelog
Changelog of Git Changelog Maven plugin.
## forgerock-openbanking-aspsp-1.0.51
[b2e71590259a5be](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b2e71590259a5be) Ben Jefferies *2020-01-23 21:09:02*
Fix update dynamic registration bug (#106)

A parsing bug of the CN causes update of OIDC client to fail with a 500
with the stacktrace

```
java.lang.IllegalArgumentException: badly formatted directory string
	at org.bouncycastle.asn1.x500.style.IETFUtils.rDNsFromString(Unknown Source)
	at org.bouncycastle.asn1.x500.style.BCStyle.fromString(Unknown Source)
	at org.bouncycastle.asn1.x500.X500Name.<init>(Unknown Source)
	at org.bouncycastle.asn1.x500.X500Name.<init>(Unknown Source)
	at com.forgerock.openbanking.aspsp.as.api.registration.dynamic.DynamicRegistrationApiController.verifyRegistrationRequest(DynamicRegistrationApiController.java:383)
	at com.forgerock.openbanking.aspsp.as.api.registration.dynamic.DynamicRegistrationApiController.updateClient(DynamicRegistrationApiController.java:221)
```

This change fixes the parsing
## forgerock-openbanking-aspsp-1.0.50
[f3ced19b038affc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f3ced19b038affc) JamieB *2020-01-23 10:26:09*
Bump again
[db17f64f87a5521](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/db17f64f87a5521) JamieB *2020-01-23 09:41:25*
Bump openbanking-auth version
## forgerock-openbanking-aspsp-1.0.49
[7e852de02338a09](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7e852de02338a09) Julien Renaux *2020-01-23 09:27:45*
fix consent UI on tiny screens and with TPP long name. https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/100 (#103)
[dcae7a807538cfc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/dcae7a807538cfc) dependabot-preview[bot] *2020-01-23 09:25:10*
Bump ob-clients.version from 1.0.21 to 1.0.22

Bumps `ob-clients.version` from 1.0.21 to 1.0.22.

Updates `forgerock-openbanking-jwkms-client` from 1.0.21 to 1.0.22
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.21...forgerock-openbanking-reference-implementation-clients-1.0.22)

Updates `forgerock-openbanking-analytics-client` from 1.0.21 to 1.0.22
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.21...forgerock-openbanking-reference-implementation-clients-1.0.22)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.21 to 1.0.22
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.21...forgerock-openbanking-reference-implementation-clients-1.0.22)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.48
[c292cb9da16f186](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c292cb9da16f186) Ben Jefferies *2020-01-22 15:44:52*
Remove spring boot plugin (#104)

The plugin causes a fat jar to be built. We do not really care if it's a
fat jar because we'd run it from intellij. The fat jars take up a lot of
bintray space
## forgerock-openbanking-aspsp-1.0.47
[1116775e94c2f2e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1116775e94c2f2e) Ben Jefferies *2020-01-22 11:26:21*
Fix parsing CN correctly (#102)

Parse the CN correctly to create the TPP when they register.

Fixes https://github.com/OpenBankingToolkit/openbanking-reference-implementation/issues/104
## forgerock-openbanking-aspsp-1.0.46
[468b2fd9023c775](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/468b2fd9023c775) dependabot-preview[bot] *2020-01-21 13:59:36*
Bump ob-common.version from 1.0.61 to 1.0.62

Bumps `ob-common.version` from 1.0.61 to 1.0.62.

Updates `forgerock-openbanking-model` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Updates `forgerock-openbanking-am` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Updates `forgerock-openbanking-jwt` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Updates `forgerock-openbanking-oidc` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Updates `forgerock-openbanking-upgrade` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Updates `forgerock-openbanking-ssl` from 1.0.61 to 1.0.62
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.61...forgerock-openbanking-starter-commons-1.0.62)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.45
[3f7d30fe9c9f1cd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3f7d30fe9c9f1cd) dependabot-preview[bot] *2020-01-20 17:29:21*
Bump ob-clients.version from 1.0.20 to 1.0.21

Bumps `ob-clients.version` from 1.0.20 to 1.0.21.

Updates `forgerock-openbanking-jwkms-client` from 1.0.20 to 1.0.21
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.20...forgerock-openbanking-reference-implementation-clients-1.0.21)

Updates `forgerock-openbanking-analytics-client` from 1.0.20 to 1.0.21
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.20...forgerock-openbanking-reference-implementation-clients-1.0.21)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.20 to 1.0.21
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.20...forgerock-openbanking-reference-implementation-clients-1.0.21)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.44
[e5c3034eb4a05e6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e5c3034eb4a05e6) Julien Renaux *2020-01-20 09:30:50*
use npm to get openbanking-ui-cli (#81)
## forgerock-openbanking-aspsp-1.0.43
[3112d287c5ea4f1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3112d287c5ea4f1) Ben Jefferies *2020-01-20 08:55:59*
Skip the perform of a maven release (#82)

We do not need to push the samples up and they consume a lot of storage
causing us to hit limits
## forgerock-openbanking-aspsp-1.0.42
[76487e6cfc4fa54](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/76487e6cfc4fa54) dependabot-preview[bot] *2020-01-17 16:16:50*
Bump ob-clients.version from 1.0.16 to 1.0.20

Bumps `ob-clients.version` from 1.0.16 to 1.0.20.

Updates `forgerock-openbanking-jwkms-client` from 1.0.16 to 1.0.20
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.16...forgerock-openbanking-reference-implementation-clients-1.0.20)

Updates `forgerock-openbanking-analytics-client` from 1.0.16 to 1.0.20
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.16...forgerock-openbanking-reference-implementation-clients-1.0.20)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.16 to 1.0.20
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.16...forgerock-openbanking-reference-implementation-clients-1.0.20)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[2d9af8accf3b22e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2d9af8accf3b22e) dependabot-preview[bot] *2020-01-17 16:16:21*
Bump ob-common.version from 1.0.59 to 1.0.61

Bumps `ob-common.version` from 1.0.59 to 1.0.61.

Updates `forgerock-openbanking-model` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Updates `forgerock-openbanking-am` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Updates `forgerock-openbanking-jwt` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Updates `forgerock-openbanking-oidc` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Updates `forgerock-openbanking-upgrade` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Updates `forgerock-openbanking-ssl` from 1.0.59 to 1.0.61
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.59...forgerock-openbanking-starter-commons-1.0.61)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[618b37d5da917ef](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/618b37d5da917ef) dependabot-preview[bot] *2020-01-17 16:33:30*
Bump forgerock-openbanking-jwkms-embedded from 1.1.48 to 1.1.50

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.48 to 1.1.50.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.48...forgerock-openbanking-reference-implementation-jwkms-1.1.50)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[fcecf6d259bd6b0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fcecf6d259bd6b0) dependabot-preview[bot] *2020-01-17 16:15:32*
Bump forgerock-openbanking-starter-parent from 1.0.57 to 1.0.60

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.57 to 1.0.60.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.57...forgerock-openbanking-starter-parent-1.0.60)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.41
[df32735de4e9550](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/df32735de4e9550) Jorge Sanchez Perez *2020-01-17 15:43:10*
Fix the api protection payment with client credentials grant type (#70)

* fix client cledentials Grant type to Get domestic payment with the paymentId

* Unit tests and refactorization of payment enpoints

* Fix Unit tests copyright

* Refactorization of test classes to use Mockito

* Refactorization of test classes to use Mockito

* fix copyright
## forgerock-openbanking-aspsp-1.0.40
[5f046a8b9222f07](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5f046a8b9222f07) Julien Renaux *2020-01-15 10:05:19*
allow any config key to be overwritten by docker env (#69)
[9dc472dbd520bdd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9dc472dbd520bdd) dependabot-preview[bot] *2020-01-15 10:00:35*
Bump ob-common.version from 1.0.58 to 1.0.59

Bumps `ob-common.version` from 1.0.58 to 1.0.59.

Updates `forgerock-openbanking-model` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Updates `forgerock-openbanking-am` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Updates `forgerock-openbanking-jwt` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Updates `forgerock-openbanking-oidc` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Updates `forgerock-openbanking-upgrade` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Updates `forgerock-openbanking-ssl` from 1.0.58 to 1.0.59
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.58...forgerock-openbanking-starter-commons-1.0.59)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[fb3bcf54235dc93](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fb3bcf54235dc93) dependabot-preview[bot] *2020-01-15 10:00:06*
Bump forgerock-openbanking-jwkms-embedded from 1.1.45 to 1.1.48

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.45 to 1.1.48.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.45...forgerock-openbanking-reference-implementation-jwkms-1.1.48)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[b8e0562d452fde7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b8e0562d452fde7) JamieB *2020-01-16 20:07:14*
Remove unused forgerock-eidas-psd2-sdk-cert dependencies

- These need to be removed before the latest parent pom can be used as
the eidas-psd2 library has moved and it's groupId and artifactId have
changed, meaning this library would fail to build when dependabot
tried to update to the lastest parent pom
## forgerock-openbanking-aspsp-1.0.39
[689b1f70241bc83](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/689b1f70241bc83) Julien Renaux *2020-01-14 13:01:16*
update UI docs https://github.com/OpenBankingToolkit/openbanking-refe… (#62)

* update UI docs https://github.com/OpenBankingToolkit/openbanking-reference-implementation/issues/63

* Load from maven central first

Co-authored-by: Ben Jefferies <benjefferies@echosoft.uk>
## forgerock-openbanking-aspsp-1.0.38
[3cad4b7d68c760f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3cad4b7d68c760f) dependabot-preview[bot] *2020-01-14 10:27:00*
Bump forgerock-openbanking-auth from 1.0.48 to 1.0.53

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.48 to 1.0.53.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.48...forgerock-openbanking-starter-auth-1.0.53)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[0a7c0ff860e39d8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0a7c0ff860e39d8) dependabot-preview[bot] *2020-01-14 10:00:26*
Bump ob-common.version from 1.0.57 to 1.0.58

Bumps `ob-common.version` from 1.0.57 to 1.0.58.

Updates `forgerock-openbanking-model` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Updates `forgerock-openbanking-am` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Updates `forgerock-openbanking-jwt` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Updates `forgerock-openbanking-oidc` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Updates `forgerock-openbanking-upgrade` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Updates `forgerock-openbanking-ssl` from 1.0.57 to 1.0.58
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.57...forgerock-openbanking-starter-commons-1.0.58)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.37
[c6e93d0fcbe0dbf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c6e93d0fcbe0dbf) dependabot-preview[bot] *2020-01-13 15:59:58*
Bump ob-clients.version from 1.0.15 to 1.0.16 (#60)

Bumps `ob-clients.version` from 1.0.15 to 1.0.16.

Updates `forgerock-openbanking-jwkms-client` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Updates `forgerock-openbanking-analytics-client` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[c6e93d0fcbe0dbf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c6e93d0fcbe0dbf) dependabot-preview[bot] *2020-01-13 15:59:58*
Bump ob-clients.version from 1.0.15 to 1.0.16 (#60)

Bumps `ob-clients.version` from 1.0.15 to 1.0.16.

Updates `forgerock-openbanking-jwkms-client` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Updates `forgerock-openbanking-analytics-client` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.15 to 1.0.16
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.15...forgerock-openbanking-reference-implementation-clients-1.0.16)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.36
[fa4dc7ed7fc2cfd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fa4dc7ed7fc2cfd) Ben Jefferies *2019-12-24 15:52:02*
trigger release
## forgerock-openbanking-aspsp-1.0.35
[05239782c797821](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/05239782c797821) dependabot-preview[bot] *2019-12-24 15:19:33*
Bump forgerock-openbanking-auth from 1.0.47 to 1.0.48

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.47 to 1.0.48.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.47...forgerock-openbanking-starter-auth-1.0.48)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.34
[555ade2ddbc9400](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/555ade2ddbc9400) Ben Jefferies *2019-12-24 13:40:19*
Use ob-clients.version in version (#58)
## forgerock-openbanking-aspsp-1.0.33
[c1a7d128800a8ee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c1a7d128800a8ee) dependabot-preview[bot] *2019-12-24 12:04:27*
Bump ob-clients.version from 1.0.13 to 1.0.15

Bumps `ob-clients.version` from 1.0.13 to 1.0.15.

Updates `forgerock-openbanking-jwkms-client` from 1.0.13 to 1.0.15
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.13...forgerock-openbanking-reference-implementation-clients-1.0.15)

Updates `forgerock-openbanking-analytics-client` from 1.0.13 to 1.0.15
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.13...forgerock-openbanking-reference-implementation-clients-1.0.15)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[e09c33a96e83780](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e09c33a96e83780) dependabot-preview[bot] *2019-12-24 12:05:19*
Bump ob-common.version from 1.0.56 to 1.0.57

Bumps `ob-common.version` from 1.0.56 to 1.0.57.

Updates `forgerock-openbanking-model` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Updates `forgerock-openbanking-am` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Updates `forgerock-openbanking-jwt` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Updates `forgerock-openbanking-oidc` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Updates `forgerock-openbanking-upgrade` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Updates `forgerock-openbanking-ssl` from 1.0.56 to 1.0.57
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.56...forgerock-openbanking-starter-commons-1.0.57)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[100a15329a82737](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/100a15329a82737) dependabot-preview[bot] *2019-12-24 10:00:38*
Bump ob-common.version from 1.0.55 to 1.0.56

Bumps `ob-common.version` from 1.0.55 to 1.0.56.

Updates `forgerock-openbanking-model` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Updates `forgerock-openbanking-am` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Updates `forgerock-openbanking-jwt` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Updates `forgerock-openbanking-oidc` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Updates `forgerock-openbanking-upgrade` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Updates `forgerock-openbanking-ssl` from 1.0.55 to 1.0.56
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.55...forgerock-openbanking-starter-commons-1.0.56)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[8070cb0df82b0ff](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8070cb0df82b0ff) dependabot-preview[bot] *2019-12-24 12:22:33*
Bump forgerock-openbanking-jwkms-embedded from 1.1.44 to 1.1.45

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.44 to 1.1.45.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.44...forgerock-openbanking-reference-implementation-jwkms-1.1.45)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[95e266cd0ce9dbb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/95e266cd0ce9dbb) dependabot-preview[bot] *2019-12-24 10:00:07*
Bump forgerock-openbanking-jwkms-embedded from 1.1.43 to 1.1.44

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.43 to 1.1.44.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.43...forgerock-openbanking-reference-implementation-jwkms-1.1.44)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.32
[005a85c2d4ee0f5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/005a85c2d4ee0f5) dependabot-preview[bot] *2019-12-23 11:56:59*
Bump ob-clients.version from 1.0.12 to 1.0.13 (#48)

* Bump ob-clients.version from 1.0.12 to 1.0.13

Bumps `ob-clients.version` from 1.0.12 to 1.0.13.

Updates `forgerock-openbanking-jwkms-client` from 1.0.12 to 1.0.13
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.12...forgerock-openbanking-reference-implementation-clients-1.0.13)

Updates `forgerock-openbanking-analytics-client` from 1.0.12 to 1.0.13
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.12...forgerock-openbanking-reference-implementation-clients-1.0.13)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>

* Fix test

Co-authored-by: Ben Jefferies <benjefferies@echosoft.uk>
[005a85c2d4ee0f5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/005a85c2d4ee0f5) dependabot-preview[bot] *2019-12-23 11:56:59*
Bump ob-clients.version from 1.0.12 to 1.0.13 (#48)

* Bump ob-clients.version from 1.0.12 to 1.0.13

Bumps `ob-clients.version` from 1.0.12 to 1.0.13.

Updates `forgerock-openbanking-jwkms-client` from 1.0.12 to 1.0.13
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.12...forgerock-openbanking-reference-implementation-clients-1.0.13)

Updates `forgerock-openbanking-analytics-client` from 1.0.12 to 1.0.13
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.12...forgerock-openbanking-reference-implementation-clients-1.0.13)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>

* Fix test

Co-authored-by: Ben Jefferies <benjefferies@echosoft.uk>
## forgerock-openbanking-aspsp-1.0.31
[eee1dea4227c89f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/eee1dea4227c89f) dependabot-preview[bot] *2019-12-23 10:54:32*
Bump forgerock-openbanking-jwkms-embedded from 1.1.42 to 1.1.43

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.42 to 1.1.43.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.42...forgerock-openbanking-reference-implementation-jwkms-1.1.43)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[66f23a12bdad230](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/66f23a12bdad230) JamieB *2019-12-23 10:45:18*
Release madness!
## forgerock-openbanking-aspsp-1.0.30
[c66c1cfc8db90eb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c66c1cfc8db90eb) dependabot-preview[bot] *2019-12-23 10:00:40*
Bump ob-clients.version from 1.0.11 to 1.0.12

Bumps `ob-clients.version` from 1.0.11 to 1.0.12.

Updates `forgerock-openbanking-jwkms-client` from 1.0.11 to 1.0.12
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.11...forgerock-openbanking-reference-implementation-clients-1.0.12)

Updates `forgerock-openbanking-analytics-client` from 1.0.11 to 1.0.12
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.11...forgerock-openbanking-reference-implementation-clients-1.0.12)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[717d0079324ae47](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/717d0079324ae47) dependabot-preview[bot] *2019-12-23 10:19:03*
Bump forgerock-openbanking-jwkms-embedded from 1.1.41 to 1.1.42

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.41 to 1.1.42.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.41...forgerock-openbanking-reference-implementation-jwkms-1.1.42)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.29
[fc9651abfb0501e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fc9651abfb0501e) dependabot-preview[bot] *2019-12-20 10:00:09*
Bump ob-common.version from 1.0.54 to 1.0.55

Bumps `ob-common.version` from 1.0.54 to 1.0.55.

Updates `forgerock-openbanking-model` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Updates `forgerock-openbanking-am` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Updates `forgerock-openbanking-jwt` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Updates `forgerock-openbanking-oidc` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Updates `forgerock-openbanking-upgrade` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Updates `forgerock-openbanking-ssl` from 1.0.54 to 1.0.55
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.54...forgerock-openbanking-starter-commons-1.0.55)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[8e79f435ed8d34f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8e79f435ed8d34f) dependabot-preview[bot] *2019-12-20 10:00:32*
Bump forgerock-openbanking-jwkms-embedded from 1.1.40 to 1.1.41

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.40 to 1.1.41.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.40...forgerock-openbanking-reference-implementation-jwkms-1.1.41)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.28
[34634904cee558c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/34634904cee558c) Julien Renaux *2019-12-19 15:18:28*
fix actions job condition (#42)
## forgerock-openbanking-aspsp-1.0.27
[7eb7962abe0c438](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7eb7962abe0c438) dependabot-preview[bot] *2019-12-18 10:00:11*
Bump forgerock-openbanking-auth from 1.0.45 to 1.0.47

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.45 to 1.0.47.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.45...forgerock-openbanking-starter-auth-1.0.47)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[69a1a368588fd28](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/69a1a368588fd28) dependabot-preview[bot] *2019-12-19 10:16:42*
Bump ob-clients.version from 1.0.8 to 1.0.11

Bumps `ob-clients.version` from 1.0.8 to 1.0.11.

Updates `forgerock-openbanking-jwkms-client` from 1.0.8 to 1.0.11
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.8...forgerock-openbanking-reference-implementation-clients-1.0.11)

Updates `forgerock-openbanking-analytics-client` from 1.0.8 to 1.0.11
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.8...forgerock-openbanking-reference-implementation-clients-1.0.11)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[f51b32404052fa9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f51b32404052fa9) dependabot-preview[bot] *2019-12-18 10:00:37*
Bump ob-clients.version from 1.0.5 to 1.0.8

Bumps `ob-clients.version` from 1.0.5 to 1.0.8.

Updates `forgerock-openbanking-jwkms-client` from 1.0.5 to 1.0.8
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.5...forgerock-openbanking-reference-implementation-clients-1.0.8)

Updates `forgerock-openbanking-analytics-client` from 1.0.5 to 1.0.8
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.5...forgerock-openbanking-reference-implementation-clients-1.0.8)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[b4c976c7bdafd6e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b4c976c7bdafd6e) dependabot-preview[bot] *2019-12-18 10:20:25*
Bump ob-common.version from 1.0.52 to 1.0.54

Bumps `ob-common.version` from 1.0.52 to 1.0.54.

Updates `forgerock-openbanking-model` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Updates `forgerock-openbanking-am` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Updates `forgerock-openbanking-jwt` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Updates `forgerock-openbanking-oidc` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Updates `forgerock-openbanking-upgrade` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Updates `forgerock-openbanking-ssl` from 1.0.52 to 1.0.54
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.52...forgerock-openbanking-starter-commons-1.0.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[5f52fa85804af7a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5f52fa85804af7a) dependabot-preview[bot] *2019-12-19 10:00:40*
Bump forgerock-openbanking-jwkms-embedded from 1.1.39 to 1.1.40

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.39 to 1.1.40.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.39...forgerock-openbanking-reference-implementation-jwkms-1.1.40)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[013234e5be190b9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/013234e5be190b9) dependabot-preview[bot] *2019-12-18 10:20:06*
Bump forgerock-openbanking-jwkms-embedded from 1.1.38 to 1.1.39

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.38 to 1.1.39.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.38...forgerock-openbanking-reference-implementation-jwkms-1.1.39)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[513c9d67060ee2e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/513c9d67060ee2e) dependabot-preview[bot] *2019-12-18 10:01:01*
Bump forgerock-openbanking-starter-parent from 1.0.56 to 1.0.57

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.56 to 1.0.57.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.56...forgerock-openbanking-starter-parent-1.0.57)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[65e36799a9dacd2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/65e36799a9dacd2) Julien Renaux *2019-12-19 10:31:36*
migrating codefresh to actions

migrating codefresh to actions
[2e155d7025e96b8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2e155d7025e96b8) dependabot-preview[bot] *2019-12-19 10:00:19*
Bump spring-security-multi-auth-starter

Bumps spring-security-multi-auth-starter from 2.1.5.0.0.52 to 2.2.1.0.0.54.

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.26
[b6d60110c0804ea](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b6d60110c0804ea) Ben Jefferies *2019-12-17 12:30:15*
Flatten nested modules (#31)

To address the problem highlighted by bintray

```
A quick look in your package, to be more precise: /forgerock/openbanking/aspsp/forgerock-openbanking-aspsp, shows there is only .pom file.
As you may know, JCenter hosts java applications that follow maven convention.
In addition to the .pom file, your version should include a binary jar file, a sources jar, and optionally a javadoc jar.
Your files should be under a maven path layout.
(see https://www.jfrog.com/confluence/display/BT/Promoting+Your+Material#_including_your_package_in_jcenter)
```
## forgerock-openbanking-aspsp-1.0.25
[6de0ae47905c0da](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6de0ae47905c0da) dependabot-preview[bot] *2019-12-17 10:00:44*
Bump forgerock-openbanking-jwkms-embedded from 1.1.37 to 1.1.38

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.37 to 1.1.38.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.37...forgerock-openbanking-reference-implementation-jwkms-1.1.38)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.24
[a11c303b5072db4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a11c303b5072db4) Jorge Sanchez Perez *2019-12-16 15:05:50*
Update parent and multiauth login (#30)

* Update starter parent to the last version, add multiauth dependency and manual onboard refactored

* update pom and aligned by master branch
## forgerock-openbanking-aspsp-1.0.23
[3f89be12cfb2239](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3f89be12cfb2239) dependabot-preview[bot] *2019-12-16 07:21:57*
Bump ob-clients.version from 1.0.5 to 1.0.7

Bumps `ob-clients.version` from 1.0.5 to 1.0.7.

Updates `forgerock-openbanking-jwkms-client` from 1.0.5 to 1.0.7
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.5...forgerock-openbanking-reference-implementation-clients-1.0.7)

Updates `forgerock-openbanking-analytics-client` from 1.0.5 to 1.0.7
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.5...forgerock-openbanking-reference-implementation-clients-1.0.7)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.22
[42bc35358f70802](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/42bc35358f70802) dependabot-preview[bot] *2019-12-16 10:00:54*
Bump forgerock-openbanking-auth from 1.0.45 to 1.0.47

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.45 to 1.0.47.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.45...forgerock-openbanking-starter-auth-1.0.47)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.21
[ba1765d93f7c4a6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ba1765d93f7c4a6) Ben Jefferies *2019-12-13 13:01:34*
Update auth (#27)

* Update auth

Update auth to use the latest library

* Update auth and fix method change
## forgerock-openbanking-aspsp-1.0.20
[d92a4403f5c18f0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d92a4403f5c18f0) dependabot-preview[bot] *2019-12-13 10:00:18*
Bump ob-clients.version from 1.0.2 to 1.0.5

Bumps `ob-clients.version` from 1.0.2 to 1.0.5.

Updates `forgerock-openbanking-jwkms-client` from 1.0.2 to 1.0.5
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.2...forgerock-openbanking-reference-implementation-clients-1.0.5)

Updates `forgerock-openbanking-analytics-client` from 1.0.2 to 1.0.5
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.2...forgerock-openbanking-reference-implementation-clients-1.0.5)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[80c9be71ca9cadf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/80c9be71ca9cadf) dependabot-preview[bot] *2019-12-13 10:00:50*
Bump ob-common.version from 1.0.50 to 1.0.52

Bumps `ob-common.version` from 1.0.50 to 1.0.52.

Updates `forgerock-openbanking-model` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Updates `forgerock-openbanking-am` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Updates `forgerock-openbanking-jwt` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Updates `forgerock-openbanking-oidc` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Updates `forgerock-openbanking-upgrade` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Updates `forgerock-openbanking-ssl` from 1.0.50 to 1.0.52
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.50...forgerock-openbanking-starter-commons-1.0.52)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[1ee274663dfc0cf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1ee274663dfc0cf) dependabot-preview[bot] *2019-12-13 10:24:17*
Bump forgerock-openbanking-jwkms-embedded from 1.1.34 to 1.1.37

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.34 to 1.1.37.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.34...forgerock-openbanking-reference-implementation-jwkms-1.1.37)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.19
[6063038fcaf4720](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6063038fcaf4720) Ben Jefferies *2019-12-12 13:57:45*
Remove plugin (#23)
## forgerock-openbanking-aspsp-1.0.18
[55832a3aa87aad3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/55832a3aa87aad3) Ben Jefferies *2019-12-12 12:52:51*
Dependabot config (#21)

* Add dependabot configuration

Add dependabot configuration to automerge. Dependabot will automerge PRs
that have successful checks.

https://dependabot.com/docs/config-file/\#automerged_updates
[3a06b07f9868e5b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3a06b07f9868e5b) dependabot-preview[bot] *2019-12-12 12:53:39*
Bump ob-common.version from 1.0.49 to 1.0.50

Bumps `ob-common.version` from 1.0.49 to 1.0.50.

Updates `forgerock-openbanking-model` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Updates `forgerock-openbanking-am` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Updates `forgerock-openbanking-jwt` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Updates `forgerock-openbanking-oidc` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Updates `forgerock-openbanking-upgrade` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Updates `forgerock-openbanking-ssl` from 1.0.49 to 1.0.50
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.49...forgerock-openbanking-starter-commons-1.0.50)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[8343cf61d958066](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8343cf61d958066) dependabot-preview[bot] *2019-12-12 12:53:31*
Bump forgerock-openbanking-jwkms-embedded from 1.1.31 to 1.1.34

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.31 to 1.1.34.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.31...forgerock-openbanking-reference-implementation-jwkms-1.1.34)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[fe50a3dac46604c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fe50a3dac46604c) dependabot-preview[bot] *2019-12-12 12:53:51*
Bump asciidoctor-maven-plugin from 1.5.6 to 1.6.0

Bumps [asciidoctor-maven-plugin](https://github.com/asciidoctor/asciidoctor-maven-plugin) from 1.5.6 to 1.6.0.
- [Release notes](https://github.com/asciidoctor/asciidoctor-maven-plugin/releases)
- [Commits](https://github.com/asciidoctor/asciidoctor-maven-plugin/compare/asciidoctor-maven-plugin-1.5.6...asciidoctor-maven-plugin-1.6.0)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.17
[84b08fc1f999346](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/84b08fc1f999346) Ben Jefferies *2019-12-12 09:39:13*
Bump forgerock-openbanking-starter-parent from 1.0.54 to 1.0.56 (#18)

Bump forgerock-openbanking-starter-parent from 1.0.54 to 1.0.56
[e5a9cb7b628425d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e5a9cb7b628425d) dependabot-preview[bot] *2019-12-12 07:13:20*
Bump forgerock-openbanking-starter-parent from 1.0.54 to 1.0.56

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.54 to 1.0.56.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.54...forgerock-openbanking-starter-parent-1.0.56)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.16
[759be4a2e42fae4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/759be4a2e42fae4) Ben Jefferies *2019-12-11 16:26:55*
Bump ob-common.version from 1.0.45 to 1.0.49 (#11)

Bump ob-common.version from 1.0.45 to 1.0.49
[61affbc5bdbd6af](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/61affbc5bdbd6af) Ben Jefferies *2019-12-11 16:31:47*
Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54 (#15)

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.52 to 1.0.54.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.52...forgerock-openbanking-starter-parent-1.0.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[b6f60b06659338a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b6f60b06659338a) Ben Jefferies *2019-12-11 16:26:41*
Re-enable IT (#5)

Re-enable IT
[94b72409e07baa0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/94b72409e07baa0) dependabot-preview[bot] *2019-12-11 15:56:30*
Bump ob-clients.version from 1.0.1 to 1.0.2

Bumps `ob-clients.version` from 1.0.1 to 1.0.2.

Updates `forgerock-openbanking-jwkms-client` from 1.0.1 to 1.0.2
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.1...forgerock-openbanking-reference-implementation-clients-1.0.2)

Updates `forgerock-openbanking-analytics-client` from 1.0.1 to 1.0.2
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.1...forgerock-openbanking-reference-implementation-clients-1.0.2)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[61affbc5bdbd6af](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/61affbc5bdbd6af) Ben Jefferies *2019-12-11 16:31:47*
Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54 (#15)

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.52 to 1.0.54.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.52...forgerock-openbanking-starter-parent-1.0.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[b5f19593be0225e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b5f19593be0225e) dependabot-preview[bot] *2019-12-11 15:56:46*
Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.52 to 1.0.54.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.52...forgerock-openbanking-starter-parent-1.0.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[8e72b124c4363c6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8e72b124c4363c6) dependabot-preview[bot] *2019-12-11 15:54:05*
Bump ob-common.version from 1.0.45 to 1.0.49

Bumps `ob-common.version` from 1.0.45 to 1.0.49.

Updates `forgerock-openbanking-model` from 1.0.45 to 1.0.49

Updates `forgerock-openbanking-am` from 1.0.45 to 1.0.49

Updates `forgerock-openbanking-jwt` from 1.0.45 to 1.0.49

Updates `forgerock-openbanking-oidc` from 1.0.45 to 1.0.49

Updates `forgerock-openbanking-upgrade` from 1.0.45 to 1.0.49

Updates `forgerock-openbanking-ssl` from 1.0.45 to 1.0.49

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[680d79c66dc26b9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/680d79c66dc26b9) Ben Jefferies *2019-12-11 09:54:54*
Re-enable IT
## forgerock-openbanking-aspsp-1.0.15
[179f64d2d2d396d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/179f64d2d2d396d) Quentin Castel *2019-12-11 15:03:02*
trigger release
## forgerock-openbanking-aspsp-1.0.14
[882d2ee923d7b11](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/882d2ee923d7b11) Quentin Castel *2019-12-11 13:53:52*
Move to jwkms 1.1.31
[8ccbbb9f14a5849](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8ccbbb9f14a5849) Julien Renaux *2019-12-11 09:30:11*
fix dockerfile
[3179e5a4d8d1804](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3179e5a4d8d1804) Julien Renaux *2019-12-11 09:30:11*
missing build version
[bb7b55f665cce38](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bb7b55f665cce38) Julien Renaux *2019-12-11 09:30:10*
migrate Bank and Register apps
## forgerock-openbanking-aspsp-1.0.13
[631c8ec96d8b8b7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/631c8ec96d8b8b7) Quentin Castel *2019-12-11 11:25:22*
Update README.md
## forgerock-openbanking-aspsp-1.0.12
[e8d8f351f6b5812](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e8d8f351f6b5812) Quentin Castel *2019-12-11 12:05:02*
Add back javadoc
## forgerock-openbanking-aspsp-1.0.11
[6c382a717f39cad](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6c382a717f39cad) Quentin Castel *2019-12-11 11:20:28*
Use the same groupeID
## forgerock-openbanking-uk-aspsp-1.0.10
[7014a50e851e9e0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7014a50e851e9e0) Quentin Castel *2019-12-11 09:18:25*
Add Distribution management
## forgerock-openbanking-uk-aspsp-1.0.9
[eb509c0ccad740b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/eb509c0ccad740b) Quentin Castel *2019-12-11 08:22:35*
trigger release
## forgerock-openbanking-uk-aspsp-1.0.8
[a45cbee574e922b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a45cbee574e922b) Ben Jefferies *2019-12-03 15:27:02*
Change MTLS response model to simple list (#11)

The authorities do not need to be anything more complex then the string
list of authorities.
[31579adca5407bd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/31579adca5407bd) Ben Jefferies *2019-12-04 11:32:11*
Use latest ob-common without hateoas (#12)

* Bump common
[4cae8bd61e80474](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4cae8bd61e80474) Ben Jefferies *2019-12-04 11:31:00*
Github actions does not support DIND (#13)

* Github actions does not support DIND 

Whilst Actions does not support DIND the best way to run the integration
tests is by running it the same way we do locally.

There is another solution we could explore which is github action
services
help.github.com/en/actions/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#jobsjob_idservices
although this wouldn't immediately solve our problem and we'd need to
change the way our docker composes worked so they are consistent which
has it's downsides.
[c29dc1b08d70999](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c29dc1b08d70999) Ben Jefferies *2019-12-04 15:40:24*
Fix tests by mocking jwkms calls (#14)
[a07b270def1b397](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a07b270def1b397) Ben Jefferies *2019-12-05 10:09:50*
Explicitly toggle include admins (#15)

Explicitly toggle include admins to ensure branch protection is disabled before release
[84e4735278bc728](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/84e4735278bc728) Ben Jefferies *2019-12-06 09:00:47*
Use latest spring multi auth for 2.1.5 (#16)

* Use latest spring multi auth for 2.1.5

We don't want to upgrade to 2.2.1 yet as there is breaking changes so
used 2.1.5 multi auth

* Bump parent to revert spring version

* Bump parent to revert spring cloud version

* Fix test
[b42462a1db11fe2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b42462a1db11fe2) Ben Jefferies *2019-10-25 13:51:58*
Fix IT for rs gateway (#2)

* Fix IT for rs gateway

Tests now run successfully when running `mvn clean install` and in codefresh
[2c864c769d4e0be](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2c864c769d4e0be) Ben Jefferies *2019-10-25 16:13:09*
Enable all integration tests and build samples (#3)

* Enable all IT and build sample docker images

RS mock store and RS RCS tests running locally and in codefresh.
Build docker images in codefresh for sample projects.

* Generate files
[7360efe195f20ab](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7360efe195f20ab) Ben Jefferies *2019-11-13 16:15:13*
Temp disable/enable include admins in branch protection (#5)

See https://github.com/benjefferies/branch-protection-bot for backstory.
[e17ea687ee50d5e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e17ea687ee50d5e) Ben Jefferies *2019-11-19 09:40:53*
Add github actions for ob-aspsp (#6)

* Add github actions for ob-aspsp

* Remove the codefresh.yml
[6b5cf150f105664](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6b5cf150f105664) Quentin Castel *2019-12-10 19:41:25*
Move the repo to the new org
[d504b1d42cbecde](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d504b1d42cbecde) Quentin Castel *2019-12-06 15:42:17*
Change to org OpenBankingToolkit
[0c3cd87c0b07daf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0c3cd87c0b07daf) Ben Jefferies *2019-12-04 14:10:19*
Update maven.yml
[6e7c667f4db469d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6e7c667f4db469d) Quentin Castel *2019-11-20 16:54:33*
Disable IT tests for now
[ef453527e4b803d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef453527e4b803d) Quentin Castel *2019-11-20 16:14:56*
Use maven github actions
[5c2cd92427d8c14](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5c2cd92427d8c14) Quentin Castel *2019-11-01 13:29:38*
CDR version of the ASPSP
[a32063d7a836642](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a32063d7a836642) Quentin Castel *2019-10-16 14:19:42*
Wrong repo
[471002253b05d83](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/471002253b05d83) Quentin Castel *2019-10-16 14:10:54*
add gitignore
[298e45e06f5f157](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/298e45e06f5f157) Quentin Castel *2019-10-16 13:53:12*
Remove Dockerfiles and docker-compose files
[351f2cabfeaceb2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/351f2cabfeaceb2) Quentin Castel *2019-10-16 13:46:16*
Fix JAXB missing dependencies
[301dfc83b9b9351](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/301dfc83b9b9351) Quentin Castel *2019-10-16 11:36:45*
Remove IT tests and docker build for now, as they will run in ob-ref
[4f0cacaae76c9f8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4f0cacaae76c9f8) Ben Jefferies *2019-10-16 11:08:36*
Add .mvn for multi module issue
[be933aea1a8d7db](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/be933aea1a8d7db) Quentin Castel *2019-10-16 10:55:15*
Add back the other CF stages
[773cd514654d1ed](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/773cd514654d1ed) Quentin Castel *2019-10-16 10:48:41*
Debug iteration
[04a833bc480a850](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/04a833bc480a850) Quentin Castel *2019-10-16 10:46:24*
Debug iteration
[fcabcdf05275e78](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fcabcdf05275e78) Quentin Castel *2019-10-16 10:42:41*
Remove other steps than IT to debug
[071f26711cbea5a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/071f26711cbea5a) Quentin Castel *2019-10-16 10:41:21*
Remove other steps than IT to debug
[7b4b1feb095fdef](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7b4b1feb095fdef) Quentin Castel *2019-10-16 10:39:47*
Remove other steps than IT to debug
[c62ae5480b79dee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c62ae5480b79dee) Quentin Castel *2019-10-16 10:38:24*
Remove other steps than IT to debug
[8ec06f71aa204ee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8ec06f71aa204ee) Quentin Castel *2019-10-16 10:36:36*
Add debug for CF
[c0d6cdf7c24cf96](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c0d6cdf7c24cf96) Quentin Castel *2019-10-16 10:29:08*
Rename RS-API to RS-Gateway
[2fcd375320a6a94](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2fcd375320a6a94) Quentin Castel *2019-10-16 10:25:26*
Add IT tests to CF
[f8803d2f0d15b7f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f8803d2f0d15b7f) Quentin Castel *2019-10-16 10:09:08*
Wrong stage for docker build
[d7bbc051b2eb4be](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d7bbc051b2eb4be) Quentin Castel *2019-10-16 10:06:11*
Add docker build to CF to test
[99fe1673ee34587](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/99fe1673ee34587) Ben Jefferies *2019-10-16 08:47:06*
Add working directory explicitly
[9942f559638698a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9942f559638698a) Quentin Castel *2019-10-16 08:05:38*
Small fix on the ASPSP to pass the e2e tests
[d373ffd02253a78](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d373ffd02253a78) Quentin Castel *2019-10-15 08:56:18*
Fix AS discovery
[bd68b151644ffac](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bd68b151644ffac) Quentin Castel *2019-10-14 16:22:11*
Fix #https://github.com/ForgeCloud/ob-reference-implementation/issues/1772 Refresh token flow now working as before
[70f1c1363aa157c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/70f1c1363aa157c) Quentin Castel *2019-10-14 14:27:50*
Cleanup pom and bump to latest ob-*
[9771b1d4d5380b5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9771b1d4d5380b5) Quentin Castel *2019-10-14 11:13:02*
Bump to ob-parent 1.0.31
[de6a3b6bfa7bbf2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/de6a3b6bfa7bbf2) Quentin Castel *2019-10-08 11:07:04*
Fix as-gateway
[7be37c7f4d29d7f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7be37c7f4d29d7f) Quentin Castel *2019-10-07 09:31:54*
Make it compile
[f91132add0b28e6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f91132add0b28e6) Quentin Castel *2019-10-04 16:24:20*
Refactor each module into a server with a sample
[8ac4730015e48fd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8ac4730015e48fd) Quentin Castel *2019-10-04 14:10:51*
Fixing the IT tests but still with config server
[81356fa9be60afd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/81356fa9be60afd) Quentin Castel *2019-10-04 10:04:35*
Import the missing classes from ob-ref->commons and make things compiling
[b4b25b85f4196af](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b4b25b85f4196af) Quentin Castel *2019-10-04 08:17:38*
Copy over the ASPSP microservices and setup the maven repo
