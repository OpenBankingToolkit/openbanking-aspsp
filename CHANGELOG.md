# Git Changelog Maven plugin changelog
Changelog of Git Changelog Maven plugin.
## Unreleased
### GitHub [#208](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/208) bumped the parent version 1.0.73
[094f4025242a00e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/094f4025242a00e) Jorge Sanchez Perez *2020-06-23 17:38:32*
bumped the parent version 1.0.73 (#208)

Force the merge like as administrator to unlock work.
### GitHub [#209](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/209) Release/1.0.86
[d2be7b8b9c1b1eb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d2be7b8b9c1b1eb) Jorge Sanchez Perez *2020-06-23 18:13:50*
Release/1.0.86 (#209)

* Release candidate: prepare release 1.0.86

* Release candidate: prepare for next development iteration

* changelog updated
[0ee32fe1b6392e3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0ee32fe1b6392e3) Matt Wills *2020-06-18 15:32:18*
Bumped version of parent pom to pull in latest converters from uk-datamodel (#216)
[499a9be21344a26](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/499a9be21344a26) Matt Wills *2020-06-18 15:25:44*
Release candidate: prepare for next development iteration
## 1.0.85
### GitHub [#203](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/203) Handler events (Data APIs /api/data/events)
[954bec38b7eb2e3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/954bec38b7eb2e3) Jorge Sanchez Perez *2020-06-16 13:14:11*
Handler events (Data APIs /api/data/events) (#203)

* import events feature for aggregated polling

* Integration test for rs-store data events controller

* Integration test for rs-store data events controller, delete de events created on test

* Integration test for rs-store data events controller, code format

* integration test

* change the url events context hardcoded to a constant
### GitHub [#204](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/204) Make OBRisk1.PaymentCodeContext field required v3.1.3
[d73d2db5d95768b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d73d2db5d95768b) Jamie Bowen *2020-06-16 13:04:03*
Make OBRisk1.PaymentCodeContext field required v3.1.3 (#204)

* Release candidate: prepare release 1.0.83

* Release candidate: prepare for next development iteration

* Make OBRisk1.PaymentCodeContext field configurably a required field -
v3.1.3

This work was done for all version, but version 3.1.3 has been created
recently and these risk validators also need to be applied in the 3.1.3
payment controllers.

Part fix for https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/196

Some implementors have expressed a need to be able to enfore that a
PaymentCodeContext is provided in the Risk object when requesting a
consent. This PR enables this feature to be turned on by setting the
following spring config setting to true;
`rs.api.payment.validate.risk.require-payment-context-code`
### GitHub [#205](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/205) Release/1.0.84
[49eda970989f335](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/49eda970989f335) Jorge Sanchez Perez *2020-06-16 15:10:42*
Release/1.0.84 (#205)

* javadoc for OBEventNotification2 object

* Release candidate: prepare release 1.0.84

* Release candidate: prepare for next development iteration

* changelog updated
[22e91f504289040](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/22e91f504289040) Matt Wills *2020-06-18 14:27:05*
Bumped version of parent pom to pull in latest converters from uk-datamodel (#216)
[2a5d119633adb1a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2a5d119633adb1a) Matt Wills *2020-06-18 13:56:30*
Added required version for versions prior to 3.1.3 in the rs-store (#216)
[30e9a9a542dd0d5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/30e9a9a542dd0d5) Matt Wills *2020-06-18 15:25:33*
Release candidate: prepare release 1.0.85
[861981393939c0a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/861981393939c0a) JamieB *2020-06-15 11:27:45*
Release candidate: prepare for next development iteration
## 1.0.83
[553440e6b90bbf9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/553440e6b90bbf9) JamieB *2020-06-15 11:27:36*
Release candidate: prepare release 1.0.83
[91f910625f5fcb1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/91f910625f5fcb1) JamieB *2020-06-15 10:23:12*
Make OBRisk1.PaymentCodeContext field configurably a required field

Part fix for https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/196

Some implementors have expressed a need to be able to enfore that a
PaymentCodeContext is provided in the Risk object when requesting a
consent. This PR enables this feature to be turned on by setting the
following spring config setting to true;
`rs.api.payment.validate.risk.require-payment-context-code`
[50f645798ab6ff6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/50f645798ab6ff6) Matt Wills *2020-06-15 09:11:57*
Release candidate: prepare for next development iteration
## 1.0.82
### GitHub [#175](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/175) Use new merge-master flow
[d0113073476393f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d0113073476393f) Jonathan Gazeley *2020-04-21 13:51:06*
Use new merge-master flow (#175)

Use new merge-master flow
### GitHub [#177](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/177) upgrade CLI
[de8bdc233ca80db](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/de8bdc233ca80db) Julien Renaux *2020-04-23 15:01:15*
upgrade CLI (#177)

* upgrade CLI

* push latest image

* adding docker-compose.override.yml into gitignore
### GitHub [#181](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/181) Release 1.0.79
[9f6afb8063341ee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9f6afb8063341ee) Jorge Sanchez Perez *2020-04-24 17:06:53*
Release 1.0.79 (#181)

* [maven-release-plugin] prepare release forgerock-openbanking-aspsp-1.0.79

* [maven-release-plugin] prepare for next development iteration
### GitHub [#182](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/182) upgrade openbanking-ui-cli to handle building forge rock new theme as…
[8aea7d836351a41](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8aea7d836351a41) Julien Renaux *2020-04-28 08:29:42*
upgrade openbanking-ui-cli to handle building forge rock new theme as the other themes (#182)
### GitHub [#183](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/183) use common utils/forms. 
[7477ad1fe49d907](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7477ad1fe49d907) Julien Renaux *2020-04-29 11:22:44*
use common utils/forms. https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/8 (#183)
### GitHub [#184](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/184) upgrade @forgerock/openbanking-ui-cli. https://github.com/OpenBanking…
[f351e3229a4ea1e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f351e3229a4ea1e) Julien Renaux *2020-04-29 16:38:42*
upgrade @forgerock/openbanking-ui-cli. https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/8 (#184)
[ea2a27cb4d43ff9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ea2a27cb4d43ff9) Matt Wills *2020-06-12 15:52:31*
Removed tests which have been moved to the uk-datamodel repo (#216)
[4f937d0ec38fdbb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4f937d0ec38fdbb) Matt Wills *2020-06-12 15:41:03*
Bumped version of parent pom to pull in correct version of uk-datamodel (#216)
[5fcd86d3b12268b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5fcd86d3b12268b) Matt Wills *2020-06-12 14:55:21*
Moved account related converters to package of that name (#216)
[509ce5aa585b8f6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/509ce5aa585b8f6) Matt Wills *2020-06-12 12:07:17*
Deleted converters - using ones in uk-datamodel instead (#216)
[45d99de2f681fa0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/45d99de2f681fa0) Matt Wills *2020-06-10 15:34:27*
Fixes for international standing orders and file payments (#216)
[66cb9d2142fe782](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/66cb9d2142fe782) Matt Wills *2020-06-10 11:35:49*
Removed InternationalStandingOrderConsent3Repository (using InternationalStandingOrderConsent4Repository and FRInternationalStandingOrderConsent4 instead). Added required converter methods (#216)
[84cb495d7b0faf5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/84cb495d7b0faf5) Matt Wills *2020-06-09 15:14:52*
Removed InternationalScheduledConsent2Repository (using InternationalScheduledConsent4Repository and FRInternationalScheduledConsent4 instead). Added required converter methods (#216)
[b40ebc821710d67](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b40ebc821710d67) Matt Wills *2020-06-09 09:35:01*
Removed InternationalConsent2Repository (using InternationalConsent4Repository instead). Added required converter methods (#216)
[6b32c6da81b0a96](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6b32c6da81b0a96) Matt Wills *2020-06-08 08:04:22*
Added null checks to converters (#216)
[7f6ed1e97fe8ac9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7f6ed1e97fe8ac9) Matt Wills *2020-06-05 15:30:45*
Fixes to enable scheduled payments to work (#216)
[9a86b147dbe5451](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9a86b147dbe5451) Matt Wills *2020-05-28 14:42:51*
Changes for v3.1.3 of the Payment Initiation API.
Many of the changes in the rsstore controllers need implementing - search for "TODO #216" statements (#232)
[fd08b064e6746e4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fd08b064e6746e4) Matt Wills *2020-05-05 12:50:27*
Waiver 007 expiry - enabled detached JWT signature verification (#219)
[30af22b186cb15b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/30af22b186cb15b) Matt Wills *2020-06-05 10:52:29*
Converting back to OBDomesticStandingOrder2 for verification method (#232)
[2035d40b22e24a2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2035d40b22e24a2) Matt Wills *2020-06-04 15:04:56*
Removed 'consumes' attribute from @RequestMapping annotations on get methods (#232)
[99e53cb2356cf24](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/99e53cb2356cf24) Matt Wills *2020-06-04 14:06:34*
Fixed 2 issues in converters. Added unit tests (#232)
[bfc8f6951aaca33](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bfc8f6951aaca33) Matt Wills *2020-06-03 15:35:11*
Added ConsentStatusCodeToResponseDataStatusConverter to convert ConsentStatusCode to new StatusEnum (#232)
[610181a80ea2959](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/610181a80ea2959) Matt Wills *2020-06-03 15:14:43*
Fixed TODO statement concerning scaSupportData in OBWriteDomesticScheduledConsent3Data. Fixed self links in Controllers (#232)
[4835ed7175f166c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4835ed7175f166c) Matt Wills *2020-06-03 12:11:43*
Removed unnecessary international payment converters. Storing native OB objects in Mongo via new repositories and FR Data objects (#232)
[a7cf0ba72bc44d2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a7cf0ba72bc44d2) Matt Wills *2020-06-02 15:49:44*
Reduce duplication within some of the repetitive converters (#232)
[736127098468f9e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/736127098468f9e) Matt Wills *2020-06-02 10:28:55*
Added converters and implemented controllers for scheduled international payments and international standing orders (#232)
Need to address TODO comments, particularly the population of the destinationCountry and extendedPurpose
Need to refactor converters to reduce duplication (and potentially re-use pre-existing converters - i.e. converters added for previous versions)
[53e3d38938fdf8e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/53e3d38938fdf8e) Matt Wills *2020-06-01 14:41:01*
Added converters and implemented controllers for immediate international payments (#232)
[72a599e90019d32](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/72a599e90019d32) Matt Wills *2020-06-01 11:22:51*
Added converters and implemented controllers for file payments (#232)
[bf2e2272c3c0500](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bf2e2272c3c0500) Matt Wills *2020-06-01 09:53:05*
Added converters and implemented controllers for rs-store domestic standing orders (#232)
[be4d9d736391375](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/be4d9d736391375) Matt Wills *2020-05-29 15:20:07*
Added converters and implemented controllers for rs-store domestic payments and scheduled domestic payments (#232)
[3a73cabc7cabe48](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3a73cabc7cabe48) Matt Wills *2020-05-29 08:40:03*
Converted model classes to previous versions in order to perform verification (#232)
[b0375d4b8ea8261](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b0375d4b8ea8261) Matt Wills *2020-05-28 14:45:27*
Removed unsupported endpoints from application.yml (#232)
[9a86b147dbe5451](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9a86b147dbe5451) Matt Wills *2020-05-28 14:42:51*
Changes for v3.1.3 of the Payment Initiation API.
Many of the changes in the rsstore controllers need implementing - search for "TODO #216" statements (#232)
[9f6afb8063341ee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9f6afb8063341ee) Jorge Sanchez Perez *2020-04-24 17:06:53*
Release 1.0.79 (#181)

* [maven-release-plugin] prepare release forgerock-openbanking-aspsp-1.0.79

* [maven-release-plugin] prepare for next development iteration
[d183f414fc0641e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d183f414fc0641e) dependabot-preview[bot] *2020-05-20 08:04:22*
Bump forgerock-openbanking-auth from 1.0.55 to 1.0.57

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.55 to 1.0.57.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Changelog](https://github.com/OpenBankingToolkit/openbanking-auth/blob/master/pom.xml.releaseBackup)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.55...forgerock-openbanking-starter-auth-1.0.57)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[2e773335c865e63](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2e773335c865e63) dependabot-preview[bot] *2020-04-29 11:24:07*
Bump ob-clients.version from 1.0.31 to 1.0.32

Bumps `ob-clients.version` from 1.0.31 to 1.0.32.

Updates `forgerock-openbanking-jwkms-client` from 1.0.31 to 1.0.32
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.31...forgerock-openbanking-reference-implementation-clients-1.0.32)

Updates `forgerock-openbanking-analytics-client` from 1.0.31 to 1.0.32
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.31...forgerock-openbanking-reference-implementation-clients-1.0.32)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.31 to 1.0.32
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.31...forgerock-openbanking-reference-implementation-clients-1.0.32)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[83088724d129b8a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/83088724d129b8a) dependabot-preview[bot] *2020-05-15 13:39:56*
Bump ob-common.version from 1.0.74 to 1.0.75

Bumps `ob-common.version` from 1.0.74 to 1.0.75.

Updates `forgerock-openbanking-model` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Updates `forgerock-openbanking-am` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Updates `forgerock-openbanking-jwt` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Updates `forgerock-openbanking-oidc` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Updates `forgerock-openbanking-upgrade` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Updates `forgerock-openbanking-ssl` from 1.0.74 to 1.0.75
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.74...forgerock-openbanking-starter-commons-1.0.75)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[f3b878ca0944044](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f3b878ca0944044) JamieB *2020-05-15 09:52:09*
Updated ui project version to updating-ui-version-to-3.1.2-queen-rc7
[162d7fe6da5c45e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/162d7fe6da5c45e) Matt Wills *2020-06-15 09:11:46*
Release candidate: prepare release 1.0.82
[475850b80e3e28f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/475850b80e3e28f) jorgesanchezperez *2020-05-13 10:53:11*
Fix CLIENT_CREDENTIALS Grant type thru all payments APIs and versions APIS
[849dc8d817838b8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/849dc8d817838b8) jorgesanchezperez *2020-04-24 15:42:03*
skip test on github actions and rolback version to 1.0.79-SNAPSHOT to create again the release
[c5ae6dc9c2e6c6c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c5ae6dc9c2e6c6c) JamieB *2020-04-24 10:47:30*
Use latest version of ob-commons
[bd2d31c60998e30](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bd2d31c60998e30) Julien Renaux *2020-04-24 09:44:19*
remove unused file
[ea0beafab3e80a3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ea0beafab3e80a3) Julien Renaux *2020-04-24 08:45:41*
upgrade common ui. https://github.com/OpenBankingToolkit/openbanking-common/issues/59
[e567d3cd251e50f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e567d3cd251e50f) JamieB *2020-04-22 15:53:06*
Updated ui project version to updating-ui-version-to-
## forgerock-openbanking-aspsp-1.0.78
### GitHub [#167](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/167) fix missing translation
[5938b495358e044](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5938b495358e044) Julien Renaux *2020-03-25 13:08:00*
fix missing translation (#167)
[8a355ad05606cad](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8a355ad05606cad) dependabot-preview[bot] *2020-03-26 10:00:41*
Bump ob-common.version from 1.0.70 to 1.0.73

Bumps `ob-common.version` from 1.0.70 to 1.0.73.

Updates `forgerock-openbanking-model` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Updates `forgerock-openbanking-am` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Updates `forgerock-openbanking-jwt` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Updates `forgerock-openbanking-oidc` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Updates `forgerock-openbanking-upgrade` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Updates `forgerock-openbanking-ssl` from 1.0.70 to 1.0.73
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.70...forgerock-openbanking-starter-commons-1.0.73)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[713d263525955d6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/713d263525955d6) dependabot-preview[bot] *2020-04-02 10:00:32*
Bump forgerock-openbanking-jwkms-embedded from 1.1.66 to 1.1.67

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.66 to 1.1.67.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.66...forgerock-openbanking-reference-implementation-jwkms-1.1.67)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[f99fcab495bc69c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f99fcab495bc69c) dependabot-preview[bot] *2020-03-26 10:00:36*
Bump forgerock-openbanking-jwkms-embedded from 1.1.65 to 1.1.66

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.65 to 1.1.66.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.65...forgerock-openbanking-reference-implementation-jwkms-1.1.66)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[1034914d5badece](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1034914d5badece) dependabot-preview[bot] *2020-04-02 10:00:15*
Bump forgerock-openbanking-starter-parent from 1.0.66 to 1.0.67

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.66 to 1.0.67.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.66...forgerock-openbanking-starter-parent-1.0.67)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[59120058fde5dc9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/59120058fde5dc9) Jonathan Gazeley *2020-04-21 08:06:47*
Use base64 version of secret
[b78181e5c9cfa0e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b78181e5c9cfa0e) Jonathan Gazeley *2020-04-14 15:26:20*
Quote json
[de5d1a645ca9dfb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/de5d1a645ca9dfb) Jonathan Gazeley *2020-04-14 15:21:44*
Login securely
[b51a475413161b3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b51a475413161b3) Jonathan Gazeley *2020-04-14 15:12:50*
Use correct namespace
[5e828b8a595dec4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5e828b8a595dec4) Jonathan Gazeley *2020-04-14 13:45:13*
Push Docker images to GCR instead of Codefresh
[99a2afa078bc041](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/99a2afa078bc041) Jonathan Gazeley *2020-04-08 14:36:57*
Use https
[d327f23f8f37b7d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d327f23f8f37b7d) Jonathan Gazeley *2020-04-08 13:48:51*
Add UK mirror for central repository
[7ebcacb160ec407](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7ebcacb160ec407) Jonathan Gazeley *2020-04-02 11:38:43*
Push artifacts to artifactory
[14f4056ec8a4ba5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/14f4056ec8a4ba5) Jonathan Gazeley *2020-04-01 15:14:32*
Correct variable names
[bdeffa8760a2f7e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bdeffa8760a2f7e) Jonathan Gazeley *2020-04-01 12:39:56*
Additionally pull deps from artifactory
[04f6fb4f41c322c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/04f6fb4f41c322c) Jonathan Gazeley *2020-03-19 12:08:52*
Release maven output to artifactory
## forgerock-openbanking-aspsp-1.0.77
### GitHub [#166](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/166) making sure docker build fails on node error
[d0b5318d315a9dc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d0b5318d315a9dc) Julien Renaux *2020-03-25 12:27:36*
making sure docker build fails on node error (#166)

* making sure docker build fails on node error

* bump project
## forgerock-openbanking-aspsp-1.0.76
[15343784400242d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/15343784400242d) JamieB *2020-03-17 12:55:15*
Change UI project version to 3.1.2-smiths-SNAPSHOT

Part fix for https://github.com/OpenBankingToolkit/openbanking-reference-implementation/issues/148
## forgerock-openbanking-aspsp-1.0.75
### GitHub [#155](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/155) adding registration feature flag
[3fee2024d7ffa40](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3fee2024d7ffa40) Julien Renaux *2020-03-13 10:05:02*
adding registration feature flag (#155)
[732dc77499ddbbc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/732dc77499ddbbc) dependabot-preview[bot] *2020-03-13 10:10:24*
Bump ob-clients.version from 1.0.30 to 1.0.31

Bumps `ob-clients.version` from 1.0.30 to 1.0.31.

Updates `forgerock-openbanking-jwkms-client` from 1.0.30 to 1.0.31
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.30...forgerock-openbanking-reference-implementation-clients-1.0.31)

Updates `forgerock-openbanking-analytics-client` from 1.0.30 to 1.0.31
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.30...forgerock-openbanking-reference-implementation-clients-1.0.31)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.30 to 1.0.31
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.30...forgerock-openbanking-reference-implementation-clients-1.0.31)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[51ff14887b0b1e6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/51ff14887b0b1e6) dependabot-preview[bot] *2020-03-13 09:55:59*
Bump forgerock-openbanking-jwkms-embedded from 1.1.64 to 1.1.65

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.64 to 1.1.65.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.64...forgerock-openbanking-reference-implementation-jwkms-1.1.65)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.74
[a4bc71eb174a23f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a4bc71eb174a23f) dependabot-preview[bot] *2020-03-12 16:50:24*
Bump ob-common.version from 1.0.69 to 1.0.70

Bumps `ob-common.version` from 1.0.69 to 1.0.70.

Updates `forgerock-openbanking-model` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Updates `forgerock-openbanking-am` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Updates `forgerock-openbanking-jwt` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Updates `forgerock-openbanking-oidc` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Updates `forgerock-openbanking-upgrade` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Updates `forgerock-openbanking-ssl` from 1.0.69 to 1.0.70
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.69...forgerock-openbanking-starter-commons-1.0.70)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.73
[54113ac50e4b7c5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/54113ac50e4b7c5) JamieB *2020-03-12 15:12:31*
UI workflow now updates master-dev deployment version

Fixes https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/156
## forgerock-openbanking-aspsp-1.0.72
[4a2e8aca5db577a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4a2e8aca5db577a) dependabot-preview[bot] *2020-03-11 09:14:14*
Bump forgerock-openbanking-jwkms-embedded from 1.1.63 to 1.1.64

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.63 to 1.1.64.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.63...forgerock-openbanking-reference-implementation-jwkms-1.1.64)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.71
[df4341014272135](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/df4341014272135) dependabot-preview[bot] *2020-03-04 09:50:46*
Bump ob-clients.version from 1.0.27 to 1.0.30

Bumps `ob-clients.version` from 1.0.27 to 1.0.30.

Updates `forgerock-openbanking-jwkms-client` from 1.0.27 to 1.0.30
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.27...forgerock-openbanking-reference-implementation-clients-1.0.30)

Updates `forgerock-openbanking-analytics-client` from 1.0.27 to 1.0.30
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.27...forgerock-openbanking-reference-implementation-clients-1.0.30)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.27 to 1.0.30
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.27...forgerock-openbanking-reference-implementation-clients-1.0.30)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[9075ddb19608fb7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9075ddb19608fb7) dependabot-preview[bot] *2020-03-04 10:07:55*
Bump forgerock-openbanking-jwkms-embedded from 1.1.59 to 1.1.63

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.59 to 1.1.63.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.59...forgerock-openbanking-reference-implementation-jwkms-1.1.63)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.70
[b4e8ff7b199b024](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b4e8ff7b199b024) dependabot-preview[bot] *2020-03-03 18:54:35*
Bump ob-common.version from 1.0.67 to 1.0.69

Bumps `ob-common.version` from 1.0.67 to 1.0.69.

Updates `forgerock-openbanking-model` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Updates `forgerock-openbanking-am` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Updates `forgerock-openbanking-jwt` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Updates `forgerock-openbanking-oidc` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Updates `forgerock-openbanking-upgrade` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Updates `forgerock-openbanking-ssl` from 1.0.67 to 1.0.69
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.67...forgerock-openbanking-starter-commons-1.0.69)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.69
[5a8c0660966a518](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5a8c0660966a518) dependabot-preview[bot] *2020-03-03 16:25:43*
Bump forgerock-openbanking-starter-parent from 1.0.65 to 1.0.66

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.65 to 1.0.66.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.65...forgerock-openbanking-starter-parent-1.0.66)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.68
### GitHub [#148](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/148) Create UI docker images tagged with queen-rc4
[28a4060337f8fad](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/28a4060337f8fad) Jamie Bowen *2020-03-03 15:15:40*
Create UI docker images tagged with queen-rc4 (#148)

* Create UI docker images tagged with queen-rc4

* Use ForgeCloud ACCESS_TOKEN to obtain customer assets
## forgerock-openbanking-aspsp-1.0.67
[f4c0179443566b4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f4c0179443566b4) dependabot-preview[bot] *2020-02-28 12:17:17*
Bump ob-clients.version from 1.0.26 to 1.0.27

Bumps `ob-clients.version` from 1.0.26 to 1.0.27.

Updates `forgerock-openbanking-jwkms-client` from 1.0.26 to 1.0.27
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.26...forgerock-openbanking-reference-implementation-clients-1.0.27)

Updates `forgerock-openbanking-analytics-client` from 1.0.26 to 1.0.27
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.26...forgerock-openbanking-reference-implementation-clients-1.0.27)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.26 to 1.0.27
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.26...forgerock-openbanking-reference-implementation-clients-1.0.27)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[ea01d28e4a71acb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ea01d28e4a71acb) dependabot-preview[bot] *2020-02-28 12:16:41*
Bump ob-common.version from 1.0.66 to 1.0.67

Bumps `ob-common.version` from 1.0.66 to 1.0.67.

Updates `forgerock-openbanking-model` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Updates `forgerock-openbanking-am` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Updates `forgerock-openbanking-jwt` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Updates `forgerock-openbanking-oidc` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Updates `forgerock-openbanking-upgrade` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Updates `forgerock-openbanking-ssl` from 1.0.66 to 1.0.67
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.66...forgerock-openbanking-starter-commons-1.0.67)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.66
[6c0ae68d2419727](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6c0ae68d2419727) dependabot-preview[bot] *2020-02-28 11:51:55*
Bump forgerock-openbanking-jwkms-embedded from 1.1.58 to 1.1.59

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.58 to 1.1.59.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.58...forgerock-openbanking-reference-implementation-jwkms-1.1.59)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.65
### GitHub [#132](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/132) V9update
[bef45c30373ac8f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bef45c30373ac8f) Julien Renaux *2020-02-28 10:53:39*
V9update (#132)

* remove deprecated dependency

* remove deprecated dependency

* finished

* fix tests

* remove unused dependencies
[f6913a422828c7f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f6913a422828c7f) dependabot-preview[bot] *2020-02-28 10:58:55*
Bump ob-common.version from 1.0.65 to 1.0.66

Bumps `ob-common.version` from 1.0.65 to 1.0.66.

Updates `forgerock-openbanking-model` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Updates `forgerock-openbanking-am` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Updates `forgerock-openbanking-jwt` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Updates `forgerock-openbanking-oidc` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Updates `forgerock-openbanking-upgrade` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Updates `forgerock-openbanking-ssl` from 1.0.65 to 1.0.66
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.65...forgerock-openbanking-starter-commons-1.0.66)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.64
[2b20be4a171537c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2b20be4a171537c) dependabot-preview[bot] *2020-02-28 10:00:14*
Bump forgerock-openbanking-jwkms-embedded from 1.1.54 to 1.1.58

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.54 to 1.1.58.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.54...forgerock-openbanking-reference-implementation-jwkms-1.1.58)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.63
[700cba608bc6c6f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/700cba608bc6c6f) dependabot-preview[bot] *2020-02-28 09:08:35*
Bump ob-clients.version from 1.0.25 to 1.0.26

Bumps `ob-clients.version` from 1.0.25 to 1.0.26.

Updates `forgerock-openbanking-jwkms-client` from 1.0.25 to 1.0.26
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.25...forgerock-openbanking-reference-implementation-clients-1.0.26)

Updates `forgerock-openbanking-analytics-client` from 1.0.25 to 1.0.26
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.25...forgerock-openbanking-reference-implementation-clients-1.0.26)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.25 to 1.0.26
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.25...forgerock-openbanking-reference-implementation-clients-1.0.26)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[93afb2b9c6d1de4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/93afb2b9c6d1de4) dependabot-preview[bot] *2020-02-28 08:57:43*
Bump ob-common.version from 1.0.64 to 1.0.65

Bumps `ob-common.version` from 1.0.64 to 1.0.65.

Updates `forgerock-openbanking-model` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Updates `forgerock-openbanking-am` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Updates `forgerock-openbanking-jwt` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Updates `forgerock-openbanking-oidc` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Updates `forgerock-openbanking-upgrade` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Updates `forgerock-openbanking-ssl` from 1.0.64 to 1.0.65
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.64...forgerock-openbanking-starter-commons-1.0.65)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[de56ee3c42573b6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/de56ee3c42573b6) dependabot-preview[bot] *2020-02-28 09:08:49*
Bump forgerock-openbanking-starter-parent from 1.0.62 to 1.0.65

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.62 to 1.0.65.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.62...forgerock-openbanking-starter-parent-1.0.65)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.62
### GitHub [#135](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/135) Set project version to 3.1.2-queen-rc3
[ea182198e06f822](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ea182198e06f822) Jamie Bowen *2020-02-25 13:43:41*
Set project version to 3.1.2-queen-rc3 (#135)
### GitHub [#136](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/136) Set version to 3.1.2 queen rc3
[72ab24134a98083](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/72ab24134a98083) Jamie Bowen *2020-02-25 15:16:45*
Set version to 3.1.2 queen rc3 (#136)

* Set project version to 3.1.2-queen-rc3

* Correct project version string
### GitHub [#138](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/138) Update merge master git hub action to fropenbanking user and secrets
[342effb6862cd89](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/342effb6862cd89) Jorge Sanchez Perez *2020-02-27 17:08:06*
Update merge master git hub action to fropenbanking user and secrets (#138)

* update merge master git hub action to fropenbanking user and secrets

* trigger github actions
[463838e9eebf36f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/463838e9eebf36f) JamieB *2020-02-26 20:49:46*
Add .gitignore settings to help github action testing
## forgerock-openbanking-aspsp-1.0.61
[af10eeaace13140](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/af10eeaace13140) JamieB *2020-02-06 21:48:37*
move aggregated endpoint to /events rather than /event

- Part Fixes https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/128
## forgerock-openbanking-aspsp-1.0.60
[09cfa5aadb9361f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/09cfa5aadb9361f) dependabot-preview[bot] *2020-02-06 12:20:59*
Bump ob-clients.version from 1.0.23 to 1.0.25

Bumps `ob-clients.version` from 1.0.23 to 1.0.25.

Updates `forgerock-openbanking-jwkms-client` from 1.0.23 to 1.0.25
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.23...forgerock-openbanking-reference-implementation-clients-1.0.25)

Updates `forgerock-openbanking-analytics-client` from 1.0.23 to 1.0.25
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.23...forgerock-openbanking-reference-implementation-clients-1.0.25)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.23 to 1.0.25
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.23...forgerock-openbanking-reference-implementation-clients-1.0.25)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[09871233a433a2f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/09871233a433a2f) dependabot-preview[bot] *2020-02-06 12:06:12*
Bump forgerock-openbanking-jwkms-embedded from 1.1.50 to 1.1.54

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.50 to 1.1.54.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.50...forgerock-openbanking-reference-implementation-jwkms-1.1.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[f593d86c6727204](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f593d86c6727204) JamieB *2020-02-06 12:08:59*
Use the openbanking-uk-datamodel rather than openbanking-sdk

See https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/5
## forgerock-openbanking-aspsp-1.0.59
[41db5b5db2eb595](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/41db5b5db2eb595) JamieB *2020-02-05 17:10:56*
Use latest version of openbanking-commons

Which uses the openbanking-uk-datamodel binaries rather than
openbanking-sdk. See https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/5
## forgerock-openbanking-aspsp-1.0.58
### GitHub [#121](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/121) empty
[9c3b8306da73022](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9c3b8306da73022) Jorge Sanchez Perez *2020-02-05 16:32:09*
empty (#121)
## forgerock-openbanking-aspsp-1.0.57
### GitHub [#119](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/119) Media type for csv to support text/csv files
[1f0e47b7536fb76](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1f0e47b7536fb76) Jorge Sanchez Perez *2020-02-05 11:21:24*
Media type for csv files added on file payment type, to support csv media type files (#119)
### GitHub [#120](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/120) Fix event subscription api test autherror
[32fb8f89845c239](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/32fb8f89845c239) Jorge Sanchez Perez *2020-02-05 14:31:12*
Fix event subscription api test autherror (#120)

* Fix authentication error on event subscrition api test

* Delete non used import
## forgerock-openbanking-aspsp-1.0.56
[30b307820bdc976](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/30b307820bdc976) dependabot-preview[bot] *2020-01-30 14:25:18*
Bump ob-clients.version from 1.0.22 to 1.0.23

Bumps `ob-clients.version` from 1.0.22 to 1.0.23.

Updates `forgerock-openbanking-jwkms-client` from 1.0.22 to 1.0.23
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.22...forgerock-openbanking-reference-implementation-clients-1.0.23)

Updates `forgerock-openbanking-analytics-client` from 1.0.22 to 1.0.23
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.22...forgerock-openbanking-reference-implementation-clients-1.0.23)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.22 to 1.0.23
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.22...forgerock-openbanking-reference-implementation-clients-1.0.23)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.55
[844ee06ac6b0cd7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/844ee06ac6b0cd7) dependabot-preview[bot] *2020-01-30 13:27:16*
Bump ob-common.version from 1.0.62 to 1.0.63

Bumps `ob-common.version` from 1.0.62 to 1.0.63.

Updates `forgerock-openbanking-model` from 1.0.62 to 1.0.63

Updates `forgerock-openbanking-am` from 1.0.62 to 1.0.63

Updates `forgerock-openbanking-jwt` from 1.0.62 to 1.0.63

Updates `forgerock-openbanking-oidc` from 1.0.62 to 1.0.63

Updates `forgerock-openbanking-upgrade` from 1.0.62 to 1.0.63

Updates `forgerock-openbanking-ssl` from 1.0.62 to 1.0.63

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
## forgerock-openbanking-aspsp-1.0.54
### GitHub [#111](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/111) Create OpenAPI specs of rs store
[4a9539ab781827f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4a9539ab781827f) Ben Jefferies *2020-01-30 12:37:05*
Create OpenAPI specs of rs store (#111)

* Create OpenAPI specs of rs store

Still work in progress but wanted to share initial workings with the
team. I reverse generated the swagger by enabling SpringFox swagger
endpoints and then refactoring the various endpoints into the different
specs. The specs need to live separately because the `swagger-codegen`
tool uses the first part of the path to decide which java files to
create, because the first path was `/openbanking` for all APIs it was
creating one massive file.

To tests run

```
forgerock-openbanking-uk-aspsp-rs/forgerock-openbanking-uk-aspsp-rs-mock-store/generate-bank-resource-server.sh spring spring-mvc --group-id com.forgerock.openbanking -DuseBeanValidation=true -DinterfaceOnly=true
```

See https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/108
for more information
## forgerock-openbanking-aspsp-1.0.53
[e664677769a97c5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e664677769a97c5) Ben Jefferies *2020-01-24 12:45:08*
Trigger build
## forgerock-openbanking-aspsp-1.0.52
### GitHub [#107](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/107) Example of how we could generate changelogs
[ad5b3aaa1d18df0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ad5b3aaa1d18df0) Ben Jefferies *2020-01-24 10:26:25*
Example of how we could generate changelogs (#107)

Dependabot will use changelogs to generate information for pull
requests https://github.com/dependabot/dependabot-core/issues/158. Would
be interesting to know how well dependabot pulls this information out
## forgerock-openbanking-aspsp-1.0.51
### GitHub [#106](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/106) Fix update dynamic registration bug
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
### GitHub [#103](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/103) fix consent UI on tiny screens and with TPP long name. 
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
### GitHub [#104](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/104) Remove spring boot plugin
[c292cb9da16f186](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c292cb9da16f186) Ben Jefferies *2020-01-22 15:44:52*
Remove spring boot plugin (#104)

The plugin causes a fat jar to be built. We do not really care if it's a
fat jar because we'd run it from intellij. The fat jars take up a lot of
bintray space
## forgerock-openbanking-aspsp-1.0.47
### GitHub [#102](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/102) Fix parsing CN correctly
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
### GitHub [#81](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/81) use npm to get openbanking-ui-cli
[e5c3034eb4a05e6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e5c3034eb4a05e6) Julien Renaux *2020-01-20 09:30:50*
use npm to get openbanking-ui-cli (#81)
## forgerock-openbanking-aspsp-1.0.43
### GitHub [#82](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/82) Skip the perform of a maven release
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
### GitHub [#70](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/70) Fix the api protection payment with client credentials grant type
[df32735de4e9550](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/df32735de4e9550) Jorge Sanchez Perez *2020-01-17 15:43:10*
Fix the api protection payment with client credentials grant type (#70)

* fix client cledentials Grant type to Get domestic payment with the paymentId

* Unit tests and refactorization of payment enpoints

* Fix Unit tests copyright

* Refactorization of test classes to use Mockito

* Refactorization of test classes to use Mockito

* fix copyright
## forgerock-openbanking-aspsp-1.0.40
### GitHub [#69](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/69) allow any config key to be overwritten by docker env
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
### GitHub [#62](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/62) update UI docs https://github.com/OpenBankingToolkit/openbanking-refe…
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
### GitHub [#60](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/60) Bump ob-clients.version from 1.0.15 to 1.0.16
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
### GitHub [#58](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/58) Use ob-clients.version in version
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
### GitHub [#48](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/48) Bump ob-clients.version from 1.0.12 to 1.0.13
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
### GitHub [#42](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/42) fix actions job condition
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
### GitHub [#31](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/31) Flatten nested modules
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
### GitHub [#30](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/30) Update parent and multiauth login
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
### GitHub [#27](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/27) Update auth
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
### GitHub [#23](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/23) Remove plugin
[6063038fcaf4720](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6063038fcaf4720) Ben Jefferies *2019-12-12 13:57:45*
Remove plugin (#23)
## forgerock-openbanking-aspsp-1.0.18
### GitHub [#21](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/21) Dependabot config
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
### GitHub [#18](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/18) Bump forgerock-openbanking-starter-parent from 1.0.54 to 1.0.56
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
### GitHub [#11](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/11) Bump ob-common.version from 1.0.45 to 1.0.49
[759be4a2e42fae4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/759be4a2e42fae4) Ben Jefferies *2019-12-11 16:26:55*
Bump ob-common.version from 1.0.45 to 1.0.49 (#11)

Bump ob-common.version from 1.0.45 to 1.0.49
### GitHub [#15](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/15) Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54
[61affbc5bdbd6af](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/61affbc5bdbd6af) Ben Jefferies *2019-12-11 16:31:47*
Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54 (#15)

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.52 to 1.0.54.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/forgerock-openbanking-starter-parent-1.0.52...forgerock-openbanking-starter-parent-1.0.54)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
### GitHub [#5](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/5) Re-enable IT
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
### GitHub [#11](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/11) Bump ob-common.version from 1.0.45 to 1.0.49
[a45cbee574e922b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a45cbee574e922b) Ben Jefferies *2019-12-03 15:27:02*
Change MTLS response model to simple list (#11)

The authorities do not need to be anything more complex then the string
list of authorities.
### GitHub [#12](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/12) Bump asciidoctor-maven-plugin from 1.5.6 to 1.6.0
[31579adca5407bd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/31579adca5407bd) Ben Jefferies *2019-12-04 11:32:11*
Use latest ob-common without hateoas (#12)

* Bump common
### GitHub [#13](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/13) Bump forgerock-openbanking-auth from 1.0.38 to 1.0.39
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
### GitHub [#14](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/14) Bump ob-clients.version from 1.0.1 to 1.0.2
[c29dc1b08d70999](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c29dc1b08d70999) Ben Jefferies *2019-12-04 15:40:24*
Fix tests by mocking jwkms calls (#14)
### GitHub [#15](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/15) Bump forgerock-openbanking-starter-parent from 1.0.52 to 1.0.54
[a07b270def1b397](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a07b270def1b397) Ben Jefferies *2019-12-05 10:09:50*
Explicitly toggle include admins (#15)

Explicitly toggle include admins to ensure branch protection is disabled before release
### GitHub [#16](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/16) Bump forgerock-openbanking-auth from 1.0.38 to 1.0.41
[84e4735278bc728](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/84e4735278bc728) Ben Jefferies *2019-12-06 09:00:47*
Use latest spring multi auth for 2.1.5 (#16)

* Use latest spring multi auth for 2.1.5

We don't want to upgrade to 2.2.1 yet as there is breaking changes so
used 2.1.5 multi auth

* Bump parent to revert spring version

* Bump parent to revert spring cloud version

* Fix test
### GitHub [#2](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/2) trigger release
[b42462a1db11fe2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b42462a1db11fe2) Ben Jefferies *2019-10-25 13:51:58*
Fix IT for rs gateway (#2)

* Fix IT for rs gateway

Tests now run successfully when running `mvn clean install` and in codefresh
### GitHub [#3](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/3) Add Distribution management
[2c864c769d4e0be](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2c864c769d4e0be) Ben Jefferies *2019-10-25 16:13:09*
Enable all integration tests and build samples (#3)

* Enable all IT and build sample docker images

RS mock store and RS RCS tests running locally and in codefresh.
Build docker images in codefresh for sample projects.

* Generate files
### GitHub [#5](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/5) Re-enable IT
[7360efe195f20ab](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7360efe195f20ab) Ben Jefferies *2019-11-13 16:15:13*
Temp disable/enable include admins in branch protection (#5)

See https://github.com/benjefferies/branch-protection-bot for backstory.
### GitHub [#6](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/6) Use the same groupeID
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
