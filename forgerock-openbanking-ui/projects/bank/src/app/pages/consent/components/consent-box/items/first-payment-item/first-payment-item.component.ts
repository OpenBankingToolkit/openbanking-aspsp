import { Component, OnInit } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';

@Component({
  selector: 'app-first-payment-item',
  templateUrl: './first-payment-item.component.html',
  styleUrls: ['./first-payment-item.component.scss']
})
export class FirstPaymentItemComponent implements OnInit {
  firstPaymentLabel: string;
  firstPaymentDateLabel: string;
  firstPaymentAmountLabel: string;

  firstPaymentDate: Date;
  firstPaymentAmount: OBActiveOrHistoricCurrencyAndAmount;

  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
