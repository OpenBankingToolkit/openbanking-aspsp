import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ConsentComponent } from './consent.component';
import { ForgerockSimpleLayoutModule } from '@forgerock/openbanking-ngx-common/layouts/simple';

const routes: Routes = [
  {
    path: '**',
    component: ConsentComponent
  }
];

@NgModule({
  imports: [ForgerockSimpleLayoutModule, RouterModule.forChild(routes)],
  exports: [ForgerockSimpleLayoutModule, RouterModule]
})
export class ConsentRoutingModule {}
