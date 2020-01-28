export const permissionMocks = {
  ReadAccountsBasic: {
    AccountId: '22289',
    Currency: 'GBP',
    AccountType: 'Personal',
    AccountSubType: 'CurrentAccount',
    Nickname: 'Bills'
  },
  ReadAccountsDetail: {
    AccountId: '22289',
    Currency: 'GBP',
    AccountType: 'Personal',
    AccountSubType: 'CurrentAccount',
    Nickname: 'Bills',
    Account: {
      SchemeName: 'SortCodeAccountNumber',
      Identification: '80200110203345',
      Name: 'Mr Kevin',
      SecondaryIdentification: '00021'
    }
  },
  ReadBalances: {
    AccountId: '22289',
    Amount: {
      Amount: '1230.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Credit',
    Type: 'InterimAvailable',
    DateTime: '2017-04-05T10:43:07+00:00',
    CreditLine: [
      {
        Included: true,
        Amount: {
          Amount: '1000.00',
          Currency: 'GBP'
        },
        Type: 'Pre-Agreed'
      }
    ]
  },
  ReadBeneficiariesBasic: {
    AccountId: '22289',
    BeneficiaryId: 'Ben1',
    Reference: 'Towbar Club'
  },
  ReadBeneficiariesDetail: {
    AccountId: '22289',
    BeneficiaryId: 'Ben1',
    Reference: 'Towbar Club',
    CreditorAccount: {
      SchemeName: 'SortCodeAccountNumber',
      Identification: '80200112345678',
      Name: 'Mrs Juniper'
    }
  },
  ReadDirectDebits: {
    AccountId: '22289',
    DirectDebitId: 'DD03',
    MandateIdentification: 'Caravanners',
    DirectDebitStatusCode: 'Active',
    Name: 'Towbar Club 3 - We Love Towbars',
    PreviousPaymentDateTime: '2017-04-05T10:43:07+00:00',
    PreviousPaymentAmount: {
      Amount: '0.57',
      Currency: 'GBP'
    }
  },
  ReadOffers: {
    AccountId: '22289',
    OfferId: 'Offer1',
    OfferType: 'LimitIncrease',
    Description: 'Credit limit increase for the account up to £10000.00',
    Amount: {
      Amount: '10000.00',
      Currency: 'GBP'
    }
  },
  ReadPAN: {
    Party: {
      PartyId: 'PABC123',
      PartyType: 'Sole',
      Name: 'Semiotec',
      Email: 'contact@semiotec.co.jp',
      Address: {
        AddressType: 'Business',
        StreetName: 'Street',
        BuildingNumber: '15',
        PostCode: 'NW1 1AB',
        TownName: 'London'
      }
    }
  },
  ReadParty: {
    Party: {
      PartyId: 'PABC123',
      PartyType: 'Sole',
      Name: 'Semiotec',
      Email: 'contact@semiotec.co.jp',
      Address: {
        AddressType: 'Business',
        StreetName: 'Street',
        BuildingNumber: '15',
        PostCode: 'NW1 1AB',
        TownName: 'London'
      }
    }
  },
  ReadPartyPSU: {
    AccountId: '22289',
    ProductId: '51B',
    ProductType: 'PersonalCurrentAccount',
    ProductName: '321 Product',
    PCA: {}
  },
  ReadProducts: {
    AccountId: '22289',
    ProductId: '51B',
    ProductType: 'PersonalCurrentAccount',
    ProductName: '321 Product',
    PCA: {}
  },
  ReadScheduledPaymentsBasic: {
    AccountId: '22289',
    ScheduledPaymentId: 'SP03',
    ScheduledPaymentDateTime: '2017-05-05T00:00:00+00:00',
    ScheduledType: 'Execution',
    InstructedAmount: {
      Amount: '10.00',
      Currency: 'GBP'
    }
  },
  ReadScheduledPaymentsDetail: {
    AccountId: '22289',
    ScheduledPaymentId: 'SP03',
    ScheduledPaymentDateTime: '2017-05-05T00:00:00+00:00',
    ScheduledType: 'Execution',
    InstructedAmount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditorAccount: {
      SchemeName: 'SortCodeAccountNumber',
      Identification: '23605490179017',
      Name: 'Mr Tee'
    }
  },
  ReadStandingOrdersBasic: {
    AccountId: '22289',
    StandingOrderId: 'Ben3',
    Frequency: 'EvryWorkgDay',
    Reference: 'Towbar Club 2 - We Love Towbars',
    FirstPaymentDateTime: '2017-08-12T00:00:00+00:00',
    FirstPaymentAmount: {
      Amount: '0.57',
      Currency: 'GBP'
    },
    NextPaymentDateTime: '2017-08-13T00:00:00+00:00',
    NextPaymentAmount: {
      Amount: '0.56',
      Currency: 'GBP'
    },
    FinalPaymentDateTime: '2027-08-12T00:00:00+00:00',
    FinalPaymentAmount: {
      Amount: '0.56',
      Currency: 'GBP'
    },
    StandingOrderStatusCode: 'Active'
  },
  ReadStandingOrdersDetail: {
    AccountId: '22289',
    StandingOrderId: 'Ben3',
    Frequency: 'EvryWorkgDay',
    Reference: 'Towbar Club 2 - We Love Towbars',
    FirstPaymentDateTime: '2017-08-12T00:00:00+00:00',
    FirstPaymentAmount: {
      Amount: '0.57',
      Currency: 'GBP'
    },
    NextPaymentDateTime: '2017-08-13T00:00:00+00:00',
    NextPaymentAmount: {
      Amount: '0.56',
      Currency: 'GBP'
    },
    FinalPaymentDateTime: '2027-08-12T00:00:00+00:00',
    FinalPaymentAmount: {
      Amount: '0.56',
      Currency: 'GBP'
    },
    StandingOrderStatusCode: 'Active',
    CreditorAccount: {
      SchemeName: 'SortCodeAccountNumber',
      Identification: '80200112345678',
      Name: 'Mrs Juniper'
    }
  },
  ReadStatementsBasic: {
    AccountId: '32389',
    StatementId: '9034ee-4ewa4e-342er6',
    StatementReference: '002',
    Type: 'RegularPeriodic',
    StartDateTime: '2017-09-01T00:00:00+00:00',
    EndDateTime: '2017-09-30T23:59:59+00:00',
    CreationDateTime: '2017-10-01T00:00:00+00:00',
    StatementAmount: [
      {
        Amount: {
          Amount: '4060.00',
          Currency: 'GBP'
        },
        CreditDebitIndicator: 'Credit',
        Type: 'PreviousClosingBalance'
      }
    ]
  },
  ReadStatementsDetail: {
    AccountId: '32389',
    StatementId: '9034ee-4ewa4e-342er6',
    StatementReference: '002',
    Type: 'RegularPeriodic',
    StartDateTime: '2017-09-01T00:00:00+00:00',
    EndDateTime: '2017-09-30T23:59:59+00:00',
    CreationDateTime: '2017-10-01T00:00:00+00:00',
    StatementAmount: [
      {
        Amount: {
          Amount: '2700.00',
          Currency: 'GBP'
        },
        CreditDebitIndicator: 'Credit',
        Type: 'ClosingBalance'
      },
      {
        Amount: {
          Amount: '4060.00',
          Currency: 'GBP'
        },
        CreditDebitIndicator: 'Credit',
        Type: 'PreviousClosingBalance'
      }
    ]
  },
  ReadTransactionsCredits: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Credit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    }
  },
  ReadTransactionsDebits: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Debit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    }
  },
  ReadTransactionsBasic: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Credit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    }
  },
  ReadTransactionsDetail: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Credit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    TransactionInformation: 'Cash from Aubrey',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    },
    Balance: {
      Amount: {
        Amount: '230.00',
        Currency: 'GBP'
      },
      CreditDebitIndicator: 'Credit',
      Type: 'InterimBooked'
    }
  },
  ReadTransactionsCredit: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Credit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    TransactionInformation: 'Cash from Aubrey',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    },
    Balance: {
      Amount: {
        Amount: '230.00',
        Currency: 'GBP'
      },
      CreditDebitIndicator: 'Credit',
      Type: 'InterimBooked'
    }
  },
  ReadTransactionsDebit: {
    AccountId: '22289',
    TransactionId: '123',
    TransactionReference: 'Ref 123',
    Amount: {
      Amount: '10.00',
      Currency: 'GBP'
    },
    CreditDebitIndicator: 'Debit',
    Status: 'Booked',
    BookingDateTime: 'date',
    ValueDateTime: '2017-04-05T10:45:22+00:00',
    TransactionInformation: 'Cash from Aubrey',
    BankTransactionCode: {
      Code: 'ReceivedCreditTransfer',
      SubCode: 'DomesticCreditTransfer'
    },
    ProprietaryBankTransactionCode: {
      Code: 'Transfer',
      Issuer: 'AlphaBank'
    },
    Balance: {
      Amount: {
        Amount: '230.00',
        Currency: 'GBP'
      },
      CreditDebitIndicator: 'Credit',
      Type: 'InterimBooked'
    }
  }
};
