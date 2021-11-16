import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatSharedModule } from 'bank/src/app/mat-shared.module';
import { TranslateSharedModule } from 'bank/src/app/translate-shared.module';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { ConsentRoutingModule } from './consent-routing.module';
import { ConsentComponent } from './consent.component';
import { SinglePaymentComponent } from './single-payment/single-payment.component';
import { AccountComponent } from './account/account.component';
import { DynamicComponent } from './dynamic/dynamic.component';
import { PermissionsComponent } from './permissions/permissions.component';
import { DomesticPaymentComponent } from 'bank/src/app/pages/consent/domestic-payment/domestic-payment.component';
import { DomesticSchedulePaymentComponent } from 'bank/src/app/pages/consent/domestic-schedule-payment/domestic-schedule-payment.component';
import { DomesticStandingOrderComponent } from 'bank/src/app/pages/consent/domestic-standing-order/domestic-standing-order.component';
import { InternationalPaymentComponent } from 'bank/src/app/pages/consent/international-payment/international-payment.component';
import { FundsConfirmationComponent } from 'bank/src/app/pages/consent/funds-confirmation/funds-confirmation.component';
import { FilePaymentComponent } from 'bank/src/app/pages/consent/file-payment/file-payment.component';
import { CancelComponent } from "bank/src/app/pages/consent/components/cancel/cancel.component";

import { InternationalSchedulePaymentComponent } from 'bank/src/app/pages/consent/international-schedule-payment/international-schedule-payment.component';
import { InternationalStandingOrderComponent } from 'bank/src/app/pages/consent/international-standing-order/international-standing-order.component';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';
import { ConsentBoxComponentModule } from './components/consent-box/consent-box.module';
import { SubmitBoxComponentModule } from './components/submit-box/submit-box.module';
import { AccountSelectionComponentModule } from './components/account-selection/account-selection.module';
import { AccountCheckboxModule } from './components/account-checkbox/account-checkbox.module';
import { RejectComponent } from './components/reject/reject.component';

@NgModule({
  imports: [
    CommonModule,
    ConsentRoutingModule,
    MatSharedModule,
    TranslateSharedModule,
    ForgerockSharedModule,
    ForgerockCustomerLogoModule,
    ConsentBoxComponentModule,
    SubmitBoxComponentModule,
    AccountSelectionComponentModule,
    AccountCheckboxModule
  ],
  declarations: [
    ConsentComponent,
    SinglePaymentComponent,
    AccountComponent,
    DomesticPaymentComponent,
    DomesticSchedulePaymentComponent,
    DomesticStandingOrderComponent,
    InternationalPaymentComponent,
    InternationalSchedulePaymentComponent,
    InternationalStandingOrderComponent,
    FundsConfirmationComponent,
    FilePaymentComponent,
    DynamicComponent,
    PermissionsComponent,
    CancelComponent,
    RejectComponent
  ],
  entryComponents: [
    SinglePaymentComponent,
    AccountComponent,
    DomesticPaymentComponent,
    DomesticSchedulePaymentComponent,
    DomesticStandingOrderComponent,
    InternationalPaymentComponent,
    InternationalSchedulePaymentComponent,
    InternationalStandingOrderComponent,
    FundsConfirmationComponent,
    FilePaymentComponent,
    CancelComponent,
    RejectComponent
  ],
  exports: [
    ConsentComponent,
    SinglePaymentComponent,
    AccountComponent,
    DomesticPaymentComponent,
    DomesticSchedulePaymentComponent,
    DomesticStandingOrderComponent,
    InternationalPaymentComponent,
    InternationalSchedulePaymentComponent,
    InternationalStandingOrderComponent,
    FundsConfirmationComponent,
    FilePaymentComponent,
    DynamicComponent,
    PermissionsComponent,
    CancelComponent,
    RejectComponent
  ]
})
export class ConsentModule {}
