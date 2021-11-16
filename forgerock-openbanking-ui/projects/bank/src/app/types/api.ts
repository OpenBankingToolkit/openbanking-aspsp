import { OBAccount2, OBActiveOrHistoricCurrencyAndAmount, OBCashBalance1 } from './ob';
import { IntentType } from 'bank/src/app/types/IntentType';
import { OBAccountPermissions } from 'bank/src/app/types/OBAccountPermissions';

export interface FRAccountWithBalance {
  id: string;
  userID: string;
  updated: string;
  account: OBAccount2;
  balances: OBCashBalance1;
}

export module ApiResponses {
  export interface ConsentDetailsResponse {
    redirectUri: string;
    decisionAPIUri: string;
    intentType: IntentType;
    accounts: FRAccountWithBalance[];
    username: string;
    logo: string;
    clientId: string;
    clientName: string;
    merchantName: string;
    aispName: string;
    // optional
    canceledByUser?: boolean;
    permissions?: OBAccountPermissions[];
    expiredDate?: string;
    fromTransaction?: string;
    toTransaction?: string;
    instructedAmount?: OBActiveOrHistoricCurrencyAndAmount;
    account: OBAccount2;

    standingOrder?: {
      Frequency: string;
      Reference: string;
      FirstPaymentDateTime: string;
      FirstPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;
      NextPaymentDateTime: string;
      NextPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;
      FinalPaymentDateTime: string;
      FinalPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;
    };
    scheduledPayment?: {
      Reference: string;
      ScheduledPaymentDateTime: string;
      InstructedAmount?: OBActiveOrHistoricCurrencyAndAmount;
    };
    rate?: Rate;
    numberOfTransactions?: string;
    totalAmount?: string;
    paymentReference?: string;
    fileReference?: string;
    requestedExecutionDateTime?: string;
    currencyOfTransfer?: string;
    expirationDateTime?: string;
    // special ui treatment
    userActions?:UserActions;
    decisionResponse?: ConsentDecisionResponse;
  }

  export interface ConsentDecisionResponse {
    consentJwt: string;
    requestMethod: string;
    redirectUri: string
  }

  export interface UserActions {
    acceptedByUser?: boolean;
    rejectedByUser?: boolean;
    canceledByUser?: boolean;
    cancelRedirectUri?: string;
  }
}

export class Rate {
  RateType: string;
  UnitCurrency: string;
  ExchangeRate: number;
  ContractIdentification: string;
}
