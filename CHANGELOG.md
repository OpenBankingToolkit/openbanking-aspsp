# Git Changelog Maven plugin changelog
Changelog of Git Changelog Maven plugin.
## Unreleased
### GitHub [#462](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/462) Intermediate checkin with new 3.1.9r3 datamodels
[ef3db5f856d2af9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef3db5f856d2af9) Jorge Sanchez Perez *2021-12-15 13:51:37*
Feature/36 variable recurring payments for 3 1 8 (#469)

* 45: Add VRP generated API and Controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* commit before rebase master

* 45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* Release candidate: prepare release 1.5.6

* Release candidate: prepare for next development iteration

* after rebase master

* 47: vrp consent controllers
- Fix simbols
- Fix Shortcuts annotations in VRP controller interfaces
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 47: VRP consent controllers
- Fix VRP request Mapping controllers annotation
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Intermediate checkin with new 3.1.9r3 datamodels (#462)

Some of the Consent stuff doesn't compile, but the VRP side now
compiling

* 47: vrp consent controllers (#463)

* 47: vrp consent controllers
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller (#464)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller
- Updated discovery configuration
- Updated resource link service to introduce the vrp links
- Other improvements for vrp consent
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 48: Develop DomesticVrpApi and controllers

Hand over to Jorge!

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Issue/47 implementation of vrp payment consent controller (#466)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Added store IT test

* 36-47: VRP payment consent controller
- Added Integration tests for VRP payment consent
- Few fixes
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 36-47: VRP consent controller flow
- Added implementation to consent details and decision to present data on Bank UI
- Added implementation consent operations (Post, Get, delete)
- Added Integration tests
- Set the proper snapshot dependencies for development iteration on branch 36 (must be updated before merge on master)
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* VRP Funds confirmation

* Fix Domestic consent controller constructor to delete unused service

* Fix integration tests for VRP funds confirmation

* 36: Add filters to test vrp Payments risk and initiation

In accord with this spec;
https://openbankinguk.github.io/read-write-api-site3/v3.1.8/resources-and-data-models/vrp/domestic-vrps.html#post-domestic-vrps

Each VRP request need to test that the initiation and risk objects match
those that were provided when the consent was created.

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36: Further VRP request filters

Add filters to ensure that Creditor is provided in the request if it
doesn't exist in the consent

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36-48: VRP payment controller validations
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Vrp payment submission controller and integration tests

* vrp payment contrller gateway server implementations

* Fix node version github workflow to build and tests the UIs

* Update version to 1.6.0

* Fix npm tests for VRP payment component

Co-authored-by: JamieB <jamie.bowen@forgerock.com>
Co-authored-by: Jamie Bowen <jpublic@chantrycottage.net>
### GitHub [#463](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/463) 47: vrp consent controllers
[ef3db5f856d2af9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef3db5f856d2af9) Jorge Sanchez Perez *2021-12-15 13:51:37*
Feature/36 variable recurring payments for 3 1 8 (#469)

* 45: Add VRP generated API and Controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* commit before rebase master

* 45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* Release candidate: prepare release 1.5.6

* Release candidate: prepare for next development iteration

* after rebase master

* 47: vrp consent controllers
- Fix simbols
- Fix Shortcuts annotations in VRP controller interfaces
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 47: VRP consent controllers
- Fix VRP request Mapping controllers annotation
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Intermediate checkin with new 3.1.9r3 datamodels (#462)

Some of the Consent stuff doesn't compile, but the VRP side now
compiling

* 47: vrp consent controllers (#463)

* 47: vrp consent controllers
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller (#464)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller
- Updated discovery configuration
- Updated resource link service to introduce the vrp links
- Other improvements for vrp consent
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 48: Develop DomesticVrpApi and controllers

Hand over to Jorge!

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Issue/47 implementation of vrp payment consent controller (#466)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Added store IT test

* 36-47: VRP payment consent controller
- Added Integration tests for VRP payment consent
- Few fixes
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 36-47: VRP consent controller flow
- Added implementation to consent details and decision to present data on Bank UI
- Added implementation consent operations (Post, Get, delete)
- Added Integration tests
- Set the proper snapshot dependencies for development iteration on branch 36 (must be updated before merge on master)
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* VRP Funds confirmation

* Fix Domestic consent controller constructor to delete unused service

* Fix integration tests for VRP funds confirmation

* 36: Add filters to test vrp Payments risk and initiation

In accord with this spec;
https://openbankinguk.github.io/read-write-api-site3/v3.1.8/resources-and-data-models/vrp/domestic-vrps.html#post-domestic-vrps

Each VRP request need to test that the initiation and risk objects match
those that were provided when the consent was created.

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36: Further VRP request filters

Add filters to ensure that Creditor is provided in the request if it
doesn't exist in the consent

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36-48: VRP payment controller validations
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Vrp payment submission controller and integration tests

* vrp payment contrller gateway server implementations

* Fix node version github workflow to build and tests the UIs

* Update version to 1.6.0

* Fix npm tests for VRP payment component

Co-authored-by: JamieB <jamie.bowen@forgerock.com>
Co-authored-by: Jamie Bowen <jpublic@chantrycottage.net>
### GitHub [#464](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/464) 47: vrp payment consent controller
[ef3db5f856d2af9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef3db5f856d2af9) Jorge Sanchez Perez *2021-12-15 13:51:37*
Feature/36 variable recurring payments for 3 1 8 (#469)

* 45: Add VRP generated API and Controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* commit before rebase master

* 45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* Release candidate: prepare release 1.5.6

* Release candidate: prepare for next development iteration

* after rebase master

* 47: vrp consent controllers
- Fix simbols
- Fix Shortcuts annotations in VRP controller interfaces
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 47: VRP consent controllers
- Fix VRP request Mapping controllers annotation
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Intermediate checkin with new 3.1.9r3 datamodels (#462)

Some of the Consent stuff doesn't compile, but the VRP side now
compiling

* 47: vrp consent controllers (#463)

* 47: vrp consent controllers
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller (#464)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller
- Updated discovery configuration
- Updated resource link service to introduce the vrp links
- Other improvements for vrp consent
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 48: Develop DomesticVrpApi and controllers

Hand over to Jorge!

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Issue/47 implementation of vrp payment consent controller (#466)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Added store IT test

* 36-47: VRP payment consent controller
- Added Integration tests for VRP payment consent
- Few fixes
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 36-47: VRP consent controller flow
- Added implementation to consent details and decision to present data on Bank UI
- Added implementation consent operations (Post, Get, delete)
- Added Integration tests
- Set the proper snapshot dependencies for development iteration on branch 36 (must be updated before merge on master)
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* VRP Funds confirmation

* Fix Domestic consent controller constructor to delete unused service

* Fix integration tests for VRP funds confirmation

* 36: Add filters to test vrp Payments risk and initiation

In accord with this spec;
https://openbankinguk.github.io/read-write-api-site3/v3.1.8/resources-and-data-models/vrp/domestic-vrps.html#post-domestic-vrps

Each VRP request need to test that the initiation and risk objects match
those that were provided when the consent was created.

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36: Further VRP request filters

Add filters to ensure that Creditor is provided in the request if it
doesn't exist in the consent

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36-48: VRP payment controller validations
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Vrp payment submission controller and integration tests

* vrp payment contrller gateway server implementations

* Fix node version github workflow to build and tests the UIs

* Update version to 1.6.0

* Fix npm tests for VRP payment component

Co-authored-by: JamieB <jamie.bowen@forgerock.com>
Co-authored-by: Jamie Bowen <jpublic@chantrycottage.net>
### GitHub [#466](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/466) Issue/47 implementation of vrp payment consent controller
[ef3db5f856d2af9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef3db5f856d2af9) Jorge Sanchez Perez *2021-12-15 13:51:37*
Feature/36 variable recurring payments for 3 1 8 (#469)

* 45: Add VRP generated API and Controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* commit before rebase master

* 45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* Release candidate: prepare release 1.5.6

* Release candidate: prepare for next development iteration

* after rebase master

* 47: vrp consent controllers
- Fix simbols
- Fix Shortcuts annotations in VRP controller interfaces
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 47: VRP consent controllers
- Fix VRP request Mapping controllers annotation
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Intermediate checkin with new 3.1.9r3 datamodels (#462)

Some of the Consent stuff doesn't compile, but the VRP side now
compiling

* 47: vrp consent controllers (#463)

* 47: vrp consent controllers
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller (#464)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller
- Updated discovery configuration
- Updated resource link service to introduce the vrp links
- Other improvements for vrp consent
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 48: Develop DomesticVrpApi and controllers

Hand over to Jorge!

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Issue/47 implementation of vrp payment consent controller (#466)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Added store IT test

* 36-47: VRP payment consent controller
- Added Integration tests for VRP payment consent
- Few fixes
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 36-47: VRP consent controller flow
- Added implementation to consent details and decision to present data on Bank UI
- Added implementation consent operations (Post, Get, delete)
- Added Integration tests
- Set the proper snapshot dependencies for development iteration on branch 36 (must be updated before merge on master)
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* VRP Funds confirmation

* Fix Domestic consent controller constructor to delete unused service

* Fix integration tests for VRP funds confirmation

* 36: Add filters to test vrp Payments risk and initiation

In accord with this spec;
https://openbankinguk.github.io/read-write-api-site3/v3.1.8/resources-and-data-models/vrp/domestic-vrps.html#post-domestic-vrps

Each VRP request need to test that the initiation and risk objects match
those that were provided when the consent was created.

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36: Further VRP request filters

Add filters to ensure that Creditor is provided in the request if it
doesn't exist in the consent

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36-48: VRP payment controller validations
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Vrp payment submission controller and integration tests

* vrp payment contrller gateway server implementations

* Fix node version github workflow to build and tests the UIs

* Update version to 1.6.0

* Fix npm tests for VRP payment component

Co-authored-by: JamieB <jamie.bowen@forgerock.com>
Co-authored-by: Jamie Bowen <jpublic@chantrycottage.net>
### GitHub [#469](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/469) Feature/36 variable recurring payments for 3 1 8
[ef3db5f856d2af9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ef3db5f856d2af9) Jorge Sanchez Perez *2021-12-15 13:51:37*
Feature/36 variable recurring payments for 3 1 8 (#469)

* 45: Add VRP generated API and Controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* commit before rebase master

* 45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45

* Release candidate: prepare release 1.5.6

* Release candidate: prepare for next development iteration

* after rebase master

* 47: vrp consent controllers
- Fix simbols
- Fix Shortcuts annotations in VRP controller interfaces
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 47: VRP consent controllers
- Fix VRP request Mapping controllers annotation
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Intermediate checkin with new 3.1.9r3 datamodels (#462)

Some of the Consent stuff doesn't compile, but the VRP side now
compiling

* 47: vrp consent controllers (#463)

* 47: vrp consent controllers
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller (#464)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* 47: vrp payment consent controller
- Updated discovery configuration
- Updated resource link service to introduce the vrp links
- Other improvements for vrp consent
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 48: Develop DomesticVrpApi and controllers

Hand over to Jorge!

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Issue/47 implementation of vrp payment consent controller (#466)

* 47: vrp consent controllers
- rebase
- First approach to implement the consent controllers
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Delete uk-datamodel snapshot to use the parent snapshot in the development iteration of VRP payments

* Added store IT test

* 36-47: VRP payment consent controller
- Added Integration tests for VRP payment consent
- Few fixes
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* 36-47: VRP consent controller flow
- Added implementation to consent details and decision to present data on Bank UI
- Added implementation consent operations (Post, Get, delete)
- Added Integration tests
- Set the proper snapshot dependencies for development iteration on branch 36 (must be updated before merge on master)
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/47

* VRP Funds confirmation

* Fix Domestic consent controller constructor to delete unused service

* Fix integration tests for VRP funds confirmation

* 36: Add filters to test vrp Payments risk and initiation

In accord with this spec;
https://openbankinguk.github.io/read-write-api-site3/v3.1.8/resources-and-data-models/vrp/domestic-vrps.html#post-domestic-vrps

Each VRP request need to test that the initiation and risk objects match
those that were provided when the consent was created.

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36: Further VRP request filters

Add filters to ensure that Creditor is provided in the request if it
doesn't exist in the consent

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36

* 36-48: VRP payment controller validations
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/48

* Vrp payment submission controller and integration tests

* vrp payment contrller gateway server implementations

* Fix node version github workflow to build and tests the UIs

* Update version to 1.6.0

* Fix npm tests for VRP payment component

Co-authored-by: JamieB <jamie.bowen@forgerock.com>
Co-authored-by: Jamie Bowen <jpublic@chantrycottage.net>
### GitHub [#470](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/470) 36: VRP payments
[edd48bf92c4daa5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/edd48bf92c4daa5) Jorge Sanchez Perez *2021-12-20 16:45:02*
36: VRP payments (#470)

- Bumped latest dependencies
- Few fixes to support VRP payments
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/36
### GitHub [#471](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/471) Release/1.6.0
[4c3c6e8e8b6e351](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4c3c6e8e8b6e351) Jorge Sanchez Perez *2021-12-21 10:21:32*
Release/1.6.0 (#471)

* Release candidate: prepare release 1.6.0

* Release candidate: prepare for next development iteration

* Changelog updated
[3f95a06597a5266](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3f95a06597a5266) JamieB *2022-01-18 09:10:35*
815: Returns customer info objects

Issue: https://github.com/ForgeCloud/ob-deploy/issues/815
[952035d7215dad6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/952035d7215dad6) JamieB *2022-01-17 11:28:01*
815: Further CustomerInfo update work

Now deletes CustomerInfo if it exists when DELETE /api/data/user
Update for existing CustomerInfo added.

findByUserId and deleteByUserId added to FRCustomerInfoRepository

Issue: https://github.com/ForgeCloud/ob-deploy/issues/815
[321bcacbefee872](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/321bcacbefee872) JamieB *2022-01-11 15:46:32*
815: /user/data API can now recieve and create FRCustomerInfo

Issue: https://github.com/ForgeCloud/ob-deploy/issues/815
[0d5af55cab8e3ef](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0d5af55cab8e3ef) JamieB *2022-01-10 14:32:45*
815: Filter AccountAccessConsents for CustomerInfo consent

Also add empty controllers for the customer info endpoint
Issue: https://github.com/ForgeCloud/ob-deploy/issues/815

Intermediate checking so I can rebase on master

815: Creates customer info consents

Route are in place for customer info endpoints, but the store endpoint
is not implemented yet.

Should be enough for Jorge to start implementing the Customer Info
consent dialog.

Building OBRI with;
- this version of ASPSP
- associated snapshot version of parent and commons

Will allow the use of Postman to create a CustomerInfo type
AccountAccessRequest with the following data;

```
{
    "_id" : "CICf74150ba-24b6-4156-8647-0fddda685fe0",
    "accountAccessConsent" : {
        "data" : {
            "consentId" : "CICf74150ba-24b6-4156-8647-0fddda685fe0",
            "creationDateTime" : ISODate("2022-01-10T14:23:02.033Z"),
            "status" : "AWAITINGAUTHORISATION",
            "statusUpdateDateTime" : ISODate("2022-01-10T14:23:02.033Z"),
            "permissions" : [
                "READCUSTOMERINFOCONSENT"
            ],
            "expirationDateTime" : ISODate("2027-02-10T17:48:45.000Z"),
            "transactionFromDateTime" : ISODate("2017-02-10T17:48:45.000Z"),
            "transactionToDateTime" : ISODate("2027-02-10T17:48:45.000Z")
        },
        "risk" : {
            "data" : "{}"
        }
    },
    "clientId" : "fa7a9584-3396-489a-888f-a8d231d1e70a",
    "aispId" : "fa7a9584-3396-489a-888f-a8d231d1e70a",
    "aispName" : "Anonymous - 61dc40d5b3cb1f00112f2afa",
    "consentId" : "CICf74150ba-24b6-4156-8647-0fddda685fe0",
    "accountIds" : [],
    "created" : ISODate("2022-01-10T14:23:02.039Z"),
    "updated" : ISODate("2022-01-10T14:23:02.039Z"),
    "obVersion" : "v3_1_8",
    "_class" : "com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent"
}
```

Note the consent ID starts with CIC rather than AAC denoting the need to
gain consent using the CustomerInfoConsent dialog rather than the
AccountAccess Consent dialog.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/815
[4833a4411f2b7f9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4833a4411f2b7f9) JamieB *2021-11-26 09:45:58*
Release candidate: prepare for next development iteration
## 1.5.6
### GitHub [#456](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/456) 794: UI redirection customer experience
[eb58e6dccb7cb99](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/eb58e6dccb7cb99) Jorge Sanchez Perez *2021-11-16 16:04:23*
794: UI redirection customer experience (#456)

* 794: UI redirection customer experience
- Issue: https://github.com/ForgeCloud/ob-deploy/issues/794

* Fix ng lint
### GitHub [#457](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/457) Release/1.5.5
[18b50ef6ffaf807](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/18b50ef6ffaf807) Jorge Sanchez Perez *2021-11-17 08:34:07*
Release/1.5.5 (#457)

* Changelog updated

* Release candidate: prepare release 1.5.5

* Release candidate: prepare for next development iteration
[60ade108053dc75](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/60ade108053dc75) JamieB *2021-11-26 09:45:54*
Release candidate: prepare release 1.5.6
[cfe1045b2b47703](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/cfe1045b2b47703) JamieB *2021-11-26 09:08:26*
45: Use latest uk-datamodel with 3.1.8r5 swagger models

Some changes to VRP and Funds Conformation

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/45
[eef7145f5305af3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/eef7145f5305af3) JamieB *2021-10-12 06:53:39*
Release candidate: prepare for next development iteration
[104306b2f933965](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/104306b2f933965) JamieB *2021-09-24 12:45:20*
Release candidate: prepare for next development iteration
[3075d775b0f69a2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3075d775b0f69a2) JamieB *2021-09-24 10:12:47*
Release candidate: prepare for next development iteration
## 1.5.4
[ac7a863a04bc5e0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ac7a863a04bc5e0) JamieB *2021-10-11 15:57:23*
802: Use latest commons and parent

Allow creation of OBRIRoles from PSD2Role
Also;
Pulls in 1.0.3 of spring-security-mult-auth that has improved logging

Issue: ForgeCloud/ob-deploy#802
[870b8d2c984ebb2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/870b8d2c984ebb2) JamieB *2021-10-12 06:53:35*
Release candidate: prepare release 1.5.4
## 1.5.3
[e26a2637e56ef2f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e26a2637e56ef2f) JamieB *2021-09-24 12:45:16*
Release candidate: prepare release 1.5.3
[48452a76eba81a7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/48452a76eba81a7) JamieB *2021-09-24 11:33:37*
797: Database upgrade does not save authorisationNumber

Issue: https://github.com/ForgeCloud/ob-deploy/issues/797
## 1.5.2
### GitHub [#451](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/451) 39: Branch protection bot upgrade
[f1ba82a48442111](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f1ba82a48442111) Jorge Sanchez Perez *2021-09-23 14:35:42*
39: Branch protection bot upgrade (#451)

- Upgrade github workflows with bot version 1.0.7
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/39
[6a72b528f152965](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6a72b528f152965) JamieB *2021-09-24 10:12:44*
Release candidate: prepare release 1.5.2
[1e84b529cf5238a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1e84b529cf5238a) JamieB *2021-09-24 09:38:42*
775: Perform database upgrade to fix TPP records

The database upgrade is controlled by a spring config setting that is
placed in the application.yaml;

```
as:
  mongo-migration:
    tpp-migration:
      enabled: true
```

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
[55c823e355ce230](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/55c823e355ce230) JamieB *2021-09-23 14:41:39*
775: Start of database migration
[191f3d1ecafc790](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/191f3d1ecafc790) JamieB *2021-09-23 14:41:39*
775: Allow TPPs to register multiple software statements

This contains fixes for;
Issue: https://github.com/ForgeCloud/ob-deploy/issues/796
Issue: https://github.com/ForgeCloud/ob-deploy/issues/795

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
[a8abfff98e9ad8c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a8abfff98e9ad8c) JamieB *2021-09-15 15:07:31*
Release candidate: prepare for next development iteration
## 1.5.1
### GitHub [#443](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/443) Release/1.4.8
[058348cdaf09714](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/058348cdaf09714) Jorge Sanchez Perez *2021-08-13 09:53:24*
Release/1.4.8 (#443)

* Release 1.4.8
- Bumped common version 1.2.4
- bumped client version 1.2.3
- Bumped auth version 1.1.3
- Bumped jwkms version 1.2.3

* Release candidate: prepare release 1.4.8

* Release candidate: prepare for next development iteration
### GitHub [#444](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/444) 789: Missing account consent field statusUpdateDateTime
[6b6425fef5d164e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6b6425fef5d164e) Jorge Sanchez Perez *2021-08-26 08:25:48*
789: Missing account consent field statusUpdateDateTime (#444)

- Fix the missing field statusUpdateDateTime across all versions
Issue: https://github.com/ForgeCloud/ob-deploy/issues/789
### GitHub [#446](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/446) Release/1.4.9
[4afeb1a871ab03e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4afeb1a871ab03e) Jorge Sanchez Perez *2021-08-31 13:56:50*
Release/1.4.9 (#446)

* Change log updated

* Release candidate: prepare release 1.4.9

* Release candidate: prepare for next development iteration
### GitHub [#448](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/448) Release/1.5.0
[9d669f224210d73](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9d669f224210d73) Jorge Sanchez Perez *2021-09-02 16:42:26*
Release/1.5.0 (#448)

* Release 1.5.0
- Bumped commons version 1.2.5
- Bumped clients version 1.2.4
- Bumped auth version 1.1.4
- Bumped jwksm version 1.2.4
- Update development version to 1.5.0-SNAPSHOT for the next release generation

* Release candidate: prepare release 1.5.0

* Release candidate: prepare for next development iteration

* Release candidate: rollback the release of 1.5.0

* Bumped latest versions of FR dependencies

* Release candidate: prepare release 1.5.0

* Release candidate: prepare for next development iteration
[da696c4b31e1edb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/da696c4b31e1edb) JamieB *2021-09-15 15:07:27*
Release candidate: prepare release 1.5.1
[6098c549aa5e0ae](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6098c549aa5e0ae) JamieB *2021-09-15 14:53:10*
755: Allows TPP to Register multiple software statements

All DCR endpoints now work, have fixed functional tests and done an
account access hybrid flow via postman.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
PR: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/449
[36f69a7520a2fc6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/36f69a7520a2fc6) JamieB *2021-09-02 11:03:09*
775: Fix some IT tests

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
[3568378e7f2c309](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3568378e7f2c309) JamieB *2021-09-01 14:24:47*
775: Rebased onto multiple-registrations branch

Rebased onto the work John has been doing to move
DirectorySoftwareStatement into commons and modify the Tpp Class.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
[37a74de1b38eee5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/37a74de1b38eee5) JamieB *2021-09-01 13:20:17*
755: Fix endpoint authorization matls checks

Matls used to work like this;
spring-security-multi-auth would look up the tpp using the certificateCN
(only worked for OB Transport certs), and put the oidc client from teh
Tpp record into the spring principal name. Then the Endpoint Wrapper
would look at the access_token and get the oauth2 clientId from there
and check it was the same as the spring principal name.

However, the presented cert can now not be tied to a specific OAuth2
client Id. All the spring-security-multi-auth can do is populate the
principal name with the authorizationNumber taken from the PSD2 eIDAS
certificate. So, the endpoint wrapper must look for tpp based on the
OAuth2 Client Id (unique to a Tpp record) and check that the
authorizationNumber in that TPP record matches the Spring principal
name. This will ensure that the access_token was issued to an OAuth2
client that belongs to the TPP as identified in the presented transport
certificate.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/775
[5bbcdd6ec827b00](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5bbcdd6ec827b00) jterryfr *2021-09-01 12:46:04*
fix failing test
[ee34b54b5f2613a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ee34b54b5f2613a) jterryfr *2021-09-01 10:39:26*
migrate the directory software statement to common
[d9b2e0ca83d7adc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d9b2e0ca83d7adc) jterryfr *2021-09-01 10:39:26*
allow string ssa aswell as directorySoftwareStatement
[a19780e0f6f60ff](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a19780e0f6f60ff) jterryfr *2021-09-01 10:39:26*
add authorisationNumber code
[21b1a438d0711ca](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/21b1a438d0711ca) jterryfr *2021-09-01 10:39:26*
remvoe string ssa
[e84b2f7ac4d9f65](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e84b2f7ac4d9f65) jterryfr *2021-09-01 10:39:25*
allow multiple registrations
[da62eb6032e494e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/da62eb6032e494e) jterryfr *2021-09-01 10:33:34*
add authorisationNumber code
[d210704793b3ba5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d210704793b3ba5) jterryfr *2021-08-31 15:50:57*
allow string ssa aswell as directorySoftwareStatement
[e652ae7faa2f610](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e652ae7faa2f610) jterryfr *2021-08-31 12:05:02*
remvoe string ssa
[c9341d1d80c0027](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c9341d1d80c0027) jterryfr *2021-08-31 12:03:53*
migrate the directory software statement to common
[852a366f5cb7edb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/852a366f5cb7edb) jterryfr *2021-08-31 09:12:31*
allow multiple registrations
[963f0b5c1e6cb4f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/963f0b5c1e6cb4f) JamieB *2021-08-05 14:04:30*
Release candidate: prepare for next development iteration
## 1.4.7
### GitHub [#428](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/428) 426: [bank UI] Confirmation page when user Cancel the consent
[bc8074b917f395a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bc8074b917f395a) Jorge Sanchez Perez *2021-06-16 14:49:06*
426: [bank UI] Confirmation page when user Cancel the consent (#428)

* 426: Confirmation page when user Cancel the consent
- Cancel component added
- Cancel component integrated
- Confirmation window when user cancels the consent
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/426

* delete comented source

* delete cancel test, not necessary for simple component

* delete imports declared but never used to pass lint
### GitHub [#429](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/429) 766: funds confirmation consent grant type
[2564c3325ee278c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2564c3325ee278c) Jorge Sanchez Perez *2021-06-21 11:27:56*
766: funds confirmation consent grant type (#429)

* 766: funds confirmation consent grant type
- Fix gran type for funds confirmation consents
Issue: https://github.com/ForgeCloud/ob-deploy/issues/766

* Added expected Grant type

* fix log expected scopes
### GitHub [#436](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/436) 434-766: funds confirmation payments grant type fix
[b1551ef78a12a8c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b1551ef78a12a8c) Jorge Sanchez Perez *2021-06-24 10:29:20*
766: funds confirmation payments grant type fix (#436)

- Rollback grant type change for funds confirmations API
- Grant type fixed for payments/funds confirmation endpoints
- Fix applied on: Domestic payments, international payments and international scheduled payments
Issues:
- https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/434
- https://github.com/ForgeCloud/ob-deploy/issues/766
### GitHub [#437](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/437) Release/1.4.5
[f9a11b9d03ac7a2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f9a11b9d03ac7a2) Jorge Sanchez Perez *2021-06-24 12:23:12*
Release/1.4.5 (#437)

* Release 1.4.5
- Bumped common 1.2.1
- Bumped clients 1.2.1
- Bumped jwkms 1.2.1
- Bumped auth 1.1.1

* Release candidate: prepare release 1.4.5

* Release candidate: prepare for next development iteration
### GitHub [#438](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/438) 433: improve loggin /access_token endpoint (Matls verification)
[8f150496bf8fb46](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8f150496bf8fb46) Jorge Sanchez Perez *2021-06-28 14:35:05*
433: improve loggin /access_token endpoint (Matls verification) (#438)

- Improved loggin Matls Request Verification Service
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/433
### GitHub [#439](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/439) Release/1.4.6
[7fb583a5a91c0c1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7fb583a5a91c0c1) Jorge Sanchez Perez *2021-06-28 14:54:32*
Release/1.4.6 (#439)

* Release candidate: prepare release 1.4.6

* Release candidate: prepare for next development iteration

* chnagelog updated
[4a74a6cef99fc09](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4a74a6cef99fc09) JamieB *2021-08-05 14:04:26*
Release candidate: prepare release 1.4.7
[ee20ec5d988dc22](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ee20ec5d988dc22) JamieB *2021-08-05 13:41:01*
422: Replace the use of lombok

Address another review comment

PR: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/440/files
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[7c5d4709577c0fc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7c5d4709577c0fc) JamieB *2021-08-05 13:03:34*
422: Better log messages with context

Addresses PR Comments

PR: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/440
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[5097dab093e3c61](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5097dab093e3c61) JamieB *2021-08-05 12:54:13*
422: Use ifPresent to handle optional

Addresses a PR Comment

PR: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/440
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[911cdd31f0357c5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/911cdd31f0357c5) JamieB *2021-08-05 12:08:03*
422: Review of log levels and message

Result of PR comments

- Better log message and review of levels.
- Sorted out imports

PR: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/440
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[7663505f1a5d985](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7663505f1a5d985) JamieB *2021-08-03 10:53:30*
422: Better logging and in spec error handling

As well as some tidying up in preparation for fixing existing Dynamic
Client Registration issues.

Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/440
[90e77e745250fa5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/90e77e745250fa5) JamieB *2021-07-28 09:26:55*
intermediate checkin
[66936ed7da8a0a5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/66936ed7da8a0a5) JamieB *2021-07-19 15:47:33*
422: Refactoring Dynamic Client Registration

Better log message
Error handling now as OB spec, using OAuth2 and Dynamic client
registration error specifications

Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[92e5c7c138df770](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/92e5c7c138df770) JamieB *2021-07-07 15:31:49*
422: improving registration logging and errors
[87ca9e69439dcf0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/87ca9e69439dcf0) JamieB *2021-06-18 15:36:39*
422: Improve DELETE /register logging and error handling

And general tidy and refactor. These Dynamic Client Registration
endpoints were returning Open Banking errors, when they should be
returning errors inline with teh OAuth2 specifications. Also I have
added a test_facility_advice field to the response to help reduce
support tickets for both our customers, and as a result, ourselves.

Still a long way to go on this.

Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/422
[c281da384dabbba](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c281da384dabbba) Matt Wills *2021-06-10 13:10:39*
32: Version prefix in v3.1.7 and v3.1.8 Accounts API

- Fix version prefix (from 'V' to 'v') in v3.1.7 and v3.1.8 Account API URLs

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
[b0704b02d1c8154](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b0704b02d1c8154) JamieB *2021-06-02 13:53:21*
Release candidate: prepare for next development iteration
[dc53032d15bc9f4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/dc53032d15bc9f4) JamieB *2021-06-02 13:09:25*
Release candidate: rollback the release of 1.4.3
[857948eaae36fee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/857948eaae36fee) JamieB *2021-06-02 12:54:46*
Release candidate: prepare release 1.4.3
[c9b2efe15b39437](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c9b2efe15b39437) JamieB *2021-06-02 12:43:55*
749: make x-headless-auth-enabled false default /authorize

During refactoring for issue
https://github.com/ForgeCloud/ob-deploy/issues/745 I inadvertantly
removed the default value for the X_HEADLESS_AUTH_ENABLED setting. It
should be false by default.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/749
[715919c227dde22](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/715919c227dde22) JamieB *2021-05-26 15:02:43*
745: Addressed more comments
[674b30928f499b0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/674b30928f499b0) JamieB *2021-05-26 13:53:57*
745: Address review comments
[488ac7ed3b94abf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/488ac7ed3b94abf) JamieB *2021-05-25 10:20:17*
Address PR Comments
[0e0ba07ec78a002](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0e0ba07ec78a002) JamieB *2021-05-25 08:13:49*
745: Make hybrid flow redirect uri's id_token verifiable

Re-sign the id token that get's passed to the redirect URI after consent
has been given via the hybrid flow.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/745
[82f8ccbff075bf2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/82f8ccbff075bf2) Matt Wills *2021-05-24 14:43:50*
Release candidate: prepare for next development iteration
## 1.4.4
[41f8db04407bd5d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/41f8db04407bd5d) JamieB *2021-06-02 13:53:15*
Release candidate: prepare release 1.4.4
[431a39cb7f64e75](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/431a39cb7f64e75) JamieB *2021-06-02 13:48:15*
749: set dev version to 1.4.4-SNAPSHOT before creating release

Something odd has happened with the aspsp versions. We're using 1.4.3
currently in openbanking-reference-implementation, but master has
1.4.3-SNAPSHOT in the poms!!!

Issue: https://github.com/ForgeCloud/ob-deploy/issues/749
## 1.4.2
### GitHub [#342](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/342) Release/1.1.118
[141816573d0231d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/141816573d0231d) Jorge Sanchez Perez *2021-02-22 15:07:48*
Release/1.1.118 (#342)

* Bumped versions
- Bumped starter parent version 1.1.86
- Bumped clients version 1.0.45
- Bumped common version 1.0.90
- Bumped auth version 1.0.66
- Bumped jwkms version 1.1.77

* Release candidate: prepare release 1.1.118

* Release candidate: prepare for next development iteration
### GitHub [#346](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/346) 201: Analytics empty data
[c0e3227e4ed0b8c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c0e3227e4ed0b8c) Jorge Sanchez Perez *2021-03-11 08:46:35*
201: Analytics empty data (#346)

* 201: Analytics empty data
- Bumped `ob-common` version 1.1.0
- Added new dependency `ob-common-annotations`
- Refactorisation of classes to use the new origin of annotation `OpenBankingAPI`
- Bumped `ob-clients` version 1.1.0
- Bumped `ob-auth` version 1.0.67
- Bumped `ob-jwkms` version 1.1.78
- Upgrade minor version to 1.2.0-SNAPSHOT
- Prepare for the next release

* Changelog updated
### GitHub [#350](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/350) Release/1.2.0
[32efab6fcf30f25](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/32efab6fcf30f25) Jorge Sanchez Perez *2021-03-11 09:18:43*
Release/1.2.0 (#350)

* Release candidate: prepare release 1.2.0

* Release candidate: prepare for next development iteration
### GitHub [#354](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/354) Release/1.2.1
[8e85606d358c96d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8e85606d358c96d) Jorge Sanchez Perez *2021-03-18 09:49:29*
Release/1.2.1 (#354)

* Release candidate: prepare release 1.2.1

* Release candidate: prepare for next development iteration
### GitHub [#356](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/356) 16: Switched to GenericOBDiscoveryAPILinks in uk-datamodel
[47a5dac51797c70](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/47a5dac51797c70) Matt Wills *2021-03-25 11:09:09*
Revert "Merge pull request #356 from OpenBankingToolkit/issue/16-discovery-serialization"

This reverts commit 95736879f65e84956368935f33eb6ddcc3368e44, reversing
changes made to 8ad781b544611fb6c6654ffa8b5305c1f7268fad.
### GitHub [#358](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/358) 23: GetAccountStatementFile: 501 Not Implemented
[e31214edb3b0b1c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e31214edb3b0b1c) Jorge Sanchez Perez *2021-03-25 16:37:50*
23: GetAccountStatementFile: 501 Not Implemented (#358)

* 23: GetAccountStatementFile: 501 Not Implemented
- Improvements in the response.
- Integration tests adapted
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/23

* improve error message

* improve error message for integration tests
### GitHub [#359](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/359) Issue/23 get account statement file not implemented
[3222e0077e5e720](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3222e0077e5e720) Jorge Sanchez Perez *2021-03-26 09:40:49*
Issue/23 get account statement file not implemented (#359)

* 23: GetAccountStatementFile: 501 Not Implemented
- Improvements in the response.
- Integration tests adapted
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/23

* improve error message

* improve error message for integration tests

* added comment for GetAccountStatementFile operation

* added comment for not found resolution
### GitHub [#362](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/362) 27: upgrade datatype &#39;countrySubDivision&#39;
[9848d03dc2507c2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9848d03dc2507c2) Jorge Sanchez Perez *2021-04-07 07:40:06*
27: upgrade datatype 'countrySubDivision' (#362)

* 27: upgrade datatype 'countrySubDivision'
- Clean and tidy up old migration classes
- Create new changeLog to upgrade the datatype for payments
- Bumped starter parent version 1.1.88

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/27

* Deleted commented source lines

* Change property name of ObjectToUpdate class

* Added deprecated annotation for legacy classes, deleted the obVersion filter

* Mongobee local runner renamed and moved into test sources directory

* Added interface on legacy classes to get the id and countrySubDivision values

* Fix license format

* improvements and format
### GitHub [#364](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/364) Release/1.2.3
[e7fb62c6a21c994](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e7fb62c6a21c994) Jorge Sanchez Perez *2021-04-07 11:47:42*
Release/1.2.3 (#364)

* Release candidate: prepare release 1.2.3

* Release candidate: prepare for next development iteration

* Release candidate: rollback the release of 1.2.3

* Bumped versions
- Bumped common version 1.1.1
- Bumped clients version 1.1.1
- Bumped auth version 1.0.68
- Bumped jwkms version 1.1.79

* Release candidate: prepare release 1.2.3

* Release candidate: prepare for next development iteration
### GitHub [#368](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/368) 28: Get statement from GCP bucket
[641999645c4b878](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/641999645c4b878) Jorge Sanchez Perez *2021-04-20 14:14:15*
28: Get statement from GCP bucket (#368)

* 28: Get statement from GCP bucket
- Bumped starter parent 1.1.89
- Bumped common version 1.1.2
- Bumped clients version 1.1.2
- Bumped auth version 1.0.69
- Bumped jwkms version 1.1.80
- Fetch statement from GCP bucket implemented
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/28

* fix license header

* added OBErrorResponse when could not get the content length, added the IT test for that case

* Deleted redudant Array Creation

* delete unused imports and format the source

* 28: fetch statement pdf resource from gcp
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/28
### GitHub [#374](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/374) Release/1.3.0
[3814a23a87850d9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3814a23a87850d9) Jorge Sanchez Perez *2021-04-20 14:55:39*
Release/1.3.0 (#374)

* upgrade the version

* Release candidate: prepare release 1.3.0

* Release candidate: prepare for next development iteration

* added changelog
### GitHub [#376](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/376) 741: Report file payment, csv file type not supported
[4f31533bc8180a4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4f31533bc8180a4) Jorge Sanchez Perez *2021-05-04 14:58:20*
741: Report file payment, csv file type not supported (#376)

* 741: Report file payment, csv file type not supported
- Report file not supported for CSV file types, response improved
Issue: https://github.com/ForgeCloud/ob-deploy/issues/741

* added response improvement on version 3.0

* 741: unsupported file type
- added new exception 'UnsupportedFileTypeException'
- Payment report file service update to throw the specific exception
- Controllers updated to catch the new exception

* fix body message quotes
### GitHub [#377](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/377) Release/1.3.1
[f37ac92de401455](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f37ac92de401455) Jorge Sanchez Perez *2021-05-06 10:36:14*
Release/1.3.1 (#377)

* Release candidate: prepare release 1.3.1

* Release candidate: prepare for next development iteration

* changelog
### GitHub [#378](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/378) 31: self links event-subscriptions correction
[f0989595cc17900](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f0989595cc17900) Jorge Sanchez Perez *2021-05-07 14:39:42*
31: self links event-subscriptions correction (#378)

- fix self links on event-subscriptions
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/31
### GitHub [#380](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/380) Release/1.3.2
[52f899ddaf6b9dc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/52f899ddaf6b9dc) Jorge Sanchez Perez *2021-05-07 14:54:44*
Release/1.3.2 (#380)

* Release candidate: prepare release 1.3.2

* Release candidate: prepare for next development iteration
### GitHub [#391](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/391) 32: Bumped version of parent pom to pull in minor fix to datamodel
[0e5b86be2bf47ae](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0e5b86be2bf47ae) Matt Wills *2021-05-13 10:34:21*
32: Bumped version of parent pom to pull in minor fix to datamodel (#391)

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
### GitHub [#392](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/392) 742: token endpoint 500 error improvements
[d1c2a4ea43d7d45](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d1c2a4ea43d7d45) Jorge Sanchez Perez *2021-05-14 07:38:05*
742: token endpoint 500 error improvements (#392)

- Bumped starter parent version 1.1.97
- Bumped common version 1.1.4
- Bumped clients version 1.1.4
- Bumped jwkms version 1.1.83
- Bumped auth version 1.0.71
- Added support to build module independently avoiding license error.
- Added UnsupportedOIDCAuthMethodsException and UnsupportedOIDCGrantTypeException in custom handler
Issue: https://github.com/ForgeCloud/ob-deploy/issues/742
### GitHub [#397](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/397) Release/1.3.4
[92492510eba8e77](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/92492510eba8e77) Jorge Sanchez Perez *2021-05-14 08:05:45*
Release/1.3.4 (#397)

* changelog

* Release candidate: prepare release 1.3.4

* Release candidate: prepare for next development iteration
### GitHub [#404](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/404) Release/1.4.0
[e56fad339682b16](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e56fad339682b16) Jorge Sanchez Perez *2021-05-17 10:31:59*
Release/1.4.0 (#404)

* Release 1.4.0
- Bumped common version 1.2.0
- Bumped clients version 1.2.0
- Bumped jwkms version 1.2.0
- Bumped auth version 1.1.0
- Upgrade aspsp version to 1.4.0

* Release candidate: prepare release 1.4.0

* Release candidate: prepare for next development iteration

* changelog
### GitHub [#406](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/406) Release/1.4.1
[c20afa490c34de8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c20afa490c34de8) Jorge Sanchez Perez *2021-05-18 14:33:09*
Release/1.4.1 (#406)

* changelog

* Release candidate: prepare release 1.4.1

* Release candidate: prepare for next development iteration
[8fc3878e3b83fa2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8fc3878e3b83fa2) Matt Wills *2021-05-24 14:42:15*
Release candidate: prepare release 1.4.2
[75f094eb79dcb7c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/75f094eb79dcb7c) Matt Wills *2021-05-24 13:36:57*
33: v3.1.8 of Events and Funds Confirmation APIs

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/33
[ff6bd0672f383d6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ff6bd0672f383d6) Matt Wills *2021-05-24 13:25:28*
33: v3.1.8 of Accounts API gateway controllers and interfaces

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/33
[0a1b7f53da3cc34](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0a1b7f53da3cc34) Matt Wills *2021-05-24 12:54:37*
33: v3.1.8 of Payment API controllers and interfaces

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/33
[492016f2f9a6054](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/492016f2f9a6054) Matt Wills *2021-05-21 14:27:18*
76: Migrated to v3.1.8 of uk-datamodel

- Fixed compilation errors after moving to consolidated enums
- Fixes prior to new v3.1.8 controller classes and interfaces

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/76
[9f3308ce4919304](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9f3308ce4919304) Matt Wills *2021-05-18 10:12:41*
32: Removed accidental import of org.bouncycastle.asn1.x500.style.RFC4519Style.postalAddress

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
[764b09728e0b43b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/764b09728e0b43b) Matt Wills *2021-05-17 09:59:54*
32: v3.1.7 Discovery config

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
[708ca312a764b16](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/708ca312a764b16) Matt Wills *2021-05-14 10:31:10*
32: Bumped version of parent pom to include v3.1.7 of Events/Funds datamodel

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
[7e7eac2b578b928](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7e7eac2b578b928) Matt Wills *2021-05-14 09:14:32*
32: v3.1.7 of Events and Funds API

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/32
[6891d6ec4fbeb64](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6891d6ec4fbeb64) Matt Wills *2021-05-12 11:54:03*
32: Bumped parent pom to release version

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/32
[9daa4c020e616a9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9daa4c020e616a9) Matt Wills *2021-05-12 11:53:24*
32: Additional changes after regeneration of v3.1.7 datamodel (using Open API spec)

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/32
[0336d2691fa87ce](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0336d2691fa87ce) Matt Wills *2021-05-12 11:53:24*
32: v3.1.7 of Payments API controllers

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/32
[79073ae9e7c4814](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/79073ae9e7c4814) JamieB *2021-05-11 08:02:35*
Release candidate: prepare for next development iteration
[d46d7cb97ca4a0c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d46d7cb97ca4a0c) JamieB *2021-05-11 07:23:52*
34: Use forgerock spring-security-multi-auth

Issue: https://github.com/OpenBankingToolkit/openbanking-jwkms/pull/121
[c6d6941a043526e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c6d6941a043526e) Matt Wills *2021-05-10 13:57:39*
32: v3.1.7 of Accounts API controllers

- Includes correction to v3.1.6 of AccountsApi

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/32
[cd06381131fbc8b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/cd06381131fbc8b) Matt Wills *2021-05-07 15:04:41*
32: v3.1.7 of the Accounts API datamodel (prior to new controllers)

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/32
[2c8e8e022f91825](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2c8e8e022f91825) Matt Wills *2021-03-25 09:55:27*
16: Switch to GenericOBDiscoveryAPILinks in uk-datamodel

Issue: https://github.com/OpenBankingToolkit/openbanking-uk-datamodel/issues/16
[8fed42b0aaa563e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8fed42b0aaa563e) JamieB *2021-03-18 17:34:43*
Release candidate: prepare for next development iteration
[b78ec6751ee3bc4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b78ec6751ee3bc4) jorgesanchezperez *2021-03-17 17:31:18*
Deleted application.yml file from rs-mock-store-server library
[0049d9050fa579c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0049d9050fa579c) jorgesanchezperez *2021-03-17 17:29:06*
increase default values for limits defined on DataCreator
[9af9357b7254cd4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9af9357b7254cd4) jorgesanchezperez *2021-03-17 17:25:24*
Fix license
[5c211b60d24784f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5c211b60d24784f) jorgesanchezperez *2021-03-17 17:24:08*
Added default values for data creator documents and accounts limits
[20e8a19e9790614](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/20e8a19e9790614) jorgesanchezperez *2021-03-17 17:11:17*
351-352: self link by version and funds confirmations
Issues:
https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/351
https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/352
[0bd6069b9ede3cf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0bd6069b9ede3cf) Matt Wills *2021-03-10 11:29:45*
20 - Fix expected JSON format

- Fix the expected JSON format for objects within AccountWithBalance (that's returned as part of the various ConsentDetails objects to the consent UI).

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/20
[5d03755734ada6f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5d03755734ada6f) Matt Wills *2021-03-01 08:55:24*
296 - Minor fixes and corrections
[1e18b5080b61311](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1e18b5080b61311) jorgesanchezperez *2021-02-17 15:20:00*
Release candidate: prepare for next development iteration
## 1.3.3
[fd3ba6bccc88d73](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fd3ba6bccc88d73) JamieB *2021-05-11 08:02:28*
Release candidate: prepare release 1.3.3
[c4f059c5d87c7c3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c4f059c5d87c7c3) JamieB *2021-05-11 07:39:18*
34: Empty commit to force codefresh build
## 1.2.2
[d24b7341d82295e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d24b7341d82295e) JamieB *2021-03-18 17:34:37*
Release candidate: prepare release 1.2.2
[49c8d51b3ae697f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/49c8d51b3ae697f) JamieB *2021-03-18 17:12:30*
721: Need a default spring value for all spring config

So that this can run even if no spring config server is available the
rs.data.upload.limit.documents spring config value. This was causing
failure of integration tests in the obri project when no spring config
was available.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/721
## 1.1.117
### GitHub [#314](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/314) 14: payment refunds - Domestic payments
[7990cc32fc3f982](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7990cc32fc3f982) Jorge Sanchez Perez *2021-01-11 10:09:45*
14: payment refunds - Domestic payments (#314)

* 14: payment refunds - Domestic payments
- Generated API version 3.1.4 and delete extended version 3.1.3
  - modules: aspsp-rs-gateway, aspsp-rs-mock-store
- Create converter for version 3.1.4
- Fix converter 3.1.5 to handler `Read refund account` enumeration values in consent
- Introduced in the payment response the `refund` object account when `read refund account` is set to `YES`
- Created Integration tests for versions 3.1.4 and 3.1.6(extends 3.1.5)
- Domestic payments
- International payments
Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/14
* Update starter parent to the latest version
* suggested changes and improvements
* fix packagePayment method name to responseEntity on all versions, delete final modifier on RefundPaymentsFactory
### GitHub [#319](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/319) Release/1.0.111
[bdb56329e6a2f02](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bdb56329e6a2f02) Jorge Sanchez Perez *2021-01-11 15:15:30*
Release/1.0.111 (#319)

* Release 1.0.111
- Refund fixes
- starter parent updated to the lastest version
- prepare for next development iteration
### GitHub [#322](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/322) Release/1.0.112
[c60c3c987af6225](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c60c3c987af6225) Jorge Sanchez Perez *2021-01-12 11:17:40*
Release/1.0.112 (#322)

* Release candidate: prepare release 1.0.112
* Release candidate: prepare for next development iteration
### GitHub [#323](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/323) 18: Discover RS aspsp payments 3.1.4 links
[4726880f61e5c4d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4726880f61e5c4d) Jorge Sanchez Perez *2021-01-14 09:24:20*
18: Discover RS aspsp payments 3.1.4 links (#323)

- Updated the value obReference on @OpenBankingAPI annotation to fix the links published by discovery service
  - URL: rs.aspsp.${DOMAIN}/open-banking/discovery
### GitHub [#324](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/324) Release/1.0.113
[58e7ca0d31e3bee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/58e7ca0d31e3bee) Jorge Sanchez Perez *2021-01-14 10:27:25*
Release/1.0.113 (#324)

* Release candidate: prepare release 1.0.113

* Release candidate: prepare for next development iteration
### GitHub [#328](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/328) 672: payment frequency pattern validation
[5a90bea1d1cd3b4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5a90bea1d1cd3b4) Jorge Sanchez Perez *2021-02-04 10:47:30*
672: payment frequency pattern validation (#328)

* 672: Payment Frequency pattern validation error
- update the regular expression for frequency type 'IntrvlMnthDay'
Issue: https://github.com/ForgeCloud/ob-deploy/issues/672

* - JUnit tests added to validate the payment frequency interval

Co-authored-by: Matt Wills <matt.wills@forgerock.com>
### GitHub [#330](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/330) 674: response_types FAPI compliant
[237e74d0433d721](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/237e74d0433d721) Jorge Sanchez Perez *2021-02-10 08:08:09*
674: response_types FAPI compliant (#330)

Issue: https://github.com/ForgeCloud/ob-deploy/issues/674
### GitHub [#332](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/332) Release/1.0.114
[4f505efc9528a6f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4f505efc9528a6f) Jorge Sanchez Perez *2021-02-10 08:48:02*
Release/1.0.114 (#332)

* Release candidate: prepare release 1.0.114

* Release candidate: prepare for next development iteration

* changelog
### GitHub [#333](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/333) Upgrade ui github actions
[22520a316018988](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/22520a316018988) Jorge Sanchez Perez *2021-02-11 11:12:59*
upgrade ui github actions reestricted only to PR, master merge and Release publish (#333)
### GitHub [#334](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/334) Release/1.0.115
[f96c0fa793c9224](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f96c0fa793c9224) Jorge Sanchez Perez *2021-02-11 14:57:24*
Release/1.0.115 (#334)

* Release candidate: prepare release 1.0.115

* Release candidate: prepare for next development iteration

* Improve github UI actions to get the version from release tag and maven pom file, to manage the versions in the same way that maven artifacts

* fix ui-release action

* Delete working directory fro checkout step to get the version from ./pom.xml file

* Fix action

* Fix build actions
### GitHub [#335](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/335) Release/1.0.116
[9e9e7b79999bf95](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9e9e7b79999bf95) Jorge Sanchez Perez *2021-02-11 15:33:29*
Release/1.0.116 (#335)

* Release candidate: prepare release 1.0.116

* Release candidate: prepare for next development iteration

* fix id prepare version ui action
### GitHub [#336](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/336) Update ui-release action
[c57a260e9499d03](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c57a260e9499d03) Jorge Sanchez Perez *2021-02-11 16:22:36*
Update ui-release action to update the release-published.json file from the root folder instead of from master-dev folder (#336)
### GitHub [#338](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/338) Fix push event to master
[8a133e20f858d88](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8a133e20f858d88) Jorge Sanchez Perez *2021-02-12 09:55:22*
Fix push event to master (#338)
### GitHub [#340](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/340) 327: Aggregated polling API scopes by version
[7dc2b64b7e8119f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7dc2b64b7e8119f) Jorge Sanchez Perez *2021-02-17 14:40:25*
327: Aggregated polling API scopes by version (#340)

- Update Aggregated polling scopes by version
- Bumped latest common version 1.0.89
Issue: https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/327
[481b2566a86d94f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/481b2566a86d94f) jorgesanchezperez *2021-02-17 15:19:50*
Release candidate: prepare release 1.1.117
[c1ddc763b46e6c9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c1ddc763b46e6c9) JamieB *2021-01-11 15:46:34*
635: Support OBWac and OBSeal Open Banking certs

This code allows registration with Open Banking Test Directory issued
OBWac and OBSeal certificates.

Issue: https://github.com/ForgeCloud/ob-deploy/issues/635
[92f3cb2843318b8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/92f3cb2843318b8) Matt Wills *2020-12-21 11:34:30*
Release candidate: prepare for next development iteration
## 1.0.110
### GitHub [#310](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/310) 13: International Payments flow error
[73af6a81dce87c1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/73af6a81dce87c1) Jorge Sanchez Perez *2020-12-09 09:52:37*
13: International Payments flow error (#310)

* 13: International Payments flow error
- ZD: 55780
- Add conditional control to manage null calculating exchange rates
- Added tests for optional fields
* Fix license
[0f311bc979e970d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0f311bc979e970d) Matt Wills *2020-12-21 11:34:21*
Release candidate: prepare release 1.0.110
[cab542300a690b8](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/cab542300a690b8) Matt Wills *2020-12-21 08:58:33*
344: Extend internal ID filtering

 - Extend AccountDataInternalIdFilter to filter at an object level, rather than at the API level
   Add filtering for product IDs

Issue: https://github.com/OpenBankingToolkit/openbanking-reference-implementation/issues/344
[713410ab33f16e4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/713410ab33f16e4) Matt Wills *2020-12-14 15:54:35*
Fix to enable "CreateFundsConfirmationConsent" to appear in Discovery endpoint

Issue: https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/15
[671c9af01b1a3d2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/671c9af01b1a3d2) Matt Wills *2020-11-06 14:24:30*
Release candidate: prepare for next development iteration
## 1.0.109
### GitHub [#288](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/288) Feature 238 - Events
[0d0f71bf542d439](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0d0f71bf542d439) Jorge Sanchez Perez *2020-10-12 14:52:04*
Feature 238 - Events (#288)

* Feature 238 - Events
- Fix Versioning of callbackURLs APIs
- versions: 3.0, 3.1, 3.1.1, 3.1.2, 3.1.3, 3.1.4, 3.1.5, 3.1.6

* Feature 238 - Events
- Common helper class
- Static method to filter callbackUrls by version
### GitHub [#289](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/289) Release/1.0.104
[ed93739073a4830](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ed93739073a4830) Jorge Sanchez Perez *2020-10-13 08:16:56*
Release/1.0.104 (#289)

* Release candidate: prepare release 1.0.104

* Release candidate: prepare for next development iteration

* changelog updated
### GitHub [#290](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/290) Part of issue 238 - Event notifications API
[c3f6074aa82a465](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c3f6074aa82a465) Jorge Sanchez Perez *2020-10-14 13:09:34*
Part of issue 238 - Event notifications API (#290)

- Fix Event susbcription API for all versions
- v3.1.2, v3.1.3, v3.1.4, v3.1.5, v3.1.6
- Fix Update Event Subscription API EntityResponse
### GitHub [#291](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/291) Release/1.0.5
[0d5e8da9a11d616](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0d5e8da9a11d616) Jorge Sanchez Perez *2020-10-14 14:47:37*
Release/1.0.5 (#291)

* Release candidate: prepare release 1.0.105

* Release candidate: prepare for next development iteration

* changelog updated

* change repository uk.maven.org to repo1.maven.org
### GitHub [#295](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/295) Feature 238 - Event Notifications API
[a88c553b7ae23a1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a88c553b7ae23a1) Jorge Sanchez Perez *2020-10-22 14:33:37*
Feature 238 - Event Notifications API (#295)

* Implementation of checks and validations
* Event Notifications utils
* Make Safe thread Event response util
* Generic Event Notifications api enhancements
### GitHub [#296](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/296) Release/1.0.106
[8e943d76bd519d9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/8e943d76bd519d9) Matt Wills *2020-11-06 12:33:43*
Fixed class names of legacy international "submission" documents (#296)
[bc8b0505430a540](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bc8b0505430a540) Matt Wills *2020-10-30 09:11:13*
Test commit on new machine (minor change) #296
[4c04b6c64543efb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4c04b6c64543efb) Jorge Sanchez Perez *2020-10-23 10:18:20*
Release/1.0.106 (#296)

* Release candidate: prepare release 1.0.106

* Release candidate: prepare for next development iteration

* changelog updated
[2041ada4ae95476](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2041ada4ae95476) Matt Wills *2020-10-21 09:51:59*
Migration of Events and Funds documents to v3.1.6 objects with new FR model domain (#296)
[5dc29d11646472e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5dc29d11646472e) Matt Wills *2020-10-20 13:42:20*
Migration of additional documents to simplified naming strategy, with new FR model objects (#296)
[d446ad3686aef5e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d446ad3686aef5e) Matt Wills *2020-10-19 10:38:21*
Restored account data migration. Renamed migration specific converters to xMigrator to avoid name conflicts and to make it clear they're only intended for migration (#296)
[695f72670736d36](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/695f72670736d36) Matt Wills *2020-10-15 09:37:29*
Fixed verifyRiskAndInitiation check (#296)
[4d1aafe7ee6f144](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4d1aafe7ee6f144) Matt Wills *2020-10-14 20:03:51*
Reverted FRAccountData and FRUserData back to OB objects (as these are passed back to the customer). Moved them out of the persistence package, since they are not saved in Mongo (#296).
[1dffc5e810b2f92](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1dffc5e810b2f92) Matt Wills *2020-10-14 08:04:42*
Removed redundant findByIdentification method to fix Spring wiring issue (#296)
[61d0a2236bb1e1a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/61d0a2236bb1e1a) Matt Wills *2020-10-13 15:16:01*
Replaced OBWriteInternationalConsentResponse6DataExchangeRateInformation with FRExchangeRateInformation within international document classes.
Made collections within FR domain objects plural (#296).
[2a4eed2bb99661b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2a4eed2bb99661b) Matt Wills *2020-10-07 09:57:55*
Accounts, Events and Funds confirmation refactoring (#296)

Stage 1: Removed previous version mongo documents and repositories. Switched to one set of FR documents and repositories (with no number prefix) (#296)

To do:
Stage 2: Either fix or remove CDR
Stage 3: Create new FR domain domains objects
Stage 4: Convert document classes to use new domain objects
Stage 5: Fix account, event and funds migration
[63c3e732e8d57a6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/63c3e732e8d57a6) Matt Wills *2020-10-07 09:44:11*
Added javadoc to FR domain objects (#296)
[5c3272baf991774](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5c3272baf991774) Matt Wills *2020-10-07 09:44:11*
Fixed idempotency checks and made logging more consistent (#296)
[4695f1f772077f9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4695f1f772077f9) Matt Wills *2020-10-07 09:44:11*
Tidied up currency conversion (#296)
[0d38bbf3db00f45](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0d38bbf3db00f45) Matt Wills *2020-10-07 09:44:11*
Rebased master. Bumped version of parent to include required changes from uk-datamodel. Removed unused method from DetachedJwsVerifier (#296)
[7c2e54213d1c60d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7c2e54213d1c60d) Matt Wills *2020-10-07 09:44:10*
FR mongo submission documents now using new FR submission objects, instead of OB model (#296)
[13b68b0a65d94b0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/13b68b0a65d94b0) Matt Wills *2020-10-07 09:44:10*
Moved accounts, events and funds FR documents under persistence package (#296)
[c22b492c588e5a7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c22b492c588e5a7) Matt Wills *2020-10-07 09:44:09*
Moved accounts, events and funds FR documents under persistence package (#296)
[2b02a773e708540](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2b02a773e708540) Matt Wills *2020-10-07 09:44:09*
Renamed and moved submission repositories and FR documents (#296)
[3dde9c0acdfc25a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3dde9c0acdfc25a) Matt Wills *2020-10-07 09:44:08*
Changed FR consent mongo document classes to use new FR model objects, instead of OB model. Fixes to ITs to follow (#296)
[309b9f0bdfb21b3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/309b9f0bdfb21b3) Matt Wills *2020-10-07 09:44:08*
Added domain objects for Submission based FR document classes (#296)
[1e6c0f3b555e68d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1e6c0f3b555e68d) Matt Wills *2020-10-07 09:44:08*
Fixed ITs (#296)
[0ac2d389d451ab0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0ac2d389d451ab0) Matt Wills *2020-10-07 09:44:08*
Removed redundant submission repositories and FR documents (#296)
### GitHub [#298](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/298) Feature/238 3 event notifications
[d7bb9446f0f1983](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d7bb9446f0f1983) Jorge Sanchez Perez *2020-10-27 08:43:12*
Feature/238 3 event notifications (#298)

* Event Notifications API - Feature 238
- improvements callback URL response
  - Response data empty with htpp code 200 when doesn't exist callback resource to return

* New integration test implemented for callbackurl 3.1.2

* update method name
[eba9782c348f4bb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/eba9782c348f4bb) Matt Wills *2020-11-02 16:00:41*
Updated version in package.json to 3.1.6-smiths-rc2 (#620)
[1d1dfda0670a225](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1d1dfda0670a225) Matt Wills *2020-11-06 14:23:16*
Release candidate: prepare release 1.0.109
[f39c936a3cab808](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f39c936a3cab808) JamieB *2020-11-02 15:31:53*
Addresses a PR comment

From this PR https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/300
[7e69af229f1e71e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7e69af229f1e71e) Matt Wills *2020-10-30 10:56:07*
Release candidate: prepare for next development iteration
[03aa1764ad272fd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/03aa1764ad272fd) JamieB *2020-10-28 15:31:53*
Release candidate: prepare for next development iteration
[dc9ee81b8f4aef9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/dc9ee81b8f4aef9) Matt Wills *2020-10-13 13:48:35*
Removed CDR poms (which were re-introduced after merging master)
[ff15efad0332659](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ff15efad0332659) Matt Wills *2020-10-13 13:27:08*
Added javadoc and moved EventsHelper
[d8e7b27d359bbdb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d8e7b27d359bbdb) Matt Wills *2020-10-13 12:21:24*
Multiple required changes to compile and run tests, after moving to new FR domain model
[467a046c17283ea](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/467a046c17283ea) Matt Wills *2020-10-13 11:09:01*
Added FR Event and Funds domain model for storing within mongo
[b671faa44d1fd32](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b671faa44d1fd32) Matt Wills *2020-10-08 09:16:23*
Added FR Account domain model for storing within mongo
Renamed some of the existing payment domain objects due to name clashes
[2392d90dee57361](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2392d90dee57361) Matt Wills *2020-10-07 11:16:36*
Removed CDR modules
[f41ead4b52c921f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f41ead4b52c921f) Matt Wills *2020-10-07 09:44:08*
Removed legacy payment consent repositories and their corresponding FR document classes. Removed redundant converters. Updated conversion methods within API controllers
[01e143ac8a5097f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/01e143ac8a5097f) JamieB *2020-10-07 09:43:22*
Release candidate: prepare for next development iteration

This reverts commit b198ec1ab8b95a1c75ebef72bfdf52c7c43b1ffe, reversing
changes made to 006af5906e56448d8e15b7c3f6043d35bf5ddf1e.
## 1.0.108
### GitHub [#296](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/296) Release/1.0.106
[7646f626054255c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7646f626054255c) Matt Wills *2020-10-30 10:44:44*
CHANGELOG.md update #296
[3fd6fa3250047a2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3fd6fa3250047a2) Matt Wills *2020-10-30 10:55:57*
Release candidate: prepare release 1.0.108
## 1.0.107
[d38b02082439ac4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d38b02082439ac4) JamieB *2020-10-28 15:31:44*
Release candidate: prepare release 1.0.107
[6e3353ad3843cf5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6e3353ad3843cf5) JamieB *2020-10-28 15:20:56*
Use non-snapshot version of openbanking-commons

Part of https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/12
[1687d23e9fba497](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1687d23e9fba497) JamieB *2020-10-28 08:06:51*
Uses TppRepository from commons

Part fix for https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/12
[98f89a3fb19e57e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/98f89a3fb19e57e) JamieB *2020-10-27 17:40:58*
Expects the jws iss to be {{orgId}}/{{softwareId}}

Part fix for https://github.com/OpenBankingToolkit/openbanking-toolkit/issues/12
## 1.0.103
### GitHub [#170](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/170) Bump forgerock-openbanking-starter-parent from 1.0.66 to 1.0.67
[7cf1703eed6fe35](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7cf1703eed6fe35) Matt Wills *2020-07-22 13:52:02*
Separated the reporting of the API versions, so that the read/write API and client registration API can be specified separately (#170)
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
### GitHub [#215](https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/215) Account data returned from 3.1 `/accounts` endpoint contains incorrect scheme name value
[1464ec7eb469be1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1464ec7eb469be1) Matt Wills *2020-07-02 10:05:25*
CHANGELOG.md (#215)
[6c7b595d9d754cc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6c7b595d9d754cc) Matt Wills *2020-07-02 09:29:19*
Fixed scheme name conversion (#215)
### GitHub [#216](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/216) New repositories and account data for v3.1.3 (#252)
[2ff0882134831a9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2ff0882134831a9) Matt Wills *2020-07-03 15:18:07*
v3.1.4 of the events and funds confirmation APIs (#216)
[a55b6e5d5128ddc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a55b6e5d5128ddc) Matt Wills *2020-07-03 13:20:11*
v3.1.4 of the events and funds confirmation APIs (#216)
[6c24c6a2b770614](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6c24c6a2b770614) Matt Wills *2020-07-03 11:10:57*
Added v3.1.3 of the events and funds confirmation APIs (#216)
[f1f0d38a300836f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f1f0d38a300836f) Matt Wills *2020-07-02 08:26:34*
Fixed version in 'self' links for v3.1.4 (#216)
[e00f885d81e9f3f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e00f885d81e9f3f) Matt Wills *2020-07-01 18:34:47*
API interfaces and controllers for v3.1.4 of accounts and payments (#216)
[6726d7a0f984360](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6726d7a0f984360) Matt Wills *2020-07-01 08:00:28*
Migrated to new repositories for 3.1.3 model objects. Populating dummy account data using new objects. Added several converter methods (#216)
[6c0ee34aeffe3e4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6c0ee34aeffe3e4) Matt Wills *2020-07-01 07:59:29*
Brought v3.1.3 of the payment API controllers in-line with account API ones. Removed unnecessary/duplicate annotations (e.g. swagger @ApiParam) and using constructor injection instead of autowired fields (#216)
[44dec8cf50dd414](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/44dec8cf50dd414) Matt Wills *2020-06-24 13:20:49*
Brought v3.1.3 of the payment API controllers in-line with account API ones. Removed unnecessary/duplicate annotations (e.g. swagger @ApiParam) and using constructor injection instead of autowired fields (#216)
[31df7829405d769](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/31df7829405d769) Matt Wills *2020-06-24 10:46:10*
Controller interfaces and implementations for v3.1.3 of the accounts API. New fields not yet being returned. (#216)
[0ee32fe1b6392e3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0ee32fe1b6392e3) Matt Wills *2020-06-18 15:32:18*
Bumped version of parent pom to pull in latest converters from uk-datamodel (#216)
[22e91f504289040](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/22e91f504289040) Matt Wills *2020-06-18 14:27:05*
Bumped version of parent pom to pull in latest converters from uk-datamodel (#216)
[2a5d119633adb1a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2a5d119633adb1a) Matt Wills *2020-06-18 13:56:30*
Added required version for versions prior to 3.1.3 in the rs-store (#216)
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
### GitHub [#219](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/219) Fixed version in &#39;self&#39; links for v3.1.4 (#216)
[fd08b064e6746e4](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fd08b064e6746e4) Matt Wills *2020-05-05 12:50:27*
Waiver 007 expiry - enabled detached JWT signature verification (#219)
### GitHub [#224](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/224) Bump ob-clients.version from 1.0.32 to 1.0.35
[5e63b9b0bf71cbc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5e63b9b0bf71cbc) dependabot-preview[bot] *2020-07-02 15:32:52*
Bump ob-clients.version from 1.0.32 to 1.0.35 (#224)

Bumps `ob-clients.version` from 1.0.32 to 1.0.35.

Updates `forgerock-openbanking-jwkms-client` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Updates `forgerock-openbanking-analytics-client` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>

Co-authored-by: dependabot-preview[bot] <27856297+dependabot-preview[bot]@users.noreply.github.com>
Co-authored-by: Jorge Sanchez Perez <54277573+jorgesanchezperez@users.noreply.github.com>
### GitHub [#232](https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/232) Version string in OIDC well-known endpoint should provide version of Dynamic Client Registration OB specification implemented
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
### GitHub [#233](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/233) Release/1.0.91
[0333770b7b00d05](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0333770b7b00d05) Jorge Sanchez Perez *2020-07-27 13:26:33*
Release/1.0.91 (#233)

* bumped the latest version of openbanking clients 1.0.36, jwkms 1.1.71 auth 1.0.59

* Release candidate: prepare release 1.0.91

* Release candidate: prepare for next development iteration

* changelog updated
### GitHub [#252](https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/252) Update Data APIs to use Version reference in the request mapping
[aa26278aac8b74d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/aa26278aac8b74d) Matt Wills *2020-06-24 15:05:13*
Added new repositories and 'FR' document classes for new objects introduced in v3.1.3 of the Accounts API (#252)
### GitHub [#258](https://github.com/OpenBankingToolkit/openbanking-aspsp/issues/258) Exception calling GET /v1.1/accounts
[fd8cf8c4f160154](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fd8cf8c4f160154) Matt Wills *2020-09-08 16:17:30*
Fix for conversion of scheme name (#258)
### GitHub [#262](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/262) Release/1.0.100
[cf9422ca60afade](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/cf9422ca60afade) Matt Wills *2020-07-07 13:41:26*
Prevented the inclusion of the b64 claim header if version is 3.1.4 or greater (#262)
### GitHub [#272](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/272) Bump forgerock-openbanking-auth from 1.0.59 to 1.0.60
[389c96e83405f2a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/389c96e83405f2a) Matt Wills *2020-08-26 14:43:24*
Validation of b64 header if version if 3.1.3 or lower. Moved generation and verification of detached JWS into separate classes (#272)
[39e8f371f8cbcf9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/39e8f371f8cbcf9) Matt Wills *2020-08-25 08:30:05*
v3.1.6 of aggregated polling, callback, events and funds confirmation (#272)
[c4583adf760e466](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c4583adf760e466) Matt Wills *2020-08-24 15:19:40*
Additional fixes for v3.1.6 of the accounts API. Avoided returning OBReadAccount6 which was causing an issue (I think because it extends HashMap?) (#272)
[c24cdd136135b99](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c24cdd136135b99) Matt Wills *2020-08-21 13:56:24*
Added controllers for v3.1.6 of the accounts API (#272)
[9d5b955a38c7632](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9d5b955a38c7632) Matt Wills *2020-08-21 07:53:28*
Added controllers for v3.1.6 of the payments API (#272)
[e3828a87603039c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e3828a87603039c) Matt Wills *2020-08-19 10:25:46*
Added v3.1.5 CHANGELOG.md (#272)
[ba7dbe22ac7c936](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ba7dbe22ac7c936) Matt Wills *2020-08-19 07:36:56*
Added v3.1.5 controllers of aggregated polling, callback API, event subscription and funds confirmation. Updated application.yml (#272)
[fe06fcf8d4c6cb6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fe06fcf8d4c6cb6) Matt Wills *2020-08-07 14:43:04*
Changes for v3.1.5 of accounts (#272)
[e7e43775d59e878](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e7e43775d59e878) Matt Wills *2020-08-06 11:12:16*
Added DebtorIdentification1 in v3.1.5 responses  (#272)
[982978849a240fa](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/982978849a240fa) Matt Wills *2020-08-05 16:19:47*
Switched to new converters within uk-datamodel. Applied a small number of fixes (#272)
[993af400372191d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/993af400372191d) Matt Wills *2020-08-04 10:14:39*
Added controllers and their (mostly) generated interfaces to the rs-store and rs-gateway modules for 3.1.5 of the payment API. Added new repository and FR documents for new model objects (#272)
[70514dd73e1bff1](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/70514dd73e1bff1) dependabot-preview[bot] *2020-08-03 19:26:53*
Bump forgerock-openbanking-starter-parent from 1.0.74 to 1.0.75

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.74 to 1.0.75.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/1.0.74...1.0.75)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>

Added new repositories and document classes for v3.1.5. Migrated controllers and services to new repositories (#272)
[0b001c4e8833477](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0b001c4e8833477) Matt Wills *2020-07-29 18:26:26*
Added controllers and their (mostly) generated interfaces to the rs-store and rs-gateway modules for 3.1.5 of the payment API. Added new repository and FR documents for new model objects (#272)
### GitHub [#279](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/279) Bump forgerock-openbanking-auth from 1.0.59 to 1.0.62
[c361d84fbffa314](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c361d84fbffa314) Matt Wills *2020-09-08 10:44:48*
Added mongobee ChangeLog classes to migrate OB model objects from v3.1.2 to v3.1.6 (#279)
[ba78368a08125fb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/ba78368a08125fb) Matt Wills *2020-09-08 10:33:43*
Release candidate: prepare for next development iteration

Added mongobee ChangeLog classes to migrate OB model objects from v3.1.2 to 3.1.6 (#279)
### GitHub [#280](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/280) Bump forgerock-openbanking-jwkms-embedded from 1.1.71 to 1.1.73
[183df0b0c098b2d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/183df0b0c098b2d) Matt Wills *2020-09-01 10:22:10*
Ensured detached JWS headers containing a b64 claim are rejected from 3.1.4 onwards (#280)
### GitHub [#282](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/282) Make release 1.0.101
[0893846c278ec7b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/0893846c278ec7b) Matt Wills *2020-09-10 09:50:16*
Fix for idempotency verification. Removed redundant repositories (#282)
### GitHub [#283](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/283) Feature/lbg/issue obdeploy 606
[81188506085341e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/81188506085341e) Jorge Sanchez Perez *2020-09-30 11:32:23*
Feature/lbg/issue obdeploy 606 (#283)

* Payment File Type - MediaType changed (Batch and Bulk)
- changed the mediaType to `text/plain` instead of `text/csv`

* badge release changed to github release
### GitHub [#284](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/284) Release/1.0.102
[d2c38ec9e36f729](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d2c38ec9e36f729) Jorge Sanchez Perez *2020-09-30 12:09:46*
Release/1.0.102 (#284)

* Release candidate: prepare release 1.0.102

* Release candidate: prepare for next development iteration

* changelog updated
### GitHub [#296](https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/296) Release/1.0.106
[aea2d305be072d7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/aea2d305be072d7) Matt Wills *2020-09-17 08:10:15*
New Payment consent FR model objects (#296)
[9f6afb8063341ee](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9f6afb8063341ee) Jorge Sanchez Perez *2020-04-24 17:06:53*
Release 1.0.79 (#181)

* [maven-release-plugin] prepare release forgerock-openbanking-aspsp-1.0.79

* [maven-release-plugin] prepare for next development iteration
[961727b0f223e9a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/961727b0f223e9a) dependabot-preview[bot] *2020-07-27 08:41:17*
Bump forgerock-openbanking-auth from 1.0.57 to 1.0.58

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.57 to 1.0.58.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.57...1.0.58)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[d183f414fc0641e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d183f414fc0641e) dependabot-preview[bot] *2020-05-20 08:04:22*
Bump forgerock-openbanking-auth from 1.0.55 to 1.0.57

Bumps [forgerock-openbanking-auth](https://github.com/OpenBankingToolkit/openbanking-auth) from 1.0.55 to 1.0.57.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-auth/releases)
- [Changelog](https://github.com/OpenBankingToolkit/openbanking-auth/blob/master/pom.xml.releaseBackup)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-auth/compare/forgerock-openbanking-starter-auth-1.0.55...forgerock-openbanking-starter-auth-1.0.57)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[5e63b9b0bf71cbc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/5e63b9b0bf71cbc) dependabot-preview[bot] *2020-07-02 15:32:52*
Bump ob-clients.version from 1.0.32 to 1.0.35 (#224)

Bumps `ob-clients.version` from 1.0.32 to 1.0.35.

Updates `forgerock-openbanking-jwkms-client` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Updates `forgerock-openbanking-analytics-client` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.32 to 1.0.35
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/forgerock-openbanking-reference-implementation-clients-1.0.32...1.0.35)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>

Co-authored-by: dependabot-preview[bot] <27856297+dependabot-preview[bot]@users.noreply.github.com>
Co-authored-by: Jorge Sanchez Perez <54277573+jorgesanchezperez@users.noreply.github.com>
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
[c493e48047f8f0f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c493e48047f8f0f) dependabot-preview[bot] *2020-07-02 15:01:06*
Bump ob-common.version from 1.0.76 to 1.0.77

Bumps `ob-common.version` from 1.0.76 to 1.0.77.

Updates `forgerock-openbanking-model` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

Updates `forgerock-openbanking-am` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

Updates `forgerock-openbanking-jwt` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

Updates `forgerock-openbanking-oidc` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

Updates `forgerock-openbanking-upgrade` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

Updates `forgerock-openbanking-ssl` from 1.0.76 to 1.0.77
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-common/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-common/compare/forgerock-openbanking-starter-commons-1.0.76...1.0.77)

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
[522b942951e70d3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/522b942951e70d3) dependabot-preview[bot] *2020-07-02 15:16:30*
Bump forgerock-openbanking-jwkms-embedded from 1.1.67 to 1.1.70

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.67 to 1.1.70.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/forgerock-openbanking-reference-implementation-jwkms-1.1.67...1.1.70)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[2fa17af225eb155](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2fa17af225eb155) JamieB *2020-09-23 14:11:38*
Updated ui project version to updating-ui-version-to-3.1.6-rem-rc1
[f3b878ca0944044](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f3b878ca0944044) JamieB *2020-05-15 09:52:09*
Updated ui project version to updating-ui-version-to-3.1.2-queen-rc7
[3430f68c3636578](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/3430f68c3636578) JamieB *2020-10-06 09:34:36*
Release candidate: prepare release 1.0.103
[1a001bf7f085d96](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1a001bf7f085d96) JamieB *2020-10-06 09:24:52*
Fix missing analytics entries use 1.0.41 of ob clients

Use latests versions of ob libs that use version 1.0.41 of openbanking
clients which contains a fix

Part fix for https://github.com/OpenBankingToolkit/openbanking-analytics/issues/163
[62ebb09658028a3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/62ebb09658028a3) dependabot-preview[bot] *2020-10-05 08:59:14*
Bump forgerock-openbanking-jwkms-embedded from 1.1.73 to 1.1.74

Bumps [forgerock-openbanking-jwkms-embedded](https://github.com/OpenBankingToolkit/openbanking-jwkms) from 1.1.73 to 1.1.74.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-jwkms/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-jwkms/compare/1.1.73...1.1.74)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[43bb526864e027b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/43bb526864e027b) JamieB *2020-09-29 09:37:28*
Release candidate: prepare for next development iteration
[b43e7382a8f9c7d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/b43e7382a8f9c7d) JamieB *2020-09-29 08:56:28*
Improved error when incorrect type is used in b64 jws header
[f2aec21fa339ddd](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f2aec21fa339ddd) JamieB *2020-09-29 08:39:03*
Remove duplicate test
[e99722e3becea3d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e99722e3becea3d) JamieB *2020-09-29 08:27:14*
Remove unused import
[01e82666a58b8af](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/01e82666a58b8af) JamieB *2020-09-29 08:10:06*
More refactoring and addressing of PR comments
[92126b6c2976f10](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/92126b6c2976f10) JamieB *2020-09-28 15:54:13*
Refactor and Optimise DetachedJwsVerifier

Fix bad format in debug string
Refactoring of DetachedJwsVerifier
Optimisation of DetachedJwsVerifier
Removed needless reconstruction of full jws in methods just
manipulating/using the header
Made newly added test work like other tests
Add missing javadoc
Makes ClaimsUtils naming more explicit - Now called JwsClaimsUtils
[2dac7dd056b5484](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2dac7dd056b5484) JamieB *2020-09-28 14:00:01*
Address PR comments

https://github.com/OpenBankingToolkit/openbanking-aspsp/pull/281/
[97e8718e4238aaf](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/97e8718e4238aaf) JamieB *2020-09-28 13:07:10*
Use nimbusds shaded minidev library

Latest nimbusds is packaged with a shaded version of minidev. aspsp used
to have minidev as a package, the version of which was controlled in the
openbanking-parent pom. We now need to remove that dependency and
changes our code to use classes directly from the shaded nimbusds
libary.
[68fd47483d0596b](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/68fd47483d0596b) dependabot-preview[bot] *2020-09-28 08:45:21*
Bump ob-clients.version from 1.0.36 to 1.0.40

Bumps `ob-clients.version` from 1.0.36 to 1.0.40.

Updates `forgerock-openbanking-jwkms-client` from 1.0.36 to 1.0.40
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/1.0.36...1.0.40)

Updates `forgerock-openbanking-analytics-client` from 1.0.36 to 1.0.40
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/1.0.36...1.0.40)

Updates `forgerock-openbanking-analytics-webclient` from 1.0.36 to 1.0.40
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-clients/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-clients/compare/1.0.36...1.0.40)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[86ee5a444b8ba34](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/86ee5a444b8ba34) Matt Wills *2020-09-10 10:50:20*
Release candidate: prepare for next development iteration
[450d679943400e6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/450d679943400e6) Matt Wills *2020-09-09 10:04:02*
Release candidate: prepare for next development iteration
[4ebbaeac1b213f0](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4ebbaeac1b213f0) Matt Wills *2020-09-08 11:01:17*
Release candidate: prepare for next development iteration
[e66b49e5c8c8eb5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e66b49e5c8c8eb5) Matt Wills *2020-09-01 14:31:13*
Release candidate: prepare for next development iteration
[d154dbe73395342](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d154dbe73395342) Matt Wills *2020-08-25 10:46:22*
Release candidate: prepare for next development iteration
[617d29c81c8f22f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/617d29c81c8f22f) dependabot-preview[bot] *2020-08-24 09:28:12*
Bump forgerock-openbanking-starter-parent from 1.0.77 to 1.0.78

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.77 to 1.0.78.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/1.0.77...1.0.78)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[2ea9ea313b934ab](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2ea9ea313b934ab) Matt Wills *2020-08-19 10:25:03*
Release candidate: prepare for next development iteration
[7ec863b4bcd153c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7ec863b4bcd153c) Matt Wills *2020-08-17 13:20:53*
Release candidate: prepare for next development iteration
[75b0b9339586ae7](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/75b0b9339586ae7) Matt Wills *2020-08-07 14:03:25*
Release candidate: prepare for next development iteration
[bda0abf628cc814](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bda0abf628cc814) Matt Wills *2020-08-06 11:29:16*
Release candidate: prepare for next development iteration
[1789644675d4e15](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1789644675d4e15) Matt Wills *2020-08-05 17:10:41*
Release candidate: prepare for next development iteration
[fd03ab6808ccdc2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/fd03ab6808ccdc2) dependabot-preview[bot] *2020-08-03 09:22:23*
Bump forgerock-openbanking-starter-parent from 1.0.74 to 1.0.75

Bumps [forgerock-openbanking-starter-parent](https://github.com/OpenBankingToolkit/openbanking-parent) from 1.0.74 to 1.0.75.
- [Release notes](https://github.com/OpenBankingToolkit/openbanking-parent/releases)
- [Commits](https://github.com/OpenBankingToolkit/openbanking-parent/compare/1.0.74...1.0.75)

Signed-off-by: dependabot-preview[bot] <support@dependabot.com>
[83395b4746927a3](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/83395b4746927a3) Matt Wills *2020-07-08 13:13:56*
Release candidate: prepare for next development iteration
[bcc00fb83dee872](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/bcc00fb83dee872) Matt Wills *2020-07-03 15:18:07*
Release candidate: prepare release 1.0.89
[a37d624a020e763](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a37d624a020e763) Matt Wills *2020-07-03 15:18:07*
Release candidate: prepare for next development iteration
[c49ad84133a90bb](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/c49ad84133a90bb) Matt Wills *2020-07-02 09:59:37*
Release candidate: prepare for next development iteration
[9fe3ef91242428d](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/9fe3ef91242428d) Matt Wills *2020-07-01 08:17:33*
Release candidate: prepare for next development iteration
[499a9be21344a26](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/499a9be21344a26) Matt Wills *2020-06-18 15:25:44*
Release candidate: prepare for next development iteration
[861981393939c0a](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/861981393939c0a) JamieB *2020-06-15 11:27:45*
Release candidate: prepare for next development iteration
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
## 1.0.101
[00bf3b045b6951c](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/00bf3b045b6951c) JamieB *2020-09-29 09:37:20*
Release candidate: prepare release 1.0.101
[d2a5578aae14713](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/d2a5578aae14713) JamieB *2020-09-29 09:28:26*
Use latest openbanking-starter-parent 1.0.83
## 1.0.100
[58d5f5075306aaa](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/58d5f5075306aaa) Matt Wills *2020-09-10 10:43:41*
Release candidate: prepare release 1.0.100
## 1.0.99
[40c050ffb3d641f](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/40c050ffb3d641f) Matt Wills *2020-09-09 10:03:52*
Release candidate: prepare release 1.0.99
## 1.0.98
[4b05133ce427990](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/4b05133ce427990) Matt Wills *2020-09-08 11:01:07*
Release candidate: prepare release 1.0.98
## 1.0.97
[e1243b3125b9dfc](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/e1243b3125b9dfc) Matt Wills *2020-09-01 14:31:03*
Release candidate: prepare release 1.0.97
## 1.0.96
[f34ea494074644e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f34ea494074644e) Matt Wills *2020-08-25 10:46:13*
Release candidate: prepare release 1.0.96
## 1.0.95
[2fb6815233db8c6](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/2fb6815233db8c6) Matt Wills *2020-08-19 10:24:54*
Release candidate: prepare release 1.0.95
## 1.0.94
[f0e96ca20c43849](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/f0e96ca20c43849) Matt Wills *2020-08-17 13:20:43*
Release candidate: prepare release 1.0.94
## 1.0.93
[1b222323cfa28ed](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/1b222323cfa28ed) Matt Wills *2020-08-06 11:29:06*
Release candidate: prepare release 1.0.93
## 1.0.92
[7b4e262fb1cfa7e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/7b4e262fb1cfa7e) Matt Wills *2020-08-05 17:10:32*
Release candidate: prepare release 1.0.92
## 1.0.90
[6463452fa35f11e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/6463452fa35f11e) Matt Wills *2020-07-08 13:13:44*
Release candidate: prepare release 1.0.90
## 1.0.88
[a00bb425f9e4236](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/a00bb425f9e4236) Matt Wills *2020-07-02 09:59:27*
Release candidate: prepare release 1.0.88
## 1.0.87
[090f7754aa6ece2](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/090f7754aa6ece2) Matt Wills *2020-07-01 08:17:22*
Release candidate: prepare release 1.0.87
## 1.0.85
[30e9a9a542dd0d5](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/30e9a9a542dd0d5) Matt Wills *2020-06-18 15:25:33*
Release candidate: prepare release 1.0.85
## 1.0.83
[553440e6b90bbf9](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/553440e6b90bbf9) JamieB *2020-06-15 11:27:36*
Release candidate: prepare release 1.0.83
## 1.0.82
[162d7fe6da5c45e](https://github.com/OpenBankingToolkit/openbanking-aspsp/commit/162d7fe6da5c45e) Matt Wills *2020-06-15 09:11:46*
Release candidate: prepare release 1.0.82
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
