[<img src="https://raw.githubusercontent.com/ForgeRock/forgerock-logo-dev/master/forgerock-logo-dev.png" align="right" width="220px"/>](https://developer.forgerock.com/)

ForgeRock OpenBanking ASPSP
========================

A set of micro-services to help you build an ASPSP layer in front of your existing bank system.
We also provide a mock of the bank backend, which turns up to be very handy for testing, sandboxes or even help you u
understand the necessary integration with your current backend.

Our ForgeRock ASPSP capacity is compose of the following microservices:
* Open Banking gateway
    * AS
    * RS
* Open Banking Remote Consent Services (RCS)
* Manual on-boarding
* Mock Bank portal
* Mock RS store
* Mock RS payment simulation


# Services

## Open Banking Gateway

### AS

You need to have a layer in front of the authorisation server, to make it Open Banking UK compliant.
It is mainly around registration, which requires to allow different source of trust (EIDAS and OBIE directory).

This AS gateway is designed to be integrated in front of ForgeRock AM. You may be able to integrate in front of other
OIDC provider but this certainly requires some twiking.

Features:
* Open Banking dynamic registration
* EIDAS onboarding
* Manual on-boarding (design as a layer in front of the dynamic registration)
* Formatting of the AS errors in the Open Banking format
* Add interaction IDs
* Layer in front of the authorisation endpoints to allow app to app
* Add FAPI headers


### RS

You resource server needs to have some very specific Open Banking security features. It's not just about verifying
the access token (see FAPI standard)

This RS gateway does all the heavy lifting in term of security and provides you a simple integration with your RS backend.
It basically act as a layer in front of the Open Banking APIs, and populate extra headers with the information needed.

Example:
1. A TPP call /transactions
1. The RS gateway will do all the security check
1. The RS gateway will extract from the consent the PSU accounts IDs 
1. The RS will call your backend with the same APIs but will add a new header 'x-ob-account-ids' with the list of account IDs

What does it means for your backend? You can consider all the security check done, and you can trust all the parameters (GET and headers) to be correct
and just return the data requested.
The RS gateway will even override the requested 'from' and 'to' dates with what the PSU has authorised. All you need to do, is returning the data
requested in the Open Banking format and triggering existing bank task like payments.

To simplify your integration work, we provide you a new swagger. This is basically the Open Banking swagger but with all the extra headers that you will
receive from the RS gateway.

## Remote Consent Services (RCS)

Open Banking consent is way richer than a usual OIDC consent. This is way the remote consent services comes in play: It allows the AS
to delegate the consent to a remote trusted party, which we call RCS.
The scope of this micro-service is to handle the consent for the AS, and return a 'true/false' decision back to it.
This RCS is designed for Open Banking and will therefore, present consent following the Open Banking UX guideline.
The UI we provide are here as sample, to show you how the backend works and how the flow is working (from a UI point of view).
You can still use the default UI we provide for your testing environment or even your sandbox

## Manual on-boarding

You may want to offer a manual onboarding flow to your TPPs. This microservices is a layer in front of the AS-Gateway, which offers a UI flow to on-board.
You can offer different login method, like OBIE directory login, eidas or even a custom ForgeRock directory login. It's
actually calling an AM auth tree, that you got fully control over.

## Mock Bank portal

The bank portal is the backend of the bank portal UI. It offers an authentication and user profile management.
In the future, we would offer a full management of the PSU accounts. It will be a way to customise the Open Banking data for a testing purposes.
 
## Mock RS store

The RS store is mocking your bank backend. It does use a mongo database to store the PSU data in the latest Open Banking format.
You can use directly the Mock RS store for your dev environment and sandbox. You can also use it to introspect the integration with the 
rest of the eco-system and copy-paste the logic to your real backend.
One other way to use the Mock RS store is to use it as a project template: You modify the RS store to actually call your backend, which can be in a different data-model than Open Banking.

## Mock RS payment simulation

The RS payment simulation is for development or sandbox purposes. It allows a fake environment to still have a payment simulation.
It does sent event to the TPP, you can therefore copy its behaviour for your payment system.
You can do like the RS store and use it as a project template: It will listen to your current backend payment and will handle the event notification
in an Open Banking standard way.