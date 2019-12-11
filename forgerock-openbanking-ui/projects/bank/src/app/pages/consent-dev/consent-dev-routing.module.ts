import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ConsentDevComponent } from './consent-dev.component';

const routes: Routes = [
  {
    path: '**',
    component: ConsentDevComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConsentDevRoutingModule {}
