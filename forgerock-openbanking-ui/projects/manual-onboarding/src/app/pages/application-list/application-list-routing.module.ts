import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ApplicationListContainerComponent } from './application-list.container';

const routes: Routes = [
  {
    path: '**',
    component: ApplicationListContainerComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ApplicationListRoutingModule {}
