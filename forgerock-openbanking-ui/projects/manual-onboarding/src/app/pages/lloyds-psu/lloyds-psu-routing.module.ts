import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LloydsPsuComponent } from './lloyds-psu.component';

const routes: Routes = [
  {
    path: '**',
    component: LloydsPsuComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LloydsPsuRoutingModule {}
