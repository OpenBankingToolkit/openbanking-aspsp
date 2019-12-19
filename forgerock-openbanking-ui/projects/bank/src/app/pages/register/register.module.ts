import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import { CanDeactivateGuard } from '@forgerock/openbanking-ngx-common/guards';

import {
  ForgerockAuthRegisterModule,
  ForgerockAuthRegisterComponent
} from '@forgerock/openbanking-ngx-common/authentication';

const routes: Routes = [
  {
    path: '**',
    component: ForgerockAuthRegisterComponent,
    canDeactivate: [CanDeactivateGuard]
  }
];

@NgModule({
  imports: [CommonModule, ForgerockAuthRegisterModule, RouterModule.forChild(routes)]
})
export class RegisterModule {}
