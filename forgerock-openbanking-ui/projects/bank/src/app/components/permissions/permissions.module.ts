import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslateModule } from '@ngx-translate/core';

import { PermissionsComponent } from './permissions.component';

@NgModule({
  imports: [CommonModule, MatExpansionModule, TranslateModule],
  declarations: [PermissionsComponent],
  exports: [PermissionsComponent]
})
export class PermissionsModule {}
