import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';

import { SubmitBoxComponent } from './submit-box.component';

@NgModule({
  imports: [CommonModule, TranslateModule, FlexLayoutModule, MatButtonModule, MatProgressSpinnerModule],
  declarations: [SubmitBoxComponent],
  exports: [SubmitBoxComponent]
})
export class SubmitBoxComponentModule {}
