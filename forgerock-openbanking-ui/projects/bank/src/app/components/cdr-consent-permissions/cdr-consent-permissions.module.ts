import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { CdrConsentPermissionsComponent } from './cdr-consent-permissions.component';

@NgModule({
  declarations: [CdrConsentPermissionsComponent],
  exports: [CdrConsentPermissionsComponent],
  imports: [CommonModule, TranslateModule]
})
export class CdrConsentPermissionsModule {}
