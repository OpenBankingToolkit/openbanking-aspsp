import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { SharingPeriodComponent } from './sharing-period.component';

@NgModule({
  declarations: [SharingPeriodComponent],
  exports: [SharingPeriodComponent],
  imports: [CommonModule, TranslateModule]
})
export class SharingPeriodModule {}
