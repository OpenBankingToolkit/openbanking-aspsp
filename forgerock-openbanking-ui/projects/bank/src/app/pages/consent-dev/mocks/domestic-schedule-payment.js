module.exports = {
  type: 'domesticSchedulePaymentConsentDetails',
  scheduledPayment: {
    ScheduledPaymentId: '2876d876876',
    ScheduledPaymentDateTime: '2019-11-02T00:00:00+00:00',
    Reference: 'DSR-037',
    InstructedAmount: {
      Amount: '200.00',
      Currency: 'GBP'
    },
    CreditorAccount: {
      SchemeName: 'UK.OBIE.SortCodeAccountNumber',
      Identification: '08080021325698',
      Name: 'Tom Kirkman'
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
  merchantName: 'demo - Postman',
  paymentReference: 'DSR-037',
  intentType: 'PAYMENT_DOMESTIC_SCHEDULED_CONSENT',
  decisionAPIUri: '/api/rcs/consent/decision/'
};
