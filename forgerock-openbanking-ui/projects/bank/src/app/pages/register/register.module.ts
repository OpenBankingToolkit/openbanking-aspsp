import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';

import { CanDeactivateGuard } from 'ob-ui-libs/guards';

import { ForgerockAuthRegisterModule, ForgerockAuthRegisterComponent } from 'ob-ui-libs/authentication';

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
