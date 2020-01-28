# RS Mock Store
The RS mock store is an example bank integration. It's a demonstration of how to integrate the RS gateway with a bank.

In our example we store and retrieve bank data from mongo. In an existing ASPSP or bank environment it's likely the RS store
will act as a integration between the OpenBanking Toolkit and the internal bank APIs.

## Getting started
To satisfy the contract RS gateway expects you'll need to implement the integration to your bank. To make this easier
we have a set of OpenAPI specifications that forms the contract between RS gateway and your bank integration service. We
recommend that you use swagger-codegen to generate the server code. [generate-bank-resource-server.sh](./generate-bank-resource-server.sh) is an
example of how you can do this using spring MVC.