import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { TranslateModule } from '@ngx-translate/core';

import { LloydsPsuComponent } from './lloyds-psu.component';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { LloydsPsuRoutingModule } from './lloyds-psu-routing.module';

@NgModule({
  imports: [CommonModule, MatCardModule, TranslateModule, ForgerockSharedModule, LloydsPsuRoutingModule],
  declarations: [LloydsPsuComponent]
})
export class LloydsPsuModule {}
