module.exports = {
  type: 'AccountsConsentDetails',
  permissions: [
    'ReadAccountsDetail',
    'ReadBalances',
    'ReadBeneficiariesDetail',
    'ReadDirectDebits',
    'ReadProducts',
    'ReadStandingOrdersDetail',
    'ReadTransactionsCredits',
    'ReadTransactionsDebits',
    'ReadTransactionsDetail',
    'ReadOffers',
    'ReadPAN',
    'ReadParty',
    'ReadPartyPSU',
    'ReadScheduledPaymentsDetail',
    'ReadStatementsDetail'
  ],
  fromTransaction: '2017-02-10T17:48:45+00:00',
  toTransaction: '2020-02-10T17:48:45+00:00',
  accounts: [
    {
      id: 'ea61c6e1-3310-4115-bb1c-c8bc35edb27d',
      userID: 'demo',
      account: {
        AccountId: 'ea61c6e1-3310-4115-bb1c-c8bc35edb27d',
        Currency: 'GBP',
        AccountType: 'Personal',
        AccountSubType: 'CurrentAccount',
        Nickname:
          'UK Bills loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong',
        Account: [
          {
            SchemeName: 'SortCodeAccountNumber',
            Identification:
              '75608343829678 loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong',
            Name: 'demo',
            SecondaryIdentification: '69310297'
          }
        ]
      },
      latestStatementId: 'e7b41738-c2fa-4e3c-aaff-5c49c551190a',
      updated: 1549728791974,
      balances: [
        {
          AccountId: 'ea61c6e1-3310-4115-bb1c-c8bc35edb27d',
          CreditDebitIndicator: 'Credit',
          Type: 'InterimAvailable',
          DateTime: '2019-02-09T16:13:11+00:00',
          Amount: {
            Amount: '136.01',
            Currency: 'GBP'
          }
        }
      ]
    },
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
            Amount: '849.77',
            Currency: 'EUR'
          }
        }
      ]
    },
    {
      id: '823591df-a2ac-4993-b1b6-031040fe21f4',
      userID: 'demo',
      account: {
        AccountId: '823591df-a2ac-4993-b1b6-031040fe21f4',
        Currency: 'GBP',
        AccountType: 'Personal',
        AccountSubType: 'CurrentAccount',
        Nickname: 'Household',
        Account: [
          {
            SchemeName: 'SortCodeAccountNumber',
            Identification: '36706024057704',
            Name: 'demo'
          }
        ]
      },
      latestStatementId: '065f835e-f5b6-4b5d-9a1d-6515ce697e0f',
      updated: 1549728792425,
      balances: [
        {
          AccountId: '823591df-a2ac-4993-b1b6-031040fe21f4',
          CreditDebitIndicator: 'Debit',
          Type: 'InterimAvailable',
          DateTime: '2019-02-09T16:13:12+00:00',
          Amount: {
            Amount: '819.91',
            Currency: 'GBP'
          }
        }
      ]
    }
  ],
  username: 'demo',
  logo: 'https://www.getpostman.com/img/logos/postman/header-treatment.svg',
  clientId: 'f8081961-ceb4-428f-a3e0-dac115c8c36e',
  aispName:
    'quentin - loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong postman',
  expiredDate: '2020-02-10T17:48:45+00:00',
  intentType: 'ACCOUNT_ACCESS_CONSENT',
  decisionAPIUri: '/api/rcs/consent/decision/'
};
