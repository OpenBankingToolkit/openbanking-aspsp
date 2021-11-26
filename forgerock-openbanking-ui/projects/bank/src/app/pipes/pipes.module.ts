import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountFormatPipe} from 'bank/src/app/pipes/account.pipe';
import {BalanceFormatPipe} from 'bank/src/app/pipes/balance.pipe';
import {AmountFormatPipe} from 'bank/src/app/pipes/amount.pipe';
import {FrequencyFormatPipe} from 'bank/src/app/pipes/frequency.pipe';
import {TranslatePipe} from '@ngx-translate/core';
import {AccountSortCodeFormatPipe} from "bank/src/app/pipes/account-sort-code.pipe";
import {AccountNumberFormatPipe} from "bank/src/app/pipes/account-number.pipe";

@NgModule({
  imports: [CommonModule],
  declarations: [AccountFormatPipe, AccountSortCodeFormatPipe, AccountNumberFormatPipe, BalanceFormatPipe, AmountFormatPipe, FrequencyFormatPipe],
  exports: [AccountFormatPipe, AccountSortCodeFormatPipe, AccountNumberFormatPipe, BalanceFormatPipe, AmountFormatPipe, FrequencyFormatPipe],
  providers: [AmountFormatPipe, TranslatePipe]
})
export class BankPipesModule {
}
