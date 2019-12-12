import { Component, OnInit } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';
import { Rate } from 'bank/src/app/types/api';

@Component({
  selector: 'app-rate-amount-item',
  templateUrl: './rate-amount-item.component.html',
  styleUrls: ['./rate-amount-item.component.scss']
})
export class RateAmountItemComponent implements OnInit {
  payload: any;

  amount: OBActiveOrHistoricCurrencyAndAmount;
  rate: Rate;

  cssClass: string;

  constructor() {}

  ngOnInit() {}

  amountInOtherCurrent(amount: OBActiveOrHistoricCurrencyAndAmount, rate: number, currencyOfTransfer: string) {
    const toOtherCurrencyAmount = new OBActiveOrHistoricCurrencyAndAmount();
    toOtherCurrencyAmount.Currency = currencyOfTransfer;
    toOtherCurrencyAmount.Amount = amount.Amount * rate;
    return toOtherCurrencyAmount;
  }
}
