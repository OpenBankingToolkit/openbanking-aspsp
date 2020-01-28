import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DevCDRAuthoriseComponent } from './dev-cdr-authorise.component';

const routes: Routes = [
  {
    path: '**',
    component: DevCDRAuthoriseComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DevCDRAuthoriseRoutingModule {}
