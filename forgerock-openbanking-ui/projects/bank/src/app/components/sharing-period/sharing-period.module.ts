import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { ForgerockPipesModule } from '@forgerock/openbanking-ngx-common/pipes';

import { SharingPeriodComponent } from './sharing-period.component';

@NgModule({
  declarations: [SharingPeriodComponent],
  exports: [SharingPeriodComponent],
  imports: [CommonModule, TranslateModule, ForgerockPipesModule]
})
export class SharingPeriodModule {}
