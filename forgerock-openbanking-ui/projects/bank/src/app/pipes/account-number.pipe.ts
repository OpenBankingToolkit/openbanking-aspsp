import { Pipe, PipeTransform } from '@angular/core';
import { OBCashAccount3 } from 'bank/src/app/types/ob';

@Pipe({
  name: 'AccountNumberFormat',
  pure: false
})
export class AccountNumberFormatPipe implements PipeTransform {
  transform(account: OBCashAccount3): string {
    if ('SortCodeAccountNumber' === account.SchemeName) {
      return account.Identification.replace(/(\d{2})(\d{2})(\d{2})(\d{8})/g, '$1-$2-$3 $4');
    } else if ('IBAN' === account.SchemeName) {
      return account.Identification;
    }
    return account.Identification;
  }
}
