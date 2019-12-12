import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SessionLostComponent } from './session-lost.component';

const routes: Routes = [
  {
    path: '**',
    component: SessionLostComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SessionLostRoutingModule {}
