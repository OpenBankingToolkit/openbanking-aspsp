import { Component, OnInit } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';

@Component({
  selector: 'app-next-payment-item',
  templateUrl: './next-payment-item.component.html'
})
export class NextPaymentItemComponent implements OnInit {
  nextPaymentLabel: string;

  nextPaymentDateLabel: string;
  nextPaymentAmountLabel: string;
  frequencyLabel: string;

  nextPaymentDate: Date;
  nextPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;
  frequency: string;

  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
