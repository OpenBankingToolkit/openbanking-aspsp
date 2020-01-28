import { Component, OnInit } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';

@Component({
  selector: 'app-final-payment-item',
  templateUrl: './final-payment-item.component.html'
})
export class FinalPaymentItemComponent implements OnInit {
  finalPaymentLabel: string;
  finalPaymentDateLabel: string;
  finalPaymentAmountLabel: string;

  finalPaymentDate: Date;
  finalPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;

  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
