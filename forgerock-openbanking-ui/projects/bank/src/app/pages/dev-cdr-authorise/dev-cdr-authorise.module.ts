import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DevCDRAuthoriseRoutingModule } from './dev-cdr-authorise-routing.module';
import { DevCDRAuthoriseComponent } from './dev-cdr-authorise.component';
import { CDRConsentModule } from '../../components/cdr-consent/cdr-consent.module';

@NgModule({
  declarations: [DevCDRAuthoriseComponent],
  imports: [CommonModule, DevCDRAuthoriseRoutingModule, CDRConsentModule]
})
export class DevCDRAuthoriseModule {}
