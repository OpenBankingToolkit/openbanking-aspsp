import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ConsentComponent } from './consent.component';
import { ForgerockSimpleLayoutModule } from 'ob-ui-libs/layouts/simple';

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
