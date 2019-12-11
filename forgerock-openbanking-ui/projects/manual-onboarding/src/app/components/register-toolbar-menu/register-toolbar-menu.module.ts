import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { TranslateModule } from '@ngx-translate/core';
import { FlexLayoutModule } from '@angular/flex-layout';

import { RegisterToolbarMenuComponent } from './register-toolbar-menu.component';
import { RegisterToolbarMenuContainer } from './register-toolbar-menu.container';

const declarations = [RegisterToolbarMenuComponent, RegisterToolbarMenuContainer];

@NgModule({
  declarations,
  entryComponents: declarations,
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    TranslateModule,
    FlexLayoutModule
  ],
  exports: declarations
})
export class RegisterToolbarMenuComponentModule {}
