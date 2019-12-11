import { Pipe, PipeTransform } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';
import { TranslateService } from '@ngx-translate/core';

@Pipe({
  name: 'AmountFormat',
  pure: false
})
export class AmountFormatPipe implements PipeTransform {
  constructor(public translateService: TranslateService) {}

  transform(amount: OBActiveOrHistoricCurrencyAndAmount): string {
    const local = this.translateService.getBrowserCultureLang() || 'en-UK';
    const formatter = new Intl.NumberFormat(local, {
      style: 'currency',
      currency: amount.Currency,
      minimumFractionDigits: 2
    });

    return formatter.format(amount.Amount);
  }
}
