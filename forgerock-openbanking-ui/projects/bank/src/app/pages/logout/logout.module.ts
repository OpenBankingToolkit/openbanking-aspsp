import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { LogoutRoutingModule } from './logout-routing.module';
import { LogoutComponent } from './logout.component';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

@NgModule({
  imports: [
    CommonModule,
    LogoutRoutingModule,
    MatCardModule,
    MatButtonModule,
    TranslateModule,
    ForgerockCustomerLogoModule
  ],
  declarations: [LogoutComponent]
})
export class LogoutModule {}
