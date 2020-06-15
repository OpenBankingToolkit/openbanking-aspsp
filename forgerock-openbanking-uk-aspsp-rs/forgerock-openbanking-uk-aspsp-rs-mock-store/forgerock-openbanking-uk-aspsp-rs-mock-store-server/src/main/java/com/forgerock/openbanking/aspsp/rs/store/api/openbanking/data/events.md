# Data Events

## Objects

### FRDataEvent

| Field | Description | Class | Occurrence |
| --- | --- | --- | --- |
| tppId | The tpp user name, user for import, update and export | Max 128 text | 1..1 |
| jti | JWT ID to export and update | Max 128 text | 0..1 |  
| events | Event to import and update | OBEventNotification2 Object | 0..1 |

### OBEventNotification2

| Field | Description | Class | Occurrence |
| --- | --- | --- | --- |
| iat | Issued at | int | 1..1 |
| jti | JWT ID | Max 128 text | 1..1 |
| sub | Subject | anyURI | 1..1 |
| aud | Audience | Max 128 text | 1..1 |
| txn | Transaction Identifier | Max 128 text | 1..1 |
| toe | Time of event | int | 1..1 |
| events | Collection of events | OBEvent | 1..1 |

## Context 
- GET `/api/data/events`: export event by tppId
- GET `/api/data/events/all`: export all events
- POST `/api/data/events`: import events
- PUT `/api/data/events`: update events

## Request Examples
### GET `/api/data/events` export events by tppId
> The field `tppId` is mandatory.
```json
{ "tppId": "3ffb98cc-be98-4b10-a405-bde41e88c2c7"}
```
### GET `/api/data/events/all` export all events
- EMPTY Body

### POST `/api/data/events` import events by tppId
> The field `tppId` is mandatory.

> The Array field `events` cannot be empty.
```json
{ 
"tppId": "3ffb98cc-be98-4b10-a405-bde41e88c2c7",
"events":
  [
    {
      "iss": "https://examplebank.com/",
      "iat": "1516239022",
      "jti": "b460a07c-4962-43d1-85ee-9dc10fbb8f6c",
      "sub": "https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/pmt-7290-003",
      "aud": "7umx5nTR33811QyQfi",
      "txn": "dfc51628-3479-4b81-ad60-210b43d02306",
      "toe": "1516239022",
      "events": {
        "urn:uk:org:openbanking:events:resource-update": {
          "subject": {
            "subject_type": "http://openbanking.org.uk/rid_http://openbanking.org.uk/rty",
            "http://openbanking.org.uk/rid": "pmt-7290-003",
            "http://openbanking.org.uk/rty": "domestic-payment",
            "http://openbanking.org.uk/rlk": [
              {
                "version": "v3.0",
                "link": "https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/pmt-7290-003"
              },
              {
                "version": "v1.1",
                "link": "https://examplebank.com/api/open-banking/v1.1/payment-submissions/pmt-7290-003"
              }
            ]
          }
        }
      }
    }
  ]
}
```
### PUT `/api/data/events` update events by tppId
> The field `tppId` is mandatory.

> The field `events[].jti` must be an existing jti.
```json
{ 
"tppId": "3ffb98cc-be98-4b10-a405-bde41e88c2c7",
"events":
  [
    {
      "iss": "https://examplebank.com/",
      "iat": "1516239022",
      "jti": "b460a07c-4962-43d1-85ee-9dc10fbb8f6c",
      "sub": "https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/pmt-7290-003",
      "aud": "7umx5nTR33811QyQfi",
      "txn": "dfc51628-3479-4b81-ad60-210b43d02306",
      "toe": "1516239022",
      "events": {
        "urn:uk:org:openbanking:events:resource-update": {
          "subject": {
            "subject_type": "http://openbanking.org.uk/rid_http://openbanking.org.uk/rty",
            "http://openbanking.org.uk/rid": "pmt-7290-003",
            "http://openbanking.org.uk/rty": "domestic-payment",
            "http://openbanking.org.uk/rlk": [
              {
                "version": "v3.0",
                "link": "https://examplebank.com/api/open-banking/v3.0/pisp/domestic-payments/pmt-7290-003"
              },
              {
                "version": "v1.1",
                "link": "https://examplebank.com/api/open-banking/v1.1/payment-submissions/pmt-7290-003"
              }
            ]
          }
        }
      }
    }
  ]
}
```
## The Swagger Specification for Event
[read write api specs swagger](https://github.com/OpenBankingUK/read-write-api-specs)

```json
{
  "swagger": "2.0",
  "info": {
    "title": "Event Notification API Specification - TPP Endpoints",
    "description": "Swagger for Event Notification API Specification - TPP Endpoints",
    "termsOfService": "https://www.openbanking.org.uk/terms",
    "contact": {
      "name": "Service Desk",
      "email": "ServiceDesk@openbanking.org.uk"
    },
    "license": {
      "name": "open-licence",
      "url": "https://www.openbanking.org.uk/open-licence"
    },
    "version": "v3.0.0"
  },
  "basePath": "/open-banking/v3.0",
  "schemes": [
    "https"
  ],
  "paths": {
    "/event-notifications": {
      "post": {
        "summary": "Send an event notification",
        "operationId": "CreateEventNotification",
        "consumes": [
          "application/jwt"
        ],
        "tags": [
          "Event Notification"
        ],
        "parameters": [
          {
            "$ref": "#/parameters/OBEventNotification1Param"
          },
          {
            "$ref": "#/parameters/x-fapi-financial-id-Param"
          },
          {
            "$ref": "#/parameters/x-fapi-interaction-id-Param"
          }
        ],
        "responses": {
          "202": {
            "description": "Accepted"
          }
        }
      }
    }
  },
  "parameters": {
    "OBEventNotification1Param": {
      "in": "body",
      "name": "OBEventNotification1Param",
      "description": "Create an Callback URI",
      "required": true,
      "schema": {
        "type": "string",
        "format": "base64"
      }
    },
    "x-fapi-financial-id-Param": {
      "in": "header",
      "name": "x-fapi-financial-id",
      "type": "string",
      "required": true,
      "description": "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB."
    },
    "x-fapi-interaction-id-Param": {
      "in": "header",
      "name": "x-fapi-interaction-id",
      "type": "string",
      "required": false,
      "description": "An RFC4122 UID used as a correlation id."
    }
  },
  "securityDefinitions": {
    "TPPOAuth2Security": {
      "type": "oauth2",
      "flow": "application",
      "tokenUrl": "https://authserver.example/token",
      "scopes": {
        "accounts": "Ability to read Accounts information",
        "fundsconfirmation": "Ability to confirm funds",
        "payments": "Generic payment scope"
      },
      "description": "TPP client credential authorisation flow with the ASPSP"
    }
  },
  "definitions": {
    "OBEvent1": {
      "description": "Events.",
      "type": "object",
      "properties": {
        "urn:uk:org:openbanking:events:resource-update": {
          "$ref": "#/definitions/OBEventResourceUpdate1"
        }
      },
      "required": [
        "urn:uk:org:openbanking:events:resource-update"
      ],
      "additionalProperties": false
    },
    "OBEventLink1": {
      "description": "Resource links to other available versions of the resource.",
      "type": "object",
      "properties": {
        "version": {
          "description": "Resource version.",
          "type": "string",
          "minLength": 1,
          "maxLength": 10
        },
        "link": {
          "description": "Resource link.",
          "type": "string"
        }
      },
      "required": [
        "version",
        "link"
      ],
      "additionalProperties": false,
      "minProperties": 1
    },
    "OBEventNotification1": {
      "description": "The resource-update event.",
      "type": "object",
      "properties": {
        "iss": {
          "description": "Issuer.",
          "type": "string"
        },
        "iat": {
          "description": "Issued At. ",
          "type": "string",
          "minLength": 1,
          "maxLength": 10,
          "pattern": "[0-9]{1,10}"
        },
        "jti": {
          "description": "JWT ID.",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "aud": {
          "description": "Audience.",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "sub": {
          "description": "Subject",
          "type": "string",
          "format": "uri"
        },
        "txn": {
          "description": "Transaction Identifier.",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "toe": {
          "description": "Time of Event.",
          "type": "string",
          "minLength": 1,
          "maxLength": 10,
          "pattern": "[0-9]{1,10}"
        },
        "events": {
          "$ref": "#/definitions/OBEvent1"
        }
      },
      "required": [
        "iss",
        "iat",
        "jti",
        "aud",
        "sub",
        "txn",
        "toe",
        "events"
      ],
      "additionalProperties": false
    },
    "OBEventResourceUpdate1": {
      "description": "Resource-Update Event.",
      "type": "object",
      "properties": {
        "subject": {
          "$ref": "#/definitions/OBEventSubject1"
        }
      },
      "required": [
        "subject"
      ],
      "additionalProperties": false
    },
    "OBEventSubject1": {
      "description": "The resource-update event.",
      "type": "object",
      "properties": {
        "subject_type": {
          "description": "Subject type for the updated resource. ",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "http://openbanking.org.uk/rid": {
          "description": "Resource Id for the updated resource.",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "http://openbanking.org.uk/rty": {
          "description": "Resource Type for the updated resource.",
          "type": "string",
          "minLength": 1,
          "maxLength": 128
        },
        "http://openbanking.org.uk/rlk": {
          "items": {
            "$ref": "#/definitions/OBEventLink1"
          },
          "type": "array",
          "description": "Resource links to other available versions of the resource.",
          "minItems": 1
        }
      },
      "required": [
        "subject_type",
        "http://openbanking.org.uk/rid",
        "http://openbanking.org.uk/rty",
        "http://openbanking.org.uk/rlk"
      ],
      "additionalProperties": false
    }
  }
}
```