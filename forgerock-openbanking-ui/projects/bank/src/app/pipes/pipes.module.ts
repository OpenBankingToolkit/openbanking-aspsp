import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountNumberFormatPipe } from 'bank/src/app/pipes/account-number.pipe';
import { BalanceFormatPipe } from 'bank/src/app/pipes/balance.pipe';
import { AmountFormatPipe } from 'bank/src/app/pipes/amount.pipe';
import { FrequencyFormatPipe } from 'bank/src/app/pipes/frequency.pipe';
import { TranslatePipe } from '@ngx-translate/core';

@NgModule({
  imports: [CommonModule],
  declarations: [AccountNumberFormatPipe, BalanceFormatPipe, AmountFormatPipe, FrequencyFormatPipe],
  exports: [AccountNumberFormatPipe, BalanceFormatPipe, AmountFormatPipe, FrequencyFormatPipe],
  providers: [AmountFormatPipe, TranslatePipe]
})
export class BankPipesModule {}
