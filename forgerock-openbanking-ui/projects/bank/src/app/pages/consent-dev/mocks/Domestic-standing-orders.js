module.exports = {
  type: 'domesticStandingOrderPaymentConsentDetails',
  standingOrder: {
    StandingOrderId: 'PDSOC_5d736288-ef59-486a-a8f5-7abf8b2bae0c',
    Frequency: 'IntrvlMnthDay:01:15',
    Reference:
      'Pocket money for loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Damien',
    FirstPaymentDateTime: '2018-12-06T06:06:06+00:00',
    FinalPaymentDateTime: '2020-11-20T06:06:06+00:00',
    FirstPaymentAmount: {
      Amount: '6.66',
      Currency: 'GBP'
    },
    NextPaymentAmount: {
      Amount: '7.00',
      Currency: 'GBP'
    },
    FinalPaymentAmount: {
      Amount: '7.00',
      Currency: 'GBP'
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
  paymentReference:
    'Pocket money for loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong Damien',
  intentType: 'PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT',
  decisionAPIUri: '/api/rcs/consent/decision/'
};
