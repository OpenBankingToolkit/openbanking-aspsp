import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {TranslateModule} from '@ngx-translate/core';
import {RouterModule} from '@angular/router';

import {ForgerockCustomerLogoModule} from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';
import {ConsentBoxComponent} from './consent-box.component';
import {StringItemComponent} from './items/string-item/string-item.component';
import {DateItemComponent} from './items/date-item/date-item.component';
import {InstructedAmountItemComponent} from './items/instructed-amount-item/instructed-amount-item.component';
import {FirstPaymentItemComponent} from './items/first-payment-item/first-payment-item.component';
import {NextPaymentItemComponent} from './items/next-payment-item/next-payment-item.component';
import {FinalPaymentItemComponent} from './items/final-payment-item/final-payment-item.component';
import {AccountNumberItemComponent} from './items/account-number-item/account-number-item.component';
import {ExchangeRateItemComponent} from './items/exchange-rate-item/exchange-rate-item.component';
import {RateAmountItemComponent} from './items/rate-amount-item/rate-amount-item.component';
import {KeyValueItemComponent} from './items/key-value/key-value.component';
import {DynamicItemComponent} from './items/dynamic-item/dynamic-item.component';
import {TransactionsPeriodItemComponent} from 'bank/src/app/pages/consent/components/consent-box/items/transactions-period-item/transactions-period-item.component';
import {BankPipesModule} from 'bank/src/app/pipes/pipes.module';
import {ForgerockPipesModule} from '@forgerock/openbanking-ngx-common/pipes';
import {VrpAccountItemComponent} from "bank/src/app/pages/consent/components/consent-box/items/vrp-account-item/vrp-account-item.component";

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    FlexLayoutModule,
    RouterModule,
    ForgerockCustomerLogoModule,
    BankPipesModule,
    ForgerockPipesModule
  ],
  declarations: [
    ConsentBoxComponent,
    StringItemComponent,
    DateItemComponent,
    InstructedAmountItemComponent,
    FirstPaymentItemComponent,
    NextPaymentItemComponent,
    FinalPaymentItemComponent,
    AccountNumberItemComponent,
    ExchangeRateItemComponent,
    RateAmountItemComponent,
    DynamicItemComponent,
    TransactionsPeriodItemComponent,
    VrpAccountItemComponent,
    KeyValueItemComponent
  ],
  entryComponents: [
    StringItemComponent,
    DateItemComponent,
    InstructedAmountItemComponent,
    FirstPaymentItemComponent,
    NextPaymentItemComponent,
    FinalPaymentItemComponent,
    AccountNumberItemComponent,
    VrpAccountItemComponent,
    ExchangeRateItemComponent,
    RateAmountItemComponent,
    DynamicItemComponent,
    TransactionsPeriodItemComponent

  ],
  exports: [ConsentBoxComponent]
})
export class ConsentBoxComponentModule {
}
