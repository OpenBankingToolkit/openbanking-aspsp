import { Pipe, PipeTransform } from '@angular/core';
import { OBCashAccount3 } from 'bank/src/app/types/ob';

@Pipe({
  name: 'AccountFormat',
  pure: false
})
export class AccountFormatPipe implements PipeTransform {
  transform(account: OBCashAccount3): string {
    if (account.SchemeName.includes('SortCodeAccountNumber')) {
      return account.Identification.replace(/(\d{2})(\d{2})(\d{2})(\d{8})/g, '$1-$2-$3 $4');
    } else if ('IBAN' === account.SchemeName) {
      return account.Identification;
    }
    return account.Identification;
  }
}
