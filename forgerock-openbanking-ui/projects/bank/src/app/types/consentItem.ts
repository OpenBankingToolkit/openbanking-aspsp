export enum ItemType {
  STRING = 'STRING',
  RATE_AMOUNT = 'RATE_AMOUNT',
  NEXT_PAYMENT = 'NEXT_PAYMENT',
  INSTRUCTED_AMOUNT = 'INSTRUCTED_AMOUNT',
  FIRST_PAYMENT = 'FIRST_PAYMENT',
  FINAL_PAYMENT = 'FINAL_PAYMENT',
  EXCHANGE_RATE = 'EXCHANGE_RATE',
  DATE = 'DATE',
  ACCOUNT_NUMBER = 'ACCOUNT_NUMBER',
  TRANSACTION_PERIOD = 'TRANSACTION_PERIOD',
  VRP_ACCOUNT_NUMBER = 'VRP_ACCOUNT_NUMBER',
  ADDRESS = 'ADDRESS'
}

export interface Item {
  type: ItemType;
  payload: any;
}

export interface IConsentEventEmitter {
  decision: string;
  sharedAccounts?: string[];
  accountId?: string;
}
