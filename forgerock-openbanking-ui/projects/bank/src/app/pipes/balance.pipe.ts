import { Pipe, PipeTransform } from '@angular/core';
import { OBCashBalance1 } from 'bank/src/app/types/ob';
import { AmountFormatPipe } from 'bank/src/app/pipes/amount.pipe';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'BalanceFormat',
  pure: false
})
export class BalanceFormatPipe implements PipeTransform {
  constructor(private amountFormatPipe: AmountFormatPipe, private translateService: TranslateService) {}

  transform(balance: OBCashBalance1): string {
    return (
      this.translateService.instant('BALANCE_TYPE.' + balance.Type) +
      ' : ' +
      this.amountFormatPipe.transform(balance.Amount)
    );
  }
}
