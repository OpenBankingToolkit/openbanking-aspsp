import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatSharedModule } from 'bank/src/app/mat-shared.module';
import { TranslateSharedModule } from 'bank/src/app/translate-shared.module';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';

import { CDRConsentComponent } from './cdr-consent.component';
import { CDRAccountComponent } from 'bank/src/app/components/cdr-consent/account/account.component';
import { CDRConsentDynamicComponent } from './cdr-consent-dynamic.component';
import { CDRDataRecipientInfoComponentModule } from '../cdr-data-recipient-info/cdr-data-recipient-info.module';
import { AccountCheckboxModule } from 'bank/src/app/components/account-checkbox/account-checkbox.module';
import { PermissionsModule } from 'bank/src/app/components//permissions/permissions.module';
import { SharingPeriodModule } from '../sharing-period/sharing-period.module';
import { ConsentStepsModule } from '../consent-steps/consent-steps.module';

const ConsentTypeComponents = [CDRAccountComponent];

@NgModule({
  imports: [
    CommonModule,
    MatSharedModule,
    TranslateSharedModule,
    ForgerockSharedModule,
    ForgerockCustomerLogoModule,
    CDRDataRecipientInfoComponentModule,
    AccountCheckboxModule,
    PermissionsModule,
    SharingPeriodModule,
    ConsentStepsModule
  ],
  declarations: [CDRConsentComponent, CDRConsentDynamicComponent, ...ConsentTypeComponents],
  entryComponents: [...ConsentTypeComponents],
  exports: [CDRConsentDynamicComponent]
})
export class CDRConsentModule {}
