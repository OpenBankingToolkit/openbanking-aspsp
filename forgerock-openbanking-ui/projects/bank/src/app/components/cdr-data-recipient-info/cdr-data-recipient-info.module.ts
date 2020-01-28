import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';

import { CDRDataRecipientInfoComponent } from './cdr-data-recipient-info.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [CommonModule, TranslateModule, FlexLayoutModule, MatIconModule],
  declarations: [CDRDataRecipientInfoComponent],
  exports: [CDRDataRecipientInfoComponent]
})
export class CDRDataRecipientInfoComponentModule {}
