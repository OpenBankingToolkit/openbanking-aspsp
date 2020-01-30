import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CDRConsentRoutingModule } from './cdr-consent-routing.module';

import { CDRConsentModule } from 'bank/src/app/components/cdr-consent/cdr-consent.module';

@NgModule({
  imports: [CommonModule, CDRConsentRoutingModule, CDRConsentModule]
})
export class CDRConsentPageModule {}
