import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';

import { SubmitBoxComponent } from './submit-box.component';
import {MatSharedModule} from "bank/src/app/mat-shared.module";

@NgModule({
  imports: [CommonModule, TranslateModule, FlexLayoutModule, MatButtonModule, MatProgressSpinnerModule, MatSharedModule],
  declarations: [SubmitBoxComponent],
  exports: [SubmitBoxComponent]
})
export class SubmitBoxComponentModule {}
