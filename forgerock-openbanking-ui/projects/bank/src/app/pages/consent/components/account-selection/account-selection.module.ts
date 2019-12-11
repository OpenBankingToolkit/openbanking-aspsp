import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatRadioModule } from '@angular/material/radio';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';

import { BankPipesModule } from 'bank/src/app/pipes/pipes.module';
import { AccountSelectionComponent } from './account-selection.component';

@NgModule({
  imports: [
    CommonModule,
    TranslateModule,
    FlexLayoutModule,
    MatCardModule,
    MatRadioModule,
    FormsModule,
    ReactiveFormsModule,
    BankPipesModule
  ],
  declarations: [AccountSelectionComponent],
  exports: [AccountSelectionComponent]
})
export class AccountSelectionComponentModule {}
