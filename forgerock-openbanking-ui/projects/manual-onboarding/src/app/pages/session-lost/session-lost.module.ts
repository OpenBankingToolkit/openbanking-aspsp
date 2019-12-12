import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';

import { SessionLostComponent } from './session-lost.component';
import { SessionLostRoutingModule } from './session-lost-routing.module';
import { ForgerockSharedModule } from 'ob-ui-libs/shared';

@NgModule({
  imports: [CommonModule, SessionLostRoutingModule, ForgerockSharedModule, MatButtonModule, MatCardModule],
  declarations: [SessionLostComponent]
})
export class SessionLostModule {}
