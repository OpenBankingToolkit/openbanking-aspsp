import { OBBalanceType1Code, OBCreditDebitCode } from 'bank/src/app/types/ob.enum';

export interface OBAccount2 {
  AccountId: string;
  Currency: string;
  Nickname: string;
  Account: OBCashAccount3[];
}

export interface OBCashAccount3 {
  SchemeName: string;
  Identification: string;
  Name: string;
  SecondaryIdentification: string;
}

export class OBActiveOrHistoricCurrencyAndAmount {
  Amount: number;
  Currency: string;
}

export interface OBCashBalance1 {
  CreditDebitIndicator: OBCreditDebitCode;
  Type: OBBalanceType1Code;
  Amount: OBActiveOrHistoricCurrencyAndAmount;
}
