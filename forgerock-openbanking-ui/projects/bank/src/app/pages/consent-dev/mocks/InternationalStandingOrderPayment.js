module.exports = {
  type: 'internationalStandingOrderPaymentConsentDetails',
  standingOrder: {
    StandingOrderId: 'PISOC_62dd261c-80fc-49b4-b5af-db52889b70dc',
    Frequency: 'IntrvlMnthDay:01:15',
    Reference: 'Rent',
    FirstPaymentDateTime: '2018-12-06T06:06:06+00:00',
    NextPaymentDateTime: '2018-12-06T06:06:06+00:00',
    FinalPaymentDateTime: '2020-11-20T06:06:06+00:00',
    FirstPaymentAmount: {
      Amount: '7.00',
      Currency: 'EUR'
    },
    NextPaymentAmount: {
      Amount: '7.00',
      Currency: 'EUR'
    },
    FinalPaymentAmount: {
      Amount: '7.00',
      Currency: 'EUR'
    },
    CreditorAccount: {
      SchemeName: 'UK.OBIE.SortCodeAccountNumber',
      Identification: '08080021325698',
      Name: 'Bob Clements'
    }
  },
  accounts: [
    {
      id: '4bb14b7c-c0ea-46a5-b0c0-c3ee001b5fe0',
      userID: 'demo',
      account: {
        AccountId: '4bb14b7c-c0ea-46a5-b0c0-c3ee001b5fe0',
        Currency: 'EUR',
        AccountType: 'Personal',
        AccountSubType: 'CurrentAccount',
        Nickname: 'FR Bills',
        Account: [
          {
            SchemeName: 'SortCodeAccountNumber',
            Identification: '31069191125326',
            Name: 'demo',
            SecondaryIdentification: '76409107'
          }
        ]
      },
      latestStatementId: '3bed1040-2f93-4229-b01e-c59f34504ea4',
      updated: 1549728792227,
      balances: [
        {
          AccountId: '4bb14b7c-c0ea-46a5-b0c0-c3ee001b5fe0',
          CreditDebitIndicator: 'Debit',
          Type: 'InterimAvailable',
          DateTime: '2019-02-09T16:13:11+00:00',
          Amount: {
            Amount: '894.32',
            Currency: 'EUR'
          }
        }
      ]
    }
  ],
  username: 'demo',
  logo: 'https://www.getpostman.com/img/logos/postman/header-treatment.svg',
  clientId: '7df9d21f-e2d1-42c3-b5ac-0cee2a0858ca',
  merchantName:
    'demo - loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong postman',
  currencyOfTransfer: 'EUR',
  paymentReference: 'Rent',
  intentType: 'PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT',
  decisionAPIUri: '/api/rcs/consent/decision/'
};
