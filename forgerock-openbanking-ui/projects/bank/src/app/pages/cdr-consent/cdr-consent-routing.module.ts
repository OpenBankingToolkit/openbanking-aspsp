import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ForgerockSimpleLayoutModule } from '@forgerock/openbanking-ngx-common/layouts/simple';
import { CDRConsentComponent } from 'bank/src/app/components/cdr-consent/cdr-consent.component';

const routes: Routes = [
  {
    path: '**',
    component: CDRConsentComponent
  }
];

@NgModule({
  imports: [ForgerockSimpleLayoutModule, RouterModule.forChild(routes)],
  exports: [ForgerockSimpleLayoutModule, RouterModule]
})
export class CDRConsentRoutingModule {}
