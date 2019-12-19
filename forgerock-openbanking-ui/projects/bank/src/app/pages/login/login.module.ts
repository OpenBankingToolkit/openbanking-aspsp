import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import {
  ForgerockAuthLoginModule,
  ForgerockAuthLoginComponent
} from '@forgerock/openbanking-ngx-common/authentication';

const routes: Routes = [
  {
    path: '**',
    component: ForgerockAuthLoginComponent
  }
];

@NgModule({
  imports: [CommonModule, ForgerockAuthLoginModule, RouterModule.forChild(routes)]
})
export class LoginModule {}
