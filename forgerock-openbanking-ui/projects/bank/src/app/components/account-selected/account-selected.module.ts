import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';

import { BankPipesModule } from 'bank/src/app/pipes/pipes.module';
import { AccountSelectedComponent } from './account-selected.component';

@NgModule({
  imports: [CommonModule, TranslateModule, FlexLayoutModule, MatCardModule, MatIconModule, BankPipesModule],
  declarations: [AccountSelectedComponent],
  exports: [AccountSelectedComponent]
})
export class AccountSelectedModule {}
